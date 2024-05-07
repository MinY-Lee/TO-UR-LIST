import localAxios from './http-common';

const local = localAxios();

/**로그아웃 */
export async function logout() {
    return await local.get(`/authapi/auth/logout`);
}
