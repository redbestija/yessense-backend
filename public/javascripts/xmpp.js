// test types. Set by JavaScript
var send = false; // Send test
var roundtrip = false; // Roundtrip test
var receive = false; // Receive test
var pubsub = false; // PubSub roundtrip

var timeout = 0; // timeout if needed to avoid overload
var numberOfruns = 30; // Default value for number of runs
var runCounter = 0; // current run counter

var messageArray = new Array(); // Message Queue
var messageCount = 10; // Amount of messages to be sent (Default value)
var messageType = 10; // Payload size in bytes (Default value)
var messagesOut = 0; // Outgoing message counter
var messagesIn = 0; // Incoming message counter
var messagesToReceive = 10; // number of messages to handle in receive test (Default value)
var failedMessages = 0; // Failed message counter

var username; // username used in connection (wstest or boshtest)
var password; // password used in connection (d91656ba5aef841a8c70dabd6b844bc4)
var resource;// resource used in connection.
var domain = 'ubiqloud.io' // server domain

var ownJid; // the full jid

var destJid; // target user. for roundtrip test, make sure to use the same value as the ownJid (set by javascript).

var boshhttpbase = 'http://kallesaila.fi/http-bind'; // bosh connection url
var wshttpbase = 'http://ubiqloud.io:7070/http-bind'; // websocket connection url


var pubsubBase = "pubsub.ubiqloud.io";
var pubsubNode = "xmppbenchmark"
var nodeSubscribed = false;

var testStarted; // timestamp
var testEnded; // timestamp

// messages
var message10 = '459b4f30a6';
var message100 = 'cf5af92083023d851b5c959cfd8a3bc7e546df649cd69b47b3669fb211cc0465e85b6fa1cfb1fe6559549b64d4c5921be6a4';
var message1000 = 'd2c9adb858e91a98dc77c23f849f12dbaf84812a601c1ec4de718a11f2aab10bd13842da7c471372687653e7d589cc1db2ae3051b246ab86dd2446a75cb5e86f75419b13993bd1306bbcc57ea55555d8a860d449cbc7170d250dbda3264422b590e5b49b5612f0a187a37f2b7eb5f14dc04571046e9a422e0a951eb29c25a933ad4bb3fb719c5fd9214edf9d4615d6896b61dd5615cac9d01dbf929b200c8eb8dbe5d5e8622f47010b2aa664d18078f0a2e242228f4b18413880f01c770dcb5915d90ec9bf4caae5bf4da02db1833b9ee8ca0e591bd6f49fded42a81895faf3af016b28461724bd1e7f64b3eaac09907070f815f3d4b24998ed61590062da2085997de6def587211ccf1cbeb964feed41d58e91e1fb6db942f6c3dd1f4003ad177f443db61374b1428122b4295a337a9fcb8df925fd09ec83d0c042cad7491ca9339dc712cb8ac8022ca055b44d00d0c00cf737a0d10d8bca195beaa19bf484988494e0cda17b4ee818218527ade5464ab145affd302459f74458eb6ab553cd8c0e3d296d5155d79afaa831cb43f5492ba77b5aabd560c1b92d89fc848a90939f2485eee7a9bb8ae452125519effe30f3a51d690a6f8bc57fb6cebfd12077c8d14cb2576504e6016410352a55ca0f40be20419ff267572070d67808e03463d800740d9f578ea5f6e9071b4a61ad3efd50f86b955';
var message10000 = '73848682a2c1b2f64cd7d91c3249b1c265a608cf5824780ffccaa0e14213f864d4933e38255dee9d2a54a824502fe1f3940733a42ebb3aca760f9e62a300d54ba48ae0b645409ffe6df59535034f9cdae4c44d716b55131fd22e4b30812458f00fb0ee28d0d1a90291c848506a6aa80dba1585f7e85fbacc6ca7231ec699c063f933a1e60b6adbab312d0b7536a7032421d6f0e8be8921f84faa6aa9f89f2fa3562efcad503b457ed7b5b491421fb18deaab5f7e99885c7b534ca574c184b0f6a9d551575b5dd0e6eb243e4d40caf889f450de96dd086ed264bcb405378f08c9bb9cb4d6b0ecf029126d4e35fe1a3f327d40001c9d339480672f3831821c3ae78ff6425634db9f047e4934fa9f81507ceef60ee6c684e07d8d5673dc8b12eb96f5353489c2da26ebc062546da489c1fa3b475c93599d29ad378b7551a54044a05d11aa3be63f3e18899d25997bcc35070c78f8af714897265637f81346bab78994d18777c43fa7a0164384b944d0bc2b435f2f8381dc87bbfd332b6ebf710ba736c1d6b7764f191453babf2a6bd8f7f2613665a6b1edee83bbc7e7650f90eeea66fe7d8689ed098aea0f556db6325b90014ec39927b458dea694dedadaf7aaf92a7748a9b57df255c80d508b716322c351e445c82d4de0eb1b0a15840ee797280bfbc9a467810eb11f09e4110d6feb1fe2ca262aa86f79958d1d1d6b0b90331f5141a626e28b60b590ae11ae03ee23a96e0932fa00036d229b3332040d74ce66aa51acd1cce6429d4269eede517b6079bbea4448e91448d63bf21a5f375b01773eb58afc1c72d35fb462184707f77d9c264eb345b8b02f89f47d600dcf9b31859468144a313977616b2e5d06a664c37702663a7e985d3d662fdc0e152bb4f0a34c8f019c45ce03a84535ba3d3b60f8c95b8aecca0706ba9cea1ca5be74e4a4ccd8f12e92d119e2cd5d6a3c507c92a7895f3cdaf40f83ef471a460342b6fa798e85274f9b34daf8005bbfb44ae27615bc3e4f1a05524a094c27729f37f9845510aa2f42b5c73aad164e229ebc274aa6fcef3a32643fd08d405b411bc46fcf20d268aa8db6dbb4bc7c8331d4eb5f0cc5a04647808aa60c0886aa88cd697df7bad21776d2af341425fa63fad69fedf0e070988cd39f4296b1d5b995e863767b233f7509cfb3bc6aa54378115091cdb0fefc40d6fecf2fdce414f1b6fcb73828448cf464bc0f07d443b67361a5d7a16a6d0cb4a0cd1972ee6545dade07363edb5f8993b5d439c03179aa6440067b29dda0a2320f0d18de1ac08888c867eb9542e4ceddf584db7ea22ae23186de18b4f6b7805fab9cdbc4c73a4144a02d67af0bd4867eb79c106cae648d3603b342b011294ef010b9ef984059fee423e9da4bf5322691323fa952f9baa559a667367d8688086fad0d37cb3203a02420df6528b23331c6d2d7ea6232e236e933a335044579ff3c4bd4e3cc0a1d7406d6bb53ce48c3a4e5c4da7abf4a4f3da7949f0094d9ad6a341a289e8beeaae5876fa0a6cd27b09b656323b90cc3a2330d4eb22aa1ed4e6189c9ebdf40c6659fd47d3181f4a8a41ac0b6f8491ef5e4c8d227d5a3c7900af8612885bc65fff0150e6c235ddd67b50d47ebcd4b11a9f4393fb8c6151bd9212530c884f339104d796fbb4c8539e08a29c81da9c86a3f3da49f5a49f5829ebe393a6ddc2378735fd8691f850285f46d696357420945b4ec8754ae56c709cbad2f0ab126a0991d8d6b3f6b44e925babc8f3d819fe54f90eee368b1768f5545c00f926879cfa2f5de99599a103e90f4e709402f1510403b2a7b4b52297114f93b0c5fe5cdc010c8aade206e96f82d7dcba5ca9c61ef885a3e09d329dfc5cdd6218944a90b107e65e967bb434e359be7e7f9a681823a6e8dbaa863d68cc09b26208627594a8088415442d67c45706443543d1f2c68f52ed6f1b8641d4c70c93f096e337570babcc844ad6920bfe09719a4554c0919a03038ebb9a2bc95a5af05379658871862627cd33af3731e438a16801440c7d2ff8688c4b97f7920a4ead2a599a53af954ce3edf92e1c02c2f9130b62256b5414ba15deb642b9f58889f514bbb295f771fffdfeb874dcdfb61c79e9f318c40575de80e94b65f5bacbe1d7bab664ba8d75d7e2c985b1625e665b43c9046671f74cda618ab0143223041e2633b0e2fa5f71f839b4dfbdddc895290f5633463a7953359406e3f1a2f8fa77864c2ebb3383cdaf6aa8db0a4c737749c1d5a78eaaa915d8f03409ad8b0b89cadd4e277174c7dc1172d74acb600aabaf58d3c9be066f3ef43fac8064c6243bb13956b3a75a3964299add0a5c86d30d55a90c029a07d5b6904055ead5877d2eb203d42d44400cd4351e62aa99f21c83aeb602a3f5012c3f2b754a96951c7a7f6969ba08462bcd118cb9ea1792425dd057ebe57e4ead364d60c86940628efcb9aa264343cf334c28e697ebcc1a2383a3b4f78d2d3360e0ea8cd1d7dbd73de03eff8cf992e376c4535f011f6aa74d72cfa34321d42e9f042e3ffb14f3b4dca69c68815377cc58cdcec37431b78ed020dfa7becb5dd081fdff4d06eb01ce31573184bbf700ee5ca318c423df108caac0333e3646923601c65920714a64c7bdec4ff1fecd76f3d21ddcf409704b6cfb4a5b6e408388e5186c2905631316d20c107f5509ccedb66cf07cad5b73fc9497db8a01ea4bce8f3fd3c51a22921479055e82b436f8cf04808676540783dce30ab716e58bf25691498829817bf57a35f71527bfad560e3ece401effd0fef60875fd080fb1564d843d006763c21fd71ced829dcd6f4c7830d29a4802a32830f3996853756cb127f45c95360d5e951060d1fa084e7c8128ea3383f8c7665d06c27aa3f5ddc44add7e3c3e07b14a942facfa1d7b78f2327eb9da95bcf0e39c25601ac471db36daa80291e15634f4d74105fbea57e42037ab01863156095a9f8430574e4e164d39ed4895a3f75efa8d125a2c9b4b9e5d9ed4f126272f017d58a6c03a325d8d026fe99e8a57c4f4076a28028a1a3dbd42084c10458165c43c5a788b39011766715cd44270de80906861f9eb62d835e97c64d4da3087156ba9802024ddaaa9f892128dae57347674e49c0ecfda8f10042b0668b7eb767a12796dfe93458426d6fcc689e3b14e13de4e7032cde32506b634b75215cb4beb064f6994c5b35cac26d04d6ee201e3f536ac40d2e3f76d80dd25ada30ace464069a366f828469c1b53f68c166dc67c5bc3df0548b8072a7530291cc73ad74427bef302d779cf235b85ca0e9d6a293a1fe25b4b9a3d6084a589f25180768f70eed8b482bf9749919076b0f0fc0cc1568ac3d450eda97c3036faa4098f12f831a864d8b4306cf7ca18599b4cecd8376af894e4a161a6ffff1b45a6b6ff714ed72976f6b1c86a9c18455c98956894645f1bacaf25a98a89d6cc5c2d66a483d610eae62b64742c233d9c1187d7516103327fdaef2e7b65e51f72f43d8aa821b01bf6a8434b257fdaf7ed9f8d4e4b59c677b051e52b45eaa739956de91723888664f3c4a6e5c6a5e6cd8733b4b6a0aa501a871df951d6ee150b50e8cbe602523568a4018ea3e1b2e4f80e89eb59a8668fe146d32a64f894078ed7e3ff3e9598bde98e628a796c16eaf44a1744db0cbb6da1a96d0adce9523f0f481e733c699c9b155255455791aaed770a9595507c5b28e79fc6a73d05d63dd44ef20e4f3c001aa9184ca384f3729016dcb3cdc00b6f303db6d4f42236a25307a489611eb02a50e5a8b3123e261bc89012a18445027416a370bbde352b1a29c627318ec164ca01c72633eb10524bf4396e296598635e7298a155cd39fc73cfadfbb01800ffbffefd4d8df3bab3b200497e4db1805cd42d7048e14b450f0791e55f61d7eb218e548d89b260494a4669e397244f2ebeb5564d683f14d5104a35faad18669f9f087ed42f819e0e988a8daec1c17af99facfdce031d4e1d8d2eae5958b7a3076d2c53ebf1afade7a0e3c9028af3ba34623e307ae3c22a3703eeff658553f8a8e03084f1b04a12f790225d1837397592ed890aa9750112d99a004c7089e4fff1f2b8cdd8d02815dd914326291d7ec28029ca6b23a9d435f6e492d921ee51454677e3f64c52b85788cf9c2eb6aec15a29ee8c101ebd2fcce66dfe8692c53baf45c706f3841f01ccb2b6c63b4df1b3411f195c493e10db5ddf7691042520f8c0f2e13b387bda8a0c7745d83ec080b14ef5421cd16a3a8432709f63d2b1a5b7909d83475c0e05d3e8edc1ea993b00e57e76a50bc08498bfeee568268c2cae61d61ab1254f70c3a2816616ce660be862d39272cd751a6b308b8907dfa264387109b5fc57a26887beb10ddad694f3b3baeb7c340113eb2e9c3688184a04174b713f2b30b693942283ccbba2a966e21a9ac6bfa25c81d5c2fc4e990a48c08ab8a45b45a48f718799dc1da4e28c5694c0fa9fcdb56a90da56349baa7d2f83ad254b68d1f3df7313969c2415ae931a49d15429c5e808eee199beaebc2c02f3cce1329ac1cbe58da07c74b37042fa4cfc16341f63cc33cbb4f451673ecf3359643e3c5044079ecdc7653f0e9d655e7538d5bc2770414451233ef5354ec039970b5bda58f1c4f32c0d57bf34f454b6c50902e696e0f833829ce0dce22ac22767eb7ab5582373d6b3f00135a4302c558d7e5b52e18be3065f4765f472f3259fd67237cabc673bb5feb63975472e15b81c5f3d7293ad470da3b58f89edeceabd8a6b405217cd17c35499c11671e9d596556ad26d8cd5855e59898684c02e5db5c39eaa4e86980623f3befd247b7c19fc23689b66a10b6bff95a47b103e2a8287f0e65e2cd7317e17bac09eb6b0ed929eef5f1299cf1b909ed1b2e2371784921133c1c8f005a88aa16e3c2e9a3a1f671169fa3d60212ade152c83a27a406bc28874114736e08a996d13f966c9e2133b73413d5997fa1d3122fa92699fcecce152fd525208d389126796dfcbd077d3666e896b548eaaab2713853503ea78aa8fc59857bd17851643159c70a64fca48fe0895eb4785d52688ecfa3deeac6b6d2692807f1b5a90f141ba43a91f61504d20eb4f85473fa2cd44c49a65dba6e8ea3444a50569250689d6e33ab10ec793d759754c39f99a1f5b58b3bb0d541c187f580b235d7157b53a95aee3ef5942822b69e96c0c69fc1df3de8b278e84c301762facdf31cb32592927cb9f7b85bff9b03d89fc063b6a98fbceb4c5ab3fe205dcbd9152c370df794cb1f57998045f226443a9aefcf7e742a2b8ba0b1a26c97e1982b508f227192a887fda4be2351135cfff92b5aeed4c5ef892fcbb9a8fe1ed324665cb34799bed0c47ef5522bb7719a70799e2d0ae7b35a44558dc20b0a25da941fa4a142998b1f16eed6f2462e00c905334526753c0a36f6678cca675f91c30ac4fb7bd3856ac40136c3a98220658ec85ba910db94c76130af8e5f2b8a10240a98a9156bcd4b9e1fffd642ee824df305331119b3232dff22662f7f805a781117672881fee15f625ad684f95cce97d5bdf5bed098fcb55a86cd3ad27ed00c4191392c9c0037f6b0261271002c7e8050f9e3a0640b468af7fdfd15896e5d400b896e6b1d9d4f397317845428c58363ea0cb3208edc36b91e820fcc2ba05d13c15a93517b1a9fbd0ae2f9719c6786ffa908694fa122efff249ace5d3e5f5d0848ba4a48d069667fac10e82a5a905535a8796221b0350498dfe1f0739e39926b1a6dd82474675188e53a3982ecad1ec366e9109af515aafc62a99661eed259c8b6446703541f532610a2e5ee3956ec148b815f593f717bdc9caa067fd1e7ee8c122623abe33651c4fbaf9981d52a2860cd95e81b60c4cd36b08b4655e43f3a3f5f403d324d9ae8ca31c30878ed42d6f9fe9d60fa3cfdc28187499dcbc13e6ff74de36141dc674ba686aecbf558603a890240de27f48f8a3b3024dfee244681b487d1a2f54988de0d82879d19b51144da81a8496e50381ccda9dbd7e903a4a723e8474308ca99193353c28e3101f35b19f0fef76bb89f7409274bd03c71e7d3c5ce506ddac4ac9f93924ede466f92ea444cdf5e99dff31ef6d35189e836772d89d78d93e201f36ac5026fbfed37b7f1ce985dd837f05cee56e68843faf02e10ad6cd0483669b63e5a27886eb899f274ebb6c375d7947364a2790926dfb997c5e809e6c12d8ee18ef5790732a73d18f9114a1f04c75a4e07236de608e62c1caa861655f5a5ab293040c2a3ec00b5330b3bdf9e12d501f361280496060c7c51dd55fd0f1b1b849b72279aa26b509c41510ad5ddd26ba27f059cc9cb98b4523704a75f3ece14dc498f69eed3084cdda5e7e7e20194a8f2c5e93ef23d6cd878e687e709e0ea03f4ada56d15419b42281653fa2de19e9e5f9e157c15c204ec454911b9fa8f1fe6f9e5c599ce015101827c730451c7b07cd644c6250532d05904ac9dd975128ed3220cdfe112c54b311be36e906b83383d91c733d6be7b1388e70d704daf49de8bd4bf43b7eb5978c120cec4433a3546e0c01ac57c3f32a4679140ca325caddb11f87cd39ef23c8f2eb6731bde8bc5a969d57eaba68f126bafc029ce00e481eded551496776363a5913018a56dff64ca64152eb3bf2298bd788c3ed8271ec1c3c11b4f7870ed386c44402ca3ff383e1a6f434cb1a95ced94539c8981dd17676f5983d8d1285a3bb0815b3c9e093e0006d2c85276c05f529926fc736ac286983c6cbb97e07d9e93f9a308619a55a5f7eb9786f98a1f3e5488c060d0758a0f61ced120e256fa4a8d37b31b7ab05b560a2618f6fe1be41e8fd8ca3c2635498ac07b7387c8ca3b6af63320efb35cd369c84098f165b2e5aeb88d7e6ad38831faa0bde0676fe024b5c1823578787bb90e0025e14ec25cc21aeb5cb25956ba7857944e60b3b24fe94b1c2d08682d67adc34a51823d7dd31be627b02babc89364d687e4fd3201e5673680c0efca79d52b75a7b4f71166dd088904becfd090ae7b4abdd674a3a3581f162b19859aed23f22a1e648ed318e1942beb95869dc688ae620655';

function initMessageQueue() { // Init the message queue before tests


	var msgBody;
	if (messageType == 10) {
		msgBody = message10;
	} else if (messageType == 100) {
		msgBody = message100;
	} else if (messageType == 1000) {
		msgBody = message1000;
	} else {
		msgBody = message10000;
	}
	//console.log('message body: ' + msgBody); // print the payload to verify that the right one is selected
	var aMsg;
	if (pubsub) {
		aMsg = $build('data', { 'type' : 'message' }).t(msgBody).toString();
	} else {
		aMsg = $msg({
			to : destJid,
			from : ownJid,
			type : 'message'
		}).t(msgBody);
	}

	for (i = 0; i < messageCount; i++) { // init queue
		messageArray.push(aMsg);
	}
	//console.log('array length: ' + messageArray.length); // check that the queue is full
	if (pubsub) {
		if (nodeSubscribed) {
			publishMsg();
		} else {
			connection.pubsub.subscribe(
                    ownJid,
                    pubsubBase,
                    pubsubNode,
                    [],
                    onEvent,
                    onSubscribe
                    );
		}
	} else {
		sendMsg();
	}
}

function onEvent(msg) {
	var delay = $('delay', msg);
	if (pubsub && nodeSubscribed && delay.size() == 0) {
		//$('#iResp').append('<div class="msg"><b>Message from '+from+'</b><br />'+$(msg).text()+'</div>');
		messagesOut++; // increase counter by one
		if (messageArray.length == 0) { // last message
			testEnded = Date.now();
			console.log('run number '+Number(runCounter+1)+' pubsub roundtrip test ended. Elapsed time: ' + ((testEnded - testStarted) / 1000) + ' seconds.');
			console.log('messages out: ' + messagesOut
					+ ' - failed messages: ' + failedMessages);
			runCounter++;
			if (runCounter < numberOfruns) {
				reInitMessageQueue();
				return true;
			} else {
				$('#test-button').removeAttr('disabled');
			}
		}
		if (messageArray.length > 0) { // Queue not empty
			try {
				var toSend = messageArray[messageArray.length - 1];
				messageArray.pop();
				connection.pubsub.publish(
					      ownJid,
					      pubsubBase,
					      pubsubNode,
					      [toSend],
					      onPublish
					    );
			} catch (e) {
				console.log('send failed: ' + e.message);
				failedMessages++;

			}
		}
	}
	return true;
}

function onSubscribe() {
	nodeSubscribed = true;
	$('#iResp').append('<div class="msg">Subscribed to '+pubsubNode+'!</div>');
	publishMsg();
	return true;
}

function reInitMessageQueue() {
	messageArray = new Array();
	messagesOut = 0;
	messagesIn = 0;
	failedMessages = 0;
	
	if (roundtrip || send || pubsub) {
		initMessageQueue();
	}
	
	return true;
}

function initTest(form) {
	$('#test-button').attr('disabled','disabled');
	roundtrip = false;
	send = false;
	receive = false;
	pubsub = false;
	
	numberOfruns = $('input[name="run-type"]').val();
	runCounter = 0;

	messageArray = new Array();
	messageCount = $('input[name="count-type"]').val();
	messageType = $('select[name="payload-type"]').val();
	messagesOut = 0;
	messagesIn = 0;
	messagesToReceive = $('input[name="count-type"]').val();
	failedMessages = 0;
	
	var testType = $('select[name="test-type"]').val();
	if (testType == 'roundtrip') {
		roundtrip = true;
		destJid = ownJid;
		initMessageQueue();
	} else if (testType == 'send') {
		send = true;
		destJid = $('input[name="test-destjid"]').val();
		//destJid = ownJid;
		initMessageQueue();
	} else if (testType == 'pubsub') {
		pubsub = true;
		initMessageQueue();
	} else {
		receive = true;
	}
	
	return false;
}

function onPublish() {
	return true;
}

function publishMsg() {
	if (pubsub && nodeSubscribed) {
		try {
			var toSend = messageArray[messageArray.length - 1];
			messageArray.pop();
			testStarted = Date.now();
			console.log('run number '+Number(runCounter+1)+' pubsub roundtrip test started ('+messageType+' bytes / '+messageCount+' messages).');
			connection.pubsub.publish(
				      ownJid,
				      pubsubBase,
				      pubsubNode,
				      [toSend],
				      onPublish
				    );
		} catch (e) {
			console.log('intial send failed: ' + e.message);
			failedMessages++;
			return false;
		}
	}
	return false;
}


function sendMsg() { // Send the initial message for send and roundtrip tests
	if (send) {
		try {
			var toSend = messageArray[messageArray.length - 1];
			messageArray.pop();
			testStarted = Date.now();
			console.log('run number '+Number(runCounter+1)+' send test started ('+messageType+' bytes / '+messageCount+' messages).');
			connection.send(toSend)
			while(messageArray.length > 0) {
				try {
					var toSend = messageArray[messageArray.length - 1];
					messageArray.pop();
					connection.send(toSend)
				} catch (e) {
					console.log('send failed: ' + e.message);
					failedMessages++;

				}
			}
			testEnded = Date.now();
			console.log('run number '+Number(runCounter+1)+' send test ended. Elapsed time: ' + ((testEnded - testStarted) / 1000) + ' seconds.');
			if (destJid != ownJid) {
				runCounter++;
				if (runCounter < numberOfruns) {
					reInitMessageQueue();
				} else {
					$('#test-button').removeAttr('disabled');
				}
			}
		} catch (e) {
			console.log('intial send failed: ' + e.message);
			failedMessages++;
			return false;
		}

		return false;
	} else if (roundtrip) {
		try {
			var toSend = messageArray[messageArray.length - 1];
			messageArray.pop();
			testStarted = Date.now();
			console.log('run number '+Number(runCounter+1)+' roundtrip test started ('+messageType+' bytes / '+messageCount+' messages).');
			connection.send(toSend);
		} catch (e) {
			console.log('intial send failed: ' + e.message);
			failedMessages++;
			return false;
		}

		return false;
	}
	return false;
}

function onMessage(msg) {
	var to = msg.getAttribute('to');
	var from = msg.getAttribute('from');
	var type = msg.getAttribute('type');
	var elems = msg.getElementsByTagName('body');

	if (type == 'message') {
		var body = $(msg).text();
		//Log the messages if you want, effect on performance for roundtrip tests is insane
		//console.log('I got a message from ' + from + ': ' + $(msg).text());
		if (roundtrip && from == ownJid) {
			//$('#iResp').append('<div class="msg"><b>Message from '+from+'</b><br />'+$(msg).text()+'</div>');
			messagesOut++; // increase counter by one
			if (messageArray.length == 0) { // last message
				testEnded = Date.now();
				console.log('run number '+Number(runCounter+1)+' roundtrip test ended. Elapsed time: ' + ((testEnded - testStarted) / 1000) + ' seconds.');
				console.log('messages out: ' + messagesOut
						+ ' - failed messages: ' + failedMessages);
				runCounter++;
				if (runCounter < numberOfruns) {
					reInitMessageQueue();
					return true;
				} else {
					$('#test-button').removeAttr('disabled');
				}
			}
			if (messageArray.length > 0) { // Queue not empty
				try {
					var toSend = messageArray[messageArray.length - 1];
					messageArray.pop();
					connection.send(toSend);

				} catch (e) {
					console.log('send failed: ' + e.message);
					failedMessages++;

				}
			}
		} else if (receive) {
			//$('#iResp').append('<div class="msg"><b>Message from '+from+'</b><br />'+$(msg).text()+'</div>');
			if (messagesIn == 0) {
				testStarted = Date.now();
				console.log('run number '+Number(runCounter+1)+' receive test started.');
			}
			messagesIn++;
			if (messagesToReceive == messagesIn) {
				testEnded = Date.now();
				console.log('run number '+Number(runCounter+1)+' receive test ended. Elapsed time: ' + ((testEnded - testStarted) / 1000) + ' seconds.');
				console.log('messages captured: ' + messagesIn);
				runCounter++;
				if (runCounter < numberOfruns) {
					reInitMessageQueue();
				} else {
					$('#test-button').removeAttr('disabled');
				}
			}
		} else if (send) {
			//$('#iResp').append('<div class="msg"><b>Message from '+from+'</b><br />'+$(msg).text()+'</div>');
			messagesOut++;
			if (messagesOut == messageCount) {
				testEnded = Date.now();
				console.log('run number '+Number(runCounter+1)+' send test got final message. Elapsed time: ' + ((testEnded - testStarted) / 1000) + ' seconds.');
				runCounter++;
				if (runCounter < numberOfruns) {
					reInitMessageQueue();
				} else {
					$('#test-button').removeAttr('disabled');
				}
			}
			
		}

	} else if (type == 'error'){
		$('#iResp').append('<div class="err">Error of type '+$('error', msg).eq(0).attr('type')+' received </div>');
		
	}
	//document.getElementById('iResp').lastChild.scrollIntoView();
	// we must return true to keep the handler alive.  
	// returning false would remove it after it finishes.
	return true;
}

function onIq(msg) {
	//console.log('iq: ' + msg);
	return true;
}

function onPresence(msg) {
	var from = msg.getAttribute('from');
	var type = msg.getAttribute('type');
	if (type == null || type.length == 0) {
		$('#iResp').append('<div class="msg">'+from+' has become available!</div>');
	} else if (type == 'error'){
		$('#iResp').append('<div class="err">Presence of type '+type+' received from '+from+'</div>');
	} else {
		$('#iResp').append('<div class="msg">Presence of type '+type+' received from '+from+'</div>');
	}
	document.getElementById('iResp').lastChild.scrollIntoView();
	return true;
}

function pingHandler(ping) {
	console.log("Received Ping: " + $(ping).text());
	connection.ping.pong( ping );
	return true;
}

function onConnect(status) {
	if (status == Strophe.Status.CONNECTING) {
		console.log('Strophe is connecting.');
		$('#iResp').append('<div class="msg"><b>Connecting...</b></div>');
	} else if (status == Strophe.Status.CONNFAIL) {
		console.log('Strophe failed to connect.');
		$('#iResp').append('<div class="err"><b>Connection Failed!</b></div>');
	} else if (status == Strophe.Status.DISCONNECTING) {
		console.log('Strophe is disconnecting.');
		$('#iResp').append('<div class="msg"><b>Disconnecting...</b></div>');
	} else if (status == Strophe.Status.DISCONNECTED) {
		console.log('Strophe is disconnected.');
		$('#test-logout').attr('disabled','disabled');
		$('#test-button').attr('disabled','disabled');
		$('#test-login').removeAttr('disabled');
		$('#iResp').append('<div class="msg"><b>Disconnected!</b></div>');
	} else if (status == Strophe.Status.CONNECTED) {
		console.log('Strophe is connected.');
		$('#test-login').attr('disabled','disabled');
		$('#test-logout').removeAttr('disabled');
		$('#test-button').removeAttr('disabled');
		$('#iResp').append('<div class="msg"><b>Connected! Lets get ready to rumble...</b></div>');

		connection.addHandler(onMessage, null, 'message', null, null, null);
		connection.addHandler(onIq, null, 'iq', null, null, null);
		connection.addHandler(onPresence, null, 'presence', null, null, null);
		connection.send($pres().tree());
		// Uncomment to send ping requests to server
		//connection.ping.ping( domain, function() {console.log('ping sent')}, function() {console.log('ping error')}, 6000 );
		connection.ping.addPingHandler( pingHandler );
	}
	document.getElementById('iResp').lastChild.scrollIntoView();
}

function connectTest(form) {
	
	username = $('input[name="test-username"]').val();
	password = $('input[name="test-password"]').val();
	resource = $('input[name="test-resource"]').val();
	
	var manager = $('select[name="test-manager"]').val();
	
	if (manager == 'websocket') {
		connection = new Openfire.Connection(wshttpbase);
	} else {
		connection = new Strophe.Connection(boshhttpbase);
	}
	
	// Uncomment the following lines to spy on the wire traffic.
	//connection.rawInput = function (data) { console.log('RECV: ' + data); };
	//connection.rawOutput = function (data) { console.log('SEND: ' + data); };

	// Uncomment the following line to see all the debug output.
	//Strophe.log = function (level, msg) { console.log('LOG: ' + msg); };

	ownJid = username + '@' + domain + '/' + resource;
	connection.connect(ownJid, password, onConnect);

	
	return false;	
}

function disconnectTest(form) {
	nodeSubscribed = false;
	connection.disconnect();
	return false;
}

function toggleField() {
	var type = $('select[name="test-type"]').val();
	if (type == 'send') {
		$('#hidden-field').slideDown();
	} else {
		$('#hidden-field').slideUp();
	}
}

onunload = function() {
	connection.disconnect();
};