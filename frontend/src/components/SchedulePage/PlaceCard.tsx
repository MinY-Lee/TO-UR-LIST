import { BaseSyntheticEvent, useState } from 'react';
import { TourPlaceItem } from '../../types/types';
import CheckModal from '../CheckModal';

interface PropType {
    schedule: TourPlaceItem;
    isEditable: boolean;
    tourId: string;
    goToDetail: (schedule: TourPlaceItem) => void;
}

export default function PlaceCard(props: PropType) {
    const [isDeleteModalActive, setIsDeleteModalActive] =
        useState<boolean>(false);

    const bgColor = ['color-bg-blue-5', 'bg-[#FFD4D4]', 'color-bg-blue-2`'];

    const deleteItem = (event: BaseSyntheticEvent) => {
        setIsDeleteModalActive(true);
        event.stopPropagation();
    };

    const deleteOK = () => {
        //api 요청 후 모달 닫기
        console.log('요청 전송됨');
        setIsDeleteModalActive(false);
    };

    const deleteCancel = () => {
        setIsDeleteModalActive(false);
    };

    return (
        <>
            {isDeleteModalActive ? (
                <CheckModal
                    mainText="정말 삭제하시겠습니까?"
                    subText="지운 항목은 되돌릴 수 없습니다."
                    OKText="삭제"
                    CancelText="취소"
                    clickOK={deleteOK}
                    clickCancel={deleteCancel}
                />
            ) : (
                <></>
            )}
            <div
                className="w-full p-[1vw] my-[0.5vw] rounded-[2vw] border-[0.5vw] border-[#D9D9D9]"
                onClick={() => props.goToDetail(props.schedule)}
            >
                <div className="w-full flex justify-between items-center">
                    <p className="text-[6vw]">{props.schedule.placeName}</p>
                    <span
                        className="text-[6vw] material-symbols-outlined z-10"
                        onClick={deleteItem}
                    >
                        close
                    </span>
                </div>
                <div className="w-full flex">
                    {props.schedule.tourActivityList.map((activity, index) => {
                        return (
                            <p
                                className={`text-[4vw] px-[1vw] mx-[0.5vw] rounded-[2vw] ${
                                    index <= 2 ? bgColor[index] : bgColor[0]
                                }`}
                            >
                                {activity.activity}
                            </p>
                        );
                    })}
                </div>
            </div>
        </>
    );
}
