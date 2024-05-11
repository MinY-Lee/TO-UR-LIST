import { useState } from 'react';
import { City, MemberInfo } from '../../types/types';
import GhostProfile from '../../assets/image/ghostProfile.png';
import CancelIcon from '../../assets/svg/cancelIcon';
import MyButton from '../Buttons/myButton';

interface PropType {
    searchbarClick: boolean;
    selectedCity: City[];
    handleCitySelect: (city: City) => void;
    searchList: City[];
    resultList: City[];
    query: string;
}

export default function SearchResult(props: PropType) {
    return (
        <>
            {props.searchbarClick ? (
                <div id="city-list-container" className="max-h-[30vh] overflow-y-scroll p-2 w-full text-base">
                    {props.selectedCity.length > 0 &&
                        props.selectedCity.map((city, index) => (
                            <div key={index} className="flex justify-between m-2">
                                <div className="text-base">
                                    {city.countryCode}, {city.cityName}
                                </div>
                                <div className="flex items-center gap-2">
                                    <div className="text-sm color-text-blue-2">선택됨</div>
                                    <div onClick={() => props.handleCitySelect(city)}>
                                        <CancelIcon width={1.5} />
                                    </div>
                                </div>
                            </div>
                        ))}
                    {props.query !== '' && props.searchList.length === 0 ? (
                        <div className="text-lg text-center text-gray-500">검색 결과가 없습니다.</div>
                    ) : (
                        props.resultList.map((res, index) => (
                            <div key={index} className="flex justify-between m-2">
                                <div className="text-base">
                                    {res.countryCode}, {res.cityName}
                                </div>
                                <MyButton
                                    type="small"
                                    text="선택"
                                    isSelected={true}
                                    onClick={() => props.handleCitySelect(res)}
                                    className="text-sm font-medium"
                                />
                            </div>
                        ))
                    )}
                </div>
            ) : (
                ''
            )}
        </>
    );
}
