package app.media.commands;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Component;
import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Component
public class DeleteAppMediaCommand extends Command {
    @Autowired
    AppMediaRepository appRepo;

    @Override
    public ResponseEntity execute(HashMap<String, Object> map) {
        String app_id = (String) map.get("app_id");
        Optional<AppMedia> appMediaOp = appRepo.findById(app_id);

        if (!appMediaOp.isPresent())
            return new ResponseEntity("App does not exists", HttpStatus.BAD_REQUEST);

        String link = (String) map.get("link");
        appMediaOp.get().media.stream().filter(media -> media.link.equals(link)).findFirst().ifPresent(media -> {
            appMediaOp.get().media.remove(media);
        });
        appRepo.save(appMediaOp.get());

        return new ResponseEntity("Ok", HttpStatus.OK);
    }
}
