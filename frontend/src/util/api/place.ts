import localAxios from './http-common';
const local = localAxios();

/**장소 검색*/
export async function searchPlace(keyword: string) {
    return await local.get(`/api/place/search/${keyword}`);
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
