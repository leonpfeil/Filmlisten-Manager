package com.sep.server.api;

import com.sep.server.model.Chat.*;
import com.sep.server.services.ChatLogService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

   private ChatLogService chatLogService;

    public ChatController(ChatLogService chatLogService){this.chatLogService=chatLogService;}
    //https://spring.io/guides/gs/messaging-stomp-websocket/

    ///group/public ist ein broadcast an alle
    @MessageMapping("/chat/echo")
    @SendTo("/group/public")
    public Message echo(final Message message) throws Exception {
        return message;
    }

    //weiterleitung von privaten Nachrichten
    @MessageMapping("/chat/private/{username}")
    @SendTo("/private/{username}")
    public Message privateMessages(Message message) throws Exception {

        System.out.println(message.getText() + " from: " + message.getFrom());
        chatLogService.appendToChatLog(message,false);
        return message;

    }

    @MessageMapping("/chat/meta/getChatLog/{username}")
    @SendTo("/private/{username}")
    public Message getChatLog(final Message message) throws Exception {
        try
        {
            String chatLogText = chatLogService.getChatlog(message.getFrom(),message.getTo()).getChat();
            message.setText(chatLogText);
        }
        catch (Exception e) {
            message.setText("");
        }

        return message;
    }


    //webpart https://www.callicoder.com/spring-boot-websocket-chat-example/
    //Diese Methode habe ich nicht selber geschrieben
    @MessageMapping("/chat.addUser/{target}")
    @SendTo("/private/web/{target}")
    public Message addUser(@Payload Message message,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getFrom());
        return message;
    }


    //code not shared between groups and private chats
    @MessageMapping("/chat/group/{username}")
    @SendTo("/private/{username}")
    public Message groupMessages(Message message) throws Exception {

        System.out.println(message.getText() + " from: " + message.getFrom());
        chatLogService.appendToChatLog(message,true);
        return message;

    }
}
