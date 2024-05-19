import { useEffect, useState } from "react";

interface PropType {
    closeModal: () => void;
    startDate: string;
    period: number;
    selectedDate: number;
    changeDate: (oldDate: number, newDate: number) => void;
}

export default function DayChangeModal(props: PropType) {
    // console.log(props.startDate.split('T')[0]);

    const [daySelectBox, setDaySelectBox] = useState<string[]>([]);

    useEffect(() => {
        const newSelect = ["(날짜 미정)"];

        const startDate = new Date(props.startDate);

        for (let i = 1; i <= props.period; i++) {
            const date = new Date(startDate.getTime() + (i - 1) * 86400000);

            const dateString = `${date.getFullYear()}.${
                date.getMonth() + 1 >= 10
                    ? date.getMonth() + 1
                    : "0" + (date.getMonth() + 1)
            }.${date.getDate() >= 10 ? date.getDate() : "0" + date.getDate()}`;

            newSelect.push(dateString);
        }

        setDaySelectBox(newSelect);
    }, [props.startDate, props.period]);

    return (
        <>
            <div className="w-full h-full absolute top-0 left-0 z-20 opacity-20 bg-black"></div>
            <div className="w-[80%] h-[60%] absolute left-[10%] top-[20%] z-30 bg-white color-border-blue-6 border-halfvw border-rad-2vw flex flex-col justify-around items-start p-2vw">
                <div className="pl-1.5 font-bold text-xl">방문 예정 날짜</div>
                <div className="w-full h-[80%] flex flex-col items-center text-7vw overflow-y-scroll">
                    {daySelectBox.map((days, index) => {
                        return props.selectedDate !== index ? (
                            <div
                                className="pl-2 w-full h-[14%] flex-shrink-0 color-text-blue-6 text-2xl color-border-blue-2 border-b flex items-center border-collapse"
                                onClick={() =>
                                    props.changeDate(props.selectedDate, index)
                                }
                            >
                                {days}
                            </div>
                        ) : (
                            <div
                                className="pl-2 w-full h-[14%] flex-shrink-0 text-white color-border-blue-2 text-2xl border-b color-bg-blue-6 flex items-center border-collapse"
                                onClick={props.closeModal}
                            >
                                {days}
                            </div>
                        );
                    })}
                </div>
                <div className="w-full h-[10%] flex justify-around items-center text-5vw">
                    <div
                        className="w-full h-full color-bg-blue-6 text-white border-rad-2vw flex justify-center items-center"
                        onClick={props.closeModal}
                    >
                        닫기
                    </div>
                </div>
            </div>
        </>
    );
}
