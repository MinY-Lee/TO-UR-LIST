import localAxios from "./http-common";
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
