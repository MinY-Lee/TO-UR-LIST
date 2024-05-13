import localAxios from "./http-common";
const local = localAxios();

export async function GetCountryList() {
  return await local.get(`/api/country`);
}

export async function GetCityList(countryCode: string) {
  return await local.get(`/api/country/city/${countryCode}`);
}

/** 나라 정보 가져오기 **/
export async function getCountryInfo(countryCode: string) {
  return await local.get(`/api/country/${countryCode}`);
}
