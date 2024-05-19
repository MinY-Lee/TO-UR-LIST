import {useEffect, useState} from "react";
import {WebSockPlace} from "../../types/types";
import ColorMapping from "../../assets/colorMapping";

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

    const recommendActivites = props.activityList.slice(0, 3);
    const otherActivites = props.activityList.slice(3);

    return (
        <>
            <div className="w-full h-full absolute top-0 left-0 z-20 opacity-20 bg-black"></div>
            <div
                className="w-[80%] h-[60%] absolute left-[10%] top-[20%] z-30 bg-white color-border-blue-6 border-halfvw border-rad-2vw flex flex-col justify-around items-center p-2vw">
                {/* 현재 추가된 활동 */}
                <div className="w-full flex flex-col h-[30%]">
                    <div className="text-xl weight-text-semibold mb-1 px-vw">
                        현재 추가한 활동
                    </div>
                    <div className="w-full text-5vw break-keep flex flex-wrap overflow-y-auto p-vw">
                        {props.selectedSchedule.activityList.length > 0 ? (
                            props.selectedSchedule.activityList.map(
                                (activity, index) => {
                                    return (
                                        <div
                                            className={`px-2vw mb-vw text-white border-rad-2dot5vw mr-vw flex-shrink-0 ${
                                                ColorMapping()[activity]
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
                            <div className="text-base text-gray-400">
                                이 장소에서 진행할 활동을 선택해보세요! <br/>
                                활동에 필요한 준비물이 체크리스트에 자동으로 추가됩니다😊
                            </div>
                        )}
                    </div>
                </div>
                {/* 추천 활동 */}
                {/*<div className="w-full flex flex-col">*/}
                {/*    /!*<div className="text-5vw weight-text-semibold">*!/*/}
                {/*    /!*    이 장소에서 추천하는 활동*!/*/}
                {/*    /!*</div>*!/*/}
                {/*    다른 사람들이 많이 하는 활동이에요 :)*/}
                {/*    <div className="flex items-center text-5vw text-[#afafaf]">*/}
                {/*        {props.activityList.length > 0 ? (*/}
                {/*            cache.has(props.activityList[0]) ? (*/}
                {/*                <span*/}
                {/*                    className="px-2vw border-halfvw color-border-blue-6 color-bg-blue-6 text-white border-rad-2dot5vw mr-vw"*/}
                {/*                    onClick={() => {*/}
                {/*                        props.deleteActivity(*/}
                {/*                            props.selectedSchedule.tourPlaceId,*/}
                {/*                            props.activityList[0]*/}
                {/*                        );*/}
                {/*                    }}*/}
                {/*                >*/}
                {/*                    {"- " + props.activityList[0]}*/}
                {/*                </span>*/}
                {/*            ) : (*/}
                {/*                <span*/}
                {/*                    className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw mr-vw"*/}
                {/*                    onClick={() =>*/}
                {/*                        props.addActivity(*/}
                {/*                            props.selectedSchedule.placeId,*/}
                {/*                            props.selectedSchedule.tourDay,*/}
                {/*                            props.activityList[0]*/}
                {/*                        )*/}
                {/*                    }*/}
                {/*                >*/}
                {/*                    {"+ " + props.activityList[0]}*/}
                {/*                </span>*/}
                {/*            )*/}
                {/*        ) : (*/}
                {/*            <div>이 장소는 할만한게 없네요ㅠㅠ</div>*/}
                {/*        )}*/}
                {/*        {props.activityList.length > 1 ? (*/}
                {/*            cache.has(props.activityList[1]) ? (*/}
                {/*                <span*/}
                {/*                    className="px-2vw border-halfvw color-border-blue-6 color-bg-blue-6 text-white border-rad-2dot5vw mr-vw"*/}
                {/*                    onClick={() => {*/}
                {/*                        props.deleteActivity(*/}
                {/*                            props.selectedSchedule.tourPlaceId,*/}
                {/*                            props.activityList[1]*/}
                {/*                        );*/}
                {/*                    }}*/}
                {/*                >*/}
                {/*                    {"- " + props.activityList[1]}*/}
                {/*                </span>*/}
                {/*            ) : (*/}
                {/*                <span*/}
                {/*                    className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw mr-vw"*/}
                {/*                    onClick={() =>*/}
                {/*                        props.addActivity(*/}
                {/*                            props.selectedSchedule.placeId,*/}
                {/*                            props.selectedSchedule.tourDay,*/}
                {/*                            props.activityList[1]*/}
                {/*                        )*/}
                {/*                    }*/}
                {/*                >*/}
                {/*                    {"+ " + props.activityList[1]}*/}
                {/*                </span>*/}
                {/*            )*/}
                {/*        ) : (*/}
                {/*            <></>*/}
                {/*        )}*/}
                {/*        {props.activityList.length > 2 ? (*/}
                {/*            cache.has(props.activityList[2]) ? (*/}
                {/*                <span*/}
                {/*                    className="px-2vw border-halfvw color-border-blue-6 color-bg-blue-6 text-white border-rad-2dot5vw mr-vw"*/}
                {/*                    onClick={() => {*/}
                {/*                        props.deleteActivity(*/}
                {/*                            props.selectedSchedule.tourPlaceId,*/}
                {/*                            props.activityList[2]*/}
                {/*                        );*/}
                {/*                    }}*/}
                {/*                >*/}
                {/*                    {"- " + props.activityList[2]}*/}
                {/*                </span>*/}
                {/*            ) : (*/}
                {/*                <span*/}
                {/*                    className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw mr-vw"*/}
                {/*                    onClick={() =>*/}
                {/*                        props.addActivity(*/}
                {/*                            props.selectedSchedule.placeId,*/}
                {/*                            props.selectedSchedule.tourDay,*/}
                {/*                            props.activityList[2]*/}
                {/*                        )*/}
                {/*                    }*/}
                {/*                >*/}
                {/*                    {"+ " + props.activityList[2]}*/}
                {/*                </span>*/}
                {/*            )*/}
                {/*        ) : (*/}
                {/*            <></>*/}
                {/*        )}*/}
                {/*    </div>*/}
                {/*</div>*/}

                {/* 전체 보기 */}
                <div className="w-full h-[60%] flex flex-col my-vw">
                    <div className="text-5vw h-5vw weight-text-semibold px-vw">
                        활동 리스트
                    </div>
                    {/* 추천 활동 보기 */}
                    <div className="text-base text-gray-400 px-vw"> 다른 사람들이 많이 하는 활동이에요</div>
                    <div className="flex items-center text-5vw text-[#afafaf] flex-wrap mb-3vw">
                        {recommendActivites.length > 0 ? (
                            recommendActivites.map((activity) => {
                                return cache.has(activity) ? (
                                    <span
                                        className={`px-2vw text-white border-rad-2dot5vw m-vw flex-shrink-0
 ${ColorMapping()[activity]}`}
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
                                        className="px-2vw border-halfvw border-[#afafaf] border-rad-2dot5vw m-vw flex-shrink-0 "
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
                    <div
                        className="text-5vw overflow-y-scroll"
                        style={{
                            height: "calc(100% - 5vw)",
                        }}
                    >
                        <div className="text-base text-gray-400 px-vw"> 전체 리스트</div>
                        <div className="flex items-center text-5vw text-[#afafaf] flex-wrap">
                            {props.activityList.length > 0 ? (
                                props.activityList.map((activity) => {
                                    return cache.has(activity) ? (
                                        <span
                                            className={`px-2vw border-halfvw border-white text-white border-rad-2dot5vw m-vw flex-shrink-0
                                            ${ColorMapping()[activity]}`}
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
                <div className="w-full h-[8%] flex justify-around items-center text-5vw">
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
