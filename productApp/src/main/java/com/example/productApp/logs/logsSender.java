package com.example.productApp.logs;

//import org.apache.flume.Event;
//import org.apache.flume.EventDeliveryException;
//import org.apache.flume.FlumeException;
//import org.apache.flume.agent.embedded.EmbeddedAgent;
//import org.apache.flume.event.EventBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class logsSender {
//    private static final String FLUME_AGENT_HOST = "<your-flume-agent-host>";
//    private static final int FLUME_AGENT_PORT = <your-flume-agent-port>;

    public static void sendLogMessage(String message) {
        sendMessage(message, "log");
        log.info(message);
    }

    public static void sendErrorMessage(String message) {
        sendMessage(message, "error");
        log.error(message);

    }

    private static void sendMessage(String message, String messageType) {
//        try {
//            // Create a Flume event with the message and message type as headers
//            Event event = EventBuilder.withBody(message.getBytes());
//            event.getHeaders().put("MessageType", messageType);
//
//            // Create an instance of the Flume embedded agent
//            EmbeddedAgent agent = new EmbeddedAgent();
//
//            // Send the event to Flume
//            agent.append(event);
//
//            // Close the agent
//            agent.close();
//        } catch (EventDeliveryException | FlumeException e) {
//            // Handle any exceptions that occur during event delivery
//            e.printStackTrace();
//        }
    }
}
