import { useEffect, useState } from "react";
import MyButton from "../../components/Buttons/myButton";

import Checklist from "../../dummy-data/get_checklist.json";
import { Item, ItemApi } from "../../types/types";
import { HttpStatusCode } from "axios";
import { checkItem, getChecklist } from "../../util/api/checklist";
import PayTypeIcon from "../../assets/svg/payTypeIcon";
import ColorMapping from "../../assets/colorMapping";

interface PropType {
    tourId: string;
}

interface CountItem {
    [key: string]: number;
}

export default function TourCheckList(props: PropType) {
    const [checklist, setChecklist] = useState<Item[]>([]);
    const [filteredChecklist, setFilteredChecklist] = useState<Item[]>([]);
    const [filteredGroup, setFilteredGroup] = useState<CountItem>({});

    useEffect(() => {
        if (props.tourId) {
            getChecklist(props.tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        setChecklist(res.data);
                        // 중복 횟수 카운트
                        setFilteredGroup(prepareData(res.data));
                        // 중복 하나씩만 남김
                        setFilteredChecklist(filterUniqueItems(res.data));
                    }
                })
                .catch((err) => console.log(err));
        }
    }, [props]);

    // 활동 id 별 색상 부여
    const setColor = (activity: string): string => {
        if (ColorMapping()[activity]) {
            return ColorMapping()[activity];
        }
        return "color-bg-blue-3";
    };

    // 같은 체크리스트 아이템 처리
    const prepareData = (checklist: Item[]) => {
        const itemGroups: CountItem = {};

        checklist.forEach((item) => {
            const itemName = item.item;
            if (!itemGroups[itemName]) {
                itemGroups[itemName] = 0;
            }
            if (item.activity != "") {
                itemGroups[itemName]++;
            }
        });

        return itemGroups;
    };

    // 같은 항목 리스트에 여러 번 띄우지 않게 처리
    // 체크된 항목은 아래로
    const filterUniqueItems = (checklist: Item[]): Item[] => {
        const seenItems = new Set<string>();
        let uniqueItems: Item[] = [];
        let unchecked: Item[] = [];
        let checked: Item[] = [];

        checklist.forEach((item) => {
            const itemName = item.item;
            if (itemName && !seenItems.has(itemName)) {
                seenItems.add(itemName);
                // 체크 여부 구분
                item.isChecked == true ? checked.push(item) : unchecked.push(item);
            }
        });

        uniqueItems = [...unchecked, ...checked];

        return uniqueItems;
    };

    const handleCheckbox = (index: number) => {
        const { activity, isChecked, item, placeId, tourDay, tourId } = filteredChecklist[index];
        const targetItem: ItemApi = {
            activity: activity,
            isChecked: !isChecked,
            item: item,
            placeId: placeId,
            tourDay: tourDay,
            tourId: tourId,
        };

        checkItem(targetItem)
            .then((res) => {
                if (res.status == HttpStatusCode.Ok) {
                    // 화면상 반영 및 아래로 이동
                    const updatedChecklist = [...filteredChecklist];
                    updatedChecklist[index].isChecked = !updatedChecklist[index].isChecked;

                    setFilteredChecklist(updatedChecklist);
                }
            })
            .catch((err) => console.log(err));
    };

    const getActivity = (target: Item): string => {
        const itemActivity = checklist.find(
            (item) => item.item === target.item && item.activity != ""
        );

        return itemActivity ? itemActivity.activity : "";
    };

    return (
        <>
            <div className="w-full  justify-between items-end p-5 bak">
                <div className="text-xl font-bold mb-3">전체 체크리스트</div>
                <div>
                    <div className=" border-2 color-border-blue-1 rounded-2xl p-3">
                        <div className="flex w-full justify-end mb-2">
                            <MyButton
                                type="small"
                                text="편집"
                                isSelected={true}
                                onClick={() => {
                                    window.location.href = `/tour/${props.tourId}/checklist/all`;
                                }}
                                className="text-white"
                            />
                        </div>
                        <div className="flex flex-col">
                            {filteredChecklist.length == 0 ? (
                                <div className="flex justify-center items-center h-[20vh] text-xl">
                                    현재 체크리스트가 없습니다.
                                </div>
                            ) : (
                                <div className="flex flex-col gap-1">
                                    {filteredChecklist.map((item, index) => (
                                        <div
                                            key={index}
                                            className="grid grid-cols-3 justify-center m-1"
                                        >
                                            <div className="flex items-center gap-3 col-span-2">
                                                <input
                                                    id="default-checkbox"
                                                    type="checkbox"
                                                    onChange={() => handleCheckbox(index)}
                                                    checked={item.isChecked}
                                                    className="w-6 h-6 bg-gray-100 border-gray-300 rounded "
                                                />
                                                <PayTypeIcon isPublic={item.isPublic} />
                                                <label className="text-lg">{item.item}</label>
                                            </div>
                                            <div className="relative w-fit">
                                                <div>
                                                    {getActivity(item) ? (
                                                        <span
                                                            className={`${setColor(
                                                                getActivity(item)
                                                            )} ${
                                                                setColor(getActivity(item)) ==
                                                                "bg-[#2BA1F9]"
                                                                    ? "text-white"
                                                                    : "text-gray-500"
                                                            } drop-shadow-md px-2.5 py-0.5 rounded`}
                                                        >
                                                            {getActivity(item)}
                                                        </span>
                                                    ) : (
                                                        ""
                                                    )}
                                                </div>
                                                <div>
                                                    {item.activity &&
                                                    filteredGroup[item.item] > 1 ? (
                                                        <div>
                                                            <span className="sr-only">
                                                                Notifications
                                                            </span>
                                                            <div
                                                                className={`absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white ${setColor(
                                                                    item.activity
                                                                )} border-2 border-white rounded-full -top-2 -end-[35%]`}
                                                            >
                                                                {filteredGroup[item.item]}
                                                            </div>
                                                        </div>
                                                    ) : (
                                                        ""
                                                    )}
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
