package app.media.commands;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Component;
import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Component
public class DeleteAppApkCommand extends Command {

    @Autowired
    AppMediaRepository appRepo;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    @Override
    public ResponseEntity execute(HashMap<String, Object> map) {
        String app_id = (String) map.get("app_id");
        Optional<AppMedia> appMediaOp = appRepo.findById(app_id);

        if (!appMediaOp.isPresent())
            return new ResponseEntity("App does not exists", HttpStatus.BAD_REQUEST);

        String apk_id = appMediaOp.get().apk_id;
        appRepo.deleteById(app_id);
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(apk_id)));

        return new ResponseEntity("Ok", HttpStatus.OK);
    }
}
