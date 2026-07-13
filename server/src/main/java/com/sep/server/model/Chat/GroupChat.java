package com.sep.server.model.Chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GroupChat {
    @Id
    @JsonProperty
    String groupName;
    @JsonProperty
    @Column(columnDefinition = "TEXT")
    String participants;
    @JsonProperty
    boolean setToPrivate;
    @JsonProperty
    @Column(columnDefinition = "TEXT")
    String description;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public boolean isSetToPrivate() {
        return setToPrivate;
    }

    public void setSetToPrivate(boolean setToPrivate) {
        this.setToPrivate = setToPrivate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
