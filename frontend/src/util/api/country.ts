import localAxios from './http-common';
const local = localAxios();

export async function GetCountryList() {
    return await local.get(`/api/country`);
}

export async function GetCityList(countryCode: string) {
    return await local.get(`/api/country/city/${countryCode}`);
}
