import PayTypeIcon from "../../assets/svg/payTypeIcon";
import TrashIcon from "../../assets/svg/trashIcon";
import { Item, TourInfoDetail } from "../../types/types";
import ChecklistInput from "./checklistInput";

interface ItemPerPlace {
    [placeId: string]: Item[];
}

interface ItemPerDayAndPlace {
    [day: number]: ItemPerPlace;
}

interface Mapping {
    [key: string]: string[];
}

interface PropType {
    tourId: string;
    data: TourInfoDetail;
    daysList: number[];
    groupedItems: ItemPerDayAndPlace;
    isAddState: boolean[];
    handleDeleteModal: (item: Item) => void;
    handleAddState: (day: number) => void;
    onUpdate: (item: Item) => void;
}

export default function ItemListPerDay(props: PropType) {
    const mapping: Mapping = {
        walking: ["👣 산책", "color-bg-blue-3"],
        shopping: ["🛒 쇼핑", "bg-pink-100"],
    };

    // 활동 id 를 한글로 변환
    const ActivityToKor = (activity: string): string => {
        return mapping[activity][0];
    };

    // 활동 id 별 색상 부여
    const setColor = (activity: string): string => {
        return mapping[activity][1];
    };

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? "0" : ""}${num}`;
    };

    const calcDate = (day: number): string => {
        const startDate = new Date(props.data.startDate);
        const startDay = startDate.getDate();
        startDate.setDate(startDay + day - 1);

        return `${startDate.getFullYear()}.${
            startDate.getMonth() + 1
        }.${startDate.getDate()}`;
    };

    return (
        <>
            {props.daysList.map((day) => (
                <div key={day}>
                    <div className="font-bold text-xl">
                        Day{formatNumberToTwoDigits(day)}{" "}
                        {day !== 0 ? `| ${calcDate(day)}` : "| 날짜 없음"}
                    </div>
                    <div className="border-t-2 border-black mt-2 mb-2">
                        {props.groupedItems[day] ? (
                            <>
                                {props.groupedItems[day] &&
                                    Object.keys(props.groupedItems[day]).map(
                                        (placeId, index) => (
                                            <div
                                                className="ml-5 mt-3"
                                                key={index}
                                            >
                                                <div className="text-lg font-semibold">
                                                    {placeId != "" ? (
                                                        <div>{placeId}</div>
                                                    ) : (
                                                        ""
                                                    )}
                                                </div>
                                                <div>
                                                    {props.groupedItems[day][
                                                        placeId
                                                    ].map((item, index) => (
                                                        <div
                                                            key={index}
                                                            className=" grid grid-cols-2 justify-center m-1"
                                                        >
                                                            <div className="flex items-center">
                                                                <div className="ml-2">
                                                                    <PayTypeIcon
                                                                        isPublic={
                                                                            item.isPublic
                                                                        }
                                                                    />
                                                                </div>
                                                                <label className="ms-2 text-lg w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                                    {item.item}
                                                                </label>
                                                            </div>
                                                            <div className="flex justify-between">
                                                                {item.activity ? (
                                                                    <span
                                                                        className={`${setColor(
                                                                            item.activity
                                                                        )} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}
                                                                    >
                                                                        {ActivityToKor(
                                                                            item.activity
                                                                        )}
                                                                    </span>
                                                                ) : (
                                                                    <span
                                                                        className={` text-gray-500 border-2 border-dashed px-2.5 py-0.5 rounded`}
                                                                    >
                                                                        +
                                                                        활동없음
                                                                    </span>
                                                                )}
                                                                <div
                                                                    onClick={() => {
                                                                        props.handleDeleteModal(
                                                                            item
                                                                        );
                                                                    }}
                                                                    className="flex justify-end mr-2 items-center"
                                                                >
                                                                    <TrashIcon />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>
                                                {props.isAddState[day] ? (
                                                    <ChecklistInput
                                                        className="px-[3%]"
                                                        tourId={props.tourId}
                                                        checklistPerDay={
                                                            props.groupedItems
                                                        }
                                                        tourDay={day}
                                                        placeId={placeId}
                                                        onUpdate={
                                                            props.onUpdate
                                                        }
                                                    />
                                                ) : (
                                                    <div
                                                        onClick={() =>
                                                            props.handleAddState(
                                                                day
                                                            )
                                                        }
                                                        className="ml-3 mt-1 underline text-neutral-400"
                                                    >
                                                        + 항목추가
                                                    </div>
                                                )}
                                            </div>
                                        )
                                    )}
                            </>
                        ) : (
                            <div className="flex justify-center items-center h-[5vh]">
                                해당 날짜의 체크리스트가 없습니다.
                            </div>
                        )}
                    </div>
                </div>
            ))}
        </>
    );
}
