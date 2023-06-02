package app.media.commands;

import java.util.HashMap;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import lombok.extern.slf4j.Slf4j;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
