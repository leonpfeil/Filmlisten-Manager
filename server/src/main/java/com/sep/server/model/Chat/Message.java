package com.sep.server.model.Chat;

import com.fasterxml.jackson.annotation.JsonProperty;
public class  Message {
    @JsonProperty("from")
    private String from;
    @JsonProperty("text")
    private String text;
    @JsonProperty
    private String to;
    @JsonProperty
    private MessageType type;
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        CHATLOG
    }

    public String getTo() {
        return to;
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

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Message() {
    }

    public Message(String from, String text, String to, MessageType type) {
        this.from = from;
        this.text = text;
        this.to = to;
        this.type = type;
    }
}
