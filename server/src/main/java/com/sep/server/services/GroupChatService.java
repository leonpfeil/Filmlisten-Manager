package com.sep.server.services;

import com.sep.server.dbaccess.GroupChatRepository;
import com.sep.server.model.Chat.GroupChat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupChatService {
    private GroupChatRepository groupChatRepository;
    private UserProfileService userProfileService;
    public GroupChatService(GroupChatRepository groupChatRepository,UserProfileService userProfileService)
    {
        this.groupChatRepository= groupChatRepository;
        this.userProfileService= userProfileService;
    }

    public ResponseEntity<HttpStatus> joinGroup(String groupName, String requester)
    {
        GroupChat group = groupChatRepository.getGroupChatByGroupName(groupName);
        if(group != null && !group.getParticipants().contains(requester))
        {
            String list = group.getParticipants();
            list += "," + requester;
            group.setParticipants(list);
            groupChatRepository.save(group);
            return new ResponseEntity(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<HttpStatus> createGroup(String groupName, String requester, String description, boolean isPrivate)
    {
        GroupChat group = new GroupChat();
        group.setGroupName(groupName);
        group.setParticipants(requester);
        group.setSetToPrivate(isPrivate);
        group.setDescription(description);

        groupChatRepository.save(group);

        return new ResponseEntity(HttpStatus.OK);
    }

    public List<GroupChat> getGroups(String requester)
    {
        List<GroupChat> groupChats = groupChatRepository.getGroupChatsBySetToPrivate(false);

        //get private chats
        if(userProfileService.getFriends(requester).getBody() != null)
        {
            String[] friendArray = userProfileService.getFriends(requester).getBody().split(",#,");

            List<GroupChat> allPrivateChats = groupChatRepository.getGroupChatsBySetToPrivate(true);
            for(String friend : friendArray)
            {
                groupChats.addAll(allPrivateChats.stream().filter(e -> e.getParticipants().contains(friend) || e.getParticipants().contains(requester)).collect(Collectors.toList()));
            }
        }
        else
        {
            List<GroupChat> allPrivateChats = groupChatRepository.getGroupChatsBySetToPrivate(true);
            allPrivateChats.removeIf(g -> !g.getParticipants().contains(requester));
            groupChats.addAll(allPrivateChats);
        }





        return groupChats;
    }

    public boolean groupExists(String groupname) //check if group exists already in db
    {
        return groupChatRepository.existsGroupChatByGroupName(groupname);
    }
}
