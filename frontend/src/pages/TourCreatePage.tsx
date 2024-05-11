import { useState, useEffect } from 'react';

import MyButton from '../components/Buttons/myButton';
import HeaderBar from '../components/HeaderBar/HeaderBar';

import SetPlace from '../components/CreatePage/setPlace';
import SetDate from '../components/CreatePage/setDate';
import SetTitle from '../components/CreatePage/setTitle';
import CreateDone from '../components/CreatePage/createDone';
import { City, TourCardInfo } from '../types/types';
import TabBarMain from '../components/TabBar/TabBarMain';
import { createTour } from '../util/api/tour';
import { httpStatusCode } from '../util/api/http-status';

export default function TourCreatePage() {
    const [step, setStep] = useState<number>(1);
    const [isDone, setIsDone] = useState<boolean>(false);
    const [selectedCity, setSelectedCity] = useState<City[]>([]);
    const [startDate, setStartDate] = useState<Date>(new Date());
    const [endDate, setEndDate] = useState<Date>(new Date());
    const [title, setTitle] = useState<string>('');
    const [newTour, setNewTour] = useState<Object>({
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
    }); // api 보낼 객체
    const [tourCard, setTourCard] = useState<TourCardInfo>({
        tourId: '',
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
    }); // 카드 만들 객체

    // setPlace 로부터 데이터 받기
    const handleCityData = (data: City[]) => {
        setSelectedCity(data);
    };

    // setDate 로부터 데이터 받기
    const handleDateData = (data: Date[]) => {
        setStartDate(data[0]);
        setEndDate(data[1]);
    };

    // setTitle 로부터 데이터 받기
    const handleTitleData = (data: string) => {
        if (data.trim() != '') {
            setTitle(data);
        }
    };

    // TourInfoCard 객체화
    useEffect(() => {
        // 여행 생성 api 로 tourId 받아와야 함

        // City 객체를 담을 배열
        const cities: City[] = [];

        // cityList를 순회하면서 각 요소를 처리
        selectedCity.forEach((item) => {
            const city = { countryCode: item.countryCode, cityName: item.cityName };
            cities.push(city);
        });

        if (step == 2) {
            setTitle(`${selectedCity[0].cityName} 여행`);
        }
    }, [step, title]);

    useEffect(() => {
        //////////////////////////////////////////
        // api 호출 후 id 받아와서 넣고 카드 띄우기
        if (isDone) {
            createTour(newTour)
                .then((res) => {
                    if (res.status === httpStatusCode.OK) {
                        setTourCard({
                            tourId: res.data,
                            tourTitle: title,
                            cityList: selectedCity,
                            startDate: startDate.toISOString(),
                            endDate: endDate.toISOString(),
                        });
                        setStep(step + 1);
                    } else {
                        console.log(res);
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }, [isDone]);

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
            setIsDone(true);
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
            currentComponent = <CreateDone tourCardInfo={tourCard} />;
            break;
    }

    return (
        <section className="m-5 grid grid-rows-12 h-[95vh]">
            <header className="row-span-3">
                <HeaderBar />
                <h1 className="m-3 text-3xl font-bold">여행 만들기</h1>
            </header>
            <div className="gap-3 row-span-8 text-center">{currentComponent}</div>
            <div className="row-span-1">
                {step != 4 ? (
                    <MyButton
                        type="full"
                        className="text-white py-2"
                        text={step != 3 ? '선택완료' : '입력완료'}
                        isSelected={true}
                        onClick={handleStep}
                    />
                ) : (
                    <div />
                )}
            </div>
        </section>
    );
}
