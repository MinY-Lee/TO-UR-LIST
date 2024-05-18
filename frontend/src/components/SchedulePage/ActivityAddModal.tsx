import { useEffect, useState } from "react";
import { WebSockPlace } from "../../types/types";

interface PropType {
    selectedSchedule: WebSockPlace;
    closeModal: () => void;
    activityList: string[];
    addActivity: (placeId: string, tourDay: number, activity: string) => void;
    deleteActivity: (tourPlaceId: string, activity: string) => void;
}

export default function ActivityAddModal(props: PropType) {
    //활동 존재 여부 파악을 위한 hashset
    const [cache, setCache] = useState<Set<string>>(new Set());

    const bgColor = [
        "color-bg-blue-6",
        "bg-[#FFB1B1]",
        "bg-[#FF9315]",
        "bg-[#5CD651]",
    ];

    useEffect(() => {
        const newSet = new Set<string>();
        props.selectedSchedule.activityList.map((activity: string) => {
            newSet.add(activity);
        });
        setCache(newSet);
    }, [props.selectedSchedule]);

    return (
        <>
            <div className="w-full h-full absolute top-0 left-0 z-20 opacity-20 bg-black"></div>
            <div className="w-[80%] h-[60%] absolute left-[10%] top-[20%] z-30 bg-white color-border-blue-6 border-halfvw border-rad-2vw flex flex-col justify-around items-center p-2vw overflow-y-scroll">
                {/* 현재 추가된 활동 */}
                <div className="w-full flex flex-col">
                    <div className="text-7vw weight-text-semibold">
                        현재 추가된 활동
                    </div>
                    <div className="w-full text-5vw break-keep flex flex-wrap">
                        {props.selectedSchedule.activityList.length > 0 ? (
                            props.selectedSchedule.activityList.map(
                                (activity, index) => {
                                    return (
                                        <div
                                            className={`px-2vw mb-vw text-white border-rad-2dot5vw mr-vw flex-shrink-0 ${
                                                bgColor[index % 4]
                                            }`}
                                            onClick={() => {
                                                props.deleteActivity(
                                                    props.selectedSchedule
                                                        .tourPlaceId,
                                                    activity
                                                );
                                            }}
                                        >
                                            {"- " + activity}
                                        </div>
                                    );
                                }
                            )
                        ) : (
                            <div>아직 추가된 활동이 없습니다.</div>
                        )}
                    </div>
                </div>
                {/* 추천 활동 */}
                <div className="w-full flex flex-col">
                    <div className="text-5vw weight-text-semibold">
                        이 장소에서 추천하는 활동
                    </div>
                    <div className="flex items-center text-5vw text-[#afafaf]">
                        {props.activityList.length > 0 ? (
                            cache.has(props.activityList[0]) ? (
                                <span
                                    className="px-2vw border-halfvw color-border-blue-6 color-bg-blue-6 text-white border-rad-2dot5vw mr-vw"
                                    onClick={() => {
                                        props.deleteActivity(
                                            props.selectedSchedule.tourPlaceId,
                                            props.activityList[0]
                                        );
                                    }}
                                >
                                    {"- " + props.activityList[0]}
                                </span>
                            ) : (
                                <span
                                    className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw mr-vw"
                                    onClick={() =>
                                        props.addActivity(
                                            props.selectedSchedule.placeId,
                                            props.selectedSchedule.tourDay,
                                            props.activityList[0]
                                        )
                                    }
                                >
                                    {"+ " + props.activityList[0]}
                                </span>
                            )
                        ) : (
                            <div>이 장소는 할만한게 없네요ㅠㅠ</div>
                        )}
                        {props.activityList.length > 1 ? (
                            cache.has(props.activityList[1]) ? (
                                <span
                                    className="px-2vw border-halfvw color-border-blue-6 color-bg-blue-6 text-white border-rad-2dot5vw mr-vw"
                                    onClick={() => {
                                        props.deleteActivity(
                                            props.selectedSchedule.tourPlaceId,
                                            props.activityList[1]
                                        );
                                    }}
                                >
                                    {"- " + props.activityList[1]}
                                </span>
                            ) : (
                                <span
                                    className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw mr-vw"
                                    onClick={() =>
                                        props.addActivity(
                                            props.selectedSchedule.placeId,
                                            props.selectedSchedule.tourDay,
                                            props.activityList[1]
                                        )
                                    }
                                >
                                    {"+ " + props.activityList[1]}
                                </span>
                            )
                        ) : (
                            <></>
                        )}
                        {props.activityList.length > 2 ? (
                            cache.has(props.activityList[2]) ? (
                                <span
                                    className="px-2vw border-halfvw color-border-blue-6 color-bg-blue-6 text-white border-rad-2dot5vw mr-vw"
                                    onClick={() => {
                                        props.deleteActivity(
                                            props.selectedSchedule.tourPlaceId,
                                            props.activityList[2]
                                        );
                                    }}
                                >
                                    {"- " + props.activityList[2]}
                                </span>
                            ) : (
                                <span
                                    className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw mr-vw"
                                    onClick={() =>
                                        props.addActivity(
                                            props.selectedSchedule.placeId,
                                            props.selectedSchedule.tourDay,
                                            props.activityList[2]
                                        )
                                    }
                                >
                                    {"+ " + props.activityList[2]}
                                </span>
                            )
                        ) : (
                            <></>
                        )}
                    </div>
                </div>

                {/* 전체 보기 */}
                <div className="w-full h-[40%] flex flex-col">
                    <div className="text-5vw h-5vw weight-text-semibold">
                        전체 선택 가능 활동
                    </div>
                    <div
                        className="text-5vw overflow-y-scroll"
                        style={{
                            height: "calc(100% - 5vw)",
                        }}
                    >
                        <div className="flex items-center text-5vw text-[#afafaf] flex-wrap">
                            {props.activityList.length > 0 ? (
                                props.activityList.map((activity) => {
                                    return cache.has(activity) ? (
                                        <span
                                            className="px-2vw border-halfvw color-border-blue-6 color-bg-blue-6 text-white border-rad-2dot5vw m-vw flex-shrink-0"
                                            onClick={() => {
                                                props.deleteActivity(
                                                    props.selectedSchedule
                                                        .tourPlaceId,
                                                    activity
                                                );
                                            }}
                                        >
                                            {"- " + activity}
                                        </span>
                                    ) : (
                                        <span
                                            className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw m-vw flex-shrink-0"
                                            onClick={() =>
                                                props.addActivity(
                                                    props.selectedSchedule
                                                        .placeId,
                                                    props.selectedSchedule
                                                        .tourDay,
                                                    activity
                                                )
                                            }
                                        >
                                            {"+ " + activity}
                                        </span>
                                    );
                                })
                            ) : (
                                <div>이 장소는 할만한게 없네요ㅠㅠ</div>
                            )}
                        </div>
                    </div>
                </div>
                <div className="w-full h-[10%] flex justify-around items-center text-5vw">
                    <div
                        className="w-full h-full color-bg-blue-6 text-white border-rad-2vw flex justify-center items-center"
                        onClick={props.closeModal}
                    >
                        닫기
                    </div>
                </div>
            </div>
        </>
    );
}
