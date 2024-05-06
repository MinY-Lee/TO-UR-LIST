import { BaseSyntheticEvent, useEffect, useState } from "react";
import { TourInfoDetail, MemberInfo } from "../../types/types";
import MyButton from "../Buttons/myButton";
import CheckModal from "../CheckModal";
import SearchBar from "../SearchBar/mySearchBar";

import CountryList from '../../dummy-data/get_country.json';
import CityList from '../../dummy-data/get_city.json';


interface PropType {
    tourInfo: TourInfoDetail;
    onChange: (type: string) => void;
}

export default function TourEditHeader(props: PropType) {

    const [data, setData] = useState<TourInfoDetail>();
    const [hostChangeModal, setHostChangeModal] = useState<boolean>(false);
    const [hoveredMember, setHoveredMember] = useState<MemberInfo | null>(null);
    

    const [searchbarClick, setSearchbarClick] = useState<boolean>(false);
    const [title, setTitle] = useState<string>("");
    const [query, setQuery] = useState<string>("");
    const [searchList, setSearchList] = useState<string[]>([]); // 실제 검색 결과
    const [resultList, setResultList] = useState<string[]>([]); // 화면에 보여줄 검색 결과
    const [selectedCity, setSelectedCity] = useState<string[]>([]);



    useEffect(() => {
        setData(props.tourInfo);
        setTitle(data ? data.tourTitle : "");
    }, [data])

    const isHost = (member: MemberInfo): string => {
        if (member.memberType === "host") {
            return "w-14 h-14 rounded-full flex border-gradient bg-gradient-to-b from-blue-500 via-blue-400 to-blue-100";
        }
        return "";
    };

    const handleMouseEnter = (member: MemberInfo) => {
        setHoveredMember(member);
    };

    const handleMouseLeave = () => {
        setHoveredMember(null);
    };

    const handleTypeChange = (type: string) => {
        // 수정에서 여행 정보로 돌아가
        //////////////////////
        // 그 전에 수정 api 먹이기
        console.log(title);
        props.onChange(type);
    };

    const handleInputChange = (event: BaseSyntheticEvent) => {
        setTitle(event.target.value);
    };

    const handleHost = () => {
        setHostChangeModal(true)
    }

    // searchBar로부터 데이터 받아서 검색 수행
    const handleQuery = (data: string) => {
        setQuery(data);

        // 나라 -> 도시 로직인 경우 검색어를 나라 코드로 치환
        const foundCountry = CountryList.find(country => country.countryName === data);

        if (foundCountry) {
            // 코드로 도시 검색 및 결과 포맷팅
            const cityListWithCountryName: string[] = CityList
                .find(country => country.countryCode === foundCountry.countryCode)?.cityList
                ?.map(city => `${foundCountry.countryCode}, ${city}`) || [];

            setSearchList(cityListWithCountryName);
            // 선택된 도시가 있을 때 결과를 업데이트
            const updatedResultList = cityListWithCountryName.filter(city => !selectedCity.includes(city));
            setResultList(updatedResultList);
        }
    };

    const findCountryByCity = (city: string) => {
        CityList.forEach((country) => {
            if (country.cityList.includes(city)) {
                return country
            }
        })
        return {countryCode : "", cityList: []};
    }
    
    // 여행할 도시 선택 또는 해제
    const handleCitySelect = (city: string) => {
        
        let idx = -1;

        data?.cityList.forEach((target, index) => {
            target.cityName == city ? idx = index : idx = -1
        })

        if (idx === -1) {
            // 선택되지 않은 도시라면 추가
            const newCity = {
                'countryCode': findCountryByCity(city).countryCode,
                'cityName': city
            };
            data?.cityList.push(newCity);

            console.log(data?.cityList)

        } else {
            // 이미 선택된 도시라면 제거
            const updatedCities = selectedCity.filter(selected => selected !== city);
            setSelectedCity(updatedCities);
        }
    };

    return (
        <div>
            {/* {hostChangeModal ? (
                <CheckModal
                    mainText="로그아웃 하시겠습니까?"
                    subText=""
                    OKText="확인"
                    CancelText="취소"
                    clickOK={logoutProceed}
                    clickCancel={closeModal}
                />
            ) : (
                <></>
            )} */}

            <div className="w-full justify-between items-end p-5 bak">
                <div className="">
                    <div className="w-full rounded-lg border border-solid border-neutral-300 text-3xl font-bold pl-5">
                        <input
                            value={title}
                            onChange={handleInputChange}
                            className="relative m-0 -mr-0.5 w-[90%] flex-auto bg-clip-padding py-1 outline-none"
                            aria-label="Title"
                            aria-describedby="button-addon1"
                        />
                    </div>
                    <div className="flex items-center mt-2 gap-1">
                        {data?.memberList.map((member: MemberInfo, index: number) => (
                            <div key={index} className="relative" onMouseEnter={() => handleMouseEnter(member)} onMouseLeave={handleMouseLeave}>
                                <div className={`${isHost(member)}`} onClick={member.memberType == "host" ? handleHost : undefined }>
                                    {member.memberType !== "ghost"
                                        ? <div className="shadow-lg m-1 font-bold text-3xl text-blue-500 bg-blue-200 w-12 h-12 rounded-full flex justify-center items-center">
                                            {member.userNickname[0]}
                                        </div>
                                        : <div className="shadow-lg m-1 font-bold text-3xl bg-gray-400 w-12 h-12 rounded-full flex justify-center items-center">
                                            👻
                                        </div>
                                    }
                                    
                                </div>
                                {hoveredMember === member && (
                                    <div className="absolute whitespace-nowrap z-10 text-sm bottom-1 left-[40%] bg-gray-500 pl-1 pr-1 rounded-md text-white">
                                        {member.userName}
                                    </div>
                                )}
                                {member.memberType != "host"
                                    ? <div className="absolute top-[5%] left-[70%] bg-black text-white flex justify-center items-center z-10 w-[40%] h-[40%] rounded-full">x</div>
                                    : ""
                                }
                            </div>
                        ))}
                        <div className="border-dashed border-2 color-text-blue-2 border-[#5faad9] m-1 font-bold text-3xl w-12 h-12 rounded-full flex justify-center items-center">
                            +
                        </div>
                    </div>
                    <div className="flex gap-2 my-3 justify-center">
                        <input
                            className="px-[2vw] py-2 border-[0.3vw] border-neutral-300"
                            style={{ borderRadius: '1vw' }}
                            value={data?.startDate}
                            placeholder="YYYY.MM.DD"
                            // onChange={startDateChange}
                        ></input>
                        <input
                            className="px-[2vw] py-2 border-[0.3vw] border-neutral-300"
                            style={{ borderRadius: '1vw' }}
                            value={data?.endDate}
                            placeholder="YYYY.MM.DD"
                            // onChange={startDateChange}
                        ></input>
                    </div>
                    <div className="text-[5vw] flex flex-col items-center">
                        <div className="grid grid-cols-10 w-full">
                            <div className="relative col-span-1 justify-center">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6 m-1 mt-2">
                                    <path strokeLinecap="round" strokeLinejoin="round" d="M15 10.5a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
                                    <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1 1 15 0Z" />
                                </svg>
                            </div>
                            <div id='search-container' className="col-span-7 justify-center shadow-md border border-neutral-300 rounded-lg">
                                <div onClick={() => setSearchbarClick(!searchbarClick)} >
                                    <SearchBar onChange={handleQuery}/>
                                </div>
                                {searchbarClick 
                                    ? <div id='city-list-container' className="h-fit overflow-scroll p-2 w-full">
                                        {data?.cityList.length > 0 && data?.cityList.map((city, index) => (
                                            <div key={index} className="flex justify-between m-2">
                                                <div className="text-lg">{city.countryCode}, {city.cityName}</div> 
                                                <div className="flex items-center gap-2">
                                                    <div className="text-sm color-text-blue-2">선택됨</div>                                                                                                                                                                                                                                                                                                              
                                                    <div onClick={() => handleCitySelect(city.cityName)}>x</div>                                                                                                                                                                                                                                                                                                              
                                                </div>
                                            </div>
                                        ))}
                                        {query !== "" && searchList.length === 0 ? (
                                            <div className="text-lg text-center text-gray-500">검색 결과가 없습니다.</div>
                                        ) : (
                                            resultList.map((res, index) => (
                                                <div key={index} className="flex justify-between m-2">
                                                    <div className="text-lg">{res}</div>
                                                    <MyButton 
                                                        type="small" 
                                                        text="선택"
                                                        isSelected={false}
                                                        onClick={() => handleCitySelect(res)}
                                                        className="text-sm px-2"
                                                    />                                                                                                                                                                                                                                                                                                                 
                                                </div>
                                            ))
                                        )}
                                    </div>
                                    : ""
                                }
                            </div>
                            <div className="col-span-2 pl-3 pt-1 justify-center">
                                <MyButton isSelected={true} text="적용" type="full" className="py-1" onClick={() => handleTypeChange('default')}/>
                            </div>
                        </div>
                        
                        
                    </div>
                </div>
                  
            </div>
        </div>
    );
}
