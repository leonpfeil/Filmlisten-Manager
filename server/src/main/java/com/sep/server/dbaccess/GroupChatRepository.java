package com.sep.server.dbaccess;

import com.sep.server.model.Chat.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat,String> {
    GroupChat getGroupChatByGroupName(String groupname);
    List<GroupChat> getGroupChatsBySetToPrivateAndParticipantsContaining(boolean isPrivate, String requester);
    List<GroupChat> getGroupChatsBySetToPrivate(boolean isPrivate);
    boolean existsGroupChatByGroupName(String groupname);

}


