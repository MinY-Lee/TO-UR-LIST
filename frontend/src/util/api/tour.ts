import { AxiosHeaders, AxiosRequestConfig } from "axios";
import localAxios from "./http-common";
import { MemberInfo } from "../../types/types";
const local = localAxios();

/** 여행 생성 **/
export async function createTour(newTour: any) {
    return await local.post(`/api/tour`, JSON.stringify(newTour));
}

/** 나라 리스트 조회 **/
export async function getCountry() {
    return await local.get(`/api/country`);
}

/** 나라별 도시 리스트 조회 **/
export async function getCity(countryCode: string) {
    return await local.get(`/api/country/city/${countryCode}`);
}

/** 여행 세부정보 조회 **/
export async function getTour(tourId: string) {
    return await local.get(`/api/tour/${tourId}`);
}

/** 여행 삭제 **/
export async function deleteTour(tourId: string) {
    return await local.delete(`/api/tour/${tourId}`);
}

/** 여행 나가기 **/
export async function quitTour(tourId: string) {
    return await local.delete(`/api/tour`, { data: { tourId: tourId } });
}

/** 여행 제목 수정 **/
export async function editTitle(updatedData: any) {
    return await local.put(`/api/tour/title`, JSON.stringify(updatedData));
}

/** 여행 기간 수정 **/
export async function editPeriod(updatedData: any) {
    return await local.put(`/api/tour/period`, JSON.stringify(updatedData));
}

/** 여행 도시 수정 **/
export async function editCity(updatedData: any) {
    return await local.put(`/api/tour/city`, JSON.stringify(updatedData));
}
/**내 여행 목록 조회 */
export async function getMyTourList() {
    return await local.get(`/api/tour`);
}

/** 여행 멤버 초대 **/
export async function userInviteTour(newMember: any) {
    return await local.post(`/api/tour/member`, JSON.stringify(newMember));
}

/** 여행 고스트 초대 **/
export async function ghostInviteTour(newMember: any) {
    return await local.post(
        `/api/tour/member/ghost`,
        JSON.stringify(newMember)
    );
}

/** 여행 멤버 추방 **/
export async function deleteMemberApi(target: any) {
    return await local.delete(`/api/tour/member`, {
        data: JSON.stringify(target),
    });
}

/** 고스트멤버 닉네임 변경 */
export async function changeGhostNickname(target: any) {
    return await local.put(`/api/tour/member/ghost`, JSON.stringify(target));
}

/** 고스트멤버 유저로 변경 */
export async function changeGhostToGuest(ghostAndGuest: any) {
    return await local.post(
        `/api/tour/member/resurrection`,
        JSON.stringify(ghostAndGuest)
    );
}
