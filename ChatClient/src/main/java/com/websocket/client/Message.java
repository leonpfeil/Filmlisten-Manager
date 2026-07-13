package com.websocket.client;
//https://github.com/eugenp/tutorials/tree/master/spring-boot-modules/spring-boot-client
public class Message {

    private String from;
    private String text;
    private String to;
    private MessageType type;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        CHATLOG
    }

    public String getTo() {
        return to;
    }

    public Message() {
    }

    public Message(String from, String text, String to) {
        this.from = from;
        this.text = text;
        this.to = to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setText(String text) {
        this.text = text;
    }

}
