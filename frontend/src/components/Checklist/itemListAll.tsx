import { useNavigate, useSearchParams } from "react-router-dom";
import PayTypeIcon from "../../assets/svg/payTypeIcon";
import TrashIcon from "../../assets/svg/trashIcon";
import { Item, TourInfoDetail } from "../../types/types";
import ChecklistInput from "./checklistInput";
import { useEffect, useState } from "react";
import ColorMapping from "../../assets/colorMapping";

interface ItemPerPlace {
    [placeId: string]: Item[];
}

interface ItemPerDayAndPlace {
    [day: number]: ItemPerPlace;
}

interface CountItem {
    [key: string]: number;
}

interface PropType {
    filteredChecklist: Item[];
    tourId: string;
    filteredGroup: CountItem;
    handleDeleteModal: (
        item: Item,
        event: React.MouseEvent<HTMLDivElement>
    ) => void;
}

export default function ItemListAll(props: PropType) {
    const [filteredGroup, setFilteredGroup] = useState<CountItem>({});
    const [filteredChecklist, setFilteredChecklist] = useState<Item[]>([]);

    const navigate = useNavigate();

    useEffect(() => {
        setFilteredChecklist(props.filteredChecklist);
        setFilteredGroup(props.filteredGroup);
    }, [props]);

    // 활동 id 별 색상 부여
    const setColor = (activity: string): string => {
        if (ColorMapping()[activity]) {
            return ColorMapping()[activity];
        }
        return "color-bg-blue-3";
    };

    const handleEditChecklist = (item: Item) => {
        // state로 데이터 전달하며 페이지 이동
        navigate(`/tour/${props.tourId}/checklist/edit`, {
            state: { item: item },
        });
    };

    return (
        <>
            {props.filteredChecklist.length == 0 ? (
                <div className="flex justify-center items-center h-[5vh] text-xl">
                    현재 체크리스트가 없습니다.
                </div>
            ) : (
                <div className="w-[90%] flex flex-col gap-4">
                    {props.filteredChecklist.map((item, index) => (
                        <div
                            key={index}
                            className="grid grid-cols-6 justify-center"
                            onClick={() => {
                                handleEditChecklist(item);
                            }}
                        >
                            <div className="ml-2 col-span-3 flex items-center">
                                <PayTypeIcon isPublic={item.isPublic} />
                                <div className=" text-lg flex items-center ml-3">
                                    {item.item}
                                </div>
                            </div>
                            <div className="col-span-3 grid grid-cols-3 justify-center">
                                <div className="relative w-fit col-span-2">
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
                                            <span
                                                className={` text-gray-200 border-2 border-dashed px-2.5 py-0.5 rounded`}
                                            >
                                                + 활동없음
                                            </span>
                                        )}
                                    </div>
                                    <div>
                                        {item.activity &&
                                        props.filteredGroup[item.item] > 1 ? (
                                            <div className="relative">
                                                <span className="sr-only">
                                                    Notifications
                                                </span>
                                                <div className="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white color-bg-blue-1 border-2 border-white rounded-full -top-8 z-10 -end-5">
                                                    {
                                                        props.filteredGroup[
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
                                <div
                                    onClick={(event) => {
                                        props.handleDeleteModal(item, event);
                                    }}
                                    className="flex justify-end mr-2 items-center col-span-1"
                                >
                                    <TrashIcon />
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </>
    );
}
