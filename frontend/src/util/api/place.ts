import localAxios from './http-common';
const local = localAxios();

/**장소 검색*/
export async function searchPlace(keyword: string) {
    return await local.get(`/api/place/search/${keyword}`);
}
