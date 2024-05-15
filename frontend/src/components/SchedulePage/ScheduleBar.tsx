import { useRef, useState } from 'react';
import { WebSockPlace } from '../../types/types';
import DayList from './DayList';
import { useNavigate } from 'react-router-dom';
import { Client } from '@stomp/stompjs';

interface PropType {
    schedule: WebSockPlace[][];
    startDate: string;
    selectedDate: number;
    tourId: string;
    period: number;
    wsClient: Client;
}

export default function ScheduleBar(props: PropType) {
    console.log(props.schedule);

    const navigate = useNavigate();

    const [mode, setMode] = useState<number>(1);

    const swipeRef = useRef<HTMLDivElement>(null);

    /**드래그 업, 스와이프 업 하면 적용 */
    const goUpMode = () => {
        if (mode == 1) {
            setMode(0);
        } else if (mode == 2) {
            setMode(1);
        }
    };

    /**드래그 다운, 스와이프 다운 하면 적용 */
    const goDownMode = () => {
        if (mode == 0) {
            setMode(1);
        } else if (mode == 1) {
            setMode(2);
        }
    };

    let dragStartPosY = 0;
    let touchStartPosY = 0;

    const dayInfo = () => {
        const date = new Date(props.startDate);
        date.setDate(date.getDate() + props.selectedDate);
        return (
            <DayList
                dayNumber={props.selectedDate + 1}
                date={date}
                dailySchedule={props.schedule[props.selectedDate + 1]}
                isEditable={false}
                tourId={props.tourId}
                wsClient={props.wsClient}
                period={props.period}
            />
        );
    };

    return (
        <>
            <div
                className={`w-full bottom-0 absolute border-rad-2vw border-halfvw border-[#D9D9D9] z-10 bg-white ${
                    mode === 0 ? 'h-full' : mode === 1 ? 'h-[50%]' : 'h-[20%]'
                }`}
                style={{ transitionDuration: '1s' }}
            >
                {/* 추가 편집 버튼 */}
                <div className="w-[30%] h-6vw absolute right-[5%] top-2vw flex justify-between items-center">
                    <div
                        className="w-[48%] h-full text-white color-bg-blue-2 border-rad-4vw flex justify-center items-center"
                        onClick={() => {
                            navigate(`/tour/${props.tourId}/schedule/add`, {
                                state: {
                                    selectedDate: props.selectedDate,
                                    period: props.period,
                                },
                            });
                        }}
                    >
                        추가
                    </div>
                    <div
                        className="w-[48%] h-full color-bg-blue-4 border-rad-4vw flex justify-center items-center"
                        onClick={() => {
                            navigate(`/tour/${props.tourId}/schedule/edit`, {
                                state: {
                                    selectedDate: props.selectedDate,
                                    period: props.period,
                                },
                            });
                        }}
                    >
                        편집
                    </div>
                </div>
                {/* 스크롤 */}
                <div
                    className="w-full h-10vw flex justify-center items-center"
                    draggable={true}
                    onDragStart={(e) => {
                        dragStartPosY = e.clientY;

                        if (swipeRef.current) {
                            swipeRef.current.classList.add('color-bg-blue-4');
                        }
                    }}
                    onDragEnd={(e) => {
                        if (swipeRef.current) {
                            swipeRef.current.classList.remove(
                                'color-bg-blue-4'
                            );
                        }

                        if (e.clientY < dragStartPosY) {
                            //드래그 업
                            goUpMode();
                        } else if (e.clientY > dragStartPosY) {
                            //드래그 다운
                            goDownMode();
                        }
                    }}
                    onTouchStart={(e) => {
                        touchStartPosY = e.touches[0].clientY;

                        if (swipeRef.current) {
                            swipeRef.current.classList.add('color-bg-blue-4');
                        }
                    }}
                    onTouchEnd={(e) => {
                        if (swipeRef.current) {
                            swipeRef.current.classList.remove(
                                'color-bg-blue-4'
                            );
                        }

                        if (e.changedTouches[0].clientY < touchStartPosY) {
                            //스와이프 업
                            goUpMode();
                        } else if (
                            e.changedTouches[0].clientY > touchStartPosY
                        ) {
                            //스와이프 다운
                            goDownMode();
                        }
                    }}
                    ref={swipeRef}
                >
                    <div className="w-[20%] h-vw bg-[#929292] border-rad-dot5vw"></div>
                </div>
                {/* 일정 정보 표시 */}
                <div className="w-full h-[85%] overflow-y-scroll flex flex-col">
                    {props.selectedDate === -1
                        ? props.schedule.map((dailySchedule, index) => {
                              const date = new Date(props.startDate);
                              date.setDate(date.getDate() + index - 1);
                              return (
                                  <DayList
                                      key={
                                          dailySchedule.length +
                                          ' ' +
                                          date.getTime()
                                      }
                                      dayNumber={index}
                                      date={date}
                                      dailySchedule={dailySchedule}
                                      isEditable={false}
                                      tourId={props.tourId}
                                      wsClient={props.wsClient}
                                      period={props.period}
                                  />
                              );
                          })
                        : dayInfo()}
                </div>
            </div>
        </>
    );
}
