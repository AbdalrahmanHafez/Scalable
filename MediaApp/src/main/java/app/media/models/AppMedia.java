package app.media.models;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// {
// 	"_id": random,
// 	"app_id": num,
// 	"apk": <binary_id>,
// 	media: [
// 		{
// 			"type": "image" | "video",
// 			"link": <String>
// 		}
// 	]
// }

@Document("AppMedia")
public class AppMedia {
	@Id
	public String app_id;

	public String apk_id;
	public ArrayList<Media> media;

	public AppMedia() {
		super();
		media = new ArrayList<Media>();
	}

	public void insertMedia(String type, String link) {
		Media m = new Media();
		m.type = type;
		m.link = link;
		media.add(m);
	}

	public static class Media {
		public String type; // image | video
		public String link;

		public Media() {
		}
	}
}
