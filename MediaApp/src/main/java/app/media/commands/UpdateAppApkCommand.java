package app.media.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import app.media.models.AppMedia;
import app.media.repositories.AppMediaRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Component
public class UpdateAppApkCommand extends Command {
    @Autowired
    AppMediaRepository appRepo;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    @Override
    public ResponseEntity execute(HashMap<String, Object> map) {
        String app_id = (String) map.get("app_id");
        MultipartFile apkData = (MultipartFile) map.get("data");
        Optional<AppMedia> appMediaOp = appRepo.findById(app_id);

        if (!appMediaOp.isPresent())
            return new ResponseEntity<>("App doesn't exist", HttpStatus.BAD_REQUEST);

        AppMedia m = appMediaOp.get();
        String oldApkId = m.apk_id;

        try {
            m.apk_id = gfsUploadFile(apkData);
        } catch (IOException e) {
            System.out.println("[ERROR] Error while replacing apk. gfsUploadFile()");
            return new ResponseEntity<>("Error replacing apk", HttpStatus.BAD_REQUEST);
        }

        gridFsTemplate.delete(new Query(Criteria.where("_id").is(oldApkId)));

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
