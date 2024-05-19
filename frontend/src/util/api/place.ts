import localAxios from "./http-common";
const local = localAxios();

/**장소 검색*/
export async function searchPlace(
    keyword: string,
    latitude: number,
    longitude: number
) {
    return await local.get(
        `/api/place/search/${keyword}/${longitude}/${latitude}`
    );
}

/**장소 상세 정보 조회 */
export async function searchPlaceDetail(
    tourId: string,
    tourDay: number,
    placeId: string
) {
    return await local.get(`/api/place/${tourId}/${tourDay}/${placeId}`);
}

/**장소 리스트 조회 */
export async function getPlaceList(tourId: string) {
    return await local.get(`/api/place/${tourId}`);
}

// 활동 리스트 조회
export async function getActivityList(placeId: string) {
    return await local.get(`/api/activity/${placeId}`);
}

// 사진 불러오기
export async function getPhotoUrl(photoName: string) {
    return await local.get(`/api/place/v1/${photoName}`);
}
