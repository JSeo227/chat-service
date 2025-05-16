

//connect -> subscribe -> publish -> disconnect
//클라이언트 연결 -> 구독 -> 발행 -> 연결 종료

const usernamePage = document.querySelector('#username-page');
const usernameForm = document.querySelector('#usernameForm');

const chatPage = document.querySelector('#chat-page');
const messageArea = document.querySelector('#messageArea');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#messageInput');

const connectingElement = document.querySelector('.connecting');

let roomId = window.location.pathname.split('/').pop();
let memberName = null;

let stompClient = null;

const connect = () => {
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);

    //메시지 수신 or 구독
    //client.connect(headers, connectCallback, errorCallback)
    stompClient.connect({}, onConnected, onError);

}

const onConnected = () => {
    // client.subscribe(destination, callback, headers = {})
    stompClient.subscribe("/topic/chat/room/" + roomId, onMessageReceived, {});

    const message = {
        roomId: roomId,
        senderId: roomId,
        senderName: memberName,
        content: memberName + "님이 입장하였습니다.",
        status: 'ENTER'
    }

    // send(destination, headers = {}, body = '')
    stompClient.send("/app/chat/enter", {}, JSON.stringify(message));

    document.querySelector('#messageInput').value = '';
}

const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);
    console.log("Message:", message);
    console.log("Received payload:", payload);

    let usernameElement = document.createElement('span');
    let usernameText;

    const messageElement = document.createElement('li');

    switch (message.status) {
        case 'ENTER':
            console.log("Enter");
            messageElement.classList.add('event-message');
            usernameText = document.createTextNode(message.content);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
            break;

        case 'LEAVE':
            console.log("Leave");
            messageElement.classList.add('event-message');
            usernameText = document.createTextNode(message.content);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
            break;

        case 'TALK':
            console.log("Talk");
            console.log(message.content);
            messageElement.classList.add('chat-message');
            usernameText = document.createTextNode(message.sender + ": " + message.content);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
            break;

        default:
            console.warn("Unknown message type:", message.type);
            return;
    }

    const contentElement = document.createElement('p'); // 필요시 메시지 본문용
    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
};


const onError = () => {

}

const sendMessage = (event) => {

    event.preventDefault(); // form submit 시 새로고침 방지

    const message = {
        roomId: roomId,
        senderId: roomId,
        senderName: memberName,
        content: document.querySelector('#messageInput').value,
        status: 'TALK'
    }

    stompClient.send("/app/chat/send", {}, JSON.stringify(message));

    document.querySelector('#messageInput').value = '';
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected from server");
}

window.onload = () => {
    connect();
}

// window.addEventListener('beforeunload', function (e) {
//     disconnect(); // 예: 웹소켓 연결 종료 등
//
//     // 아래 줄은 사용자에게 확인창을 보여주고 싶을 때만 사용
//     // e.preventDefault(); // 최신 브라우저는 이걸 생략해도 동작
//     // e.returnValue = ''; // 표준 방식
// });



window.BeforeUnloadEvent = () => {
    disconnect();
}

// usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
// true -> capturing, false -> bubbling