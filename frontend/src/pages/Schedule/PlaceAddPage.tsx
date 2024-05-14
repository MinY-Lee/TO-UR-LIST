//dummyData
// import tourInfo from '../../dummy-data/get_tour_tourId.json';
// import tokyoTemporal from '../../dummy-data/tokyo_temporal.json';

import { useCallback, useEffect, useState } from 'react';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';
import { useLocation } from 'react-router-dom';
import { Wrapper } from '@googlemaps/react-wrapper';
import WebSocket from '../../components/TabBar/WebSocket';

import { PlaceInfo, TourInfoDetail } from '../../types/types';
import SearchMaps from '../../components/SchedulePage/SearchMaps';
import SearchSlideBar from '../../components/SchedulePage/SearchSlideBar';
import SearchBar from '../../components/SchedulePage/SearchBar';
import { searchPlace } from '../../util/api/place';
import { httpStatusCode } from '../../util/api/http-status';
import { getTour } from '../../util/api/tour';

export default function PlaceAddPage() {
    const [selectedDate, setSelectedDate] = useState<number>(-1);
    const [searchedPlaces, setSearchedPlaces] = useState<PlaceInfo[]>([]);
    const [period, setPeriod] = useState<number>(0);

    const [tourInfo, setTourInfo] = useState<TourInfoDetail>({
        tourTitle: '',
        startDate: '',
        endDate: '',
        memberList: [],
        cityList: [],
    });

    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    //useLocation
    //state 불러오기
    const location = useLocation();
    useEffect(() => {
        if (location.state) {
            //선택 날짜
            setSelectedDate(location.state.selectedDate);
            setPeriod(location.state.period);
        }

        getTour(tourId)
            .then((res) => {
                if (res.status === httpStatusCode.OK) {
                    setTourInfo(res.data);
                } else {
                    console.log('Error');
                }
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    useEffect(() => {
        if (tourInfo.cityList.length > 0) {
            //일단 api로 여행 정보 불러오기
            searchPlace(tourInfo.cityList[0].cityName + ' 관광')
                .then((res) => {
                    console.log(res);
                    setSearchedPlaces(res.data);
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }, [tourInfo.cityList]);

    const dateToString = useCallback(() => {
        const date = new Date(tourInfo.startDate);
        console.log(selectedDate);
        date.setDate(date.getDate() + selectedDate);

        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${year}.${month >= 10 ? month : '0' + month}.${
            day >= 10 ? day : '0' + day
        }`;
    }, [tourInfo.startDate]);

    const searchEvent = (
        e: React.KeyboardEvent<HTMLInputElement>,
        searchValue: string
    ) => {
        if (e.key === 'Enter') {
            searchPlace(searchValue)
                .then((res) => {
                    console.log(res);
                    if (res.status === httpStatusCode.OK) {
                        setSearchedPlaces(res.data);
                    } else {
                        console.log(res);
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    };

    return (
        <>
            <section className="w-full h-full overflow-y-hidden">
                <HeaderBar />
                <div className="w-full h-[80%] flex flex-col overflow-y-hidden">
                    {/* 현재 날짜 보여주기 */}
                    {selectedDate !== -1 ? (
                        <div className="w-full h-[5%] text-5vw px-2vw flex items-center">
                            Day {selectedDate + 1} | {dateToString()}
                        </div>
                    ) : (
                        <></>
                    )}
                    <SearchBar searchEvent={searchEvent} />
                    <Wrapper
                        apiKey={`${
                            import.meta.env.VITE_REACT_GOOGLE_MAPS_API_KEY
                        }`}
                        libraries={['marker', 'places']}
                    >
                        <SearchMaps searchedPlaces={searchedPlaces} />
                    </Wrapper>
                    <SearchSlideBar
                        searchedPlaces={searchedPlaces}
                        tourId={tourId}
                        selectedDate={selectedDate}
                        period={period}
                    />
                </div>
                <TabBarTour tourMode={2} tourId={tourId} />
                {/* <WebSocket /> */}
            </section>
        </>
    );
}
