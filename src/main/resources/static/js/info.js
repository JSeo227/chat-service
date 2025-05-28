import {GET} from "./common/fetch";

const { memberId: id } = JSON.parse(localStorage.getItem('memberSession'));
window.goMemberInfo = async () => await GET(`/members/${id}/info`);