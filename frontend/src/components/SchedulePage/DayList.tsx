import { useNavigate } from "react-router-dom";
import { WebSockPlace } from "../../types/types";
import PlaceCard from "./PlaceCard";
import { Client } from "@stomp/stompjs";

interface PropType {
    dayNumber: number;
    date: Date;
    dailySchedule: WebSockPlace[];
    isEditable: boolean;
    tourId: string;
    wsClient: Client;
    period: number;
    setIsLoading: React.Dispatch<React.SetStateAction<boolean>>;
    setSelectedSchedule: React.Dispatch<
        React.SetStateAction<WebSockPlace | undefined>
    >;
    selectedSchedule: WebSockPlace | undefined;
}

export default function DayList(props: PropType) {
    const WEEKDAY = ["일", "월", "화", "수", "목", "금", "토"];

    const dateToString = (date: Date) => {
        return `${
            date.getMonth() + 1 >= 10
                ? date.getMonth() + 1
                : "0" + (date.getMonth() + 1)
        }.${date.getDate() >= 10 ? date.getDate() : "0" + date.getDate()} (${
            WEEKDAY[date.getDay()]
        })`;
    };

    const navigate = useNavigate();

    const goToDetail = (schedule: WebSockPlace) => {
        if (props.isEditable) {
            navigate(`/tour/${props.tourId}/schedule/edit/detail`, {
                state: {
                    tourId: props.tourId,
                    tourDay: schedule.tourDay - 1,
                    placeId: schedule.placeId,
                    period: props.period,
                },
            });
        } else {
            if (schedule !== props.selectedSchedule) {
                props.setSelectedSchedule(schedule);
            }
        }
    };

    return (
        <>
            <div className="w-full flex flex-col px-[5%] my-vw">
                {/* 날짜 있냐 없냐에 따라 조건문 */}
                <div className="w-full flex justify-start items-end text-5vw">
                    {props.dayNumber === 0 ? (
                        <>
                            <div className="w-dot5vw h-full bg-[#828282] mr-vw"></div>
                            <p className="text-[#828282]">날짜 미 선택</p>
                        </>
                    ) : (
                        <>
                            <p className="mr-vw">Day {props.dayNumber}</p>
                            <div className="w-dot3vw h-6vw bg-[#828282] mr-vw text-4vw"></div>
                            <p className="text-4vw text-[#828282]">
                                {dateToString(props.date)}
                            </p>
                        </>
                    )}
                </div>
                <div className="w-full p-vw flex flex-col items-center">
                    {props.dailySchedule.length === 0 ? (
                        <></>
                    ) : (
                        <>
                            {props.dailySchedule.map((schedule) => {
                                return (
                                    <PlaceCard
                                        key={
                                            schedule.tourDay +
                                            " " +
                                            schedule.placeId
                                        }
                                        schedule={schedule}
                                        isEditable={props.isEditable}
                                        tourId={props.tourId}
                                        goToDetail={goToDetail}
                                        wsClient={props.wsClient}
                                        setIsLoading={props.setIsLoading}
                                    />
                                );
                            })}
                        </>
                    )}
                </div>
            </div>
        </>
    );
}
