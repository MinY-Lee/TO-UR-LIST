import { WebSockPlace } from "../../types/types";

interface PropType {
    startDate: string;
    endDate: string;
    selectedDate: number;
    setSelectedDate: React.Dispatch<React.SetStateAction<number>>;
    period: number;
    setSelectedSchedule: React.Dispatch<React.SetStateAction<WebSockPlace | undefined>>;
}

export default function DaySelectBar(props: PropType) {
    /** dates()
     *  날짜 띄워주기
     * @returns
     */
    const dates = () => {
        const arr = [];
        const startDate = new Date(props.startDate);
        const newDate = new Date(props.startDate);
        for (let i = 0; i < props.period; i++) {
            newDate.setDate(startDate.getDate() + i);
            arr.push(
                <div
                    key={i}
                    className={`text-5vw w-[14.29%] flex justify-center items-center flex-shrink-0`}
                    onClick={() => {
                        //선택된 일정 초기화
                        props.setSelectedSchedule(undefined);
                        if (i !== props.selectedDate) {
                            props.setSelectedDate(i);
                        } else {
                            props.setSelectedDate(-1);
                        }
                    }}
                >
                    <p
                        className={`flex w-[70%] aspect-square justify-center items-center flex-shrink-0 ${
                            props.selectedDate === i ? "bg-[#c1edff] rounded-[50%]" : ""
                        }`}
                    >
                        {newDate.getDate()}
                    </p>
                </div>
            );
        }
        return arr;
    };

    return (
        <>
            <div
                className="w-[90%] h-10vw my-2vw flex items-center px-5vw overflow-x-auto bg-white border-rad-2dot5vw"
                style={{ boxShadow: "0px 2px 4px 1px #cecece" }}
            >
                {dates()}
            </div>
        </>
    );
}
