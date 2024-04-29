import { useState } from 'react';

import MyButton from "../components/Buttons/myButton";
import HeaderBar from "../components/HeaderBar/HeaderBar";

import SetPlace from '../components/CreatePage/setPlace';
import SetDate from '../components/CreatePage/setDate';
import SetTitle from '../components/CreatePage/setTitle';

interface ParentProps {
    onChange: (data: string[]) => void;
}

export default function TourCreatePage(props: ParentProps) {
    const [step, setStep] = useState<number>(1);
    const [selectedCity, setSelectedCity] = useState<string[]>([]);
    const [startDate, setStartDate] = useState<Date>();
    const [endDate, setEndDate] = useState<Date>();
    const [title, setTitle] = useState<String>("");

    // setPlace 로부터 데이터 받기
    const handleCityData = (data: string[]) => {
        setSelectedCity(data);
    };

    // setDate 로부터 데이터 받기
    const handleDateData = (data: Date[]) => {
        setStartDate(data[0]);
        setEndDate(data[1]);
    };

    // setTitle 로부터 데이터 받기
    const handleTitleData = (data: String) => {
        setTitle(data);
    };


    const handleStep = () => {
        if (step == 1 && selectedCity.length > 0) {
            console.log(selectedCity);
            setStep(step+1);
        }
        if (step == 2 && startDate && endDate) {
            console.log(startDate);
            console.log(endDate);
            setStep(step+1);
        }
        if (step == 3) {
            console.log(title);
            setStep(step+1);
        }
    }

    // 스텝별로 다른 컴포넌트 렌더링
    let currentComponent;
    switch (step) {
        case 1:
            currentComponent = <SetPlace onChangeQuery={""} onChangeSelected={handleCityData} />;
            break;
        case 2:
            currentComponent = <SetDate onChangeDate={handleDateData}/>;
            break;
        case 3:
            currentComponent =  <SetTitle onChangeTitle={handleTitleData}/>;
            break;
        case 4:
            currentComponent = <h1>4단계</h1>;
            break;
        default:
            currentComponent = "";
            break;
    }

    return (
        <section className="m-5 flex flex-col justify-between h-[95vh]">
            <header className="">
                <HeaderBar/>
                <h1 className="m-3 text-3xl font-bold">
                    여행 만들기
                </h1>
            </header>
            <div className="m-5 gap-3 flex flex-col justify-center items-center">
                {currentComponent}
            </div>
            <MyButton type="full" text="선택완료" isSelected={false} onClick={handleStep}/>
        </section>
    );
}
