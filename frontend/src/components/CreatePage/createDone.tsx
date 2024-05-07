import { useState, useEffect } from "react";
import { TourCardInfo } from '../../types/types';
import TourCard from "../MainPage/TourCard";

interface PropType {
  tourCardInfo: TourCardInfo;
}

export default function CreateDone(props: PropType) {
    const [tourCardComponent, setTourCardComponent] = useState<JSX.Element | null>(null);

    useEffect(() => {
        if (props) {
            setTourCardComponent(<TourCard tourInfo={props.tourCardInfo}/>);
        }
    }, []);

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
