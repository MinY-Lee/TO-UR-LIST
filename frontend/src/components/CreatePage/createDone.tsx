import { useState, useEffect } from 'react';
import { TourCardInfo } from '../../types/types';
import TourCard from '../MainPage/TourCard';
import Spinner from '../../assets/svg/spinner';

interface PropType {
    tourCardInfo: TourCardInfo;
}

export default function CreateDone(props: PropType) {
    const [tourCardComponent, setTourCardComponent] = useState<JSX.Element | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true); // 로딩 상태 추가

    useEffect(() => {
        if (props) {
            setTourCardComponent(<TourCard className="color-bg-blue-3" tourInfo={props.tourCardInfo} />);
        }
        setIsLoading(false);
    }, [props]);

    return (
        <div className=" flex flex-col items-center">
            {isLoading ? (
                <div className="flex justify-center items-center h-[30vh]">
                    <Spinner />
                </div>
            ) : (
                <div className="w-full">
                    <div className="m-3">
                        <div className="text-2xl font-bold">여행이 생성됐어요 🎉</div>
                        <div className="text-lg">클릭해서 일정 및 체크리스트를 추가해보세요!</div>
                    </div>
                    <div id="card-container" className="w-full">
                        {tourCardComponent}
                    </div>
                    <div
                        className="underline text-lg mt-10"
                        onClick={() => {
                            window.location.href = `/`;
                        }}
                    >
                        메인으로 가기
                    </div>
                </div>
            )}
        </div>
    );
}
