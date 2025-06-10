export const getMedia = ({ constraints, videoElement, peerConnection, onError }) => {
    // 기존 스트림 정리
    if (localStream) {
        localStream.getTracks().forEach(track => track.stop());
    }

    // 새 미디어 요청
    navigator.mediaDevices.getUserMedia(constraints)
        .then((mediaStream) => {
            localStream = mediaStream;
            videoElement.srcObject = mediaStream;

            mediaStream.getTracks().forEach(track => {
                peerConnection.addTrack(track, mediaStream);
            });
        })
        .catch((error) => {
            console.error("getUserMedia error:", error);

            const errorMessages = {
                NotFoundError: "카메라나 마이크를 찾을 수 없습니다.",
                SecurityError: "보안 설정으로 인해 장치에 접근할 수 없습니다.",
                PermissionDeniedError: "권한이 거부되었습니다.",
            };

            const message = errorMessages[error.name] || `오류 발생: ${error.message}`;

            if (error.name !== "PermissionDeniedError" && error.name !== "SecurityError") {
                alert(message);
            }

            if (typeof onError === "function") {
                onError();
            }
        });
};