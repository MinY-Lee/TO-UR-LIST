import { useEffect, useState } from "react";
import MyButton from "./Buttons/myButton";
import {
    Item,
    TourActivity,
    TourInfoDetail,
    TourPlaceItem,
} from "../types/types";

import tourPlaceTourId from "../dummy-data/get_tour_place_tourId.json";
import { getTour } from "../util/api/tour";
import { getPlaceList } from "../util/api/place";
import { HttpStatusCode } from "axios";
import { getChecklist } from "../util/api/checklist";

interface Proptype {
    item: Item;
    filteredData: Item[];
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
    const [groupedPlaces, setGroupedPlaces] = useState<Group>();
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
        let grouped: Group = {};

        data.forEach((place) => {
            const { tourDay, placeName, activityList } = place;

            if (!grouped[tourDay]) {
                grouped[tourDay] = {};
            }

            if (!grouped[tourDay][placeName]) {
                grouped[tourDay][placeName] = [];
            }

            grouped[tourDay][placeName] = [...activityList];
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

        setDaysList(
            Array.from({ length: daysDifference + 1 }, (_, index) => index)
        );
    }, [tourData, daysDifference]);

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? "0" : ""}${num}`;
    };

    const calcDate = (day: number): string => {
        const startDate = new Date(tourData ? tourData.startDate : "");
        const startDay = startDate.getDate();
        startDate.setDate(startDay + day - 1);

        return `${startDate.getFullYear()}.${
            startDate.getMonth() + 1
        }.${startDate.getDate()}`;
    };

    const handleCheckbox = (
        day: number,
        placeId: string,
        activity?: TourActivity
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
                    isChecked: props.item.isChecked, // ??
                };

                setUpdatedChecklist([...updatedChecklist, updatedItem]);
            } else {
                const checklist = updatedChecklist.filter(
                    (item) => item.tourDay != day && item.placeId != placeId
                );
                setUpdatedChecklist(checklist);
            }
        } else {
        }
        console.log(updatedChecklist);
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
                                Day{formatNumberToTwoDigits(day)}{" "}
                                {`| ${calcDate(day)}`}
                            </div>
                            <div className="border-t-2 border-black mt-2 mb-2">
                                {groupedPlaces && groupedPlaces[day] ? (
                                    <>
                                        {Object.keys(groupedPlaces[day]).map(
                                            (placeId, index) => (
                                                <div
                                                    className="ml-3"
                                                    key={index}
                                                >
                                                    <div className="">
                                                        {placeId != "" ? (
                                                            <div className="text-lg font-bold">
                                                                {placeId}
                                                            </div>
                                                        ) : (
                                                            ""
                                                        )}
                                                    </div>
                                                    <div className="ml-3">
                                                        {groupedPlaces[day][
                                                            placeId
                                                        ].length > 0 ? (
                                                            <>
                                                                {groupedPlaces[
                                                                    day
                                                                ][placeId].map(
                                                                    (
                                                                        activity,
                                                                        index
                                                                    ) => (
                                                                        <div
                                                                            key={
                                                                                index
                                                                            }
                                                                            className=" grid grid-cols-3 justify-center m-1"
                                                                        >
                                                                            <div className="flex items-center col-span-2">
                                                                                <input
                                                                                    id={`checkbox-${index}`}
                                                                                    type="checkbox"
                                                                                    onChange={() =>
                                                                                        handleCheckbox(
                                                                                            day,
                                                                                            placeId,
                                                                                            activity
                                                                                        )
                                                                                    }
                                                                                    checked={props.filteredData
                                                                                        .map(
                                                                                            (
                                                                                                item
                                                                                            ) =>
                                                                                                item.activity
                                                                                        )
                                                                                        .includes(
                                                                                            activity.activity
                                                                                        )}
                                                                                    className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                                                                />
                                                                                <label className="ms-2 text-lg w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                                                    {
                                                                                        activity.activity
                                                                                    }
                                                                                </label>
                                                                            </div>
                                                                        </div>
                                                                    )
                                                                )}
                                                            </>
                                                        ) : (
                                                            <>
                                                                <div className=" grid grid-cols-3 justify-center m-1">
                                                                    <div className="flex items-center col-span-2">
                                                                        <input
                                                                            id={`checkbox-${index}`}
                                                                            type="checkbox"
                                                                            onChange={() =>
                                                                                handleCheckbox(
                                                                                    day,
                                                                                    placeId
                                                                                )
                                                                            }
                                                                            checked={updatedChecklist
                                                                                .map(
                                                                                    (
                                                                                        item
                                                                                    ) =>
                                                                                        item.placeId
                                                                                )
                                                                                .includes(
                                                                                    placeId
                                                                                )}
                                                                            className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                                                        />
                                                                        <label className="ms-2 text-lg w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                                            (장소에
                                                                            추가하기)
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </>
                                                        )}
                                                    </div>
                                                </div>
                                            )
                                        )}
                                    </>
                                ) : (
                                    <div className="flex justify-center items-center h-[5vh]">
                                        아직 장소/활동이 없어요.
                                    </div>
                                )}
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
                    <MyButton
                        isSelected={false}
                        onClick={props.clickCancel}
                        text="취소"
                        type="full"
                    ></MyButton>
                </div>
            </div>
        </>
    );
}
