import { useState, useEffect } from 'react';

import MyCalendar from "../../components/Calendar/myCalendar";

export default function SetDate(props) {
    const { onChangeDate } = props;

    const [startDate, setStartDate] = useState<Date>();
    const [endDate, setEndDate] = useState<Date>();

    useEffect(() => {
        // 부모 컴포넌트에 보내기
        onChangeDate([startDate, endDate]);

    }, [startDate, endDate]);

    // datepicker 로 부터 데이터 받기
    const handleDataFromChild = (data: Date[]) => {
        setStartDate(data[0]);
        setEndDate(data[1]);
    };

    return (
        <>
            <div className="text-2xl font-bold">언제 떠나시나요?</div>
            <div id='search-container' className="w-full">
                <MyCalendar onChange={handleDataFromChild}/>
            </div>
            
        </>
    );
}
