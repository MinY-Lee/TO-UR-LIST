import localAxios from './http-common';
const local = localAxios();

/**장소 검색*/
export async function createTour(newTour: any) {
    return await local.post(`/api/tour`, JSON.stringify(newTour));
}
