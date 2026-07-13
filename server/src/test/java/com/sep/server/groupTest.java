/*package com.sep.server;

import com.sep.server.dbaccess.GroupChatRepository;
import com.sep.server.model.Chat.GroupChat;
import com.sep.server.model.Chat.groupCreationJson;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class groupTest {
    @Autowired
    GroupChatRepository groupChatRepository;
    public void initialize() {

    }


    @Test
    public void testCreateGroup()
    {
        JSONObject privateGroupJson = new JSONObject();
        privateGroupJson.put("groupname", "private test group");
        privateGroupJson.put("requester", "test Requester");
        privateGroupJson.put("isPrivate",true);
        post("/group/createGroup",privateGroupJson);

        GroupChat group = groupChatRepository.getGroupChatByGroupName("private test group");
        assertEquals("Gruppenname", "private test group", group.getGroupName());


    }

    @Test
    public void testJoinGroup()
    {
        JSONObject privateGroupJson = new JSONObject();
        privateGroupJson.put("groupname", "private test group 2");
        privateGroupJson.put("requester", "test Requester");
        privateGroupJson.put("isPrivate",true);
        post("/group/createGroup",privateGroupJson);

        JSONObject json = new JSONObject();
        json.put("groupName", "private test group 2");
        json.put("requester", "new participant");
        post("/group/joinGroup", json);

        GroupChat group = groupChatRepository.getGroupChatByGroupName("private test group 2");
        Assert.assertTrue("Group Joined", group.getParticipants().contains("new participant"));
    }

    public static HttpResponse<String> post(String url, JSONObject jsonObject) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}*/
