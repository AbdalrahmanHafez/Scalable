package app.media.commands;

import java.io.IOException;
import java.util.HashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import lombok.extern.slf4j.Slf4j;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@Component
public class PostAppApkCommand extends Command {

	// @Autowired
	// MediaApplicationController mediaApplicationController;

	@Autowired
	AppMediaRepository appRepo;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Override
	public ResponseEntity execute(HashMap<String, Object> map) {
		String app_id = (String) map.get("app_id");
		MultipartFile apkData = (MultipartFile) map.get("data");

		if (appRepo.findById(app_id).isPresent())
			return new ResponseEntity<>("App already exists", HttpStatus.BAD_REQUEST);

		AppMedia m = new AppMedia();
		m.app_id = app_id;
		try {
			m.apk_id = gfsUploadFile(apkData);
		} catch (IOException e) {
			// System.out.println("[ERROR] Error while uploading apk. gfsUploadFile()");
			log.error("[ERROR] Error while uploading apk. gfsUploadFile() " + e.getMessage());
			return new ResponseEntity<>("Error uploading apk", HttpStatus.BAD_REQUEST);
		}

		appRepo.save(m);

		log.info(String.format("[INFO] App apk for %s, uploaded successfully", app_id));

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
