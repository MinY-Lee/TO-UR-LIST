import { useState, useEffect } from 'react';

import MyCalendar from '../../components/Calendar/myCalendar';

interface PropType {
    onChangeDate: ([startDate, endDate]: Date[]) => void;
}

export default function SetDate(props: PropType) {
    const [startDate, setStartDate] = useState<Date>(new Date());
    const [endDate, setEndDate] = useState<Date>(new Date());

    useEffect(() => {
        // 부모 컴포넌트에 보내기
        props.onChangeDate([startDate, endDate]);
    }, [startDate, endDate]);

    // datepicker 로 부터 데이터 받기
    const handleDataFromChild = (data: Date[]) => {
        setStartDate(data[0]);
        setEndDate(data[1]);
    };

    return (
        <>
            <div className="text-2xl font-bold">언제 떠나시나요?</div>
            <div id="search-container" className="w-full">
                <MyCalendar isDatePicker={false} onChange={handleDataFromChild} />
            </div>
        </>
    );
}
