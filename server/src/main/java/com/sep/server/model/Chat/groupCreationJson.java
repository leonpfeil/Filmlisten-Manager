package com.sep.server.model.Chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class groupCreationJson {
    @JsonProperty
    public String groupname;
    @JsonProperty
    public String requester;
    @JsonProperty
    public String description;
    @JsonProperty
    public boolean isPrivate;
}
