import localAxios from "./http-common";
const local = localAxios();

/** 체크리스트 조회 */
export async function getChecklist(tourId: string) {
  return await local.get(`/api/checklist/${tourId}`);
}
