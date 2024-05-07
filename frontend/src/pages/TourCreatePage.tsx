import { useState, useEffect } from 'react';

import MyButton from '../components/Buttons/myButton';
import HeaderBar from '../components/HeaderBar/HeaderBar';

import SetPlace from '../components/CreatePage/setPlace';
import SetDate from '../components/CreatePage/setDate';
import SetTitle from '../components/CreatePage/setTitle';
import CreateDone from '../components/CreatePage/createDone';
import { City, TourCardInfo } from '../types/types';

export default function TourCreatePage() {
    const [step, setStep] = useState<number>(1);
    const [selectedCity, setSelectedCity] = useState<string[]>([]);
    const [startDate, setStartDate] = useState<Date>(new Date());
    const [endDate, setEndDate] = useState<Date>(new Date());
    const [title, setTitle] = useState<string>('');
    const [newTour, setNewTour] = useState<TourCardInfo>({
        tourId: '0',
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
    }); // 정보들로 만들 객체

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
    const handleTitleData = (data: string) => {
        setTitle(data);
    };

    // TourInfoCard 객체화
    useEffect(() => {
        // 여행 생성 api 로 tourId 받아와야 함

        // City 객체를 담을 배열
        const cities: City[] = [];

        // cityList를 순회하면서 각 요소를 처리
        selectedCity.forEach((item) => {
            const [countryCode, cityName] = item.split(', ');
            const city = { countryCode: countryCode, cityName: cityName };
            cities.push(city);
        });

        setNewTour({
            tourId: '0', // 임시
            tourTitle: title,
            cityList: cities,
            startDate: startDate.toISOString(),
            endDate: endDate.toISOString(),
        });
    }, [step, title]);

    const handleStep = () => {
        if (step == 1 && selectedCity.length > 0) {
            console.log(selectedCity);
            setStep(step + 1);
        }
        if (step == 2 && startDate && endDate) {
            console.log(startDate);
            console.log(endDate);
            setStep(step + 1);
        }
        if (step == 3) {
            if (title == '') {
                console.log('비었음');
                setTitle(`${selectedCity} 여행`);
            }
            console.log(title);
            setStep(step + 1);
        }
    };

    // 스텝별로 다른 컴포넌트 렌더링
    let currentComponent;
    switch (step) {
        case 1:
            currentComponent = <SetPlace onChangeSelected={handleCityData} />;
            break;
        case 2:
            currentComponent = <SetDate onChangeDate={handleDateData} />;
            break;
        case 3:
            currentComponent = <SetTitle onChangeTitle={handleTitleData} />;
            break;
        case 4:
            currentComponent = <CreateDone tourCardInfo={newTour} />;
            break;
    }

    return (
        <section className="m-5 grid grid-rows-12 h-[95vh]">
            <header className="row-span-3">
                <HeaderBar />
                <h1 className="m-3 text-3xl font-bold">여행 만들기</h1>
            </header>
            <div className="gap-3 row-span-8 text-center">
                {currentComponent}
            </div>
            <div className="row-span-1">
                {step != 4 ? (
                    <MyButton
                        type="full"
                        text={step != 3 ? '선택완료' : '입력완료'}
                        isSelected={false}
                        onClick={handleStep}
                    />
                ) : (
                    <div />
                )}
            </div>
        </section>
    );
}
