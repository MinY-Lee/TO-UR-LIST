import { useRef, useState } from "react";
import { WebSockPlace } from "../../types/types";
import DayList from "./DayList";
import { useNavigate } from "react-router-dom";
import { Client } from "@stomp/stompjs";

interface PropType {
    schedule: WebSockPlace[][];
    startDate: string;
    selectedDate: number;
    tourId: string;
    period: number;
    wsClient: Client;
    setIsLoading: React.Dispatch<React.SetStateAction<boolean>>;
    setSelectedSchedule: React.Dispatch<
        React.SetStateAction<WebSockPlace | undefined>
    >;
    selectedSchedule: WebSockPlace | undefined;
}

export default function ScheduleBar(props: PropType) {
    // console.log(props.schedule);

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
                setIsLoading={props.setIsLoading}
                setSelectedSchedule={props.setSelectedSchedule}
                selectedSchedule={props.selectedSchedule}
            />
        );
    };

    return (
        <>
            <div
                className={`w-full bottom-0 absolute border-rad-2vw border-halfvw border-[#D9D9D9] z-10 bg-white ${
                    mode === 0 ? "h-full" : mode === 1 ? "h-[50%]" : "h-[20%]"
                }`}
                style={{ transitionDuration: "1s" }}
            >
                {/* 제목 */}
                <div className="w-[30%] h-6vw text-5vw weight-text-semibold absolute left-[5%] top-5vw flex justify-between items-center">
                    일정
                </div>
                {/* 추가 편집 버튼 */}
                <div className="w-[30%] h-6vw absolute right-[5%] top-5vw flex justify-between items-center">
                    <div
                        className="w-[48%] h-full text-[#232323] bg-[#e7f4ff] border-rad-2dot5vw flex justify-center items-center"
                        style={{ boxShadow: "0px 2px 4px 1px #cecece" }}
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
                    <div
                        className="w-[48%] h-full text-white color-bg-blue-6 border-rad-2dot5vw flex justify-center items-center"
                        style={{ boxShadow: "0px 2px 4px 1px #cecece" }}
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
                </div>
                {/* 스크롤 */}
                <div
                    className="w-full h-12vw flex justify-center items-start"
                    draggable={true}
                    onDragStart={(e) => {
                        dragStartPosY = e.clientY;

                        if (swipeRef.current) {
                            swipeRef.current.classList.add("color-bg-blue-4");
                        }
                    }}
                    onDragEnd={(e) => {
                        if (swipeRef.current) {
                            swipeRef.current.classList.remove(
                                "color-bg-blue-4"
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
                            swipeRef.current.classList.add("color-bg-blue-4");
                        }
                    }}
                    onTouchEnd={(e) => {
                        if (swipeRef.current) {
                            swipeRef.current.classList.remove(
                                "color-bg-blue-4"
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
                    <div className="w-[20%] h-dot5vw bg-[#929292] mt-3vw border-rad-dot5vw"></div>
                </div>
                {/* 일정 정보 표시 */}
                <div className="w-full h-[85%] overflow-y-scroll flex flex-col">
                    {props.selectedDate === -1 ? (
                        <>
                            {props.schedule.map((dailySchedule, index) => {
                                if (index === 0) return <div key={0}></div>;

                                const date = new Date(props.startDate);
                                date.setDate(date.getDate() + index - 1);
                                return (
                                    <DayList
                                        key={
                                            dailySchedule.length +
                                            " " +
                                            date.getTime()
                                        }
                                        dayNumber={index}
                                        date={date}
                                        dailySchedule={dailySchedule}
                                        isEditable={false}
                                        tourId={props.tourId}
                                        wsClient={props.wsClient}
                                        period={props.period}
                                        setIsLoading={props.setIsLoading}
                                        setSelectedSchedule={
                                            props.setSelectedSchedule
                                        }
                                        selectedSchedule={
                                            props.selectedSchedule
                                        }
                                    />
                                );
                            })}
                            {/* 미선택 */}
                            <DayList
                                key={
                                    props.schedule[0].length +
                                    " " +
                                    new Date(
                                        new Date(props.startDate).getDate() - 1
                                    ).getTime()
                                }
                                dayNumber={0}
                                date={
                                    new Date(
                                        new Date(props.startDate).getDate() - 1
                                    )
                                }
                                dailySchedule={props.schedule[0]}
                                isEditable={false}
                                tourId={props.tourId}
                                wsClient={props.wsClient}
                                period={props.period}
                                setIsLoading={props.setIsLoading}
                                setSelectedSchedule={props.setSelectedSchedule}
                                selectedSchedule={props.selectedSchedule}
                            />
                        </>
                    ) : (
                        dayInfo()
                    )}
                </div>
            </div>
        </>
    );
}
