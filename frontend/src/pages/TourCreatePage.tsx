import { useState } from 'react';

import MyButton from "../components/Buttons/myButton";
import HeaderBar from "../components/HeaderBar/HeaderBar";

import SetPlace from '../components/CreatePage/setPlace';
import SetDate from '../components/CreatePage/setDate';
import SetTitle from '../components/CreatePage/setTitle';
import CreateDone from '../components/CreatePage/createDone';
import { City } from '../types/types';

interface ParentProps {
    onChange: (data: string[]) => void;
}

export default function TourCreatePage(props: ParentProps) {
    const [step, setStep] = useState<number>(1);
    const [selectedCity, setSelectedCity] = useState<string[]>([]);
    const [startDate, setStartDate] = useState<Date>();
    const [endDate, setEndDate] = useState<Date>();
    const [title, setTitle] = useState<string>("");

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
            if (title == "") {
                console.log("비었음")
                setTitle(`${selectedCity} 여행`);
            }
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
            currentComponent = <CreateDone cityList={selectedCity} startDate={startDate} endDate={endDate} title={title}  />;
            break;
    }

    return (
        <section className="m-5 grid grid-rows-12 h-[95vh]">
            <header className="row-span-3">
                <HeaderBar/>
                <h1 className="m-3 text-3xl font-bold">
                    여행 만들기
                </h1>
            </header>
            <div className="gap-3 row-span-8 text-center">
                {currentComponent}
            </div>
            <div className='row-span-1'>
                {step != 4 ? 
                    <MyButton 
                        type="full" 
                        text={step != 3 ? "선택완료" : "입력완료"} 
                        isSelected={false} 
                        onClick={handleStep}
                    /> :
                    <div/>
                }
            </div>
        </section>
    );
}
