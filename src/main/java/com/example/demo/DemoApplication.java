package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthToolBarUI;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

import com.example.demo.models.AppMedia;
import com.example.demo.repository.ItemRepository;
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

// @EnableScheduling
// @SpringBootApplication
// @EnableRabbit
@RestController
@SpringBootApplication
@EnableMongoRepositories
public class DemoApplication {

	@Autowired
	ItemRepository itemRepo;

	// @Autowired
	// private MappingMongoConverter mongoConverter;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	public String addFile(MultipartFile upload) throws IOException {

		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", upload.getSize());

		Object fileID = gridFsTemplate.store(upload.getInputStream(), upload.getOriginalFilename(),
				upload.getContentType(), metadata);

		return fileID.toString();
	}

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
		return new ResponseEntity<>(addFile(file), HttpStatus.OK);
	}

	@GetMapping("/test")
	public String sendMessage() {
		System.out.println("[INFO] Test message");
		System.out.println("Data creation started...");

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

		return "DONE";
	}

	@GetMapping("/d/{id}")
	public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
		GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is("645cd643d8c40c3b1b42894a")));

		String file_name = "";
		String file_type = "";
		String file_size = "";
		byte[] data = null;

		System.out.println(file_name);
		System.out.println(file_type);
		System.out.println(file_size);

		if (gridFSFile != null && gridFSFile.getMetadata() != null) {
			file_name = gridFSFile.getFilename();

			file_type = gridFSFile.getMetadata().get("_contentType").toString();

			file_size = gridFSFile.getMetadata().get("fileSize").toString();

			data = new byte[Integer.parseInt(file_size)];

			IOUtils.readFully(operations.getResource(gridFSFile).getInputStream(), data);
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(file_type))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file_name + "\"")
				.body(new ByteArrayResource(data));
	}

	// @Autowired
	// Sender sender;

	// @PostMapping("/send")
	// public String sendMessage(@RequestBody String message) {
	// // Send a post request with a text body to http://localhost:8080/send

	// sender.sendMessage(message);

	// return "Message sent";
	// }

}
