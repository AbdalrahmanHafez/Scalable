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
import lombok.extern.slf4j.Slf4j;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Slf4j
@Component
public class GetAppApkCommand extends Command {

	// @Autowired
	// MediaApplicationController mediaApplicationController;

	@Autowired
	AppMediaRepository appRepo;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	@Override
	public ResponseEntity<ByteArrayResource> execute(HashMap<String, Object> map) {

		String app_id = (String) map.get("app_id");
		Optional<AppMedia> appMediaOp = appRepo.findById(app_id);

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

			try {
				IOUtils.readFully(operations.getResource(gridFSFile).getInputStream(), data);
			} catch (Exception e) {
				log.error("Error while reading file", e.getMessage());
				ResponseEntity.badRequest().body("Error while reading file");
			}
		}

		log.info(String.format("[INFO] App apk for %s, retrieved successfully", app_id));

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(file_type))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file_name + "\"")
				.body(new ByteArrayResource(data));
	}

}
