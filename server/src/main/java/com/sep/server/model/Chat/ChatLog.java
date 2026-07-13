package com.sep.server.model.Chat;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class ChatLog {

    @EmbeddedId
    ChatLogID chatLogID;
    @JsonProperty("chat")
    @Column(columnDefinition = "TEXT")
    String chat;
    @JsonProperty("timestamp")
    @Column(columnDefinition = "TEXT")
    String timestamp;
    public ChatLog() {

    }


    public ChatLogID getChatLogID() {
        return chatLogID;
    }

    public void setChatLogID(ChatLogID chatLogID) {
        this.chatLogID = chatLogID;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ChatLog(ChatLogID chatLogID, String chat, String timestamp) {
        this.chatLogID = chatLogID;
        this.chat = chat;
        this.timestamp = timestamp;
    }
}
