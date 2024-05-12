import localAxios from './http-common';
const local = localAxios();

/**장소 검색*/
export async function createTour(newTour: any) {
    return await local.post(`/api/tour`, JSON.stringify(newTour));
}

/**내 여행 목록 조회 */
export async function getMyTourList() {
    return await local.get(`/api/tour`);
}

/**여행 세부정보 조회 */
export async function getTourDetail(tourId: string) {
    return await local.get(`/api/tour/${tourId}`);
}
