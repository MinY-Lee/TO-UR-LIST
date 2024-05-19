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
    const [isUpdated, setIsUpdated] = useState<boolean>(false);

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
    }, [props, isUpdated]);

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

        checklist.forEach((item) => {
            const itemName = item.item;
            if (itemName && !seenItems.has(itemName)) {
                seenItems.add(itemName);
                uniqueItems.push(item);
            }
        });

        return uniqueItems;
    };

    const handleCheckbox = (target: Item) => {
        // 전체에서 체크 시 해당 아이템 모두 체킹
        const targetItems: ItemApi[] = [];

        checklist.forEach((checkItem) => {
            if (target.item == checkItem.item) {
                const { activity, isChecked, item, placeId, tourDay, tourId } =
                    checkItem;
                targetItems.push({
                    activity: activity,
                    isChecked: !isChecked,
                    item: item,
                    placeId: placeId,
                    tourDay: tourDay,
                    tourId: tourId,
                });
            }
        });

        if (targetItems.length > 0) {
            targetItems.forEach((targetItem) => {
                checkItem(targetItem)
                    .then((res) => {
                        if (res.status == HttpStatusCode.Ok) {
                            setIsUpdated(!isUpdated);
                        }
                    })
                    .catch((err) => console.log(err));
            });
        }
    };

    const getActivity = (target: Item): string => {
        const itemActivity = checklist.find(
            (item) => item.item === target.item && item.activity != ""
        );

        return itemActivity ? itemActivity.activity : "";
    };

    return (
        <>
            <div className="w-full justify-between items-end p-5 bak">
                <div className="text-xl font-bold mb-3">여행 체크리스트</div>
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
                                                    onChange={() =>
                                                        handleCheckbox(item)
                                                    }
                                                    checked={item.isChecked}
                                                    className="w-6 h-6 bg-gray-100 border-gray-300 rounded "
                                                />
                                                <PayTypeIcon
                                                    isPublic={item.isPublic}
                                                />
                                                <label className="text-lg">
                                                    {item.item}
                                                </label>
                                            </div>
                                            <div className="relative flex w-full justify-end pr-5">
                                                <div>
                                                    {getActivity(item) ? (
                                                        <span
                                                            className={`${setColor(
                                                                getActivity(
                                                                    item
                                                                )
                                                            )} text-white drop-shadow-md px-2.5 py-0.5 rounded`}
                                                        >
                                                            {getActivity(item)}
                                                        </span>
                                                    ) : (
                                                        ""
                                                    )}
                                                </div>
                                                <div>
                                                    {item.activity &&
                                                    filteredGroup[item.item] >
                                                        1 ? (
                                                        <div>
                                                            <span className="sr-only">
                                                                Notifications
                                                            </span>
                                                            <div
                                                                className={`absolute right-0 inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white ${setColor(
                                                                    item.activity
                                                                )} border-2 border-white rounded-full -top-2 -end-[35%]`}
                                                            >
                                                                {
                                                                    filteredGroup[
                                                                        item
                                                                            .item
                                                                    ]
                                                                }
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
