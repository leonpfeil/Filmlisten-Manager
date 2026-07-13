package com.sep.server.services;

import com.sep.server.dbaccess.ChatLogRepository;
import com.sep.server.dbaccess.MovieRepository;
import com.sep.server.model.Chat.ChatLog;
import com.sep.server.model.Chat.ChatLogID;
import com.sep.server.model.Chat.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ChatLogService {
    private ChatLogRepository chatLogRepository;

    public ChatLogService(ChatLogRepository chatLogRepository){this.chatLogRepository=chatLogRepository;}

    public void saveChat(ChatLog chatLog)
    {
        chatLogRepository.save(chatLog);
    }

    public ChatLog getChatlog(String from,String to)
    {
        String user1;
        String user2;
        if(from.compareTo(to) < 0) //'from' compared to 'to'. Damit der chatlog in einem Eintrag und nicht in 2 gespeichert wird
        {
            //from ist kleiner
            user1 = from;
            user2 = to;
        }
        else
        {
            //from ist größer
            user1 = to;
            user2 = from;
        }
        return chatLogRepository.getChatLogByChatLogID(new ChatLogID(user1,user2));
    }

    public void appendToChatLog(Message message, boolean isGroup)
    {
        //Richtigen Chatlog finden
        String user1;
        String user2;
        if(isGroup)
        {
            user1 = message.getTo();
            user2 = message.getTo();
        }
        else if(message.getFrom().compareTo(message.getTo()) < 0)  //'from' compared to 'to'. Damit der chatlog in einem Eintrag und nicht in 2 gespeichert wird
        {
            //from ist kleiner
            user1 = message.getFrom();
            user2 = message.getTo();

        }
        else //wenn in groups
        {
            //from ist größer
            user1 = message.getTo();
            user2 = message.getFrom();
        }

        ChatLog chatLog = getChatlog(user2,user1);
        //Neue Nachricht anhängen
        final String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if(chatLog == null)
        {
            chatLog = new ChatLog(new ChatLogID(user1,user2),message.getFrom() + ": " + message.getText() + "\r\n",time + "\r\n");
        }
        else
        {
            chatLog.setChat(chatLog.getChat() + message.getFrom() + ": " + message.getText() + "\r\n");
            chatLog.setTimestamp(chatLog.getTimestamp() + time + "\r\n");
        }
        saveChat(chatLog);
    }
}
