import { AccountInfo } from '../../types/types';
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

/**가계부 항목 상세 조회*/
export async function getAccount(payId: string, tourId: string, payType: string) {
    return await local.get(`/api/payment/${tourId}/${payId}?payType=${payType}`);
}

/**가계부 항목 삭제*/
export async function deleteAccount(payId: string, tourId: string, payType: string) {
    return await local.delete(`/api/payment/${tourId}/${payId}?payType=${payType}`);
}

/**가계부 항목 수정*/
export async function editAccount(payId: string, item: AccountInfo) {
    return await local.put(`/api/payment/${payId}`, JSON.stringify(item));
}
