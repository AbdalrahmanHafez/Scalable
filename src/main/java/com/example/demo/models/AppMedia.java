package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

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
	public String id;

	public int app_id;
	public String apk_id;
	public ArrayList<Media> media;

	public AppMedia() {
		super();
		// this.id = id;
		// media = new Media[1];
		media = new ArrayList<Media>();
	}

	public void insertMedia(String type, String data) {
		Media m = new Media();
		m.type = type;
		m.data = data;
		media.add(m);
	}

	public static class Media {
		public String type;
		public String data;

		public Media() {
		}

	}
}