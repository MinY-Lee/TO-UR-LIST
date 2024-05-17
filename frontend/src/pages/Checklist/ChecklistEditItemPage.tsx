import { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Item, PlaceMapping, TourPlaceItem } from "../../types/types";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import TabBarTour from "../../components/TabBar/TabBarTour";
import MyButton from "../../components/Buttons/myButton";
import ChecklistInput from "../../components/Checklist/checklistInput";

import CheckModal from "../../components/CheckModal";
import SelectModal from "../../components/SelectModal";
import { addChecklist, deleteChecklist, getChecklist, modifyItem } from "../../util/api/checklist";
import { HttpStatusCode } from "axios";
import { getPlaceList } from "../../util/api/place";
import CancelIcon from "../../assets/svg/cancelIcon";

export default function ChecklistEditItemPage() {
    const [tourId, setTourId] = useState<string>("");
    const [editItem, setEditItem] = useState<Item>({
        tourId: "",
        placeId: "",
        activity: "",
        item: "",
        tourDay: 0,
        isChecked: false,
        isPublic: false,
    });
    const [newItem, setNewItem] = useState<Item>({
        tourId: "",
        placeId: "",
        activity: "",
        item: "",
        tourDay: 0,
        isChecked: false,
        isPublic: false,
    });
    const [data, setData] = useState<Item[]>([]);
    const [filteredData, setFilteredData] = useState<Item[]>([]);
    const [checkModalActive, setCheckModalActive] = useState<boolean>(false);
    const [selectModalActive, setSelectModalActive] = useState<boolean>(false);
    const [deleteItem, setDeleteItem] = useState<Item>({
        tourId: "",
        placeId: "",
        activity: "",
        item: "",
        tourDay: 0,
        isChecked: false,
        isPublic: false,
    });
    const [placeData, setPlaceData] = useState<PlaceMapping>({});

    const location = useLocation();
    const state = location.state;
    const navigate = useNavigate();

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split("/");
        setTourId(address[address.length - 3]);
        // 전달받은 아이템
        setEditItem(state.item);

        if (tourId != "") {
            // 장소 id 랑 이름 매칭 위해
            getPlaceList(tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        let mapping: PlaceMapping = {};
                        res.data.map((schedule: TourPlaceItem) => {
                            mapping[schedule.placeId] = schedule.placeName;
                        });

                        setPlaceData(mapping);
                    }
                })
                .catch((err) => console.log(err));

            getChecklist(tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        setData(res.data);
                    }
                })
                .catch((err) => console.log(err));
        }
    }, [tourId]);

    useEffect(() => {
        filterItem();
    }, [data, editItem]);

    const handleDone = () => {
        // 이름 수정
        if (newItem.item != "") {
            modifyItem({
                oldItem: {
                    tourId: editItem.tourId,
                    placeId: editItem.placeId,
                    activity: editItem.activity,
                    tourDay: editItem.tourDay,
                    item: editItem.item,
                    isChecked: editItem.isChecked,
                },
                newItem: {
                    tourId: newItem.tourId,
                    placeId: newItem.placeId,
                    activity: newItem.activity,
                    tourDay: newItem.tourDay,
                    item: newItem.item,
                    isChecked: newItem.isChecked,
                },
            })
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok && !res.data.isDuplicated) {
                        navigate(-1);
                    }
                })
                .catch((err) => console.log(err));
        } else {
            navigate(-1);
        }
    };

    const closeModal = () => {
        setCheckModalActive(false);
    };

    const closeSelectModal = () => {
        setSelectModalActive(false);
    };

    const onUpdate = (item: Item) => {
        // 제목 수정시
        setNewItem(item);
    };

    // 해당 아이템이 사용되는 장소/활동 필터링
    const filterItem = () => {
        const dataList = data.filter((item) => item.placeId != "" && item.item == editItem.item);
        setFilteredData(dataList);
        console.log(dataList);
    };

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? "0" : ""}${num}`;
    };

    const handleDeleteModal = (item: Item) => {
        setCheckModalActive(true);
        setDeleteItem(item);
    };

    const handleDelete = () => {
        const { tourId, placeId, activity, item, tourDay, isChecked, isPublic } = deleteItem;
        if (filteredData.length == 1) {
            // 한 장소에서만 사용된 경우
            deleteChecklist({
                tourId: tourId,
                placeId: placeId,
                activity: activity,
                item: item,
                tourDay: tourDay,
                isChecked: isChecked,
            })
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        addChecklist(isPublic ? "public" : "private", {
                            tourId: tourId,
                            placeId: "",
                            activity: "",
                            tourDay: 0,
                            item: item,
                            isChecked: isChecked,
                        })
                            .then((res) => {
                                if (res.status == HttpStatusCode.Ok) {
                                    const updatedActivity = filteredData.filter(
                                        (item) => item !== deleteItem
                                    );
                                    setFilteredData(updatedActivity);
                                    setCheckModalActive(false);
                                }
                            })
                            .catch((err) => console.log(err));
                    }
                })
                .catch((err) => console.log(err));
        } else {
            // item 중 장소 활동 일치하는 아이템 삭제
            deleteChecklist({
                tourId: tourId,
                placeId: placeId,
                activity: activity,
                item: item,
                tourDay: tourDay,
                isChecked: isChecked,
            }).then((res) => {
                if (res.status == HttpStatusCode.Ok) {
                    const updatedActivity = filteredData.filter((item) => item !== deleteItem);
                    setFilteredData(updatedActivity);
                    setCheckModalActive(false);
                }
            });
        }
    };

    const handleEdit = (updatedChecklist: Item[]) => {
        // 추가나 일부 삭제
        console.log("전: " + filteredData);
        console.log("후: " + updatedChecklist);

        filteredData.map((item: Item) => {
            const { tourId, placeId, activity, tourDay, isChecked, isPublic } = item;
            if (!updatedChecklist.includes(item)) {
                // 삭제
                deleteChecklist({
                    tourId: tourId,
                    placeId: placeId,
                    activity: activity,
                    item: item.item,
                    tourDay: tourDay,
                    isChecked: isChecked,
                })
                    .then((res) => {
                        if (res.status != HttpStatusCode.Ok) {
                            if (res.status == HttpStatusCode.Ok) {
                            }
                        }
                    })
                    .catch((err) => console.log(err));
            }
        });

        updatedChecklist.map((item: Item) => {
            const { tourId, placeId, activity, tourDay, isChecked, isPublic } = item;
            if (!filteredData.includes(item)) {
                // 추가
                addChecklist(isPublic ? "public" : "private", {
                    tourId: tourId,
                    placeId: placeId,
                    activity: activity,
                    tourDay: tourDay,
                    item: item.item,
                    isChecked: isChecked,
                })
                    .then((res) => {
                        if (res.status == HttpStatusCode.Ok) {
                        }
                    })
                    .catch((err) => console.log(err));
            }
        });
        setFilteredData(updatedChecklist);
        setSelectModalActive(false);
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

            {selectModalActive ? (
                <SelectModal
                    item={editItem}
                    filteredData={filteredData}
                    tourId={tourId}
                    placeData={placeData}
                    clickOK={handleEdit}
                    clickCancel={closeSelectModal}
                />
            ) : (
                <></>
            )}

            <header>
                <HeaderBar />
            </header>
            <div className="m-5">
                <div className="flex justify-between items-center mb-5">
                    <h1 className="text-3xl font-bold">항목 수정</h1>
                    <MyButton
                        className="text-xl text-white font-medium"
                        isSelected={true}
                        onClick={handleDone}
                        text="완료"
                        type="small"
                    />
                </div>
                <div className="mb-5">
                    <ChecklistInput tourId={tourId} onUpdate={onUpdate} default={editItem} />
                </div>
                <div>
                    <div className="text-xl">사용되는 장소/활동</div>
                    <div>
                        {filteredData && filteredData.length > 0 ? (
                            filteredData.map((item, index) => (
                                <div
                                    key={index}
                                    className="grid grid-cols-5 border-2 m-2 p-3 rounded-lg color-border-blue-1"
                                >
                                    <div className="col-span-1 color-text-blue-2 text-lg">
                                        Day
                                        {formatNumberToTwoDigits(item.tourDay)}
                                    </div>
                                    {item.activity != "" ? (
                                        <div className="col-span-3 text-lg">
                                            {placeData[item.placeId]} / {item.activity}
                                        </div>
                                    ) : (
                                        <div className="col-span-3 text-lg">
                                            {placeData[item.placeId]}
                                        </div>
                                    )}

                                    <div
                                        className="col-span-1 flex justify-end items-center"
                                        onClick={() => handleDeleteModal(item)}
                                    >
                                        <CancelIcon className="w-5 h-5" />
                                    </div>
                                </div>
                            ))
                        ) : (
                            <div className="grid border-dashed border m-2 p-3 rounded-lg color-border-blue-1 color-text-blue-2 text-center">
                                사용되는 장소/활동이 없습니다
                            </div>
                        )}
                    </div>
                </div>
                <div
                    onClick={() => setSelectModalActive(true)}
                    className="underline text-center text-neutral-500 mt-10"
                >
                    장소 / 활동 추가
                </div>
            </div>
            <footer className="h-[]">
                <TabBarTour tourMode={1} tourId={tourId} type="checklist" />
            </footer>
        </>
    );
}
