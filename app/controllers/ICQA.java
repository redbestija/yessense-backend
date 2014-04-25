package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import play.db.DB;
import play.mvc.*;

/**
 * API for YouSense to connect to the server & database.
 * 
 */
public class ICQA extends Controller {

	/**
	 * A method for adding a new feedback message to the database.
	 * 
	 * data format: { "Experiencer":"test", "Activity":["Sitting"], "EventExperience":["Cold", "Dry", "Dark"], "FollowingAction":["Open window", "Sit down"],
	 * "Location":"2531", "Reason":["Window is open"], "FeelingIntensity":["3", "2", "1"], "Overall":"5", "RelativeTime":"now" }
	 * 
	 * Known problem: if user specified doesn't actually exist in the database, causes an SQL exception
	 */
	public static void postFeedback(String json) {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));

		// parse JSON for the parameters
		JsonParser parser = new JsonParser();
		JsonObject jsonMessage = (JsonObject) parser.parse(json);

		String user = jsonMessage.get("Experiencer").getAsString();
		String location = jsonMessage.get("Location").getAsString();
		int overall = Integer.parseInt(jsonMessage.get("Overall").getAsString());
		String when = jsonMessage.get("RelativeTime").getAsString();
		
		String wholeMessage = jsonMessage.get("WholeSentenceInText").getAsString();
		int feedbackSource = Integer.parseInt(jsonMessage.get("FeedbackSource").getAsString());
		
		JsonArray activityArray = jsonMessage.get("Activity").getAsJsonArray();
		JsonArray followingActionArray = jsonMessage.get("FollowingAction").getAsJsonArray();
		JsonArray reasonArray = jsonMessage.get("Reason").getAsJsonArray();
		JsonArray feelingArray = jsonMessage.get("EventExperience").getAsJsonArray();

		JsonArray feelingIntensityArray = jsonMessage.get("FeelingIntensity").getAsJsonArray();

		int adjectiveCategory = 4; // magic number for a category of newly added adjectives

		Connection conn = null;

		try {
			conn = DB.getConnection();

			// make a transaction (so all queries are executed or nothing is modified if something fails)
			conn.setAutoCommit(false);

			ResultSet rs;

			// check if location exists and add if not
			String query = "SELECT * FROM location WHERE description = ?";
			PreparedStatement queryStatement = conn.prepareStatement(query);
			queryStatement.setString(1, location);
			rs = queryStatement.executeQuery();

			if (!rs.isBeforeFirst()) {
				query = "INSERT INTO location (description) VALUES (?)";
				PreparedStatement insertStatement = conn.prepareStatement(query);
				insertStatement.setString(1, location);
				insertStatement.executeUpdate();
			}

			// insert the actual feedback
			int insertExperienceId;
			String queryExperience = "INSERT INTO feedback (user, location, overall, relative_time, whole_message, feedback_source, is_testing_value) "
					+ "VALUES ((SELECT id FROM user WHERE username = ? LIMIT 1), (SELECT id FROM location WHERE description = ? LIMIT 1), ?, ?, ?, ?,0)";

//			String queryExperience = "INSERT INTO feedback (user, location, overall, relative_time) "
//					+ "VALUES ((SELECT id FROM user WHERE username = ? LIMIT 1), (SELECT id FROM location WHERE description = ? LIMIT 1), ?, ?)";
			PreparedStatement insertExperience = conn.prepareStatement(queryExperience, Statement.RETURN_GENERATED_KEYS);
			insertExperience.setString(1, user);

			insertExperience.setString(2, location);
			insertExperience.setInt(3, overall);
			insertExperience.setString(4, when);

			insertExperience.setString(5, wholeMessage);
			insertExperience.setInt(6, feedbackSource);

			insertExperience.executeUpdate();

			rs = insertExperience.getGeneratedKeys();
			if (rs.next())
				insertExperienceId = rs.getInt(1);
			else
				throw new SQLException();

			// insert adjectives
			int insertAdjectiveId[] = new int[feelingArray.size()];
			for (int i = 0; i < feelingArray.size(); i++) {
				PreparedStatement insertAdjective = conn.prepareStatement("INSERT INTO fb_adjective (category, value) VALUES (?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				insertAdjective.setInt(1, adjectiveCategory);
				insertAdjective.setString(2, feelingArray.get(i).getAsString());
				insertAdjective.executeUpdate();

				rs = insertAdjective.getGeneratedKeys();
				if (rs.next())
					insertAdjectiveId[i] = rs.getInt(1);
				else
					throw new SQLException();
			}

			// insert feelings
			for (int i = 0; i < insertAdjectiveId.length; i++) {
				PreparedStatement insertFeeling = conn.prepareStatement("INSERT INTO fb_feeling (exp_id, adjective, intensity) VALUES (?, ?, ?)");
				insertFeeling.setInt(1, insertExperienceId);
				insertFeeling.setInt(2, insertAdjectiveId[i]);
				insertFeeling.setInt(3, feelingIntensityArray.get(i).getAsInt());
				insertFeeling.executeUpdate();
			}

			// insert reasons
			for (int i = 0; i < reasonArray.size(); i++) {
				PreparedStatement insertReason = conn.prepareStatement("INSERT INTO fb_reason (exp_id, value) VALUES (?, ?)");
				insertReason.setInt(1, insertExperienceId);
				insertReason.setString(2, reasonArray.get(i).getAsString());
				insertReason.executeUpdate();
			}

			// insert activities
			for (int i = 0; i < activityArray.size(); i++) {
				PreparedStatement insertActivity = conn.prepareStatement("INSERT INTO fb_activity (exp_id, value) VALUES (?, ?)");
				insertActivity.setInt(1, insertExperienceId);
				insertActivity.setString(2, activityArray.get(i).getAsString());
				insertActivity.executeUpdate();
			}

			// insert planned actions
			for (int i = 0; i < followingActionArray.size(); i++) {
				PreparedStatement insertPlannedAction = conn.prepareStatement("INSERT INTO fb_planned_action (exp_id, value) VALUES (?, ?)");
				insertPlannedAction.setInt(1, insertExperienceId);
				insertPlannedAction.setString(2, followingActionArray.get(i).getAsString());
				insertPlannedAction.executeUpdate();
			}

			conn.commit();

			System.out.println(time + " New feedback inserted into database");
			renderText("New feedback inserted into database");
		}
		catch (SQLException e) {
			String str = "";
			try {
				if (conn != null) {
					System.out.println(time + " Unable to insert new feedback into database, doing a rollback.");
					conn.rollback();
				}
			}
			catch (SQLException ex2) {
				System.out.println(time + " SQL Exception while doing rollback: " + ex2);
			}

			while (e != null) {
				System.out.println(time + " SQL Exception:  " + e.getMessage());
				str += e.getMessage();
				e = e.getNextException();
			}
			renderText("Error: " + str);
		}
		finally {

			if (conn != null) {
				try {
					conn.setAutoCommit(false);
					conn.close();
				}
				catch (SQLException e) {
					System.out.println(time + " SQL exception while disabling autocommit or closing database connection");
				}
			}
		}

	}

	/**
	 * A method for adding a new user to the database. Optionally also the user's home location can be determined.
	 * Use the locations name, not it's id.
	 * 
	 * Note: if the location specified isn't found, then the field will be NULL but the user still added.
	 */
	public static void addUser(String user, String location) {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;
		String resultText = "";

		try {
			conn = DB.getConnection();

			// check if user exists before adding new
			String query = "SELECT * FROM user WHERE username = ?";
			PreparedStatement queryStatement = conn.prepareStatement(query);
			queryStatement.setString(1, user);
			ResultSet rs = queryStatement.executeQuery();

			if (!rs.isBeforeFirst()) {
				String insertQuery = "INSERT INTO user (username, home_location) VALUES (?, (SELECT id FROM location WHERE description = ? LIMIT 1))";
				PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
				insertStatement.setString(1, user);
				insertStatement.setString(2, location);
				insertStatement.executeUpdate();

				resultText = "New user added successfully";
			}
			else {
				resultText = "Error: User already exists";
			}
		}
		catch (SQLException e) {
			renderText("Error: " + e.getMessage());
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				}
				catch (SQLException e) {
					System.out.println(time + " SQL exception while closing database connection");
				}
			}
		}

		renderText(resultText);
	}

	public static class QuestionAndOption{
		String question;
		int questionID;
		List<Category> categories;

		QuestionAndOption(){

		}

		QuestionAndOption(int id, String q){
			question = q; 
			questionID = id;
			categories = new ArrayList<Category>();
			categories.add(new Category (-1, "Undefined"));
		}

		void addCategory(Category cat){
			categories.add(cat);
		}


		Category getCategoryByID(int catID, String catName){
			for (int i = 0; i < categories.size(); i++){
				if (categories.get(i).id == catID){
					return categories.get(i);
				}
			}

			// No categories were found
			// Create a new one
			// Add it to the list
			Category newCategory = new Category(catID, catName);
			addCategory(newCategory);
			return newCategory;

		}

	}
	public static class Category{
		String name;
		int id;  
		List<Option> options;

		Category(){

		}
		Category(int i, String n){
			name = n;
			id = i;
			options = new ArrayList<Option>();
		}

		void addOption(int optionID, String optionName){
			Option option = new Option(optionID, optionName);
			options.add(option);
		}
	}

	public static class Option{
		public int id; 
		public String name;
		
		Option(){

		}

		Option(int i, String n){
			id = i; 
			name = n;
		}
	}

	private static QuestionAndOption getQuestionByID(List<QuestionAndOption> questionsAndOptions, int qID, String question){
		for (int i = 0; i < questionsAndOptions.size(); i++){
				if (questionsAndOptions.get(i).questionID == qID){
					return questionsAndOptions.get(i);
				}
			}

			// No categories were found
			// Create a new one
			// Add it to the list
			QuestionAndOption newQuestion = new QuestionAndOption(qID, question);
			questionsAndOptions.add(newQuestion);
			return newQuestion;

	}
	/**
	 *	Get all question and corresponding options to show in the UI 
	 */
	public static void getAllQuestionsAndOptions(String instanceIDString) {
//	public static void getAllQuestionsAndOptions(String instanceID) {
		// Select all questions, corresponding options and categories 
		int instanceID = Integer.parseInt(instanceIDString);
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;
		List<QuestionAndOption> questionsAndOptions = new ArrayList<QuestionAndOption>();
		

		try {
			conn = DB.getConnection();

			String query = "select q.id as QuestionID, q.question_text as QuestionText, opt.id as OptionID, opt.value as OptionValue, opt.category_id as CategoryID, cat.value as CategoryValue" +
			" from question as q, instance_question_option_map map, icqa.option as opt left join fb_adj_category as cat on (opt.category_id = cat.id)" + 
			" where map.instance_id = ? and map.question_id = q.id and map.option_id = opt.id";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, instanceID);
			ResultSet rs = statement.executeQuery();
			// Should return
			/*
			# QuestionID, QuestionText, OptionID, OptionValue, CategoryID, CategoryValue
			'1', 'When?', '168', 'now', NULL, NULL 
			*/
			while (rs.next()) {
				int catID  = -1;
				String catName = "";
				QuestionAndOption question = getQuestionByID (questionsAndOptions, rs.getInt("QuestionID"), rs.getString("QuestionText"));
				if (rs.getString("CategoryID") != null) {
					catID = rs.getInt("CategoryID"); 
				}
				if (rs.getString("CategoryValue") != null){
					catName = rs.getString("CategoryValue");
				}

				Category category = question.getCategoryByID(catID, catName);
				category.addOption(rs.getInt("OptionID"), rs.getString("OptionValue"));
			}
		}
		catch (SQLException e) {
			renderText("Error " + e.getMessage());
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				}
				catch (SQLException e) {
					System.out.println(time + " SQL exception while closing database connection");
				}
			}
		}

		renderJSON(questionsAndOptions);

		// Save as json 
		// Send
	}


	/**
	 *	Get options (previously used words in the feedback categories, eg. get a list of feelings that have been previously used) 
	 */
	public static void getOptions(String option) {
		// for adjectives, also include the category it belongs to
		// TODO
	}

	/**
	 * Get a list of all the locations in the database.
	 * Note: Not ready yet.
	 */
	public static void getAllLocations() {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;

		try {
			conn = DB.getConnection();

			String query = "SELECT * FROM location";
			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				// TODO
			}
		}
		catch (SQLException e) {
			renderText("Error " + e.getMessage());
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				}
				catch (SQLException e) {
					System.out.println(time + " SQL exception while closing database connection");
				}
			}
		}

		renderJSON("{\"locations\":[{\"coordinates\":\"3\",\"description\":\"coffee room\",\"source\":\"manual\"},{\"coordinates\":\"2\",\"description\":\"room\",\"source\":\"manual\"}]}");
	}

	/**
	 * Get a user's home location.
	 * Use the username as the query parameter, not the user's id.
	 */
	public static void getLocation(String user) {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;
		String resultText = "";

		try {
			conn = DB.getConnection();

			String query = "SELECT l.coordinates, l.description, s.name FROM location AS l INNER JOIN loc_source AS s "
					+ "WHERE l.id = (SELECT home_location FROM user WHERE username = ?) AND l.source_id = s.id";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, user);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				String coordinates = rs.getString("coordinates");
				String description = rs.getString("description");
				String source = rs.getString("name");

				resultText = "{\"locations\":[{\"coordinates\":\"" + coordinates + "\",\"description\":\"" + description + "\",\"source\":\"" + source
						+ "\"}]}";
			}
			else { // user's home location not set
				resultText = "{\"locations\":[]}";
			}

		}
		catch (SQLException e) {
			renderText("Error " + e.getMessage());
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				}
				catch (SQLException e) {
					System.out.println(time + " SQL exception while closing database connection");
				}
			}
		}

		renderJSON(resultText);
	}
}
