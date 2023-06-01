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

@Component
public class GetAppMediaCommand extends Command {

	// @Autowired
	// MediaApplicationController mediaApplicationController;

	@Autowired
	AppMediaRepository appRepo;

	@Override
	public ResponseEntity execute(HashMap<String, Object> map) {
		System.out.println("[Command - GetAppMedia] called");

		String app_id = (String) map.get("app_id");

		Optional<AppMedia> appMediaOp = appRepo.findById(app_id);

		if (!appMediaOp.isPresent()) {
			return new ResponseEntity<>("App does not exists", HttpStatus.BAD_REQUEST);
		}

		AppMedia appMedia = appMediaOp.get();

		return new ResponseEntity(appMedia.media, HttpStatus.OK);
	}

}
