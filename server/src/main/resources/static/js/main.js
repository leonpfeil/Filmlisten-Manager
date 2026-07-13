'use strict';
//Diese Datei ist größtenteils aus dem tutorial. Ich habe nur die Methoden connect, onConnected und sendMessage angepasst so dass das Programm die usernamen aus der URL nimmt, sowie
// das Programm so umstrukturiert dass es als 1 to 1 Chat funktioniert und nicht als Gruppenchat.
//getChatlog auch von mir
//https://www.callicoder.com/spring-boot-websocket-chat-example/
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var gotChatlog = false;
var stompClient = null;
var username = null;
var url = null;
var target = null;
var isGroup = false;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    url = window.location.href;
    username = url.match("(?<=\\?username=).+(?=&username2)")[0].replaceAll("%20"," "); //Regex um den usernamen zu erhalten
    target = url.match("(?<=&username2=).+(?=&group)")[0].replaceAll("%20"," ");

    if(url.match("(?<=&group=).")[0] == "1")
    {
        isGroup = true;
    }
    if(username) {

        var socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    if(!isGroup)
    {
        // Subscribe to the Public Topic
        stompClient.subscribe('/private/' + username, onMessageReceived);
        stompClient.subscribe('/private/' + target, onMessageReceived); //An die andere Inbox subscriben damit die eigenen Nachrichten angezeigt werden
    }
    else
    {
        stompClient.subscribe('/private/' + target, onMessageReceived); //Bei Gruppen ist "target" die Gruppe. Da sich alle eine inbox teilen muss man nur ein mal subscriben
    }

    // Tell your username to the server
    stompClient.send("/app/chat.addUser/" + target,
        {},
        JSON.stringify({from: username, to: target, type: 'JOIN'})
    )
    connectingElement.classList.add('hidden');
    getChatLog();
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function getChatLog()
{
    if(!isGroup) // 1 to 1 chat
    {
        var chatMessage = {
            from: username,
            text: '',
            to: target,
            type: 'CHATLOG'
        };
        stompClient.send("/app/chat/meta/getChatLog/" + username, {}, JSON.stringify(chatMessage))

    }
    else //groupchat
    {
        var chatMessage = {
            from: target,
            text: '',
            to: target,
            type: 'CHATLOG'
        };
        stompClient.send("/app/chat/meta/getChatLog/" + target, {}, JSON.stringify(chatMessage))
    }
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            from: username,
            text: messageInput.value,
            to: target,
            type: 'CHAT'
        };
        if(isGroup)
        {
            stompClient.send("/app/chat/group/" + target, {}, JSON.stringify(chatMessage));
        }
        else
        {
            stompClient.send("/app/chat/private/" + target, {}, JSON.stringify(chatMessage));
        }

        messageInput.value = '';
    }

    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body)
    if(message.from === target || (message.from === username && message.to === target) || isGroup)
    {
        var messageElement = document.createElement('li');
        if(message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.text = message.from + ' joined!';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.text = message.from + ' left!';
        } else if (message.type === 'CHATLOG') { //chatlog teil den ich geaddet habe
            if(message.from === username && !gotChatlog || (isGroup && !gotChatlog))
            {
                messageElement.classList.add('event-message');
                gotChatlog = true;
            }
            else
            {
                return;
            }
        } else {

            messageElement.classList.add('chat-message');
            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(message.from[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(message.from);

            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(message.from);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }
        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.text);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }

}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

document.addEventListener('DOMContentLoaded', function() {
    connect();
}, false);
messageForm.addEventListener('submit', sendMessage, true)