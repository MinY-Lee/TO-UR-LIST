import { BaseSyntheticEvent, useEffect, useState } from 'react';
import { WebSockPlace } from '../../types/types';
import CheckModal from '../CheckModal';
import { Client } from '@stomp/stompjs';

interface PropType {
    schedule: WebSockPlace;
    isEditable: boolean;
    tourId: string;
    goToDetail: (schedule: WebSockPlace) => void;
    wsClient: Client;
}

export default function PlaceCard(props: PropType) {
    const [schedule, setSchedule] = useState<WebSockPlace>(props.schedule);

    const [isDeleteModalActive, setIsDeleteModalActive] =
        useState<boolean>(false);

    const bgColor = ['color-bg-blue-5', 'bg-[#FFD4D4]', 'color-bg-blue-2`'];

    const deleteItem = (event: BaseSyntheticEvent) => {
        setIsDeleteModalActive(true);
        event.stopPropagation();
    };

    useEffect(() => {
        setSchedule(props.schedule);
    }, [props.schedule]);

    const deleteOK = () => {
        //api 요청 후 모달 닫기
        if (props.wsClient) {
            props.wsClient.publish({
                destination: `/app/place/${props.tourId}`,
                body: JSON.stringify({
                    type: 'DELETE_PLACE',
                    body: {
                        tourId: props.tourId,
                        placeId: schedule.placeId,
                        placeName: schedule.placeName,
                        tourDay: schedule.tourDay,
                    },
                }),
            });
        }
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
                className="w-full p-vw my-dot5vw border-rad-2vw border-halfvw border-[#D9D9D9]"
                onClick={() => props.goToDetail(schedule)}
            >
                <div className="w-full flex justify-between items-center">
                    <p className="text-6vw">{schedule.placeName}</p>
                    {props.isEditable ? (
                        <span
                            className="text-6vw material-symbols-outlined z-10"
                            onClick={deleteItem}
                        >
                            close
                        </span>
                    ) : (
                        <></>
                    )}
                </div>
                <div className="w-full flex">
                    {schedule.activityList.map((activity, index) => {
                        return (
                            <p
                                className={`text-4vw px-vw mx-dot5vw border-rad-2vw ${
                                    bgColor[index % 2]
                                }`}
                            >
                                {activity}
                            </p>
                        );
                    })}
                </div>
            </div>
        </>
    );
}
