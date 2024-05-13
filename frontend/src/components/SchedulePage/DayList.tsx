import { useNavigate } from 'react-router-dom';
import { TourPlaceItem } from '../../types/types';
import PlaceCard from './PlaceCard';

interface PropType {
    dayNumber: number;
    date: Date;
    dailySchedule: TourPlaceItem[];
    isEditable: boolean;
    tourId: string;
}

export default function DayList(props: PropType) {
    const dateToString = (date: Date) => {
        return `${date.getFullYear()}.${
            date.getMonth() + 1 >= 10
                ? date.getMonth() + 1
                : '0' + (date.getMonth() + 1)
        }.${date.getDate() >= 10 ? date.getDate() : '0' + date.getDate()}`;
    };

    const navigate = useNavigate();

    const goToDetail = (schedule: TourPlaceItem) => {
        if (props.isEditable) {
            navigate(`/tour/${props.tourId}/schedule/edit/detail`, {
                state: {
                    scheduleInfo: schedule,
                },
            });
        }
    };

    return (
        <>
            <div className="w-full flex px-[5%] my-[1vw]">
                {/* 날짜 있냐 없냐에 따라 조건문 */}
                <div className="w-[25%] flex justify-between">
                    {props.dayNumber === 0 ? (
                        <>
                            <div className="w-[90%] flex flex-col text-[#828282] items-center">
                                <p className="text-6vw">날짜 없음</p>
                            </div>
                            <div className="w-[0.5vw] h-full bg-[#828282]"></div>
                        </>
                    ) : (
                        <>
                            <div className="w-[90%] flex flex-col text-[#828282] items-center">
                                <p className="text-7vw">
                                    Day {props.dayNumber}
                                </p>
                                <p className="text-4vw">
                                    {dateToString(props.date)}
                                </p>
                            </div>
                            <div className="w-[0.5vw] h-full bg-[#828282]"></div>
                        </>
                    )}
                </div>
                <div className="w-[75%] p-[1vw] flex flex-col items-center">
                    {props.dailySchedule.length === 0 ? (
                        <p className="text-6vw">일정 없음</p>
                    ) : (
                        props.dailySchedule.map((schedule) => {
                            return (
                                <PlaceCard
                                    key={
                                        schedule.tourDay +
                                        ' ' +
                                        schedule.placeId
                                    }
                                    schedule={schedule}
                                    isEditable={props.isEditable}
                                    tourId={props.tourId}
                                    goToDetail={goToDetail}
                                />
                            );
                        })
                    )}
                </div>
            </div>
        </>
    );
}
