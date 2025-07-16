// Member Info
const { memberId: id, name } = JSON.parse(localStorage.getItem("memberSession"));
const memberId = id;
const memberName = name;

let stompClient = null;

// WebSocket Connect
const connect = () => {
    const socket = new SockJS("/ws-chat");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    console.log("Connected from server");
};

// WebSocket Disconnect
const disconnect = () => {
    if (stompClient && stompClient.connected) {
        stompClient.disconnect();
    }
    console.log("Disconnected from server");
};

const onConnected = () => {
    stompClient.subscribe(`/topic/chat/room/${roomId}`, onMessageReceived);

    const message = {
        roomId: roomId,
        senderId: memberId,
        senderName: memberName,
        content: `${memberName}님이 입장하였습니다.`,
        status: "ENTER"
    };

    stompClient.send("/app/chat/enter", {}, JSON.stringify(message));

    document.querySelector("#messageInput").value = "";
};

const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);
    const messageElement = createMessageElement(message);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight; // 메시지 입력시 스크롤 이동
};

const onError = (error) => {
    console.error("WebSocket connection failed:", error);
    alert("서버 연결에 실패했습니다.");
};

// 메시지 전달 함수
const sendMessage = (event) => {
    event.preventDefault(); // 데이터를 서버에 전송시 새로고침 제어

    const content = messageInput.value.trim();
    if (!content) return;

    const message = {
        roomId,
        senderId: memberId,
        senderName: memberName,
        content,
        status: "TALK",
    };

    stompClient.send("/app/chat/send", {}, JSON.stringify(message));
    messageInput.value = "";
    messageInput.focus();
};

// 메시지 UI 생성 함수
const createMessageElement = (message) => {
    const messageElement = document.createElement("li");
    const usernameElement = document.createElement("span");

    if (message.senderId === memberId) {
        messageElement.classList.add("mine");
    } else {
        messageElement.classList.add("theirs");
    }

    const messageHandlers = {
        ENTER: () => {
            messageElement.classList.add("event-message");
            usernameElement.textContent = message.content;
        },
        LEAVE: () => {
            messageElement.classList.add("event-message");
            usernameElement.textContent = message.content;
        },
        TALK: () => {
            messageElement.classList.add("chat-message");
            usernameElement.textContent = `${message.senderName}: ${message.content}`;
        },
        DEFAULT: () => {
            console.warn("Unknown message status:", message.status);
            usernameElement.textContent = message.content;
        }
    };

    const handler = messageHandlers[message.status] || messageHandlers.DEFAULT;
    handler();

    messageElement.appendChild(usernameElement);
    return messageElement;
};

// 이벤트 핸들링
window.onload = connect;                                        // 모든 리소스가 로딩된 후 실행 -> 텍스트기반이라서 무거운 리소스가 없음
window.addEventListener("beforeunload", disconnect);       // 사용자가 페이지를 닫거나 새로고침시 실행
window.onhashchange = disconnect;                               // URL 해시(#)가 변경될 때 실행 (뒤로가기)

messageForm.addEventListener("submit", sendMessage, true);