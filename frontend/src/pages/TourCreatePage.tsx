import { useState, useEffect } from "react";

import MyButton from "../components/Buttons/myButton";
import HeaderBar from "../components/HeaderBar/HeaderBar";

import SetPlace from "../components/CreatePage/setPlace";
import SetDate from "../components/CreatePage/setDate";
import SetTitle from "../components/CreatePage/setTitle";
import CreateDone from "../components/CreatePage/createDone";
import { City, TourCardInfo } from "../types/types";
import { createTour } from "../util/api/tour";
import { httpStatusCode } from "../util/api/http-status";

export default function TourCreatePage() {
    const [step, setStep] = useState<number>(1);
    const [isDone, setIsDone] = useState<boolean>(false);
    const [selectedCity, setSelectedCity] = useState<City[]>([]);
    const [startDate, setStartDate] = useState<Date>(new Date());
    const [endDate, setEndDate] = useState<Date>(new Date());
    const [isValidDate, setIsVaildDate] = useState<boolean>(false);
    const [title, setTitle] = useState<string>("");

    const [tourCard, setTourCard] = useState<TourCardInfo>({
        tourId: "",
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
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

    // date 오늘 이후인지 체크
    const checkValue = (flag: boolean) => {
        setIsVaildDate(flag);
    };

    // setTitle 로부터 데이터 받기
    const handleTitleData = (data: string) => {
        if (data.trim() != "") {
            setTitle(data);
        }
    };

    useEffect(() => {
        // City 객체를 담을 배열
        const cities: City[] = [];

        // cityList를 순회하면서 각 요소를 처리
        selectedCity.forEach((item) => {
            const city = {
                countryCode: item.countryCode,
                cityName: item.cityName,
            };
            cities.push(city);
        });

        if (step == 2) {
            setTitle(`${selectedCity[0].cityName} 여행`);
        }
    }, [step, title]);

    useEffect(() => {
        if (isDone) {
            createTour({
                tourTitle: title,
                cityList: selectedCity,
                startDate: startDate,
                endDate: endDate,
            })
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
                        // console.log(res);
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }, [isDone]);

    const handleStep = () => {
        if (step == 1 && selectedCity.length > 0) {
            setStep(step + 1);
        }
        if (step == 2 && startDate && endDate && isValidDate) {
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
            currentComponent = (
                <SetDate
                    onChangeDate={handleDateData}
                    checkValue={checkValue}
                />
            );
            break;
        case 3:
            currentComponent = <SetTitle onChangeTitle={handleTitleData} />;
            break;
        case 4:
            currentComponent = <CreateDone tourCardInfo={tourCard} />;
            break;
    }

    return (
        <section className="grid grid-rows-8 h-[95vh]">
            <header className="row-span-2">
                <HeaderBar />
                <h1 className="m-3 text-3xl font-bold">여행 만들기</h1>
            </header>

            <div className="mx-5 gap-3 row-span-8 text-center">
                {currentComponent}
            </div>
            <div className="m-5 row-span-1">
                {step != 4 ? (
                    <MyButton
                        type="full"
                        className="text-white py-2"
                        text={step != 3 ? "선택완료" : "입력완료"}
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
