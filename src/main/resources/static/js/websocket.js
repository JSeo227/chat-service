

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
    // subscribe(destination, callback, headers = {})
    stompClient.subscribe("/sub/chat/rooms/" + roomId, onMessageReceived, {});

    const message = {
        roomId: roomId,
        sender: memberName,
        content: memberName + "님이 입장하였습니다.",
        types: 'ENTER'
    }

    // send(destination, headers = {}, body = '')
    stompClient.send("/app/chat/enter", {}, JSON.stringify(message));
    // stompClient.publish({
    //     destination: "/app/chat/enter",  // 서버에서 처리할 주소
    //     body: JSON.stringify(message)
    // });

    document.querySelector('#messageInput').value = '';
}

const onError = () => {

}

const sendMessage = (event) => {

    event.preventDefault(); // form submit 시 새로고침 방지

    const message = {
        roomId: roomId,
        sender: memberName,
        content: document.querySelector('#messageInput').value,
        types: 'TALK'
    }

    stompClient.send("/app/chat/send", {}, JSON.stringify(message));

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
messageForm.addEventListener('submit', sendMessage, true);
// true -> capturing, false -> bubbling