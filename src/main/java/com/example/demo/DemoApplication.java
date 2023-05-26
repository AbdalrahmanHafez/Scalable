package com.example.demo;

import java.io.IOException;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.AppMedia;
import com.example.demo.Repository.ItemRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// @EnableScheduling
// @EnableRabbit
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@RestController
@EnableMongoRepositories
public class DemoApplication {

	@Autowired
	ItemRepository itemRepo;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@PostMapping("/uploadAppAPK")
	public ResponseEntity<String> postAppApk(
			@RequestParam("app_id") String app_id,
			@RequestParam("data") MultipartFile apkData) throws IOException {

		if (itemRepo.findById(app_id).isPresent())
			return new ResponseEntity<>("App already exists", HttpStatus.BAD_REQUEST);

		AppMedia m = new AppMedia();
		m.app_id = app_id;
		m.apk_id = gfsUploadFile(apkData);

		itemRepo.save(m);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/uploadAppMedia")
	public ResponseEntity<String> postAppMedia(
			@RequestParam("app_id") String app_id,
			@RequestParam("media_type") String media_type,
			@RequestParam("link") String link) throws IOException {

		Optional<AppMedia> appMediaOp = itemRepo.findById(app_id);
		if (!appMediaOp.isPresent())
			return new ResponseEntity<>("App does not exists", HttpStatus.BAD_REQUEST);

		AppMedia appMedia = appMediaOp.get();

		if (!media_type.equals("image") && !media_type.equals("video"))
			return new ResponseEntity<>("App media_type must be either 'image' or 'video'", HttpStatus.BAD_REQUEST);

		appMedia.insertMedia(media_type, link);

		itemRepo.save(appMedia);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/getAppAPK/{app_id}")
	public ResponseEntity<ByteArrayResource> postAppApk(@PathVariable String app_id) throws IOException {

		Optional<AppMedia> appMediaOp = itemRepo.findById(app_id);

		if (!appMediaOp.isPresent())
			return new ResponseEntity("App does not exists", HttpStatus.BAD_REQUEST);

		AppMedia m = appMediaOp.get();
		GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(m.apk_id)));

		String file_name = "";
		String file_type = "";
		String file_size = "";
		byte[] data = null;

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

	@GetMapping("/getAppMedia/{app_id}")
	public ResponseEntity<String> GetAppMedia(@PathVariable String app_id) throws IOException {

		Optional<AppMedia> appMediaOp = itemRepo.findById(app_id);

		if (!appMediaOp.isPresent()) {
			return new ResponseEntity<>("App does not exists", HttpStatus.BAD_REQUEST);
		}

		AppMedia appMedia = appMediaOp.get();

		return new ResponseEntity(appMedia.media, HttpStatus.OK);
	}

	public String gfsUploadFile(MultipartFile upload) throws IOException {

		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", upload.getSize());

		Object fileID = gridFsTemplate.store(upload.getInputStream(), upload.getOriginalFilename(),
				upload.getContentType(), metadata);

		return fileID.toString();
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

}