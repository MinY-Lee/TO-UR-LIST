import { useEffect, useState } from "react";
import MyButton from "../../components/Buttons/myButton";

// import Checklist from "../../dummy-data/get_checklist.json";
import { Item, ItemApi } from "../../types/types";
import PayTypeIcon from "../../assets/svg/payTypeIcon";
import { checkItem, getChecklist } from "../../util/api/checklist";
import { HttpStatusCode } from "axios";

interface PropType {
    tourId: string;
}

interface CountItem {
    [key: string]: number;
}

interface Mapping {
    [key: string]: string;
}

export default function MyCheckList(props: PropType) {
    const [checklist, setChecklist] = useState<Item[]>([]);
    const [filteredChecklist, setFilteredChecklist] = useState<Item[]>([]);
    const [filteredGroup, setFilteredGroup] = useState<CountItem>({});

    const mapping: Mapping = {
        산책: "color-bg-blue-3",
        쇼핑: "bg-pink-100",
    };

    // 활동 id 별 색상 부여
    const setColor = (activity: string): string => {
        if (mapping[activity]) {
            return mapping[activity];
        }
        return "color-bg-blue-3";
    };

    useEffect(() => {
        if (props.tourId != "") {
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

    // 같은 체크리스트 아이템 처리
    const prepareData = (checklist: Item[]) => {
        const itemGroups: CountItem = {};

        checklist.forEach((item) => {
            const itemName = item.item;
            if (itemName) {
                if (!itemGroups[itemName]) {
                    itemGroups[itemName] = 0;
                }
                itemGroups[itemName]++;
            }
        });

        return itemGroups;
    };

    // 같은 항목 리스트에 여러 번 띄우지 않게 처리
    const filterUniqueItems = (checklist: Item[]): Item[] => {
        const seenItems = new Set<string>();
        let uniqueItems: Item[] = [];
        let uncheck: Item[] = [];
        let check: Item[] = [];

        checklist.forEach((item) => {
            const itemName = item.item;
            if (itemName && !seenItems.has(itemName)) {
                seenItems.add(itemName);
                item.isChecked ? check.push(item) : uncheck.push(item);
            }
        });
        uniqueItems = [...uncheck, ...check];

        return uniqueItems;
    };

    const handleCheckbox = (index: number) => {
        const { activity, isChecked, item, placeId, tourDay, tourId } =
            filteredChecklist[index];

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
                    updatedChecklist[index].isChecked =
                        !updatedChecklist[index].isChecked;

                    // const movedItem = updatedChecklist.splice(index, 1)[0];
                    // updatedChecklist.push(movedItem);

                    setFilteredChecklist(updatedChecklist);
                }
            })
            .catch((err) => console.log(err));
    };

    return (
        <>
            <div className="w-full  justify-between items-end p-5 bak">
                <div>
                    <div className=" border-2 border-blue-200 rounded-2xl p-3">
                        <div className="flex w-full justify-end">
                            <MyButton
                                type="small"
                                className="text-white font-medium"
                                text="편집"
                                isSelected={true}
                                onClick={() => {
                                    window.location.href = `/tour/${props.tourId}/checklist/all`;
                                }}
                            />
                        </div>
                        <div className="flex flex-col">
                            {filteredChecklist.map((item, index) => (
                                <div
                                    key={index}
                                    className="grid grid-cols-3 justify-center m-1"
                                >
                                    <div className="flex items-center col-span-2">
                                        <input
                                            id="default-checkbox"
                                            type="checkbox"
                                            onChange={() =>
                                                handleCheckbox(index)
                                            }
                                            checked={item.isChecked}
                                            className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                        />
                                        <div className="ml-2">
                                            <PayTypeIcon
                                                isPublic={item.isPublic}
                                            />
                                        </div>
                                        <label className="ms-2 w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                            {item.item}
                                        </label>
                                    </div>

                                    <div className="relative w-fit">
                                        <div>
                                            {item.activity ? (
                                                <span
                                                    className={`${setColor(
                                                        item.activity
                                                    )} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}
                                                >
                                                    {item.activity}
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
                                                    <div className="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white color-bg-blue-1 border-2 border-white rounded-full -top-2 -end-[20%]">
                                                        {
                                                            filteredGroup[
                                                                item.item
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
                    </div>
                </div>
            </div>
        </>
    );
}
