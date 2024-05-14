import { useEffect, useState } from 'react';
import MyButton from '../../components/Buttons/myButton';

import { Item, TourInfoDetail } from '../../types/types';

import Checklist from '../../dummy-data/get_checklist.json';
import TourDetail from '../../dummy-data/get_tour_detail.json';
import PayTypeIcon from '../../assets/svg/payTypeIcon';

interface PropType {
    tourId: string;
}

interface ItemPerPlace {
    [placeId: string]: Item[];
}

interface ItemPerDayAndPlace {
    [day: number]: ItemPerPlace;
}

export default function ChecklistByDay(props: PropType) {
    const [data, setData] = useState<TourInfoDetail>({
        tourId: '',
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
        memberList: [],
    });
    const [daysDifference, setDaysDifference] = useState<number>(0);
    const [daysList, setDaysList] = useState<number[]>([]);
    const [groupedItems, setGroupedItems] = useState<ItemPerDayAndPlace>({});
    const id = props.tourId;

    useEffect(() => {
        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const tourData = TourDetail.find((tour) => tour.tourId === props.tourId);
        if (tourData) {
            setData(tourData);
        }

        const end: Date = new Date(data.endDate);
        const start: Date = new Date(data.startDate);

        // 밀리초(milliseconds) 단위의 차이를 날짜간 차이로 변환
        setDaysDifference((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1);
        setDaysList(Array.from({ length: daysDifference + 1 }, (_, index) => index));

        // 일자 및 장소 별로 그룹핑
        groupItems();
    }, [data, daysDifference]);

    interface Mapping {
        [key: string]: string[];
    }

    const mapping: Mapping = {
        walking: ['👣 산책', 'color-bg-blue-3'],
        shopping: ['🛒 쇼핑', 'bg-pink-100'],
    };

    // 활동 id 를 한글로 변환
    const ActivityIdToKor = (activityId: string): string => {
        return mapping[activityId][0];
    };

    // 활동 id 별 색상 부여
    const setColor = (activityId: string): string => {
        return mapping[activityId][1];
    };

    // day 별로 체크리스트 분류
    const groupItems = () => {
        const grouped: ItemPerDayAndPlace = {};

        Checklist.forEach((item) => {
            const { tourDay, placeId } = item;

            if (!grouped[tourDay]) {
                grouped[tourDay] = {};
            }

            if (!grouped[tourDay][placeId]) {
                grouped[tourDay][placeId] = [];
            }

            grouped[tourDay][placeId].push(item);
        });

        // state 업데이트
        setGroupedItems(grouped);
    };

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? '0' : ''}${num}`;
    };

    const calcDate = (day: number): string => {
        const startDate = new Date(data?.startDate);
        const startDay = startDate.getDate();
        startDate.setDate(startDay + day);

        return `${startDate.getFullYear()}.${startDate.getMonth() + 1}.${startDate.getDate()}`;
    };

    const handleCheckbox = (target: Item): void => {
        const updatedChecklist: ItemPerDayAndPlace = { ...groupedItems };

        if (updatedChecklist[target.tourDay] && updatedChecklist[target.tourDay][target.placeId]) {
            updatedChecklist[target.tourDay][target.placeId].forEach((item) => {
                if (item.item === target.item) {
                    item.isChecked = !item.isChecked;
                }
            });

            setGroupedItems(updatedChecklist);
        }
    };

    return (
        <>
            <div className="w-full justify-between items-end p-5">
                <div>
                    <div className=" border-2 border-blue-200 rounded-2xl p-3">
                        <div className="flex w-full justify-end">
                            <MyButton
                                type="small"
                                text="편집"
                                className="text-white font-medium"
                                isSelected={true}
                                onClick={() => {
                                    window.location.href = `/tour/${id}/checklist/day`;
                                }}
                            />
                        </div>
                        <div className="flex flex-col">
                            {daysList.map((day) => (
                                <div key={day}>
                                    <div className="font-bold text-xl">
                                        Day{formatNumberToTwoDigits(day)}{' '}
                                        {day !== 0 ? `| ${calcDate(day)}` : '| 날짜 없음'}
                                    </div>
                                    <div className="border-t-2 border-black mt-2 mb-2">
                                        {groupedItems &&
                                            groupedItems[day] &&
                                            Object.keys(groupedItems[day]).map((placeId, index) => (
                                                <div className="ml-5" key={index}>
                                                    <div className="text-lg font-semibold">
                                                        {placeId != '' ? (
                                                            <div>{placeId} (실제 지명으로 바꿔야 함)</div>
                                                        ) : (
                                                            ''
                                                        )}
                                                    </div>
                                                    <div>
                                                        {groupedItems[day][placeId].map((item, index) => (
                                                            <div
                                                                key={index}
                                                                className=" grid grid-cols-3 justify-center m-1"
                                                            >
                                                                <div className="flex items-center col-span-2">
                                                                    <input
                                                                        id={`checkbox-${index}`}
                                                                        type="checkbox"
                                                                        onChange={() => handleCheckbox(item)}
                                                                        checked={item.isChecked}
                                                                        className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                                                    />
                                                                    <div className="ml-2">
                                                                        <PayTypeIcon isPublic={item.isPublic} />
                                                                    </div>
                                                                    <label className="ms-2 text-lg w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                                        {item.item}
                                                                    </label>
                                                                </div>
                                                                <div className="relative w-fit">
                                                                    {item.activityId && (
                                                                        <span
                                                                            className={`${setColor(
                                                                                item.activityId
                                                                            )} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}
                                                                        >
                                                                            {ActivityIdToKor(item.activityId)}
                                                                        </span>
                                                                    )}
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
                    </div>
                </div>
            </div>
        </>
    );
}
