package com.sep.server.dbaccess;

import com.sep.server.model.Chat.ChatLog;
import com.sep.server.model.Chat.ChatLogID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog,String> {
    //String getChatByusernameAndsender(String username,String sender);
    ChatLog getChatLogByChatLogID(ChatLogID chatLogID);
}
