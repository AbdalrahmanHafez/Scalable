package com.example.demo.Commands;

import com.example.demo.Services.UserService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.freeze")
public class FreezeCommand extends Command{
    private boolean enabled;

    public String getName()
    {
        return "freeze";
    }
    public FreezeCommand(UserService userService) {
        super(userService);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Object execute(Object obj) {
        this.enabled = (boolean) obj;
        if (enabled) {
            return "App frozen";
            //freeze(); // Freeze the microservice
        } else {
            return "App Unfrozen";
            //unfreeze(); // Unfreeze the microservice
        }
    }

}
