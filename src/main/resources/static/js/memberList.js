import {GET} from "./common/fetch.js";

const getMemberList = async (roomId) => {
    return await GET(`/room/${roomId}/list`);
}

getMemberList(roomId)
    .then(members => {
    const memberListDiv = document.getElementById('memberList');
    memberListDiv.innerHTML = '';

    members.forEach(name => {
        const li = document.createElement('li');
        li.textContent = name;
        li.classList.add('dropdown-item');

        memberListDiv.appendChild(li);
    });
});