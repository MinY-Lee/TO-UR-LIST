import PayTypeIcon from "../../assets/svg/payTypeIcon";
import { Item, TourInfoDetail } from "../../types/types";

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
    data: TourInfoDetail;
    daysList: number[];
    groupedItems: ItemPerDayAndPlace;
    handleCheckbox: (item: Item) => void;
}

export default function ItemList(props: PropType) {
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

    return (
        <>
            {props.daysList.map((day) => (
                <div key={day}>
                    <div className="font-bold text-xl">
                        Day{formatNumberToTwoDigits(day)}{" "}
                        {day !== 0 ? `| ${calcDate(day)}` : "| 날짜 없음"}
                    </div>
                    <div className="border-t-2 border-black mt-2 mb-2">
                        {props.groupedItems &&
                            props.groupedItems[day] &&
                            Object.keys(props.groupedItems[day]).map(
                                (placeId, index) => (
                                    <div className="ml-5" key={index}>
                                        <div className="text-lg font-semibold">
                                            {placeId != "" ? (
                                                <div>{placeId} </div>
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
                                                    className=" grid grid-cols-3 justify-center m-1"
                                                >
                                                    <div className="flex items-center col-span-2">
                                                        <input
                                                            id={`checkbox-${index}`}
                                                            type="checkbox"
                                                            onChange={() =>
                                                                props.handleCheckbox(
                                                                    item
                                                                )
                                                            }
                                                            checked={
                                                                item.isChecked
                                                            }
                                                            className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                                        />
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
                                                    <div className="relative w-fit">
                                                        {item.activity && (
                                                            <span
                                                                className={`${setColor(
                                                                    item.activity
                                                                )} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}
                                                            >
                                                                {ActivityToKor(
                                                                    item.activity
                                                                )}
                                                            </span>
                                                        )}
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                )
                            )}
                    </div>
                </div>
            ))}
        </>
    );
}
