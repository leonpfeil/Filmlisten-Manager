package com.sep.server.api;

import com.sep.server.services.ChatLogService;
import com.sep.server.services.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.*;

@RestController
public class ChatRestController {
    private ChatLogService chatLogService;
    public ChatRestController(ChatLogService chatLogService){this.chatLogService=chatLogService;}
    Map<String,String> mappedUserList = new LinkedHashMap<String,String>();

    @GetMapping(path = "chat/consumeChatRequest")
    String consumeChatRequests(String username)
    {
        if(mappedUserList.containsKey(username))
        {
            String name = mappedUserList.get(username);
            mappedUserList.remove(username);
            return name;
        }
        return "";
    }

    @PostMapping(path = "chat/addRequest")
    void addRequest(String requested,String requester)
    {

        mappedUserList.put(requested,requester);
    }



    //
    Map<String,String> mappedFriendRequestList = new LinkedHashMap<String,String>();

    @GetMapping(path = "social/consumeFriendRequest")
    String consumeFriendRequests(String username)
    {
        if(mappedFriendRequestList.containsKey(username))
        {
            String name = mappedFriendRequestList.get(username);
            mappedFriendRequestList.remove(username);
            return name;
        }
        return "";
    }

    @PostMapping(path = "social/addFriendRequest")
    void addFriendRequest(String requested,String requester)
    {
        if(!mappedFriendRequestList.containsKey(requested))
        {
            mappedFriendRequestList.put(requested,requester);
        }

    }

}
