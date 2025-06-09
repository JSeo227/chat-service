/**
 *  화면 공유 실행 과정
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

let shareView = null;

const screenHandler = () => {
    const constraints = {
        audio: true,
        video: {
            width: 1980, // 최대 너비
            height: 1080, // 최대 높이
            frameRate: 50, // 최대 프레임
        },
    };

    const getCrossBrowserScreenCapture = () => {
        if (navigator.getDisplayMedia)
            return navigator.getDisplayMedia(constraints);
        else if (navigator.mediaDevices.getDisplayMedia)
            return navigator.mediaDevices.getDisplayMedia(constraints);

    }
    /**
     * 스크린캡쳐 API를 호출합니다.
     * @returns {Promise<null>}
     */
    const start = async () => {
        try {
            shareView = await getCrossBrowserScreenCapture();
        } catch (err) {
            console.log('Error getDisplayMedia', err);
        }
        return shareView;
    }
    /**
     * 스트림의 트렉을 stop()시켜 스트림이 전송을 중지합니다.
     */
    const end = () => {
        shareView.getTracks().forEach((track) => {
            // console.log("화면 공유 중지")
            track.stop();
        });
        // // 전송 중단 시 share-video 부분 hide
        // $("#share-video").hide();
    }
    /**
     * extends
     */
    this.start = start;
    this.end = end;
};

/**
 * screenHandler를 통해 스크린 API를 호출합니다
 * 원격 화면을 화면 공유 화면으로 교체
 */
const startScreenShare = async () => {

    // 스크린 API 호출 & 시작
    await screenHandler.start();

    // 1. myPeerConnection 에 연결된 다른 sender 쪽으로 - 즉 다른 Peer 쪽으로 -
    // 2. shareView 의 Track 에서 0번째 인덱스에 들어있는 값 - 즉 videoStream 로 - 교체한다.
    await myPeerConnection.getSenders().forEach((sender)=>{ // 연결된 sender 로 보내기위한 반복문

        // 3. track 를 shareView 트랙으로 교체
        sender.replaceTrack(shareView.getTracks()[0])

    })

    // // Track 가 진짜 배열인지 확인하기
    // console.dir(shareView.getTracks());
    // console.dir(localStream.getTracks());

    /**
     * 화면 공유 중지 눌렀을 때 이벤트
     */
    shareView.getVideoTracks()[0].addEventListener('ended', () =>{
        // console.log('screensharing has ended')

        // 4. 화면 공유 중지 시 Track 를 localstream 의 videoStram 로 교체함
        myPeerConnection.getSenders().forEach((sender) =>{
            sender.replaceTrack(localStream.getTracks()[1]);
        })

        // $("#share-video").hide();

    });

}

/**
 * video off 버튼을 통해 스크린 API 종료
 * @returns {Promise<void>}
 */
const stopScreenShare = async () => {
    // screen share 종료
    await screenHandler.end();
    // myPeerConnection
    await myPeerConnection.getSenders().forEach((sender) =>{

        // 4. 화면 공유 중지 시 Track 를 localstream 의 videoStram 로 교체함
        sender.replaceTrack(localStream.getTracks()[1]);
    })
}
