//dummy data
import tourInfo from '../../dummy-data/get_tour_tourId.json';
import tourSchedule from '../../dummy-data/get_tour_place_tourId.json';

import TabBarTour from '../../components/TabBar/TabBarTour';
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import DayList from '../../components/SchedulePage/DayList';
import { TourPlaceItem } from '../../types/types';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import WebSocket from '../../components/TabBar/WebSocket';

export default function TourScheduleEditPage() {
    const [selectedDate, setSelectedDate] = useState<number>(-1);
    const [period, setPeriod] = useState<number>(0);

    //스케줄 저장 배열 schedule[i]는 i일째 일정(0은 일정 없음)
    const [schedule, setSchedule] = useState<TourPlaceItem[][]>([[]]);

    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    //state로 넘어온게 n이면 n+1일차만 띄우기
    //-1이면 전체 띄우기
    const location = useLocation();
    useEffect(() => {
        if (location.state && location.state.selectedDate) {
            setSelectedDate(location.state.selectedDate);
        }
    }, []);

    /**기간 계산 */
    useEffect(() => {
        const startDate = new Date(tourInfo.startDate);
        const endDate = new Date(tourInfo.endDate);

        const period = endDate.getDate() - startDate.getDate() + 1;
        setPeriod(period);

        const tempSchedule = new Array(period + 1);
        for (let i = 0; i < tempSchedule.length; i++) {
            tempSchedule[i] = [];
        }
        for (let i = 0; i < tourSchedule.length; i++) {
            const day = tourSchedule[i].tourDay;

            tempSchedule[day].push(tourSchedule[i]);
        }
        setSchedule(tempSchedule);
    }, []);

    /**일별 보여주기 */
    const dayInfo = () => {
        const date = new Date(tourInfo.startDate);
        date.setDate(date.getDate() + selectedDate);
        return (
            <DayList
                dayNumber={selectedDate + 1}
                date={date}
                dailySchedule={schedule[selectedDate + 1]}
                isEditable={true}
                tourId={tourId}
            />
        );
    };

    return (
        <>
            <section className="w-full h-full">
                <HeaderBar />
                <div className="w-full h-[80%] px-2vw relative overflow-y-auto flex flex-col">
                    <div className="w-full h-[10%] flex justify-between items-center">
                        <p className="text-7vw">일정 편집</p>
                        <div
                            className="w-[15%] h-6vw border-rad-3vw color-bg-blue-2 text-white flex justify-center items-center"
                            onClick={() => {
                                window.location.href = `/tour/${tourId}/schedule`;
                            }}
                        >
                            완료
                        </div>
                    </div>
                    <div className="w-full h-[90%] flex flex-col justify-between items-center">
                        {selectedDate === -1
                            ? schedule.map((dailySchedule, index) => {
                                  const date = new Date(tourInfo.startDate);
                                  date.setDate(date.getDate() + index - 1);
                                  return (
                                      <DayList
                                          key={date.getTime()}
                                          dayNumber={index}
                                          date={date}
                                          dailySchedule={dailySchedule}
                                          isEditable={true}
                                          tourId={tourId}
                                      />
                                  );
                              })
                            : dayInfo()}
                    </div>
                </div>
                <TabBarTour tourMode={2} tourId={tourId} />
                {/* <WebSocket /> */}
            </section>
        </>
    );
}
