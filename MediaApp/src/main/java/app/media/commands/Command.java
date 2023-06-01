package app.media.commands;

import java.util.HashMap;
import org.springframework.http.ResponseEntity;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Command {

	public abstract ResponseEntity execute(HashMap<String, Object> map);
}
