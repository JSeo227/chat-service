'use strict';

//j-query
document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")

//connect -> subscribe -> publish -> disconnect
//클라이언트 연결 -> 구독 -> 발행 -> 연결 종료
var usernamePage = document.querySelector('#username-page');
var usernameForm = document.querySelector('#usernameForm');

var chatPage = document.querySelector('#chat-page');
var messageArea = document.querySelector('#messageArea');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#messageInput');

var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

//roomId 파라미터 가져오기
var path = location.pathname;  // '/rooms/3'

// 배열의 세 번째 요소가 '3'이므로 이 값을 가져옴
var roomId = parseInt(path.split('/')[3], 10);
console.log("roomId:", roomId);

function connect(event) {

    //로그인한후 아이디로 할지 방에서 만들지 고민중
    username = document.querySelector('#name').value.trim();
    console.log("username:", username);

    //username 중복 확인
    isDuplicate();

    // usernamePage 에 hidden 속성 추가해서 가리고
    // chatPage 를 등장시킴
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    //연결하고자하는 socket의 endpoint
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    console.log("socket:", socket)
    console.log("stompClient:", stompClient)

    //메시지 수신 or 구독
    //connect(headers, connectCallback, errorCallback)
    stompClient.connect({}, onConnected, onError);

    event.preventDefault(); // 서버로 데이터 보내는 행위 X -> 새로고침을 막음

}

function onConnected() {
    // sub 할 url -> /sub/rooms/roomId로 구독
    // subscribe(destination, callback, headers = {})
    stompClient.subscribe("/sub/chat/rooms/" + roomId, onMessageReceived, {});

    // 서버에 username 을 가진 유저가 들어왔다는 것을 알림 -> /pub/enterUser로 메시지를 보냄
    // send(destination, headers = {}, body = '')
    stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            sender: username,
//            message: username + "님이 입장하였습니다."
            type : "ENTER"
        })
    )
}

//유저 닉네임 중복 확인 에러발생
function isDuplicate() {

    $.ajax({
        type: "GET",
        url: "/chat/duplicate",
        data: {
            "username": username,
            "roomId": roomId
        },
        success: function (data) {
            console.log("함수 동작 확인 : " + data);
            username = data;
        }
    })

}

// 유저 리스트 받기
// ajax 로 유저 리스를 받으며 클라이언트가 입장/퇴장 했다는 문구가 나왔을 때마다 실행된다.
function getUserList() {

    const $list = $("#memberList");

    console.log("$list:", $list);

    $.ajax({
        type: "GET",
        url: "/chat/userList", //
        data: {
            "roomId": roomId
        },
        success: function (data) {
            var users = "";
            for (let i = 0; i < data.length; i++) {
                users += "<li class='dropdown-item'>" + data[i] + "</li>"
            }
            $list.html(users);
        }
    })

}

function onError() {
    connectingElement.textConnect = "Could not connect to server.";
    connectingElement.textColor = "red";
}

// 메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {

    event.preventDefault();

    var messageContent = messageInput.value.trim();
    console.log("messageContent:", messageContent)
    console.log("stompClient:", stompClient)

    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }

}

// 메시지를 받을 때도 마찬가지로 JSON 타입으로 받으며,
// 넘어온 JSON 형식의 메시지를 parse 해서 사용한다.
function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);

    console.log("chat:", chat);
    console.log("Received payload:", payload);
    console.log("Payload body:", payload.body);

    var messageElement = document.createElement('li');

    if (chat.type === "ENTER") {  // chatType 이 enter 라면 아래 내용
        messageElement.classList.add('event-message');

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.message);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        getUserList();

    } else if (chat.type === "LEAVE") { // chatType 가 leave 라면 아래 내용
        messageElement.classList.add('event-message');

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.message);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        getUserList();

    } else if (chat.type === "TALK"){ // chatType 이 talk 라면 아래 내용
        messageElement.classList.add('chat-message');

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.sender + ": " + chat.message);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var contentElement = document.createElement('p');

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

//창 키면 바로 연결
//window.onload = function (){
//    connect();
//}

window.BeforeUnloadEvent = function (){
    disconnect();
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
// true -> capturing, false -> bubbling