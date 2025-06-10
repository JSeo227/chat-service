// UI Elements
const videoButtonOff = document.querySelector("#video_off");
const videoButtonOn = document.querySelector("#video_on");
const audioButtonOff = document.querySelector("#audio_off");
const audioButtonOn = document.querySelector("#audio_on");
const exitButton = document.querySelector("#exit");
const localVideo = document.getElementById("local_video");
const remoteVideo = document.getElementById("remote_video");

document.querySelector("#view_on").addEventListener("click", startScreenShare);
document.querySelector("#view_off").addEventListener("click", stopScreenShare);

// Member Session Info
const { memberId: id, name } = JSON.parse(localStorage.getItem("memberSession"));

const memberId = id;
const memberName = name;

// WebSocket Connect Info
const socket = new WebSocket(`wss://${window.location.host}/signal`);

// WebRTC ICE (STUN Server) Info
const peerConnectionConfig = {
    "iceServers": [
        {"urls": "stun:stun.stunprotocol.org:3478"},
        {"urls": "stun:stun.l.google.com:19302"},
    ]
};

// WebRTC Media Server
const mediaConstraints = {
    video: true,
    audio: {
        echoCancellation: true,   // 에코 제거
        noiseSuppression: true,   // 소음 제거
        sampleRate: 44100         // 오디오 샘플레이트
    }
};

// WebRTC 변수
let localStream;
let myPeerConnection;

// WebRTC Connect
const connect = () => {
    const messageHandlers = {
        OFFER: (message) => {
            console.log("[Signal] OFFER received");
            handleOfferMessage(message);
        },
        ANSWER: (message) => {
            console.log("[Signal] ANSWER received");
            handleAnswerMessage(message);
        },
        ICE: (message) => {
            console.log("[Signal] ICE Candidate received");
            handleIceMessage(message);
        },
        ENTER: (message) => {
            console.log(`[Signal] ENTER received: ${(message.senderId === memberId) 
                ? "This client will initiate negotiation." : "Waiting for peer to initiate negotiation."}`);
            handleEnterMessage(message);
        },
        LEAVE: () => {
            console.log("[Signal] Disconnect");
            disconnect();
        }
    };

    socket.onmessage = (signal) => {
        const message = JSON.parse(signal.data);
        const handler = messageHandlers[message.type];
        if (handler) {
            handler(message);
        } else {
            handleErrorMessage("Wrong type message received from server");
        }
    };

    socket.onopen = () => {
        console.log("Socket connected room: #" + roomId);
        sendToServer({
            roomId,
            senderId: memberId,
            senderName: memberName,
            type: "ENTER",
        });
    };

    socket.onclose = () => {
        console.log("Socket has been closed");
    };

    socket.onerror = (message) => {
        handleErrorMessage(message);
    };
};

// WebRTC Disconnect
const disconnect = () => {
    sendToServer({
        roomId,
        senderId: memberId,
        senderName: memberName,
        type: "LEAVE"
    });

    if (myPeerConnection) {
        // event handler 제거
        myPeerConnection.onicecandidate = null;
        myPeerConnection.ontrack = null;
        myPeerConnection.onnegotiationneeded = null;
        myPeerConnection.oniceconnectionstatechange = null;
        myPeerConnection.onsignalingstatechange = null;
        myPeerConnection.onicegatheringstatechange = null;
        myPeerConnection.onnotiticationneeded = null;
        myPeerConnection.onremovetrack = null;

        // media stream 중지
        if (remoteVideo.srcObject) {
            remoteVideo.srcObject.getTracks().forEach(track => track.stop());
        }

        remoteVideo.src = null;
        localVideo.src = null;

        myPeerConnection.close();
        myPeerConnection = null;

        if (socket !== null) {
            console.log("close socket")
            socket.close();
        }
    }
}

// 연결 요청 + 내 미디어 정보 보냄 (A -> B)
const handleOfferMessage = (message) => {
    const description = new RTCSessionDescription(message.sdp);

    if (description !== null && message.sdp !== null) {
        // 	상대방의 연결 요청 정보를 내 PeerConnection에 등록
        myPeerConnection.setRemoteDescription(description)
            .then(() => { // 카메라/마이크 stream 확보
                return navigator.mediaDevices.getUserMedia(mediaConstraints);
            })
            .then((stream) => { // 내가 가진 media stream을 연결에 추가
                localStream = stream;
                localVideo.srcObject = localStream;
                localStream.getTracks().forEach(track => {
                    myPeerConnection.addTrack(track, localStream);
                });
            })
            .then(() => { // 상대방의 OFFER에 대한 ANSWER 생성
                return myPeerConnection.createAnswer();
            })
            .then((answer) => { // 내 local SDP 설정
                return myPeerConnection.setLocalDescription(answer);
            })
            .then(() => { // 정보를 signal 을 통해 상대방에게 보냄
                sendToServer({
                    roomId,
                    senderId: memberId,
                    senderName: memberName,
                    sdp: myPeerConnection.localDescription,
                    type: "ANSWER",
                });
            })
            .catch(handleErrorMessage)
    }
}

// 연결 수락 + 내 미디어 정보 답장 (A <- B)
const handleAnswerMessage = (message) => {
    const description = new RTCSessionDescription(message.sdp);
    // 상대방의 ANSWER 설정
    myPeerConnection.setRemoteDescription(description).catch(handleErrorMessage);
}

// 서로 연결할 수 있는 경로(네트워크 후보)를 계속 주고받음 (A <-> B)
const handleIceMessage = (message) => {
    const candidate = new RTCIceCandidate(message.candidate);
    // ICE 후보 등록 -> 네트워크 경로를 테스트
    myPeerConnection.addIceCandidate(candidate).catch(handleErrorMessage);
}

const handleEnterMessage = (message) => {
    myPeerConnection = new RTCPeerConnection(peerConnectionConfig);

    // ICE 후보(ip, port) 발생 시, 상대방에게 후보 정보를 전달
    myPeerConnection.onicecandidate =  (event) => {
        if (event.candidate) {
            sendToServer({
                roomId,
                senderId: memberId,
                senderName: memberName,
                candidate: event.candidate,
                type: "ICE",
            });
            console.log("ICE Candidate Event: ICE candidate sent");
        }
    };

    // 상대방의 media stream(audio, video) 수신 처리
    myPeerConnection.ontrack = (event) => {
        console.log("Track Event: set stream to remote video element");
        remoteVideo.srcObject = event.streams[0];
    };

    // 트랙 제거 감지
    // myPeerConnection.onremovetrack = handleRemoveTrackEvent;
    // 연결 상태 변경
    // myPeerConnection.oniceconnectionstatechange = handleICEConnectionStateChangeEvent;
    // ICE 후보 수집 진행 상태
    // myPeerConnection.onicegatheringstatechange = handleICEGatheringStateChangeEvent;
    // Signaling 상태 변경 감지
    // myPeerConnection.onsignalingstatechange = handleSignalingStateChangeEvent;

    // media 정보 설정
    getMedia(mediaConstraints);

    // 내가 연결 시작자라면 Offer 생성
    if (message.senderId === memberId) {
        myPeerConnection.onnegotiationneeded = () => {
            myPeerConnection.createOffer()
                .then((offer) => {
                    return myPeerConnection.setLocalDescription(offer);
                })
                .then(() => {
                    sendToServer({
                        roomId,
                        senderId: memberId,
                        senderName: memberName,
                        sdp: myPeerConnection.localDescription,
                        type: "OFFER"
                    })
                })
                .catch((error) => {
                    handleErrorMessage(error);
                })
        };
    }
}

const getMedia = (constraints) => {
    // 기존 스트림 종료 (재호출 대비)
    if (localStream) {
        localStream.getTracks().forEach(track => {
            track.stop();
        });
    }

    // 새 미디어 스트림 요청
    navigator.mediaDevices.getUserMedia(constraints)
        .then((mediaStream) => { // 스트림 설정
            localStream = mediaStream;
            localVideo.srcObject = mediaStream;
            localStream.getTracks().forEach(track => myPeerConnection.addTrack(track, localStream));
        })
        .catch((error) => {
            console.error("getUserMedia error:", error);

            const errorMessages = {
                NotFoundError: "카메라나 마이크를 찾을 수 없습니다.",
                SecurityError: "보안 설정으로 인해 장치에 접근할 수 없습니다.",
                PermissionDeniedError: "권한이 거부되었습니다.",
            };

            const message = errorMessages[error.name] || `카메라 또는 마이크에 접근하는 중 오류가 발생했습니다: ${error.message}`;

            if (error.name !== "PermissionDeniedError" && error.name !== "SecurityError") {
                alert(message);
            }

            disconnect();
        });
}

const sendToServer = (signal) => {
    const message = JSON.stringify(signal);
    console.log(message);
    socket.send(message);
}

const handleErrorMessage = (message) => {
    console.error(message);
}

/*
 UI Handlers
  */
// mute video buttons handler
videoButtonOff.onclick = () => {
    localStream.getVideoTracks().forEach(track => track.enabled = false);
    localVideo.style.display = "none";
    console.log("Video Off");
};

videoButtonOn.onclick = () => {
    localStream.getVideoTracks().forEach(track => track.enabled = true);
    localVideo.style.display = "inline";
    console.log("Video On");
};

// mute audio buttons handler
audioButtonOff.onclick = () => {
    localVideo.muted = true;
    console.log("Audio Off");
};

audioButtonOn.onclick = () => {
    localVideo.muted = false;
    console.log("Audio On");
};

// room exit button handler
exitButton.onclick = () => {
    disconnect();
    location.href = "/";
};

// 이벤트 핸들링
document.addEventListener("DOMContentLoaded", connect);     // DOM만 준비되면 바로 실행
window.addEventListener("beforeunload", disconnect);        // 사용자가 페이지를 닫거나 새로고침시 실행
window.onhashchange = disconnect;                                // URL 해시(#)가 변경될 때 실행 (뒤로가기)