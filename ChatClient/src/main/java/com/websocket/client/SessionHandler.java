package com.websocket.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

import static com.websocket.client.ChatClient.target;
import static com.websocket.client.ChatClient.username;

//https://github.com/eugenp/tutorials/tree/master/spring-boot-modules/spring-boot-client
//Boilerplate code aus dem Tutorial übernommen
//after connected bearbeitet, handleTransportError und getchatlog hinzugefügt
public class SessionHandler extends StompSessionHandlerAdapter {
    boolean gotChatLog = false;
    private Logger logger = LogManager.getLogger(SessionHandler.class);
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/group/public", this);
        //subscribe to own inbox
        session.subscribe("/private/"+ username, this);
        logger.info("Subscribed to inbox");
        getChatLog(session);
        session.send("/app/chat/echo", new Message(username,"Verbindung erfolgreich aufgebaut", username));
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        if(msg.getType() != Message.MessageType.CHATLOG)
        {
            System.out.println(msg.getFrom() + ": " + msg.getText());
        }
        else if(!gotChatLog)
        {
            System.out.println(msg.getText());
            gotChatLog = true;
        }
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        logger.info("Transport error");
    }

    static void getChatLog(StompSession session)
    {
        Message message = new Message();
        message.setTo(target);
        message.setFrom(username);
        message.setType(Message.MessageType.CHATLOG);
        session.send("/app/chat/meta/getChatLog/" + username,message);
    }
}
