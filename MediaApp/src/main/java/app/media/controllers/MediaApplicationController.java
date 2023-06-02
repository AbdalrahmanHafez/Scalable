package app.media.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.plaf.synth.SynthToolBarUI;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import app.media.commands.Command;
import app.media.interceptors.CustomInterceptor;
import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import lombok.extern.slf4j.Slf4j;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.connection.ClusterSettings;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

	// Controller Commands
	@PostMapping(path = "/set_max_thread_count/{thread_count}")
	public ResponseEntity<String> adjustThreads(@PathVariable String thread_count) {
		// TODO: make it a command
		try {
			threadPoolTaskExecutor.setMaxPoolSize(Integer.parseInt(thread_count));
			// System.out.println(String.format("Thread count is set to %d", thread_count));
			log.info(String.format("Thread count is set to %d", thread_count));
		} catch (Exception e) {
			// System.out.println("[ERROR]" + e.getMessage());
			log.error("[ERROR]" + e.getMessage());
			return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping("/continue")
	public ResponseEntity<String> continueServer() {
		isPaused = false;
		System.out.println("[INFO] Server is continued");
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping("/pause")
	public ResponseEntity<String> pauseServer() {
		isPaused = true;
		System.out.println("[INFO] Server is paused");
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping("/updateDB/{new_connection_uri}")
	public String updateDB(@PathVariable String new_connection_uri) {
		log.info("[INFO] trying to updateDB");

		// ConnectionString connectionString = new ConnectionString(
		// "mongodb+srv://Yousef:OrY39uo9XYO9FS4k@cluster0.urbm3.mongodb.net/?retryWrites=true&w=majority&maxPoolSize=2");

		ConnectionString connectionString = new ConnectionString(new_connection_uri);
		MongoClient newClient = MongoClients.create(connectionString);
		mongoClient = newClient;
		mongoTemplate = new MongoTemplate(newClient, "mediadb");

		log.info("[INFO] updateDB is updated");
		return "OK";
	}

	@PostMapping("/test2")
	public String test2(@RequestBody HashMap<String, Object> body) {
		System.out.println("[TEST2]");
		for (String key : body.keySet()) {
			System.out.println(String.format("key: %s, value: %s", key, body.get(key)));
		}

		return "OK";
	}

	static int value = 0;

	@GetMapping("/test")
	// @Async
	public String test() {
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
		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://Yousef:OrY39uo9XYO9FS4k@cluster0.urbm3.mongodb.net/?retryWrites=true&w=majority&maxPoolSize=2");
		MongoClient newClient = MongoClients.create(connectionString);

		mongoClient = newClient;
		mongoTemplate = new MongoTemplate(newClient, "mediadb");

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
