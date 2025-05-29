import {GET} from "./common/fetch";

window.getMemberList = async (roomId) => {
    const result = await GET(`/member/${roomId}/list`);
    console.log(result);
    return result;
}

getMemberList();