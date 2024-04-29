import { useState, useEffect } from "react";
import { TourCardInfo } from '../../types/types';
import TourCard from "../MainPage/TourCard";

import { City } from "../../types/types";


export default function CreateDone(props) {
    const {cityList, startDate, endDate, title} = props; // 받은 정보들
    const [newTour, setNewTour] = useState<TourCardInfo>(); // 정보들로 만들 객체
    const [tourCardComponent, setTourCardComponent] = useState<JSX.Element | null>(null);

    useEffect(() => {
        // 여행 생성 api 로 tourId 받아와야 함

        // City 객체를 담을 배열
        const cities: City[] = [];

        // cityList를 순회하면서 각 요소를 처리
        cityList.forEach(item => {
            const [countryCode, cityName] = item.split(', ');
            const city = { countryCode: countryCode, cityName: cityName };
            cities.push(city);
        });

        console.log(cities)

        setNewTour({
            tourId: "0", // 임시
            tourTitle: title,
            cityList: cities,
            startDate: startDate.toISOString(),
            endDate: endDate.toISOString(),
        })

        console.log(newTour);

    }, []); 

    useEffect(() => {
        console.log(newTour);
        setTourCardComponent(<TourCard tourInfo={newTour}/>);
    }, [newTour]);

  return (
    <div className=" flex flex-col items-center">
      <div className="m-3">
        <div className="text-2xl font-bold">여행이 생성됐어요 🎉</div>
        <div className="text-lg">클릭해서 일정 및 체크리스트를 추가해보세요!</div>
      </div>
      <div id="card-container" className="w-full">
        {tourCardComponent}
      </div>
    </div>
  );
}
