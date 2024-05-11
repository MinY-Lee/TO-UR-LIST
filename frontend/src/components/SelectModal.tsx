import { useEffect, useState } from 'react';
import MyButton from './Buttons/myButton';
import { TourActivity, TourInfoDetail, TourPlaceItem } from '../types/types';

import tourPlaceTourId from '../dummy-data/get_tour_place_tourId.json';
import TourDetail from '../dummy-data/get_tour_detail.json';

interface Proptype {
    tourId: string;
    clickOK: () => void;
    clickCancel: () => void;
}

interface Group {
    [tourDay: number]: {
        [placeName: string]: TourActivity[];
    };
}

export default function SelectModal(props: Proptype) {
    const [data, setData] = useState<TourPlaceItem[]>();
    const [tourData, setTourData] = useState<TourInfoDetail>({
        tourId: '',
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
        memberList: [],
    });
    const [daysDifference, setDaysDifference] = useState<number>(0);
    const [daysList, setDaysList] = useState<number[]>([]);
    const [groupedItems, setGroupedItems] = useState<Group>();

    useEffect(() => {
        setData(tourPlaceTourId);

        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const tourData = TourDetail.find((tour) => tour.tourId === props.tourId);
        if (tourData) {
            setTourData(tourData);
        }

        const end: Date = new Date(tourData ? tourData.endDate : '');
        const start: Date = new Date(tourData ? tourData.startDate : '');

        // 밀리초(milliseconds) 단위의 차이를 날짜간 차이로 변환
        setDaysDifference((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1);
        // 0 제외
        setDaysList(Array.from({ length: daysDifference }, (_, index) => index + 1));

        // day 별로 장소 및 활동 분류
        groupItems();
    }, [data]);

    const groupItems = () => {
        const grouped: Group = {};

        data?.forEach((place) => {
            const { tourDay, placeName, tourActivityList } = place;

            if (!grouped[tourDay]) {
                grouped[tourDay] = {};
            }

            if (!grouped[tourDay][placeName]) {
                grouped[tourDay][placeName] = [];
            }

            grouped[tourDay][placeName] = [...tourActivityList];
        });

        // state 업데이트
        setGroupedItems(grouped);
    };

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? '0' : ''}${num}`;
    };

    const calcDate = (day: number): string => {
        const startDate = new Date(tourData ? tourData.startDate : '');
        const startDay = startDate.getDate();
        startDate.setDate(startDay + day);

        return `${startDate.getFullYear()}.${startDate.getMonth() + 1}.${startDate.getDate()}`;
    };

    return (
        <>
            <div className="absolute w-full h-full top-0 left-0 z-20 bg-black opacity-50"></div>
            <div className="absolute rounded-2xl w-[90%] h-[70%] left-[5%] top-[15%] z-30 bg-white grid grid-rows-10 p-5 border-[0.5vw] color-border-blue-2">
                <div className="text-2xl row-span-1">장소 / 활동 추가</div>
                <div className="row-span-8 overflow-y-scroll">
                    {daysList.map((day) => (
                        <div key={day}>
                            <div className="font-bold text-xl">
                                Day{formatNumberToTwoDigits(day)} {`| ${calcDate(day)}`}
                            </div>
                            <div className="border-t-2 border-black mt-2 mb-2">
                                {groupedItems &&
                                    groupedItems[day] &&
                                    Object.keys(groupedItems[day]).map((placeId, index) => (
                                        <div className="ml-3" key={index}>
                                            <div className="text-lg font-semibold">
                                                {placeId != '' ? <div>{placeId}</div> : ''}
                                            </div>
                                            <div className="ml-3">
                                                {groupedItems[day][placeId].map((item, index) => (
                                                    <div key={index} className=" grid grid-cols-3 justify-center m-1">
                                                        <div className="flex items-center col-span-2">
                                                            <input
                                                                id={`checkbox-${index}`}
                                                                type="checkbox"
                                                                // onChange={() => handleCheckbox(item)}
                                                                // checked={item.isChecked}
                                                                className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                                            />
                                                            <label className="ms-2 text-lg w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                                {item.activity}
                                                            </label>
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    ))}
                            </div>
                        </div>
                    ))}
                </div>
                <div className="row-span-1 grid grid-cols-2 justify-center p-3 gap-2">
                    <MyButton
                        isSelected={true}
                        onClick={props.clickOK}
                        text="저장"
                        type="full"
                        className="py-2"
                    ></MyButton>
                    <MyButton isSelected={false} onClick={props.clickCancel} text="취소" type="full"></MyButton>
                </div>
            </div>
        </>
    );
}
