import localAxios from './http-common';
const local = localAxios();

/**가계부 검색*/
export async function getAccountList(tourId: string) {
    return await local.get(`/api/payment/${tourId}`);
}

/** 환율 조회 **/
export async function getCurrency(countryCode: string, date: string) {
    return await local.get(`/api/payment/currency/${countryCode}/${date}`);
}
