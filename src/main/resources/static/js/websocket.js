import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

//connect -> subscribe -> publish -> disconnect
//클라이언트 연결 -> 구독 -> 발행 -> 연결 종료

let roomId = null;
let memberName = null;

let stompClient = null;

const connect = (event) => {
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);

    //메시지 수신 or 구독
    //connect(headers, connectCallback, errorCallback)
    stompClient.connect({}, onConnected, onError);

    event.preventDefault();
}

const onConnected = () => {
    // subscribe(destination, callback, headers = {})
    stompClient.subscribe("/sub/chat/rooms/" + roomId, onMessageReceived, {});

    const message = {
        roomId: roomId,
        sender: memberName,
        content: memberName + "님이 입장하였습니다.",
        types: 'ENTER'
    }

    // send(destination, headers = {}, body = '')
    stompClient.publish({
        destination: "/app/chat/enter",  // 서버에서 처리할 주소
        body: JSON.stringify(message)
    });

    document.querySelector('#messageInput').value = '';
}

const onError = () => {

}

const sendMessage = (event) => {
    const message = {
        roomId: roomId,
        sender: memberName,
        content: document.querySelector('#messageInput').value,
        types: 'TALK'
    }

    stompClient.publish({
        destination: "/app/chat/send",  // 서버에서 처리할 주소
        body: JSON.stringify(message)
    });

    document.querySelector('#messageInput').value = '';
}

const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);
    console.log("Message: ", message);
    console.log("Received payload:", payload);

    switch (message.types) {
        case 'ENTER':
            break;
        case 'LEAVE':
            break;
        case 'TALK':
            break;
        default:
            break;
    }
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

window.BeforeUnloadEvent = () => {
    disconnect();
}

// usernameForm.addEventListener('submit', connect, true);
// messageForm.addEventListener('submit', sendMessage, true);
// true -> capturing, false -> bubbling

// <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
// <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
// <script src="/js/socket.js"></script>

