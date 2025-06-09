// UI Elements
const videoButtonOff = document.querySelector('#video_off');
const videoButtonOn = document.querySelector('#video_on');
const audioButtonOff = document.querySelector('#audio_off');
const audioButtonOn = document.querySelector('#audio_on');
const exitButton = document.querySelector('#exit');
const localVideo = document.getElementById('local_video');
const remoteVideo = document.getElementById('remote_video');

document.querySelector('#view_on').addEventListener('click', startScreenShare);
document.querySelector('#view_off').addEventListener('click', stopScreenShare);

// Member Session Info
const { memberId: id, name } = JSON.parse(localStorage.getItem("memberSession"));

const memberId = id;
const memberName = name;

// WebSocket Connect Info
const socket = new WebSocket(`wss://${window.location.host}/signal`);

// WebRTC STUN Server Info
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.stunprotocol.org:3478'},
        {'urls': 'stun:stun.l.google.com:19302'},
    ]
};

// WebRTC Media Server
const mediaConstraints = {
    video: true,
    audio: {
        echoCancellation: true,
        noiseSuppression: true,
        sampleRate: 44100
    }
};

// WebRTC 변수
let localStream;
let localVideoTracks;
let myPeerConnection;

const connect = () => {
    socket.onmessage = (signal) => {
        const message = JSON.parse(signal.data);
        switch (message.type) {
            case "offer":
                console.log('Signal OFFER received');
                handleOfferMessage(message); break;
            case "answer":
                console.log('Signal ANSWER received');
                handleAnswerMessage(message); break;
            case "ice":
                console.log('Signal ICE Candidate received');
                handleIceMessage(message); break;
            case "enter":
                console.log('Client is starting to ' + (message.data === "true)" ? 'negotiate' : 'wait for a peer'));
                handleEnterMessage(message); break;
            case "leave":
                console.log("disconnect")
                disconnect(); break;
            default:
                handleErrorMessage("Wrong type message received from server");
        }
    }

    socket.onopen = () => {
        console.log("Socket connected room: " + roomId);
        sendToServer({
            roomId: roomId,
            senderId: memberId,
            senderName: memberName,
            type: "enter",
        });
    }

    socket.onclose = () => {
        console.log('Socket has been closed');
    }

    socket.onerror = (message) => {
        handleErrorMessage(message);
    }
}

const handleOfferMessage = (message) => {
    const description = new RTCSessionDescription(message.sdp);

    if (description !== null && message.sdp !== null) {
        myPeerConnection.setRemoteDescription(description)
            .then(() => {
                return navigator.mediaDevices.getUserMedia(mediaConstraints);
            })
            .then((stream) => {
                localStream = stream;
                try {
                    localVideo.srcObject = localStream;
                } catch (error) {
                    localVideo.src = window.URL.createObjectURL(stream);
                }
                localStream.getTracks().forEach(track => myPeerConnection.addTrack(track, localStream));
            })
            .then(() => {
                return myPeerConnection.createAnswer();
            })
            .then((answer) => {
                return myPeerConnection.setLocalDescription(answer);
            })
            .then(() => {
                sendToServer({
                    roomId: roomId,
                    senderId: memberId,
                    senderName: memberName,
                    sdp: myPeerConnection.localDescription,
                    type: 'answer',
                });
            })
            .catch(handleErrorMessage)
    }

}

const handleAnswerMessage = (message) => {
    // ICE Candidate를 추가해 네트워크 간 연결
    myPeerConnection.addIceCandidate(message.sdp).catch(handleErrorMessage);
}

const handleIceMessage = (message) => {
    const candidate = new RTCIceCandidate(message.candidate);
    myPeerConnection.addIceCandidate(candidate).catch(handleErrorMessage);
}

const handleEnterMessage = (message) => {
    // browser를 서버에 연결
    myPeerConnection = new RTCPeerConnection(peerConnectionConfig);

    // browser가 ice 후보(ip, port)를 만날 때, 상대방에서 후보 정보를 전달
    myPeerConnection.onicecandidate =  (event) => {
        if (event.candidate) {
            sendToServer({
                roomId: roomId,
                senderId: memberId,
                senderName: memberName,
                candidate: event.candidate,
                type: "ice",
            });
            console.log('ICE Candidate Event: ICE candidate sent');
        }
    };
    // media stream(audio, video) 수신될때, 호출
    myPeerConnection.ontrack = (event) => {
        console.log('Track Event: set stream to remote video element');
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

    getMedia(mediaConstraints);

    if (message.data === "true") {
        myPeerConnection.onnegotiationneeded = () => {
            myPeerConnection.createOffer()
                .then((offer) => {
                    return myPeerConnection.setLocalDescription(offer);
                })
                .then(() => {
                    sendToServer({
                        roomId: roomId,
                        senderId: memberId,
                        senderName: memberName,
                        sdp: myPeerConnection.localDescription,
                        type: "offer"
                    })
                })
                .catch((error) => {
                    handleErrorMessage(error);
                })
        };
    }
}

const getMedia = (constraints) => {
    console.log(constraints)

    if (localStream) {
        localStream.getTracks().forEach(track => {
            track.stop();
        });
    }
    navigator.mediaDevices.getUserMedia(constraints)
        .then((mediaStream) => {
            localStream = mediaStream;
            localVideo.srcObject = mediaStream;
            localStream.getTracks().forEach(track => myPeerConnection.addTrack(track, localStream));
        })
        .catch((error) => {
        console.log('navigator.getUserMedia error: ', error);
        switch (error.name) {
            case "NotFoundError":
                alert("Unable to open your call because no camera and/or microphone were found.");
                break;
            case "SecurityError":
            case "PermissionDeniedError":
                // Do nothing; this is the same as the user canceling the call.
                break;
            default:
                alert("Error opening your camera and/or microphone: " + error.message);
                break;
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

const disconnect = () => {
    sendToServer({
        roomId: roomId,
        senderId: memberId,
        senderName: memberName,
        type: "leave"
    });

    if (myPeerConnection) {
        // event handler 제거
        myPeerConnection.onicecandidate = null;
        myPeerConnection.ontrack = null;
        myPeerConnection.onnegotiationneeded = null;
        myPeerConnection.oniceconnectionstatechange = null;
        myPeerConnection.onsignalingstatechange = null;
        myPeerConnection.onicegatheringstatechange = null;
        myPeerConnection.onnotificationneeded = null;
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

/*
 UI Handlers
  */
// mute video buttons handler
videoButtonOff.onclick = () => {
    localVideoTracks = localStream.getVideoTracks();
    localVideoTracks.forEach(track => localStream.removeTrack(track));
    localVideo.style.display = 'none';
    console.log('Video Off');
};

videoButtonOn.onclick = () => {
    localVideoTracks.forEach(track => localStream.addTrack(track));
    localVideo.style.display = 'inline';
    console.log('Video On');
};

// mute audio buttons handler
audioButtonOff.onclick = () => {
    localVideo.muted = true;
    console.log('Audio Off');
};

audioButtonOn.onclick = () => {
    localVideo.muted = false;
    console.log('Audio On');
};

// room exit button handler
exitButton.onclick = () => {
    disconnect();
    location.href = '/chat';
};

// DOM만 준비되면 바로 실행
document.addEventListener("DOMContentLoaded", () => {
    connect(); // 빠르게 실행됨
});

// 사용자가 페이지를 닫거나 새로고침시 실행
window.addEventListener('beforeunload', () => {
    disconnect();
});

// URL 해시(#)가 변경될 때 실행 (뒤로가기)
window.onhashchange = () => {
    disconnect();
}
