/**
 * 화면 공유 실행 과정
 * 나와 연결된 다른 peer 에 나의 화면을 공유하기 위해서는 다른 peer 에 보내는 Track 에서 stream 을 교체할 필요가 있다.
 * Track 이란 현재 MediaStream 을 구성하는 각 요소를 의미한다.
 *    - Track 는 오디오, 비디오, 자막 총 3개의 stream 으로 구성된다.
 *    - track[0] = 오디오, track[1] = 비디오
 * MediaStream 이란 video stream 과 audio steam 등의 미디어 스트림을 다루는 객체를 이야기한다
 *
 * 즉 상대방에게 보내는 track 에서 나의 웹캠 videoStream 대신 공유 화면에 해당하는 videoStream 으로 변경하는 것이다.
 *
 * 이렇듯 Track 에서 steam 을 교체 - replace - 하기 위해서는 아래와 같은 과정을 거친다.
 * 1. myPeerConnection 에서 sender 를 가져온다. sender 란 나와 연결된 다른 peer 로 생각하면 된다.
 * 2. sender 객체에서 replaceTrack 함수를 활용해서 stream 을 교체한다.
 * 3. shareView 의 Track[0] 에는 videoStream 이 들어있다. 따라서 replaceTrack 의 파라미터에 shareView.getTrack[0] 을 넣는다.
 * 4. 화면 공유 취소 시 원래 화상 화면으로 되돌리기 위해서는 다시 Track 를 localstream 으로 교체해주면 된다!
 *      이때 localStream 에는 audio 와 video 모두 들어가 있음으로 video 에 해당하는 Track[1] 만 꺼내서 교체해준다.
 */

export const ScreenShareHandler = {
    constraints: {
        audio: true,
        video: {
            width: 1980,
            height: 1080,
            frameRate: 50,
        }
    },

    /**
     * 화면 공유 시작
     * shareView에 stream 저장
     */
    async start() {
        try {
            if (navigator.mediaDevices && navigator.mediaDevices.getDisplayMedia) {
                // new browser
                shareView = await navigator.mediaDevices.getDisplayMedia(this.constraints);
            } else if (navigator.mediaDevices && navigator.getDisplayMedia) {
                // old browser
                shareView = await navigator.getDisplayMedia(this.constraints);
            } else {
                throw new Error("getDisplayMedia API is not supported in this browser.");
            }
            return shareView;
        } catch (err) {
            console.error("Error getting display media:", err);
        }
    },

    /**
     * 화면 공유 중지
     * shareView에 track(video, audio) 공유 중지
     */
    stop() {
        if (!shareView) return;
        shareView.getTracks().forEach(track => track.stop());
        shareView = null;
    },
};