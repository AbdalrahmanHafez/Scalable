package app.media.commands;

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
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.media.commands.Command;
import app.media.controllers.MediaApplicationController;
import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.rabbitmq.client.RpcClient.Response;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Component
public class PostAppApkCommand extends Command {

	@Autowired
	MediaApplicationController mediaApplicationController;

	@Autowired
	AppMediaRepository appRepo;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	@Override
	public ResponseEntity execute(HashMap<String, Object> map) {
		String app_id = (String) map.get("app_id");
		MultipartFile apkData = (MultipartFile) map.get("apkData");

		if (appRepo.findById(app_id).isPresent())
			return new ResponseEntity<>("App already exists", HttpStatus.BAD_REQUEST);

		AppMedia m = new AppMedia();
		m.app_id = app_id;
		try {
			m.apk_id = gfsUploadFile(apkData);
		} catch (IOException e) {
			System.out.println("[ERROR] Error while uploading apk. gfsUploadFile()");
			return new ResponseEntity<>("Error uploading apk", HttpStatus.BAD_REQUEST);
		}

		appRepo.save(m);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	public String gfsUploadFile(MultipartFile upload) throws IOException {

		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", upload.getSize());

		Object fileID = gridFsTemplate.store(upload.getInputStream(), upload.getOriginalFilename(),
				upload.getContentType(), metadata);

		return fileID.toString();
	}

}
