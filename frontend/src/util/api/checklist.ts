import { Item, ItemApi } from "../../types/types";
import localAxios from "./http-common";
const local = localAxios();

/** 체크리스트 조회 */
export async function getChecklist(tourId: string) {
  return await local.get(`/api/checklist/${tourId}`);
}

/** 체크리스트 추가 */
export async function addChecklist(type: string, item: ItemApi) {
  if (type == "private") {
    return await local.post(`/api/checklist/private`, JSON.stringify(item));
  }
  return await local.post(`/api/checklist/public`, JSON.stringify(item));
}
