import { CountryMapping } from '../../types/types';

export default function CountryCodeToName(countryCode: string, countryList: CountryMapping[]) {
    const resCountry = countryList.find((country) => country.countryCode === countryCode);
    return resCountry ? resCountry.countryName : '';
}
