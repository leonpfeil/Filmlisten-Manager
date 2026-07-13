package com.sep.server.model.Chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class groupJoinJson
{
    @JsonProperty
    public String groupName;
    @JsonProperty
    public String requester;
}
