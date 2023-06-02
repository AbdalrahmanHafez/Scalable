package app.media.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import app.media.commands.Command;
import lombok.extern.slf4j.Slf4j;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class MediaApplicationController {

	public static boolean isPaused = false;

	@Autowired
	private MongoClient mongoClient;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private Map<String, Command> commands;

	@PostMapping("/uploadAppAPK")
	@Async
	public CompletableFuture<ResponseEntity<String>> PostAppApk(
			@RequestParam("app_id") String app_id,
			@RequestParam("data") MultipartFile apkData) throws IOException {

		return CompletableFuture.supplyAsync(() -> {

			HashMap<String, Object> body = new HashMap<>();
			body.put("app_id", app_id);
			body.put("data", apkData);

			return commands.get("postAppApkCommand").execute(body);
		}, threadPoolTaskExecutor);

	}

	@PostMapping("/uploadAppMedia")
	@Async
	public CompletableFuture<ResponseEntity<String>> PostAppMedia(
			@RequestParam("app_id") String app_id,
			@RequestParam("media_type") String media_type,
			@RequestParam("link") String link) throws IOException {

		return CompletableFuture.supplyAsync(() -> {
			HashMap<String, Object> body = new HashMap<>();
			body.put("app_id", app_id);
			body.put("media_type", media_type);
			body.put("link", link);

			return commands.get("postAppMediaCommand").execute(body);
		}, threadPoolTaskExecutor);

	}

	@GetMapping("/getAppAPK/{app_id}")
	@Async
	public CompletableFuture<ResponseEntity<ByteArrayResource>> GetAppApk(@PathVariable String app_id)
			throws IOException {
		return CompletableFuture.supplyAsync(() -> {
			HashMap<String, Object> body = new HashMap<>();
			body.put("app_id", app_id);

			return commands.get("getAppApkCommand").execute(body);
		}, threadPoolTaskExecutor);
	}

	@GetMapping("/getAppMedia/{app_id}")
	@Async
	public CompletableFuture<ResponseEntity<String>> GetAppMedia(@PathVariable String app_id) throws IOException {
		return CompletableFuture.supplyAsync(() -> {
			HashMap<String, Object> body = new HashMap<>();
			body.put("app_id", app_id);

			return commands.get("getAppMediaCommand").execute(body);
		}, threadPoolTaskExecutor);

	}

	// delete App APK
	@DeleteMapping("/deleteAppAPK/{app_id}")
	@Async
	public CompletableFuture<ResponseEntity<String>> DeleteAppApk(@PathVariable String app_id)
			throws IOException {
		System.out.println("deleteAppApk");
		return CompletableFuture.supplyAsync(() -> {
			HashMap<String, Object> body = new HashMap<>();
			body.put("app_id", app_id);

			return commands.get("deleteAppApkCommand").execute(body);
		}, threadPoolTaskExecutor);
	}

	@DeleteMapping("/deleteAppMedia")
	@Async
	public CompletableFuture<ResponseEntity<String>> DeleteAppMedia(
			@RequestParam("app_id") String app_id,
			@RequestParam("link") String link)
			throws IOException {

		return CompletableFuture.supplyAsync(() -> {
			HashMap<String, Object> body = new HashMap<>();
			body.put("app_id", app_id);
			body.put("link", link);

			return commands.get("deleteAppMediaCommand").execute(body);
		}, threadPoolTaskExecutor);
	}

	@PatchMapping("/updateAppAPK")
	@Async
	public CompletableFuture<ResponseEntity<String>> UpdateAppAPK(
			@RequestParam("app_id") String app_id,
			@RequestParam("data") MultipartFile apkData)
			throws IOException {

		return CompletableFuture.supplyAsync(() -> {
			HashMap<String, Object> body = new HashMap<>();
			body.put("app_id", app_id);
			body.put("data", apkData);

			return commands.get("updateAppApkCommand").execute(body);
		}, threadPoolTaskExecutor);
	}

	public void setThreadCount(Integer thread_count) {
		try {
			if (thread_count < 1) {
				log.error("[ERROR] thread_count is less than 1");
				return;
			}

			threadPoolTaskExecutor.setCorePoolSize(thread_count);
			threadPoolTaskExecutor.setMaxPoolSize(thread_count);
			threadPoolTaskExecutor.setQueueCapacity(thread_count);

			log.info(String.format("[INFO] Thread count is set to %d", thread_count));
		} catch (Exception e) {
			log.error("[ERROR]" + e.getMessage());
		}
	}

	public void setDbConnectionCount(Integer count) {
		log.info("[INFO] trying to set db connections count to " + count);

		// TODO: dynamic db connection string

		// String connectionString = System.getProperty("spring.data.mongodb.uri");
		// ConnectionString connectionString = new ConnectionString(new_connection_uri);
		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://Yousef:OrY39uo9XYO9FS4k@cluster0.urbm3.mongodb.net/?retryWrites=true&w=majority&maxPoolSize="
						+ count);

		MongoClient newClient = MongoClients.create(connectionString);

		mongoClient = newClient;
		mongoTemplate = new MongoTemplate(newClient, "mediadb");

		log.info("[INFO] db connections is updated to " + count);
	}

	public void setLogLevel(String new_log_level) {
		log.info("[INFO] setting loglevel to " + new_log_level);

		Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.valueOf(new_log_level));
		// root.setLevel(Level.TRACE);

		// log.trace("This is a TRACE log");
		// log.debug("This is a DEBUG log");
		// log.info("This is an INFO log");
		// log.error("This is an ERROR log");
	}

	@Autowired
	KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

	@KafkaListener(topics = "controller_commands", groupId = "controller_commands_group")
	public void controller_handler(Map<String, Object> data) {
		// TODO: make each into command pattern
		System.out.println("[Controller Handler] Received data: " + data.toString());

		String request_action_name = (String) data.get("Action");
		if (request_action_name == null) {
			log.error("[ERROR] request_action_name is null");
			return;
		}

		switch (request_action_name) {
			case "set_error_reporting_level":
				String newLogLevel = (String) data.get("reportinglevel");
				if (newLogLevel == null) {
					log.error("[ERROR] reportinglevel is null");
					return;
				}
				setLogLevel(newLogLevel);
				break;

			case "freeze":
				isPaused = true;
				log.info("[INFO] Server is paused");
				break;

			case "continue":
				isPaused = false;
				log.info("[INFO] Server is continued");
				break;

			case "set_max_thread_count":
				Integer new_thread_count = 0;
				try {
					new_thread_count = Integer.parseInt(data.get("threads").toString());
				} catch (Exception e) {
					log.error("[ERROR] threads value is incorrect");
				}

				setThreadCount(new_thread_count);
				break;

			case "set_max_db_connections_count":
				Integer new_db_connections_count = 0;
				try {
					new_db_connections_count = Integer.parseInt(data.get("dbconnections").toString());
				} catch (Exception e) {
					log.error("[ERROR] dbconnections value is incorrect");
				}
				setDbConnectionCount(new_db_connections_count);
				break;

			default:
				log.error("[ERROR] request_action_name is not valid");
				return;
		}

		log.info("[Controller Handler] Done");
	}

	@GetMapping("/test2")
	public String test2() {
		Map<String, Object> body = new HashMap<>();

		// {
		// "Action": "Add",
		// "AppName": "ServiceX",
		// "threads": 1,
		// "dbconnections": 100,
		// "reportinglevel": "DEBUG"
		// }

		// body.put("Action", "set_error_reporting_level");
		// body.put("AppName", "ServiceX");
		// body.put("threads", 1);
		// body.put("dbconnections", 100);
		// body.put("reportinglevel", "TRACE");

		// body.put("Action", "continue");
		// body.put("Action", "freeze");

		// body.put("Action", "set_max_thread_count");
		// body.put("threads", 10);

		body.put("Action", "set_max_db_connections_count");
		body.put("dbconnections", 50);

		// TODO: set correct Topic for our service
		kafkaTemplate.send("controller_commands", body);

		System.out.println("[INFO] Sent successfully");
		return "OK";
	}

	// @Async
	@GetMapping("/test")
	public String test() {

		// System.setProperty("logging.level.root", "DEBUG");

		// change the log level for log

		// var context = (LoggerContext) LoggerFactory.getILoggerFactory();
		// var logger = context.getLogger(name);
		// if (logger != null) {
		// logger.setLevel(level);
		// } else {
		// // handle missing logger here
		// }

		// LoggerContext loggerContext = (LoggerContext)
		// LoggerFactory.getILoggerFactory();

		// Logger root = (Logger)
		// LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		// root.setLevel(Level.TRACE);

		// loggerContext.getLogger("app.media.controllers.MediaApplicationController").setLevel(Level.DEBUG);

		// log.trace("This is a TRACE log");
		// log.debug("This is a DEBUG log");
		// log.info("This is an INFO log");
		// log.error("This is an ERROR log");

		// System.setProperty("spring.data.mongodb.uri",
		// "mongodb+srv://Yousef:OrY39uo9XYO9FS4k@cluster0.urbm3.mongodb.net/?retryWrites=true&w=majority&maxPoolSize=2");

		// System.out.println("[TEST] " + mongoClient.toString());
		// System.out.println("[TEST] " + mongoTemplate.toString());

		// MongoCredential mongoCredential = MongoCredential.createCredential("Yousef",
		// "mediadb",
		// "OrY39uo9XYO9FS4k".toCharArray());
		// MongoClient newClient = MongoClients.create(
		// MongoClientSettings.builder().credential(mongoCredential)
		// .applyConnectionString(new com.mongodb.ConnectionString(
		// "mongodb+srv://cluster0.urbm3.mongodb.net/mediadb?retryWrites=true&w=majority&maxPoolSize=2"))

		// .applyToClusterSettings(builder ->
		// builder.hosts(Arrays.asList(address))).build());
		// ServerAddress address = new ServerAddress("cluster0.urbm3.mongodb.net",
		// 62797);
		// .applyToConnectionPoolSettings(builder -> builder.maxSize(2))
		// ClusterSettings settings =
		// mongoClient.getClusterDescription().getClusterSettings();
		// System.out.println(settings.getMode());
		// print the connection string
		// System.out.println(settings.getHosts().get(0));
		// settings.getHosts().forEach(host -> System.out.println(host.getHost() + ":" +
		// host.getPort()));

		// ============
		// ConnectionString connectionString = new ConnectionString(
		// "mongodb+srv://Yousef:OrY39uo9XYO9FS4k@cluster0.urbm3.mongodb.net/?retryWrites=true&w=majority&maxPoolSize=2");
		// MongoClient newClient = MongoClients.create(connectionString);

		// mongoClient = newClient;
		// mongoTemplate = new MongoTemplate(newClient, "mediadb");

		return "OK";

		// System.out.println("[TEST] TEST is called");

		// value += 1;
		// String copyValue = String.format("value: %d", value);

		// public CompletableFuture<String> test() {
		// CompletableFuture<String> res = CompletableFuture.supplyAsync(() -> {
		// System.out.println(String.format("[start] Thread, %s", copyValue));
		// log.info("Thread started");

		// // if (copyValue.equals("value: 1")) {
		// // log.error("Error occured");
		// // throw new RuntimeException("test");
		// // }

		// try {
		// Thread.currentThread().sleep(3 * 1000);
		// System.out.println(String.format("[End] Thread, %s", copyValue));
		// log.info("Thread ended");
		// } catch (InterruptedException e) {
		// log.error("InterruptedException " + e.getMessage());
		// // System.out.println("Interruptedxception");
		// e.printStackTrace();
		// }

		// return "OK";
		// }, threadPoolTaskExecutor);

		// return res;

		// System.out.println(commands.size());

		// for (String key : commands.keySet()) {
		// System.out.println(key);
		// }

		// return "OK";

		// AppMedia m = new AppMedia();
		// m.app_id = 3;
		// m.apk_id = "apk_id_string";

		// itemRepo.save(m);

		// update media

		// AppMedia m = itemRepo.findById("645cdfd6f921af2fa4db778d").get();
		// System.out.println(m.id);
		// System.out.println(m.app_id);
		// System.out.println(m.apk_id);
		// m.insertMedia("typeTest", "dataTest");
		// itemRepo.save(m);
	}

}
