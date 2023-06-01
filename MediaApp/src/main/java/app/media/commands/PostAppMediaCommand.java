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
public class PostAppMediaCommand extends Command {

	// @Autowired
	// MediaApplicationController mediaApplicationController;

	@Autowired
	AppMediaRepository appRepo;

	@Override
	public ResponseEntity execute(HashMap<String, Object> map) {
		String app_id = (String) map.get("app_id");
		String media_type = (String) map.get("media_type");
		String link = (String) map.get("link");

		Optional<AppMedia> appMediaOp = appRepo.findById(app_id);
		if (!appMediaOp.isPresent())
			return new ResponseEntity<>("App does not exists", HttpStatus.BAD_REQUEST);

		AppMedia appMedia = appMediaOp.get();

		if (!media_type.equals("image") && !media_type.equals("video"))
			return new ResponseEntity<>("App media_type must be either 'image' or 'video'", HttpStatus.BAD_REQUEST);

		appMedia.insertMedia(media_type, link);

		appRepo.save(appMedia);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
