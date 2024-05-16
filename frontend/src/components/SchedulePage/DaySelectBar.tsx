interface PropType {
    startDate: string;
    endDate: string;
    selectedDate: number;
    setSelectedDate: React.Dispatch<React.SetStateAction<number>>;
    period: number;
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
                        if (i !== props.selectedDate) {
                            props.setSelectedDate(i);
                        } else {
                            props.setSelectedDate(-1);
                        }
                    }}
                >
                    <p
                        className={`flex w-[70%] aspect-square justify-center items-center flex-shrink-0 ${
                            props.selectedDate === i
                                ? "color-bg-blue-5 rounded-[50%]"
                                : ""
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
            <div className="absolute w-[90%] h-10vw left-[5%] top-[5%] border-halfvw border-rad-5vw color-border-blue-2 flex items-center px-5vw overflow-x-scroll bg-white">
                {dates()}
            </div>
        </>
    );
}
