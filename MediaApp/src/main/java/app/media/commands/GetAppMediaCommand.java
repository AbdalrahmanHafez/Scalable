package app.media.commands;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import app.media.controllers.MediaApplicationController;
import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GetAppMediaCommand extends Command {

	// @Autowired
	// MediaApplicationController mediaApplicationController;

	@Autowired
	AppMediaRepository appRepo;

	@Override
	public ResponseEntity execute(HashMap<String, Object> map) {

		String app_id = (String) map.get("app_id");

		Optional<AppMedia> appMediaOp = appRepo.findById(app_id);

		if (!appMediaOp.isPresent()) {
			return new ResponseEntity<>("App does not exists", HttpStatus.BAD_REQUEST);
		}

		AppMedia appMedia = appMediaOp.get();

		log.info(String.format("[INFO] App media for %s, retrieved successfully", app_id));

		return new ResponseEntity(appMedia.media, HttpStatus.OK);
	}

}
