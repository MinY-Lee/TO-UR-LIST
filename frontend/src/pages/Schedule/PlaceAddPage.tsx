import { useEffect, useState } from 'react';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';
import { useLocation } from 'react-router-dom';

//dummyData
import tourInfo from '../../dummy-data/get_tour_tourId.json';

export default function PlaceAddPage() {
    const [selectedDate, setSelectedDate] = useState<number>(-1);

    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    //useLocation
    //state 불러오기
    const location = useLocation();
    useEffect(() => {
        if (location.state) {
            //선택 날짜
            if (location.state.selectedDate) {
                setSelectedDate(location.state.selectedDate);
            }
        }
    }, []);

    const dateToString = () => {
        const date = new Date(tourInfo.startDate);
        date.setDate(date.getDate() + selectedDate);

        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${year}.${month >= 10 ? month : '0' + month}.${
            day >= 10 ? day : '0' + day
        }`;
    };

    return (
        <>
            <section className="w-full h-full">
                <HeaderBar />
                <div className="w-full h-[80%] flex flex-col">
                    {/* 현재 날짜 보여주기 */}
                    {selectedDate !== -1 ? (
                        <div className="w-full h-[5%] text-[5vw] px-[2vw] flex items-center">
                            Day {selectedDate + 1} | {dateToString()}
                        </div>
                    ) : (
                        <></>
                    )}
                </div>
                <TabBarTour tourMode={2} tourId={tourId} />
            </section>
        </>
    );
}
