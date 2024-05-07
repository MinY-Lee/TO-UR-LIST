import localAxios from './http-common';
const local = localAxios();

/**유저 정보 불러오기 */
export async function getUserInfo() {
    return await local.get(`/api/user`);
}

/**닉네임 중복 체크 */
export async function checkDuplicatedNick(userNickname: string) {
    return await local.get(`/api/user/nickname/${userNickname}`);
}

/**회원가입 */
export async function register(userInfo: any) {
    return await local.post(`/api/user`, userInfo);
}

/**이름 수정 */
export async function changeName(userName: any) {
    return await local.put(`/api/user/name`, userName);
}

/**닉네임 수정 */
export async function changeNickname(userNickname: any) {
    return await local.put(`/api/user/nickname`, userNickname);
}

/**성별 수정 */
export async function changeGender(userGender: any) {
    return await local.put(`/api/user/gender`, userGender);
}

/**생년월일 수정 */
export async function changeBirthDay(userBirth: any) {
    return await local.put(`/api/user/birth`, userBirth);
}

/**회원 탈퇴 */
export async function withdraw() {
    return await local.delete(`/api/user`);
}
