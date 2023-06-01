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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.media.commands.Command;
import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

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

@RestController
public class MediaApplicationController {
	@Autowired
	private Map<String, Command> commands;

	@Autowired
	AppMediaRepository appRepo;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	@PostMapping("/uploadAppAPK")
	public ResponseEntity<String> PostAppApk(
			@RequestParam("app_id") String app_id,
			@RequestParam("data") MultipartFile apkData) throws IOException {

		HashMap<String, Object> body = new HashMap<>();
		body.put("app_id", app_id);
		body.put("data", apkData);

		return commands.get("postAppApkCommand").execute(body);
	}

	@PostMapping("/uploadAppMedia")
	public ResponseEntity<String> PostAppMedia(
			@RequestParam("app_id") String app_id,
			@RequestParam("media_type") String media_type,
			@RequestParam("link") String link) throws IOException {

		HashMap<String, Object> body = new HashMap<>();
		body.put("app_id", app_id);
		body.put("media_type", media_type);
		body.put("link", link);

		return commands.get("postAppMediaCommand").execute(body);
	}

	@GetMapping("/getAppAPK/{app_id}")
	public ResponseEntity<ByteArrayResource> GetAppApk(@PathVariable String app_id) throws IOException {
		HashMap<String, Object> body = new HashMap<>();
		body.put("app_id", app_id);

		return commands.get("getAppApkCommand").execute(body);
	}

	@GetMapping("/getAppMedia/{app_id}")
	public ResponseEntity<String> GetAppMedia(@PathVariable String app_id) throws IOException {
		HashMap<String, Object> body = new HashMap<>();
		body.put("app_id", app_id);

		return commands.get("getAppMediaCommand").execute(body);
	}

	@PostMapping("/test2")
	public String test2(@RequestBody HashMap<String, Object> body) {
		System.out.println("[TEST2]");
		for (String key : body.keySet()) {
			System.out.println(String.format("key: %s, value: %s", key, body.get(key)));
		}

		return "OK";
	}

	@GetMapping("/test")
	public String sendMessage() {
		System.out.println("[TEST] TEST is called");

		System.out.println(commands.size());

		for (String key : commands.keySet()) {
			System.out.println(key);
		}

		return "OK";

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
