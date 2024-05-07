import { useState, useEffect } from 'react';

import SearchBar from '../../components/SearchBar/mySearchBar';
import MyButton from '../../components/Buttons/myButton';

import { City } from '../../types/types';

import CountryList from '../../dummy-data/get_country.json';
import CityList from '../../dummy-data/get_city.json';

interface PropType {
    onChangeSelected: (selectedCity: string[]) => void;
}

export default function SetPlace(props: PropType) {
    const { onChangeSelected } = props;

    const [query, setQuery] = useState<string>('');
    const [searchList, setSearchList] = useState<string[]>([]); // 실제 검색 결과
    const [resultList, setResultList] = useState<string[]>([]); // 화면에 보여줄 검색 결과
    const [selectedCity, setSelectedCity] = useState<string[]>([]);

    useEffect(() => {
        // 선택된 도시가 변경될 때마다 resultList 업데이트
        const updatedResultList = searchList.filter(
            (city) => !selectedCity.includes(city)
        );
        setResultList(updatedResultList);

        // 부모 컴포넌트에 보내기
        onChangeSelected(selectedCity);
    }, [selectedCity]);

    // searchBar로부터 데이터 받아서 검색 수행
    const handleDataFromChild = (data: string) => {
        setQuery(data);
        // 나라 -> 도시 로직인 경우 검색어를 나라 코드로 치환
        const foundCountry = CountryList.find(
            (country) => country.countryName === data
        );

        if (foundCountry) {
            // 코드로 도시 검색 및 결과 포맷팅
            const cityListWithCountryName: string[] =
                CityList.find(
                    (country) =>
                        country.countryCode === foundCountry.countryCode
                )?.cityList?.map(
                    (city) => `${foundCountry.countryCode}, ${city}`
                ) || [];

            setSearchList(cityListWithCountryName);
            // 선택된 도시가 있을 때 결과를 업데이트
            const updatedResultList = cityListWithCountryName.filter(
                (city) => !selectedCity.includes(city)
            );
            setResultList(updatedResultList);
        }
    };

    // 여행할 도시 선택 또는 해제
    const handleCitySelect = (city: string) => {
        console.log(city);
        const index = selectedCity.indexOf(city);

        if (index === -1) {
            // 선택되지 않은 도시라면 추가
            setSelectedCity([...selectedCity, city]);
        } else {
            // 이미 선택된 도시라면 제거
            const updatedCities = selectedCity.filter(
                (selected) => selected !== city
            );
            setSelectedCity(updatedCities);
        }
    };

    return (
        <div className="flex flex-col items-center">
            <div className="text-2xl font-bold m-3">어디로 떠나시나요?</div>
            <div
                id="search-container"
                className="w-[90%] shadow-md border border-black rounded-lg"
            >
                <SearchBar onChange={handleDataFromChild} />
            </div>
            <div
                id="city-list-container"
                className="m-2 h-[40vh] overflow-scroll w-[90%]"
            >
                {selectedCity.length > 0 &&
                    selectedCity.map((res) => (
                        <div key={res} className="flex justify-between m-2">
                            <div className="text-lg">{res}</div>
                            <MyButton
                                type="small"
                                text="해제"
                                isSelected={true}
                                onClick={() => handleCitySelect(res)}
                            />
                        </div>
                    ))}
                {query !== '' && searchList.length === 0 ? (
                    <div className="text-lg text-center text-gray-500">
                        검색 결과가 없습니다.
                    </div>
                ) : (
                    resultList.map((res) => (
                        <div key={res} className="flex justify-between m-2">
                            <div className="text-lg">{res}</div>
                            <MyButton
                                type="small"
                                text="선택"
                                isSelected={false}
                                onClick={() => handleCitySelect(res)}
                            />
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}
