import { useEffect, useState } from "react";
import MyButton from "../../components/Buttons/myButton";

import { Item, ItemApi, TourInfoDetail } from "../../types/types";

import TourDetail from "../../dummy-data/get_tour_detail.json";
import PayTypeIcon from "../../assets/svg/payTypeIcon";
import { checkItem, getChecklist } from "../../util/api/checklist";
import { HttpStatusCode } from "axios";
import { getTour } from "../../util/api/tour";
import ItemList from "./itemList";

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
    const [checklist, setChecklist] = useState<Item[]>([]);
    const [data, setData] = useState<TourInfoDetail>({
        tourId: "",
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: [],
    });
    const [daysDifference, setDaysDifference] = useState<number>(0);
    const [daysList, setDaysList] = useState<number[]>([]);
    const [groupedItems, setGroupedItems] = useState<ItemPerDayAndPlace>({});

    useEffect(() => {
        if (props.tourId != "") {
            getChecklist(props.tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        setChecklist(res.data);
                    }
                })
                .catch((err) => console.log(err));

            getTour(props.tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        setData(res.data);
                    }
                })
                .catch((err) => console.log(err));
        }
    }, [props]);

    useEffect(() => {
        const end: Date = new Date(data.endDate);
        const start: Date = new Date(data.startDate);

        // 밀리초(milliseconds) 단위의 차이를 날짜간 차이로 변환
        setDaysDifference(
            (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1
        );
        setDaysList(
            Array.from({ length: daysDifference + 1 }, (_, index) => index)
        );
    }, [data, daysDifference]);

    useEffect(() => {
        // 일자 및 장소 별로 그룹핑
        let grouped: ItemPerDayAndPlace = {};

        checklist.forEach((item) => {
            const { tourDay, placeId } = item;

            if (!grouped[tourDay]) {
                grouped[tourDay] = {};
            }

            if (!grouped[tourDay][placeId]) {
                grouped[tourDay][placeId] = [];
            }

            grouped[tourDay][placeId].push(item);
        });

        setGroupedItems(grouped);
    }, [checklist]);

    useEffect(() => {
        if (props.tourId != "") {
        }
    }, [props]);

    const handleCheckbox = (target: Item): void => {
        const { activity, isChecked, item, placeId, tourDay, tourId } = target;
        const targetItem: ItemApi = {
            activity: activity,
            isChecked: isChecked,
            item: item,
            placeId: placeId,
            tourDay: tourDay,
            tourId: tourId,
        };

        checkItem(targetItem)
            .then((res) => {
                if (res.status == HttpStatusCode.Ok) {
                    const updatedChecklist: ItemPerDayAndPlace = {
                        ...groupedItems,
                    };

                    if (
                        updatedChecklist[target.tourDay] &&
                        updatedChecklist[target.tourDay][target.placeId]
                    ) {
                        updatedChecklist[target.tourDay][
                            target.placeId
                        ].forEach((item) => {
                            if (item.item === target.item) {
                                item.isChecked = !item.isChecked;
                            }
                        });

                        setGroupedItems(updatedChecklist);
                    }
                }
            })
            .catch((err) => console.log(err));
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
                                    window.location.href = `/tour/${props.tourId}/checklist/day`;
                                }}
                            />
                        </div>
                        <div className="flex flex-col">
                            <ItemList
                                data={data}
                                daysList={daysList}
                                groupedItems={groupedItems}
                                handleCheckbox={handleCheckbox}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
