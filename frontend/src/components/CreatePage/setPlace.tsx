import { useRef, useState, useEffect } from "react";

import SearchBar from "../../components/SearchBar/mySearchBar";
import MyButton from "../../components/Buttons/myButton";

import { City, CountryMapping } from "../../types/types";
import { GetCityList, GetCountryList } from "../../util/api/country";
import Spinner from "../../assets/svg/spinner";
import CountryCodeToName from "../TourPage/countryIdToName";
import { HttpStatusCode } from "axios";

interface PropType {
    onChangeSelected: (selectedCity: City[]) => void;
}

export default function SetPlace(props: PropType) {
    const [query, setQuery] = useState<string>("");
    const [searchList, setSearchList] = useState<City[]>([]); // 실제 검색 결과
    const [resultList, setResultList] = useState<City[]>([]); // 화면에 보여줄 검색 결과
    const [selectedCity, setSelectedCity] = useState<City[]>([]);
    const [countryList, setCountryList] = useState<CountryMapping[]>([]);
    const [countryName, setCountryName] = useState<string>("");
    const [isLoading, setIsLoading] = useState<boolean>(false); // 로딩 상태 추가

    const topRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        GetCountryList()
            .then((res) => {
                if (res.status === HttpStatusCode.Ok) {
                    setCountryList(res.data);
                }
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    useEffect(() => {
        if (topRef.current) {
            topRef.current.scrollIntoView({ behavior: "smooth" });
        }
        // 선택된 도시가 변경될 때마다 resultList 업데이트
        const updatedResultList = searchList.filter((city) => !selectedCity.includes(city));
        setResultList(updatedResultList);

        // 부모 컴포넌트에 보내기
        props.onChangeSelected(selectedCity);
    }, [selectedCity]);

    // searchBar로부터 데이터 받아서 검색 수행
    const handleDataFromChild = (data: string) => {
        setIsLoading(true); // 로딩 시작
        setSearchList([]);
        setQuery(data);
        // 나라 -> 도시 로직인 경우 검색어를 나라 코드로 치환
        const foundCountry = countryList.find((country: any) => country.countryName === data);

        if (foundCountry) {
            setCountryName(foundCountry.countryName);

            // 코드로 도시 검색 및 결과 포맷팅
            GetCityList(foundCountry.countryCode)
                .then((res) => {
                    let CityList: City[] = [];
                    res.data.map((cityName: string) => {
                        const newCity = {
                            countryCode: foundCountry.countryCode,
                            cityName: cityName,
                        };
                        CityList.push(newCity);
                    });

                    setSearchList(CityList);
                    // 선택된 도시가 있을 때 결과를 업데이트
                    const updatedResultList = CityList.filter(
                        (city) =>
                            !selectedCity.some(
                                (selected) => JSON.stringify(selected) === JSON.stringify(city)
                            )
                    );
                    setResultList(updatedResultList);
                })
                .finally(() => {
                    setIsLoading(false); // 로딩 종료
                });
        } else {
            setIsLoading(false); // 로딩 종료 (나라를 찾지 못한 경우)
        }
    };

    // 여행할 도시 선택 또는 해제
    const handleCitySelect = (city: City) => {
        // 스크롤 맨 위로
        const index = selectedCity.indexOf(city);

        if (index === -1) {
            // 선택되지 않은 도시라면 추가
            setSelectedCity([...selectedCity, city]);
        } else {
            // 이미 선택된 도시라면 제거
            const updatedCities = selectedCity.filter((selected) => selected !== city);
            setSelectedCity(updatedCities);
        }
    };

    return (
        <div className="flex flex-col items-center">
            <div className="text-2xl font-bold m-3">어디로 떠나시나요?</div>
            <div id="search-container" className="w-[90%] shadow-md border border-black rounded-lg">
                <SearchBar onChange={handleDataFromChild} />
            </div>
            <div id="city-list-container" className="m-2 h-[40vh] overflow-auto w-[90%]">
                <div ref={topRef}></div>
                {selectedCity.length > 0 &&
                    selectedCity.map((res, index) => (
                        <div key={index} className="flex justify-between m-2">
                            <div className="text-lg">
                                {CountryCodeToName(res.countryCode, countryList)}, {res.cityName}
                            </div>
                            <MyButton
                                type="small"
                                className="text-white color-bg-blue-6 font-medium"
                                text="해제"
                                isSelected={true}
                                onClick={() => handleCitySelect(res)}
                            />
                        </div>
                    ))}
                {query !== "" && searchList.length === 0 ? (
                    <>
                        {isLoading ? (
                            <div className="flex justify-center mt-5">
                                <Spinner />
                            </div>
                        ) : (
                            <div className="text-lg text-center text-gray-500">
                                검색 결과가 없습니다.
                            </div>
                        )}
                    </>
                ) : (
                    resultList.map((res, index) => (
                        <div key={index} className="flex justify-between m-2">
                            <div className="text-lg">
                                {countryName}, {res.cityName}
                            </div>
                            <MyButton
                                type="small"
                                text="선택"
                                isSelected={false}
                                onClick={() => handleCitySelect(res)}
                                className="color-text-blue-6"
                            />
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}
