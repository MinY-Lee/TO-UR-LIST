import { useEffect, useState } from "react";

import getCountryInfo from '../../dummy-data/get_country_info.json';
import { TourInfoDetail, CountryInfo } from "../../types/types";

export default function TourHeader(props: TourInfoDetail) {
    const data = props.data || {};

    const [cityInfoList, setCityInfoList] = useState<CountryInfo[]>([]);

    useEffect(() => {
        const cityCodes = data.cityList?.map(city => city.countryCode) || [];
        setCityInfoList(getCountryInfo.filter(country => cityCodes.includes(country.countryCode)));

    }, [data.cityList]);

    

    return (
        <>
            <div className="w-full justify-between items-end p-5 bak">
                    
                <div className="text-xl font-bold">
                    기본정보
                </div>
                {cityInfoList.length > 0 && cityInfoList.map((cityInfo, index) => (
                    <div className="border border-2 border-blue-200 rounded-2xl p-3">
                        <div className="grid grid-cols-3">
                            <div className="flex flex-col items-center">
                                <div className="text-gray-600">언어</div>
                                <div className="text-center whitespace-pre">{ cityInfo.language.split(',').join('\n') }</div>
                            </div>
                            <div className="flex flex-col items-center">
                                <div className="text-gray-600">전압</div>
                                <div>{ cityInfo.voltage.split(',').join('\n') }</div>
                            </div>
                            <div className="flex flex-col items-center">
                                <div className="text-gray-600">시차(한국기준)</div>
                                <div>{ cityInfo.KST }시간</div>
                            </div>
                        </div>
                            <div className="flex flex-col items-center">
                                <div className="text-gray-600">기후</div>
                                <div className="text-wrap">{ cityInfo.climate }</div>
                            </div>
                        <div>
                    </div>
                </div>
                    
                ))}
                
                
            </div>
        </>
    );
}
