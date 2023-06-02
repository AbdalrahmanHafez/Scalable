package app.media.commands;

import java.util.HashMap;
import org.springframework.http.ResponseEntity;

public abstract class Command {

	public abstract ResponseEntity execute(HashMap<String, Object> map);
}
