import { useEffect, useState } from 'react';

import { TourInfoDetail, CountryInfo } from '../../types/types';
import MySlider from './MySlider';
import { getCountryInfo } from '../../util/api/country';
import { HttpStatusCode } from 'axios';

interface PropType {
    tourInfo: TourInfoDetail;
}

export default function TourBasicInfo(props: PropType) {
    const [countryInfoList, setCountryInfoList] = useState<CountryInfo[]>([]);
    const [countryCodes, setCountryCodes] = useState<string[]>([]);

    useEffect(() => {
        setCountryCodes(props.tourInfo.cityList.map((city) => city.countryCode));
    }, [props.tourInfo]);

    useEffect(() => {
        if (countryCodes.length > 0) {
            const fetchCountryInfo = async () => {
                const newCountryInfoList: CountryInfo[] = [];
                for (const country of countryCodes) {
                    try {
                        const res = await getCountryInfo(country);
                        if (res.status === HttpStatusCode.Ok) {
                            const newInfo = {
                                climate: res.data.climate,
                                currencyUnit: res.data.currencyUnit,
                                kst: res.data.kst,
                                language: res.data.language,
                                plug_type: res.data.plug_type,
                                voltage: res.data.voltage,
                            };
                            if (
                                !newCountryInfoList.some(
                                    (countryInfo) =>
                                        countryInfo.climate === newInfo.climate &&
                                        countryInfo.currencyUnit === newInfo.currencyUnit &&
                                        countryInfo.kst === newInfo.kst &&
                                        countryInfo.language === newInfo.language &&
                                        countryInfo.plug_type === newInfo.plug_type &&
                                        countryInfo.voltage === newInfo.voltage
                                )
                            ) {
                                newCountryInfoList.push(newInfo);
                            }
                        }
                    } catch (err) {
                        console.log(err);
                    }
                }
                setCountryInfoList(newCountryInfoList);
            };

            fetchCountryInfo();
        }
    }, [countryCodes]);

    return (
        <>
            <div className="w-full justify-between items-end p-5 bak">
                <div className="text-xl font-bold">기본정보</div>
                <div className="">
                    {countryInfoList.length === 1 ? (
                        countryInfoList.map((countryInfo, index) => (
                            <div key={index} className="border-2 border-blue-200 rounded-2xl p-3">
                                <div className="grid grid-cols-3 mb-3">
                                    <div className="flex flex-col items-center">
                                        <div className="text-gray-600">언어</div>
                                        <div className="text-lg whitespace-pre text-center">
                                            {countryInfo.language.split(',').join('\n')}
                                        </div>
                                    </div>
                                    <div className="flex flex-col items-center">
                                        <div className="text-gray-600">전압</div>
                                        <div className="text-lg whitespace-pre text-center">
                                            {countryInfo.voltage.split(',').join('\n')}
                                        </div>
                                    </div>
                                    <div className="flex flex-col items-center">
                                        <div className="text-gray-600">시차(한국기준)</div>
                                        <div className="text-lg">{countryInfo.kst.toString()}시간</div>
                                    </div>
                                </div>
                                <div>
                                    <div className="flex flex-col items-center">
                                        <div className="text-gray-600">기후</div>
                                        <div className="text-wrap text-lg">{countryInfo.climate}</div>
                                    </div>
                                </div>
                                <div></div>
                            </div>
                        ))
                    ) : (
                        <MySlider countryInfoList={countryInfoList} />
                    )}
                </div>
            </div>
        </>
    );
}
