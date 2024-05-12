import localAxios from "./http-common";
const local = localAxios();

/**가계부 검색*/
export async function getAccountList(tourId: string) {
  return await local.get(`/api/pay/${tourId}`);
}
