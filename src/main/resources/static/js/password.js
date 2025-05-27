import {POST} from "./common/fetch.js";

window.checkPassword = async (roomId) => {
    const findRoom = roomInfo.find(room => room.id === parseInt(roomId));

    if (!findRoom) {
        alert("방 정보를 찾을 수 없습니다.");
        return;
    }

    if (!findRoom.password) {
        // 비밀번호 없는 방이면 바로 이동
        window.location.href = `/room/${roomId}`;
        return;
    }

    const password = window.prompt("비밀번호를 입력해 주세요.", "");
    if (!password) {
        alert("비밀번호를 입력해주세요.");
        return;
    }

    const result = await POST('/room/check', {id: roomId, password: password})

    if (result) window.location.href = `/room/${roomId}`;
    else window.alert("비밀번호가 틀렸습니다.");
}