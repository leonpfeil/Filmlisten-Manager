package com.sep.server.model.Chat;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
public class ChatLogID implements Serializable {
    String user1;
    String user2;

    public ChatLogID(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public ChatLogID() {

    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}
