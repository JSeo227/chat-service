// UI Elements
const messageArea = document.querySelector('#messageArea');
const messageForm = document.querySelector('#messageForm');

// Member Session Info
const { memberId: id, name } = JSON.parse(localStorage.getItem("memberSession"));
const memberId = id;
const memberName = name;

let stompClient = null;

// WebSocket Connect
const connect = () => {
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);

    //  메시지 수신 or 구독
    stompClient.connect({}, onConnected, onError);

}

const onConnected = () => {
    stompClient.subscribe("/topic/chat/room/" + roomId, onMessageReceived, {});

    const message = {
        roomId: roomId,
        senderId: memberId,
        senderName: memberName,
        content: memberName + "님이 입장하였습니다.",
        status: 'ENTER'
    }

    stompClient.send("/app/chat/enter", {}, JSON.stringify(message));

    document.querySelector('#messageInput').value = '';
}

const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);
    console.log("Message:", message);
    console.log("Received payload:", payload);

    const messageElement = document.createElement('li');
    const usernameElement = document.createElement('span');
    let usernameText;

    if (message.senderId === memberId) messageElement.classList.add('mine');
    else messageElement.classList.add('theirs');

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
            messageElement.classList.add('chat-message');
            usernameText = document.createTextNode(message.senderName + ": " + message.content);
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
    alert("Could not connect to server.");
}

const sendMessage = (event) => {

    event.preventDefault(); // form submit 시 새로고침 방지

    const message = {
        roomId: roomId,
        senderId: memberId,
        senderName: memberName,
        content: document.querySelector('#messageInput').value,
        status: 'TALK'
    }

    stompClient.send("/app/chat/send", {}, JSON.stringify(message));

    document.querySelector('#messageInput').value = '';
}

// WebSocket Disconnect
const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected from server");
}

// 모든 리소스가 로딩된 후 실행
// -> 텍스트기반이라서 무거운 리소스가 없음
window.onload = () => {
    connect();
}

// 사용자가 페이지를 닫거나 새로고침시 실행
window.addEventListener('beforeunload', () => {
    disconnect();
});

// URL 해시(#)가 변경될 때 실행 (뒤로가기)
window.onhashchange = () => {
    disconnect();
}

messageForm.addEventListener('submit', sendMessage, true);
// true -> capturing, false -> bubbling