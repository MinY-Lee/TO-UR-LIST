import { useState, useEffect } from 'react';
import { subMonths } from 'date-fns';
import getCalendar from './getCalendar';

interface ChildProps {
    onChange: (data: Date[]) => void;
}

export default function myCalendar(props: ChildProps) {
    const { weekCalendarList, currentDate, setCurrentDate, weekDayList } = getCalendar();
    
    const [startDate, setStartDate] = useState<Date | undefined>();
    const [endDate, setEndDate] = useState<Date | undefined>();
    
    useEffect(() => {
      if (startDate && endDate){
        props.onChange([startDate, endDate]);
      }
    }, [startDate, endDate]);

    // 펴고 접는 버전 만들 때 쓸 예정
    // const [select, setSelect] = useState<number[]>([]);

    // const [show, setShow] = useState<boolean>(false);

    // const handleChange = (selectedDate: Date) => {
    // 	console.log(selectedDate)
    // }
    // const handleClose = (state: boolean) => {
    // 	setShow(state)
    // }

    const handleDateChange = (value: Date) => {
        // 이미 있는 경우 선택 해제
        if (endDate?.getTime() === value.getTime()) {
            setEndDate(undefined);
            return;
        }

        if (startDate?.getTime() === value.getTime()) {
            setStartDate(endDate);
            setEndDate(undefined);
            return;
        }

        if (startDate && value < startDate) {
            setEndDate(startDate);
            setStartDate(value);
        } else {
            setEndDate(value);
        }
    };

    const handleEndDateChange = (value: Date) => {
        // end 만 없는 경우
        if (startDate?.getTime() === value.getTime()) {
            setStartDate(undefined);
        }

        if (startDate && value < startDate) {
            // 종료일이 시작일보다 이전인 경우, 종료일을 시작일로 설정
            setStartDate(value);
        } else if (startDate?.getTime() === value.getTime()) {
            // 겹치는 경우는 그냥 시작일로 흡수
            setEndDate(undefined);
        } else {
            setEndDate(value);
        }
    };

    return (
        <div
            id="calendar-container"
            className="flex flex-col items-center justify-center w-full mb-10"
        >
            <div
                id="move-container"
                className="flex justify-between w-[70%] m-5"
            >
                <button
                    onClick={() => {
                        setCurrentDate(subMonths(currentDate, 1));
                    }}
                >
                    &lt;
                </button>
                <div className="text-lg">
                    {currentDate.toLocaleDateString('ko-KR', {
                        year: 'numeric',
                        month: 'long',
                    })}
                </div>
                <button
                    onClick={() => {
                        setCurrentDate(subMonths(currentDate, -1));
                    }}
                >
                    &gt;
                </button>
            </div>
            <div id="weekday-container" className="grid grid-cols-7 w-full">
                {weekDayList.map((weekday, index) => (
                    <div key={index} className="col-span-1 text-center">
                        {weekday}
                    </div>
                ))}
            </div>
            <div id="day-container" className="w-full">
                {weekCalendarList.map((item) => (
                    <div
                        className="grid grid-cols-7 w-full h-[14vw] text-center"
                        key={Math.random()}
                    >
                        {item.map((day, index) => {
                            const selectedDate = new Date(
                                currentDate.getFullYear(),
                                currentDate.getMonth(),
                                day
                            );
                            const isBetween =
                                startDate &&
                                endDate &&
                                selectedDate > startDate &&
                                selectedDate < endDate;
                            const isStartOrEnd =
                                (startDate?.getDate() === day &&
                                    startDate.getMonth() ===
                                        currentDate.getMonth()) ||
                                (endDate &&
                                    endDate.getDate() === day &&
                                    endDate.getMonth() ===
                                        currentDate.getMonth());
                            return (
                                <button
                                    onClick={() => {
                                        const selectedDate = new Date(
                                            currentDate.getFullYear(),
                                            currentDate.getMonth(),
                                            day
                                        );

                                        if (startDate && endDate) {
                                            handleDateChange(selectedDate);
                                        } else if (startDate && !endDate) {
                                            handleEndDateChange(selectedDate);
                                        } else if (!startDate && !endDate) {
                                            setStartDate(selectedDate);
                                        }
                                    }}
                                    className={`
                                        col-span-1 h-[14vw]
                                        ${day === 0 ? ' invisible ' : ''}
                                        ${
                                            isBetween || isStartOrEnd
                                                ? 'color-bg-blue-4'
                                                : ''
                                        }
                                        ${
                                            selectedDate.getTime() ===
                                            startDate?.getTime()
                                                ? 'rounded-tl-full rounded-bl-full'
                                                : ''
                                        }
                                        ${
                                            selectedDate.getTime() ===
                                            endDate?.getTime()
                                                ? 'rounded-tr-full rounded-br-full'
                                                : ''
                                        }
                                    `}
                                    key={index}
                                >
                                    <div
                                        className={`
                                        ${index === 0 ? 'text-red-500' : ''}
                                        ${
                                            currentDate.getMonth() ===
                                                new Date().getMonth() &&
                                            day === new Date().getDate()
                                                ? 'color-text-blue-1 font-bold'
                                                : ''
                                        }
                                    
                                    `}
                                    >
                                        <div>{day}</div>
                                    </div>
                                </button>
                            );
                        })}
                    </div>
                ))}
            </div>
            {/* <div>시작일 : {startDate?.toLocaleDateString()}</div>
            <div>종료일 : {endDate?.toLocaleDateString()}</div> */}
        </div>
    );
}
