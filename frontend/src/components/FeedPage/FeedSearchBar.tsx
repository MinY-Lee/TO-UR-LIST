import { useRef, useState } from 'react';
import { City, Filter } from '../../types/types';
import { GetCityList, GetCountryList } from '../../util/api/country';
import SelectCity from './SelectCity';

interface PropType {
    isDetailActive: boolean;
}

export default function FeedSearchBar(props: PropType) {
    const inputRef = useRef<HTMLInputElement>(null);
    const countryRef = useRef<HTMLInputElement>(null);

    const searchEvent = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.keyCode === 13 && inputRef.current) {
            console.log(inputRef.current.value);
        }
    };

    const [searchCity, setSearchCity] = useState<City>({
        countryCode: '',
        cityName: '',
    });

    const [countryCode, setCountryCode] = useState<string>('');

    const [filterList, setFilterList] = useState<Filter[]>([]);
    const [startDate, setStartDate] = useState<Date>(new Date(2024, 4, 12)); //2024-05-12 초기화(서비스 추가일)
    const [endDate, setEndDate] = useState<Date>(new Date());

    const [cityList, setCityList] = useState<string[]>([]);

    const countrySearch = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.keyCode === 13 && countryRef.current) {
            console.log(countryRef.current.value);
            GetCountryList().then((res) => {
                const found = res.data.find(
                    (result: any) =>
                        result.countryName === countryRef.current?.value
                );

                if (found) {
                    setCountryCode(found.countryCode);
                    GetCityList(found.countryCode).then((res) => {
                        setCityList(res.data);
                    });
                }
            });
        }
    };

    const select = (cityName: string) => {
        setSearchCity({
            countryCode: countryCode,
            cityName: cityName,
        });
    };

    return (
        <>
            <input
                type="search"
                className="w-[90%] h-15vw text-7vw mt-2vw p-2vw border-halfvw border-[#929292] border-rad-5vw"
                placeholder="원하시는 여행을 검색해 보세요!"
                onKeyDown={searchEvent}
                ref={inputRef}
            ></input>
            {props.isDetailActive ? (
                <div className="w-[90%] h-75vw border-halfvw color-border-blue-2 border-rad-5vw flex flex-col justify-evenly items-center">
                    <div className="w-[90%] h-10vw flex text-4vw justify-around items-center relative">
                        <div className="w-[20%] h-full flex justify-center items-center">
                            나라/도시
                        </div>
                        <input
                            type="search"
                            className="w-[35%] h-full px-vw border-halfvw color-border-blue-2 border-rad-2vw"
                            placeholder="나라명을 입력하세요."
                            onKeyDown={countrySearch}
                            ref={countryRef}
                        ></input>
                        <SelectCity cityList={cityList} select={select} />
                    </div>
                </div>
            ) : (
                <></>
            )}
        </>
    );
}
