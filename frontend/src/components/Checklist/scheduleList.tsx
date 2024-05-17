import { useEffect } from "react";
import { Item, PlaceMapping, TourInfoDetail } from "../../types/types";

interface Group {
    [tourDay: number]: {
        [placeName: string]: string[];
    };
}

interface PropType {
    daysList: number[];
    groupedPlaces: Group;
    filteredData: Item[];
    placeData: PlaceMapping;
    updatedChecklist: Item[];
    tourData: TourInfoDetail;
    handleCheckbox: (day: number, placeId: string, activity?: string) => void;
}

export default function ScheduleList(props: PropType) {
    useEffect(() => {
        console.log(props.updatedChecklist);
    }, [props]);

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? "0" : ""}${num}`;
    };

    const calcDate = (day: number): string => {
        const startDate = new Date(props.tourData ? props.tourData.startDate : "");
        const startDay = startDate.getDate();
        startDate.setDate(startDay + day - 1);

        return `${startDate.getFullYear()}.${startDate.getMonth() + 1}.${startDate.getDate()}`;
    };

    return (
        <>
            {props.daysList.map((day) => (
                <div key={day}>
                    <div className="font-bold text-xl">
                        Day{formatNumberToTwoDigits(day)} {`| ${calcDate(day)}`}
                    </div>
                    <div className="border-t-2 border-black mt-2 mb-2">
                        {props.groupedPlaces && props.groupedPlaces[day] ? (
                            <>
                                {Object.keys(props.groupedPlaces[day]).map((placeId, index) => (
                                    <div className="ml-3" key={index}>
                                        <div className="">
                                            {placeId != "" ? (
                                                <div className="text-lg font-bold">
                                                    {props.placeData[placeId]}
                                                </div>
                                            ) : (
                                                ""
                                            )}
                                        </div>
                                        <div className="ml-3">
                                            {props.groupedPlaces[day][placeId].length > 0 ? (
                                                <>
                                                    {props.groupedPlaces[day][placeId].map(
                                                        (activity, index) => (
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
                                                                                day,
                                                                                placeId,
                                                                                activity
                                                                            )
                                                                        }
                                                                        checked={props.updatedChecklist
                                                                            .map(
                                                                                (item) =>
                                                                                    item.activity
                                                                            )
                                                                            .includes(activity)}
                                                                        className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                                                    />
                                                                    <label className="ms-2 text-lg w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                                        {activity}
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
                                                                    props.handleCheckbox(
                                                                        day,
                                                                        placeId
                                                                    )
                                                                }
                                                                checked={props.updatedChecklist
                                                                    .map((item) => item.placeId)
                                                                    .includes(placeId)}
                                                                className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                                                            />
                                                            <label className="ms-2 text-lg w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                                (활동없음)
                                                            </label>
                                                        </div>
                                                    </div>
                                                </>
                                            )}
                                        </div>
                                    </div>
                                ))}
                            </>
                        ) : (
                            <div className="flex justify-center items-center h-[5vh]">
                                아직 장소/활동이 없어요.
                            </div>
                        )}
                    </div>
                </div>
            ))}
        </>
    );
}
