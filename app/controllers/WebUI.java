package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import play.db.DB;
import play.mvc.*;

/**
 * API that can be used to get measurement & feedback data from the database.
 * 
 * Used by the admin UI to get data.
 * 
 */
public class WebUI extends Controller {

	/**
	 * Loads the main view of the UI (in ../views/WebUI)
	 */
	public static void index() {
		render();
	}

	/**
	 * A method for getting set of options for given instance (and question)
	 */
	public static void getInstanceQuestionOption(String instanceID, String questionID) {
		Connection conn = null;

		String json = "{\"map\":[";

		try {
			conn = DB.getConnection();
			String query = "select map.instance_id instance_id, map.question_id question_id, map.option_id option_id, map.user_id user_id, map.id as id, o.category_id category_id, " + 
							" c.value category_name,i.name instance_name, q.question_text question_text, o.value option_name " +
							" from instance_question_option_map map, question q, icqa.option o, instance i, fb_adj_category c " +  
							" where map.instance_id = i.id and map.question_id = q.id and map.option_id = o.id and o.category_id = c.id "; 
			query += " and map.instance_id = " + Integer.parseInt(instanceID);
			if (!questionID.equals("-1")){
				query += " and map.question_id = " + Integer.parseInt(questionID);
			}
			query += " order by instance_id, question_id";

			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String q_instance_id = rs.getString("instance_id");
				String q_question_id = rs.getString("question_id");
				String q_option_id = rs.getString("option_id");
				String q_user_id = rs.getString("user_id");
				String q_id = rs.getString("id");
				String q_instance_name = rs.getString("instance_name");
				String q_question_text = rs.getString("question_text");
				String q_option_name = rs.getString("option_name");				
				String q_category_id = rs.getString("category_id");				
				String q_categiry_name = rs.getString("category_name");				

				json += "{\"instance_id\":\"" + q_instance_id + "\"," + "\"question_id\":\"" + q_question_id + "\"," + "\"option_id\":\"" + q_option_id + "\","
						+ "\"user_id\":\"" + q_user_id + "\"," + "\"id\":\"" + q_id + "\"," + "\"instance_name\":\"" + q_instance_name + "\","
						+ "\"question_text\":\"" + q_question_text + "\"," + "\"option_name\":\"" + q_option_name +  "\"," + "\"category_id\":\"" + q_category_id 
						+  "\"," + "\"categiry_name\":\"" + q_categiry_name + "\"},";
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

		// to make the JSON valid, add ]} to the end to close the array
		json = json.substring(0, json.length() - 1) + "]}";
		
		renderJSON(json);
		//renderJSON("{\"Feedback\":[{\"Experiencer\":\"test\",\"Activity\":[\"Sitting\"],\"EventExperience\":[\"Cold\",\"Dry\",\"Dark\"],\"FollowingAction\":[\"Open window\",\"Sit down\"],\"Location\":\"2531\",\"Reason\":[\"Window is open\"],\"FeelingIntensity\":[\"3\",\"2\",\"1\"],\"Overall\":\"5\",\"RelativeTime\":\"now\"},{\"Experiencer\":\"user\",\"Activity\":[\"Jumping\"],\"EventExperience\":[\"Hot\",\"Humid\"],\"FollowingAction\":[\"Open window\",\"Sit down\"],\"Location\":\"2531\",\"Reason\":[\"Window is closed\"],\"FeelingIntensity\":[\"3\",\"2\"],\"Overall\":\"5\",\"RelativeTime\":\"now\"}]}");
	}

	/**
	 * A method for getting set of options for given instance (and question)
	 */
	public static void getOptionsByType(String optionType) {
		Connection conn = null;

		String json = "{\"options\":[";

		try {
			conn = DB.getConnection();

			String query =  "select o.id option_id, o.value option_name, o.category_id category_id, c.value category_name " +
							"from icqa.option o, fb_adj_category c " + 
							"where o.category_id = c.id " + 
							"order by category_name, option_name ";

			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String q_option_id = rs.getString("option_id");
				String q_option_name = rs.getString("option_name");
				String q_category_id = rs.getString("category_id");
				String q_category_name = rs.getString("category_name");

				json += "{\"option_id\":\"" + q_option_id + "\"," + "\"option_name\":\"" + q_option_name + "\"," + "\"category_id\":\"" + q_category_id + "\","
						+ "\"category_name\":\"" + q_category_name "\"},";
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

		// to make the JSON valid, add ]} to the end to close the array
		json = json.substring(0, json.length() - 1) + "]}";
		
		renderJSON(json);
		//renderJSON("{\"Feedback\":[{\"Experiencer\":\"test\",\"Activity\":[\"Sitting\"],\"EventExperience\":[\"Cold\",\"Dry\",\"Dark\"],\"FollowingAction\":[\"Open window\",\"Sit down\"],\"Location\":\"2531\",\"Reason\":[\"Window is open\"],\"FeelingIntensity\":[\"3\",\"2\",\"1\"],\"Overall\":\"5\",\"RelativeTime\":\"now\"},{\"Experiencer\":\"user\",\"Activity\":[\"Jumping\"],\"EventExperience\":[\"Hot\",\"Humid\"],\"FollowingAction\":[\"Open window\",\"Sit down\"],\"Location\":\"2531\",\"Reason\":[\"Window is closed\"],\"FeelingIntensity\":[\"3\",\"2\"],\"Overall\":\"5\",\"RelativeTime\":\"now\"}]}");
	}
	/**
	 * A method for getting feedback messages from the database.
	 * Can use the from & to parameters to filter the messages by timestamp.
	 */
	public static void getFeedback(String from, String to, String user, int limit) {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;

		String json = "{\"feedback\":[";

		try {
			conn = DB.getConnection();

			String query = "SELECT DISTINCT f.timestamp, f.relative_time, adj.value, re.value, act.value, pl.value, loc.description, user.username " +
					"FROM feedback f " +
					"JOIN fb_feeling fe ON f.id = fe.exp_id " +
					"JOIN fb_adjective adj ON fe.adjective = adj.id " +
					"JOIN fb_reason re ON f.id = re.exp_id " +
					"JOIN fb_activity act ON f.id = act.exp_id " +
					"JOIN fb_planned_action pl ON f.id = pl.exp_id " +
					"JOIN location loc ON f.location = loc.id " +
					"JOIN user ON f.user = user.id " +
					"ORDER BY f.timestamp DESC " +
					"LIMIT 50;";
			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String timestamp = rs.getString("f.timestamp");
				String relative_t = rs.getString("f.relative_time");
				String adj_value = rs.getString("adj.value");
				String re_value = rs.getString("re.value");
				String act_value = rs.getString("act.value");
				String pl_value = rs.getString("pl.value");
				String location = rs.getString("loc.description");
				String username = rs.getString("user.username");				

				json += "{\"timestamp\":\"" + timestamp + "\"," + "\"relative_t\":\"" + relative_t + "\"," + "\"adj_value\":\"" + adj_value + "\","
						+ "\"re_value\":\"" + re_value + "\"," + "\"act_value\":\"" + act_value + "\"," + "\"pl_value\":\"" + pl_value + "\","
						+ "\"location\":\"" + location + "\"," + "\"username\":\"" + username + "\"},";
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

		// to make the JSON valid, add ]} to the end to close the array
		json = json.substring(0, json.length() - 1) + "]}";
		
		renderJSON(json);
		//renderJSON("{\"Feedback\":[{\"Experiencer\":\"test\",\"Activity\":[\"Sitting\"],\"EventExperience\":[\"Cold\",\"Dry\",\"Dark\"],\"FollowingAction\":[\"Open window\",\"Sit down\"],\"Location\":\"2531\",\"Reason\":[\"Window is open\"],\"FeelingIntensity\":[\"3\",\"2\",\"1\"],\"Overall\":\"5\",\"RelativeTime\":\"now\"},{\"Experiencer\":\"user\",\"Activity\":[\"Jumping\"],\"EventExperience\":[\"Hot\",\"Humid\"],\"FollowingAction\":[\"Open window\",\"Sit down\"],\"Location\":\"2531\",\"Reason\":[\"Window is closed\"],\"FeelingIntensity\":[\"3\",\"2\"],\"Overall\":\"5\",\"RelativeTime\":\"now\"}]}");
	}

	/**
	 * Get a list of all the users in the DB (to show in the UI for the user to choose which one's feedback to look at).
	 */
	public static void getUsers() {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;

		String json = "{\"user\":[";

		try {
			conn = DB.getConnection();

			PreparedStatement statement;
			String query = "SELECT * FROM user ORDER BY username";

			statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String username = rs.getString("username");

				json += "{\"id\":\"" + id + "\"," + "\"username\":\"" + username + "\"},";
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

		// to make the JSON valid, add ]} to the end to close the array
		json = json.substring(0, json.length() - 1) + "]}";
		renderJSON(json);
	}

	/**
	 * A method for reading the measurement data from the DB.
	 * 
	 */
	public static void getMeasurement(String from, String to, String sensor, String variable, int limit) {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;

		if (limit == 0)
			limit = 100; // default amount to limit the amount of rows returned

		String json = "{\"measurement\":[";

		try {
			conn = DB.getConnection();

			// code here horrible ugly as it's hacked together in a rush :(
			PreparedStatement statement;
			if (sensor != null) {
				String query = "SELECT m.*, ms.type, ms.name FROM measurement AS m, m_source AS ms" + " WHERE ms.id = m.source_id AND ms.id = ? ";

				if (from != null && to != null) {
					query += " AND timestamp BETWEEN ? AND ?";
				}
				if (variable != null) {
					query += " AND m.variable = ?";
				}

				query += " ORDER BY id DESC LIMIT ?";
				statement = conn.prepareStatement(query);
				statement.setString(1, sensor);

				if (from != null && to != null && variable != null) {
					statement.setString(2, from);
					statement.setString(3, to);
					statement.setString(4, variable);
					statement.setInt(5, limit);
				}
				else if (from != null && to != null) {
					statement.setString(2, from);
					statement.setString(3, to);
					statement.setInt(4, limit);
				}
				else if (variable != null) {
					statement.setString(2, variable);
					statement.setInt(3, limit);
				}
				else
					statement.setInt(2, limit);

			}
			else { // get data from all sensors
				String query = "SELECT * FROM measurement AS m LEFT OUTER JOIN m_source AS ms ON m.source_id = ms.id ";

				if (from != null && to != null) {
					query += " WHERE timestamp BETWEEN ? AND ?";
					if (variable != null) {
						query += " AND m.variable = ?";
					}
				}
				else if (variable != null) {
					query += " WHERE m.variable = ?";
				}

				query += " ORDER BY m.id DESC LIMIT ?";
				statement = conn.prepareStatement(query);

				if (from != null && to != null && variable != null) {
					statement.setString(1, from);
					statement.setString(2, to);
					statement.setString(3, variable);
					statement.setInt(4, limit);
				}
				else if (from != null && to != null) {
					statement.setString(1, from);
					statement.setString(2, to);
					statement.setInt(3, limit);
				}
				else if (variable != null) {
					statement.setString(1, variable);
					statement.setInt(2, limit);
				}
				else
					statement.setInt(1, limit);

			}

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String timestamp = rs.getString("timestamp");
				String m_variable = rs.getString("variable");
				String name = rs.getString("m.name");
				String unit = rs.getString("unit");
				String value = rs.getString("value");
				String type = rs.getString("ms.type");
				String source_name = rs.getString("ms.name");

				json += "{\"timestamp\":\"" + timestamp + "\"," + "\"variable\":\"" + m_variable + "\"," + "\"name\":\"" + name + "\"," + "\"unit\":\"" + unit
						+ "\"," + "\"value\":\"" + value + "\"," + "\"type\":\"" + type + "\"," + "\"source_name\":" + source_name + "},";
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

		// to make the JSON valid, add ]} to the end to close the array
		json = json.substring(0, json.length() - 1) + "]}";

		renderJSON(json);
	}

	/**
	 * Get a list of all the sensors in the DB (to show in the UI for the user to choose which one to use).
	 */
	public static void getSensors() {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;

		String json = "{\"sensor\":[";

		try {
			conn = DB.getConnection();

			PreparedStatement statement;
			String query = "SELECT * FROM m_source ORDER BY type, name";

			statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String type = rs.getString("type");
				String name = rs.getString("name");

				json += "{\"id\":\"" + id + "\"," + "\"type\":\"" + type + "\"," + "\"name\":\"" + name + "\"},";
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

		// to make the JSON valid, add ]} to the end to close the array
		json = json.substring(0, json.length() - 1) + "]}";
		renderJSON(json);
	}

	/**
	 * Get a list of all the measurement variables in the database.
	 */
	public static void getMeasurementVariables() {
		String time = String.format("%1$TF %1$TT", new Timestamp(new Date().getTime()));
		Connection conn = null;

		String json = "{\"variable\":[";

		try {
			conn = DB.getConnection();

			PreparedStatement statement;
			String query = "SELECT DISTINCT variable FROM measurement ORDER BY variable";

			statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				String id = ""; // rs.getString("id");
				String variable = rs.getString("variable");

				json += "\"" + variable + "\",";
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

		// to make the JSON valid, add ]} to the end to close the array
		json = json.substring(0, json.length() - 1) + "]}";
		renderJSON(json);
	}

}
