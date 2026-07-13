package com.sep.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupChat {
    @JsonProperty
    String groupName;
    @JsonProperty
    String participants;
    @JsonProperty
    boolean setToPrivate;
    @JsonProperty
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
