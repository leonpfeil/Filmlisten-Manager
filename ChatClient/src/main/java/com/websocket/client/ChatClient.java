package com.websocket.client;

import java.util.Scanner;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ChatClient {

    private static String URL = "ws://localhost:8080/chat";
    public static String username;
    public static String target;
    private static StompSession session;

    public static void main(String[] args) throws Exception{
        String message;

        username = args[0];
        target = args[1];

        //Baue connection&session asuf
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new SessionHandler();
        session =  stompClient.connect(URL, sessionHandler).get();


        System.out.println("Please Enter Your Message:");
        while(true)
        {
            message = new Scanner(System.in).nextLine();
            Message messageObject = new Message();
            messageObject.setType(Message.MessageType.CHAT);
            messageObject.setFrom(username);
            messageObject.setText(message);
            messageObject.setTo(target);
            session.send("/app/chat/private/"+target,messageObject);
        }

    }


}