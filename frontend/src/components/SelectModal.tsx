import { useEffect, useState } from "react";
import MyButton from "./Buttons/myButton";

import {
    Item,
    PlaceMapping,
    TourInfoDetail,
    TourPlaceItem,
} from "../types/types";

import { getTour } from "../util/api/tour";
import { getPlaceList } from "../util/api/place";
import { HttpStatusCode } from "axios";
import { addChecklist, getChecklist } from "../util/api/checklist";
import ScheduleList from "./Checklist/scheduleList";

interface Proptype {
    item: Item;
    filteredData: Item[];
    tourId: string;
    placeData: PlaceMapping;
    clickOK: (updatedItem: Item[]) => void;
    clickCancel: () => void;
}

interface Group {
    [tourDay: number]: {
        [placeId: string]: string[];
    };
}

export default function SelectModal(props: Proptype) {
    const [data, setData] = useState<TourPlaceItem[]>([]);
    const [tourData, setTourData] = useState<TourInfoDetail>({
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: [],
    });
    const [daysDifference, setDaysDifference] = useState<number>(0);
    const [daysList, setDaysList] = useState<number[]>([]);
    const [groupedPlaces, setGroupedPlaces] = useState<Group>({});
    const [updatedChecklist, setUpdatedChecklist] = useState<Item[]>([]);

    useEffect(() => {
        setUpdatedChecklist([...props.filteredData]);
        if (props.tourId) {
            getPlaceList(props.tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        setData(res.data);
                    }
                })
                .catch((err) => console.log(err));

            getTour(props.tourId)
                .then((res) => {
                    setTourData({
                        tourTitle: res.data.tourTitle,
                        cityList: res.data.cityList,
                        startDate: res.data.startDate.split("T")[0],
                        endDate: res.data.endDate.split("T")[0],
                        memberList: res.data.memberList,
                    });
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }, [props]);

    useEffect(() => {
        // day 별로 여행 정보 장소 및 활동 분류
        const grouped: Group = {};

        data.forEach((place) => {
            const { tourDay, placeId, activityList } = place;

            if (!grouped[tourDay]) {
                grouped[tourDay] = {};
            }

            if (!grouped[tourDay][placeId]) {
                grouped[tourDay][placeId] = [];
            }

            grouped[tourDay][placeId] = [...activityList];
        });

        setGroupedPlaces(grouped);
    }, [data]);

    useEffect(() => {
        const end: Date = new Date(tourData.endDate);
        const start: Date = new Date(tourData.startDate);

        // 밀리초(milliseconds) 단위의 차이를 날짜간 차이로 변환
        setDaysDifference(
            (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1
        );
        setDaysDifference(
            (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1
        );

        setDaysList(
            Array.from({ length: daysDifference + 1 }, (_, index) => index)
        );
        setDaysList(
            Array.from({ length: daysDifference + 1 }, (_, index) => index)
        );
    }, [tourData, daysDifference]);

    const handleCheckbox = (
        day: number,
        placeId: string,
        activity?: string
    ) => {
        if (!activity) {
            if (
                !updatedChecklist.some(
                    (item) => item.tourDay == day && item.placeId == placeId
                )
            ) {
                const updatedItem = {
                    tourId: props.item.tourId,
                    placeId: placeId,
                    activity: "",
                    tourDay: day,
                    item: props.item.item,
                    isPublic: props.item.isPublic,
                    isChecked: props.item.isChecked,
                };
                setUpdatedChecklist([...updatedChecklist, updatedItem]);
            } else {
                const checklist = updatedChecklist.filter(
                    (item) => item.tourDay != day && item.placeId != placeId
                );
                setUpdatedChecklist(checklist);
            }
        } else {
            // 없으면 넣어
            if (
                !updatedChecklist.some(
                    (item) =>
                        item.tourDay == day &&
                        item.placeId == placeId &&
                        item.activity == activity
                )
            ) {
                const updatedItem = {
                    tourId: props.item.tourId,
                    placeId: placeId,
                    activity: activity ? activity : "",
                    tourDay: day,
                    item: props.item.item,
                    isPublic: props.item.isPublic,
                    isChecked: false,
                };

                setUpdatedChecklist([...updatedChecklist, updatedItem]);
            } else {
                // 있으면 빼
                const checklist = updatedChecklist.filter(
                    (item) =>
                        item.tourDay != day ||
                        item.placeId != placeId ||
                        item.activity != activity
                );
                setUpdatedChecklist(checklist);
            }
        }
    };

    return (
        <>
            <div className="absolute w-full h-full top-0 left-0 z-20 bg-black opacity-50"></div>
            <div className="absolute rounded-2xl w-[90%] h-[70%] left-[5%] top-[15%] z-30 bg-white grid grid-rows-10 p-5 border-[0.5vw] color-border-blue-2">
                <div className="text-2xl row-span-1">장소 / 활동 추가</div>
                <div className="row-span-8 overflow-y-auto">
                    <ScheduleList
                        daysList={daysList}
                        filteredData={props.filteredData}
                        groupedPlaces={groupedPlaces}
                        handleCheckbox={handleCheckbox}
                        tourData={tourData}
                        placeData={props.placeData}
                        updatedChecklist={updatedChecklist}
                    />
                </div>
                <div className="row-span-1 grid grid-cols-2 justify-center p-3 gap-2">
                    <MyButton
                        isSelected={true}
                        onClick={() => props.clickOK(updatedChecklist)}
                        text="저장"
                        type="full"
                        className="py-2 text-white h-full"
                    ></MyButton>
                    <MyButton
                        isSelected={false}
                        onClick={props.clickCancel}
                        text="취소"
                        type="full"
                        className="py-2 color-text-blue-1"
                    ></MyButton>
                </div>
            </div>
        </>
    );
}
