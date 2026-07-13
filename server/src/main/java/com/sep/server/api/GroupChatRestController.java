package com.sep.server.api;

import com.sep.server.model.Chat.GroupChat;
import com.sep.server.model.Chat.groupCreationJson;
import com.sep.server.model.Chat.groupJoinJson;
import com.sep.server.services.GroupChatService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupChatRestController {
    private final GroupChatService groupChatService;
    public GroupChatRestController(GroupChatService groupChatService){this.groupChatService =groupChatService;}

    @PostMapping(path = "group/joinGroup")
    ResponseEntity<HttpStatus> joinGroup(@RequestBody groupJoinJson json)
    {
        String group = json.groupName;
        String requester = json.requester;
        return groupChatService.joinGroup(group,requester);
    }

    @PostMapping(path = "group/createGroup")
    ResponseEntity<HttpStatus> createGroup(@RequestBody groupCreationJson json)
    {

        String groupName = json.groupname;
        String requester = json.requester;
        String description = json.description;
        boolean isPrivate = json.isPrivate;

        //check if group already exists
        if(groupChatService.groupExists(groupName))
        {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        return groupChatService.createGroup(groupName,requester,description, isPrivate);
    }

    @GetMapping(path = "group/getGroups")
    List<GroupChat> getGroups(String requester)
    {
        return groupChatService.getGroups(requester);
    }
}
