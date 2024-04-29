import { useState, useEffect } from 'react';

import MyCalendar from "../../components/Calendar/myCalendar";

export default function SetDate(props) {
    const { onChangeDate } = props;

    const [startDate, setStartDate] = useState<Date>();

    useEffect(() => {
        // 선택된 도시가 변경될 때마다 resultList 업데이트
        const updatedResultList = searchList.filter(city => !selectedCity.includes(city));
        setResultList(updatedResultList);
        
        // 부모 컴포넌트에 보내기
        onChangeDate
        // onChangeSelected(selectedCity);

    }, [selectedCity]);

    // datepicker 로 부터 데이터 받아서 검색 수행
    const handleDataFromChild = (data: string) => {
        setQuery(data);
        // 나라 -> 도시 로직인 경우 검색어를 나라 코드로 치환
        const foundCountry = CountryList.find(country => country.countryName === data);

        if (foundCountry) {
            // 코드로 도시 검색 및 결과 포맷팅
            const cityListWithCountryName: string[] = CityList
                .find(country => country.countryCode === foundCountry.countryCode)?.cityList
                ?.map(city => `${foundCountry.countryName}, ${city}`) || [];

            setSearchList(cityListWithCountryName);
            // 선택된 도시가 있을 때 결과를 업데이트
            const updatedResultList = cityListWithCountryName.filter(city => !selectedCity.includes(city));
            setResultList(updatedResultList);
        }
    };
    
    // 여행할 도시 선택 또는 해제
    const handleCitySelect = (city: string) => {
        const index = selectedCity.indexOf(city);

        if (index === -1) {
            // 선택되지 않은 도시라면 추가
            setSelectedCity([...selectedCity, city]);
        } else {
            // 이미 선택된 도시라면 제거
            const updatedCities = selectedCity.filter(selected => selected !== city);
            setSelectedCity(updatedCities);
        }
    };

    return (
        <>
            <div className="text-2xl font-bold">언제 떠나시나요?</div>
            <div id='search-container' className="w-full">
                <MyCalendar onChange={handleDataFromChild}/>
            </div>
            
        </>
    );
}
