import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

import MyButton from "../../components/Buttons/myButton";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import CheckModal from "../../components/CheckModal";
import { Item, ItemApi, TourInfoDetail } from "../../types/types";

import TabBarTour from "../../components/TabBar/TabBarTour";
import { deleteChecklist, getChecklist } from "../../util/api/checklist";
import { HttpStatusCode } from "axios";
import { getTour } from "../../util/api/tour";
import ItemListPerDay from "../../components/Checklist/itemListPerDay";

interface ItemPerPlace {
    [placeId: string]: Item[];
}

interface ItemPerDayAndPlace {
    [day: number]: ItemPerPlace;
}

export default function ChecklistEditDayPage() {
    const navigate = useNavigate();

    const [tourId, setTourId] = useState<string>("");
    const [checklist, setChecklist] = useState<Item[]>([]);
    const [data, setData] = useState<TourInfoDetail>({
        tourId: "",
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: [],
    });

    const [checkModalActive, setIsCheckModalActive] = useState<boolean>(false);
    const [deleteItem, setDeleteItem] = useState<Item>();
    const [isClicked, setIsClicked] = useState<boolean>(false);
    const [daysDifference, setDaysDifference] = useState<number>(0);
    const [daysList, setDaysList] = useState<number[]>([]);
    const [groupedItems, setGroupedItems] = useState<ItemPerDayAndPlace>({});
    const [isAddState, setIsAddState] = useState<boolean[]>([]);

    useEffect(() => {
        const address: string[] = window.location.href.split("/");
        setTourId(address[address.length - 3]);

        if (tourId != "") {
            getChecklist(tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        const checklistData = res.data;
                        setChecklist(checklistData);
                    }
                    console.log(res.data);
                })
                .catch((err) => console.log(err));

            getTour(tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        setData(res.data);
                    }
                })
                .catch((err) => console.log(err));
        }
    }, [tourId]);

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
        const end: Date = new Date(data.endDate);
        const start: Date = new Date(data.startDate);

        // 밀리초(milliseconds) 단위의 차이를 날짜간 차이로 변환
        setDaysDifference(
            (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24) + 1
        );
        setDaysList(
            Array.from({ length: daysDifference + 1 }, (_, index) => index)
        );

        // 항목추가 활성화 유무 배열 만들기
        const length = daysDifference + 1;
        const arr = Array.from({ length: length }, () => false);
        setIsAddState(arr);
    }, [data, daysDifference]);

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? "0" : ""}${num}`;
    };

    const calcDate = (day: number): string => {
        const startDate = new Date(data?.startDate);
        const startDay = startDate.getDate();
        startDate.setDate(startDay + day);

        return `${startDate.getFullYear()}.${
            startDate.getMonth() + 1
        }.${startDate.getDate()}`;
    };

    const handleAddState = (day: number) => {
        const updatedIsAddState = [...isAddState];
        for (let i = 0; i < updatedIsAddState.length; i++) {
            if (i == day) {
                updatedIsAddState[i] = true;
            } else {
                updatedIsAddState[i] = false;
            }
        }
        setIsAddState(updatedIsAddState);
    };

    const handleDone = () => {
        navigate(-1);
    };

    const closeModal = () => {
        setIsCheckModalActive(false);
    };

    const handleDelete = () => {
        if (deleteItem) {
            const { activity, isChecked, item, placeId, tourDay, tourId } =
                deleteItem;
            const targetItem: ItemApi = {
                activity: activity,
                isChecked: !isChecked,
                item: item,
                placeId: placeId,
                tourDay: tourDay,
                tourId: tourId,
            };

            deleteChecklist(targetItem)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        const updatedChecklist = groupedItems[
                            deleteItem.tourDay
                        ][deleteItem?.placeId].filter(
                            (item: Item) => item !== deleteItem
                        );
                        const updatedFullChecklist = { ...groupedItems }; // 원본 groupedItems를 복제하여 업데이트할 새 객체 생성

                        Object.keys(updatedFullChecklist).forEach(
                            (dayString) => {
                                const day = Number(dayString);
                                Object.keys(updatedFullChecklist[day]).forEach(
                                    (placeId) => {
                                        if (
                                            day == deleteItem.tourDay &&
                                            placeId == deleteItem.placeId
                                        ) {
                                            updatedFullChecklist[day][placeId] =
                                                [...updatedChecklist]; // 새로운 배열로 교체
                                        } else {
                                            updatedFullChecklist[day][placeId] =
                                                [
                                                    ...updatedFullChecklist[
                                                        day
                                                    ][placeId],
                                                ];
                                        }
                                    }
                                );
                            }
                        );

                        setGroupedItems(updatedFullChecklist);
                        setIsCheckModalActive(false);
                    }
                })
                .catch((err) => console.log(err));
        }
    };

    const handleDeleteModal = (item: Item) => {
        setIsCheckModalActive(true);
        setDeleteItem(item);
    };

    const onUpdate = (item: Item) => {
        if (groupedItems) {
            const updatedChecklist = [
                ...groupedItems[item.tourDay][item.placeId],
                item,
            ];
            const updatedFullChecklist = { ...groupedItems }; // 원본 groupedItems를 복제하여 업데이트할 새 객체 생성

            Object.keys(updatedFullChecklist).forEach((dayString) => {
                const day = Number(dayString);
                Object.keys(updatedFullChecklist[day]).forEach((placeId) => {
                    if (day == item.tourDay && placeId == item.placeId) {
                        updatedFullChecklist[day][placeId] = [
                            ...updatedChecklist,
                        ]; // 새로운 배열로 교체
                    } else {
                        updatedFullChecklist[day][placeId] = [
                            ...updatedFullChecklist[day][placeId],
                        ];
                    }
                });
            });

            setGroupedItems(updatedFullChecklist);
        }
    };

    return (
        <>
            {checkModalActive ? (
                <CheckModal
                    mainText="정말 삭제하시겠습니까?"
                    subText="지운 항목은 되돌릴 수 없습니다."
                    OKText="삭제"
                    CancelText="취소"
                    clickOK={handleDelete}
                    clickCancel={closeModal}
                />
            ) : (
                <></>
            )}

            <header>
                <HeaderBar />
            </header>
            <div className="m-5">
                <div className="flex justify-between items-center mb-5">
                    <h1 className="text-3xl font-bold">일정별 체크리스트</h1>
                    <MyButton
                        className="text-xl text-white font-medium"
                        isSelected={true}
                        onClick={() => handleDone()}
                        text="완료"
                        type="small"
                    />
                </div>

                <div className="border border-black p-5 rounded-xl overflow-y-scroll h-[70vh]">
                    <ItemListPerDay
                        data={data}
                        daysList={daysList}
                        groupedItems={groupedItems}
                        handleAddState={handleAddState}
                        handleDeleteModal={handleDeleteModal}
                        isAddState={isAddState}
                        onUpdate={onUpdate}
                        tourId={tourId}
                    />
                </div>
            </div>
            <footer className="h-[]">
                <TabBarTour tourMode={1} tourId={tourId} type="checklist" />
            </footer>
        </>
    );
}
