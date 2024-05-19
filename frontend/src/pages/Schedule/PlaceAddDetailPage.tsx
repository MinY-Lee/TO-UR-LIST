import { useLocation } from "react-router-dom";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import { useCallback, useEffect, useState } from "react";
import TabBarTour from "../../components/TabBar/TabBarTour";
import {
    TourEditDetail,
    WebSockPlace,
    TourInfoDetail,
    PlaceInfoDetail,
} from "../../types/types";

//dummy data

// import TestActivity from "../../dummy-data/test_activitylist.json";

import {
    getActivityList,
    getPhotoUrl,
    getPlaceList,
    searchPlaceDetail,
} from "../../util/api/place";
import { httpStatusCode } from "../../util/api/http-status";
import WebSocket from "../../components/TabBar/WebSocket";
import { getTour } from "../../util/api/tour";
import { Client } from "@stomp/stompjs";
import ActivityAddModal from "../../components/SchedulePage/ActivityAddModal";
import DayChangeModal from "../../components/SchedulePage/DayChangeModal";
import Loading from "../../components/Loading";

import MapLogo from "../../assets/image/map.svg";
import ClockLogo from "../../assets/image/clock.svg";
import TrashIcon from "../../assets/svg/trashIcon.tsx";
import CalcelIcon from "../../assets/svg/cancelIcon.tsx";
import ClockIcon from "../../assets/svg/clockIcon.tsx";
import CreditCardIcon from "../../assets/svg/creditCardIcon.tsx";
import MapIcon from "../../assets/svg/mapIcon";
import PlaceMapIcon from "../../assets/svg/placeMapIcon.tsx";
import { HttpStatusCode } from "axios";

export default function PlaceAddDetailPage() {
    const location = useLocation();

    //추가 모달
    const [isActivityModal, setIsActivityModal] = useState<boolean>(false);
    const [selectedSchedule, setSelectedSchedule] = useState<WebSockPlace>({
        activityList: [],
        placeId: "",
        placeName: "",
        tourDay: 0,
        tourPlaceId: "",
    });

    const [isDayChange, setIsDayChange] = useState<boolean>(false);
    const [selectedDate, setSelectedDate] = useState<number>(0);

    const [tourDay, setTourDay] = useState<number>(0);
    const [tourId, setTourId] = useState<string>("");
    const [placeId, setPlaceId] = useState<string>("");
    const [period, setPeriod] = useState<number>(0);
    const [startDate, setStartDate] = useState<Date>(new Date());

    const [isLoading, setIsLoading] = useState<boolean>(false);

    //여행 정보
    const [tourInfo, setTourInfo] = useState<TourInfoDetail>({
        tourTitle: "",
        startDate: "",
        endDate: "",
        memberList: [],
        cityList: [],
    });

    const [tourDetail, setTourDetail] = useState<TourEditDetail>();
    const [placeInfo, setPlaceInfo] = useState<PlaceInfoDetail>({
        placeId: "",
        placeName: "",
        placePrimaryType: "",
        placeLatitude: 0,
        placeLongitude: 0,
        placeAddress: "",
        placeWeekdayDescriptions: [],
        placeAcceptCashOnly: false,
        placeAcceptCreditCards: false,
        placeOpenNow: false,
        placePhotoList: [],
    });

    //전체 활동 리스트(선택 여부 무관)
    const [activityList, setActivityList] = useState<string[]>([]);

    //전체 방문 일정
    const [thisPlaceVisit, setThisPlaceVisit] = useState<WebSockPlace[]>([]);

    //웹소켓
    const [wsClient, setWsClient] = useState<Client>(new Client());

    /**state로부터 장소 정보 불러오기, 초기 세팅 */
    useEffect(() => {
        if (location.state) {
            // console.log(location.state.tourDay + 1);
            setIsLoading(true);

            setPlaceId(location.state.placeId);
            setTourDay(location.state.tourDay + 1);
            setTourId(location.state.tourId);
            setPeriod(location.state.period);
            /**장소 상세 조회 */
            searchPlaceDetail(
                location.state.tourId,
                location.state.tourDay + 1,
                location.state.placeId
            )
                .then((res) => {
                    // console.log(res);

                    if (res.status === httpStatusCode.OK) {
                        setTourDetail(res.data);
                        setPlaceInfo(res.data.placeInfo);
                        makePhotoUrl(res.data.placeInfo);
                    }
                })
                .catch((err) => {
                    console.log(err);
                })
                .finally(() => {
                    setIsLoading(false);
                });

            //전체 활동목록 받아오기
            getActivityList(location.state.placeId)
                .then((res) => {
                    // console.log(res);
                    if (res.status === httpStatusCode.OK) {
                        setActivityList(res.data);
                    }
                })
                .catch((err) => {
                    console.log(err);
                })
                .finally(() => {
                    setIsLoading(false);
                }); //원래는 이걸로 받아오는데 현재 미구현

            //dummydata이용
            // setActivityList(TestActivity);

            //여행정보 받아오기
            getTour(location.state.tourId)
                .then((res) => {
                    if (res.status === httpStatusCode.OK) {
                        setTourInfo(res.data);
                        setStartDate(new Date(res.data.startDate));
                    } else {
                        // console.log("Error");
                    }
                })
                .catch((err) => {
                    console.log(err);
                })
                .finally(() => {
                    setIsLoading(false);
                });

            //전체 스케줄 조회
            getPlaceList(location.state.tourId)
                .then((res) => {
                    const filtered = res.data.filter(
                        (place: WebSockPlace) =>
                            place.placeId === location.state.placeId
                    );
                    filtered.sort(
                        (a: WebSockPlace, b: WebSockPlace) =>
                            a.tourDay - b.tourDay
                    );
                    setThisPlaceVisit(filtered);
                })
                .finally(() => {
                    setIsLoading(false);
                });
        }
    }, []);

    /**date to string */
    const dateToString = useCallback(
        (day: number) => {
            if (day === 0) return "-";
            //ms
            const date = new Date(startDate.getTime() + (day - 1) * 86400000);

            return `${date.getFullYear()}.${
                date.getMonth() + 1 >= 10
                    ? date.getMonth() + 1
                    : "0" + (date.getMonth() + 1)
            }.${date.getDate() >= 10 ? date.getDate() : "0" + date.getDate()}`;
        },
        [startDate]
    );

    //UPDATE_PLACE도착
    const update = (newSchedule: WebSockPlace[]) => {
        setIsLoading(true);
        //장소 상세 정보 재조회
        searchPlaceDetail(tourId, tourDay, placeId)
            .then((res) => {
                if (res.status === httpStatusCode.OK) {
                    setTourDetail(res.data);
                }
            })
            .catch((err) => {
                console.log(err);
            })
            .finally(() => {
                setIsLoading(false);
            });

        //새로운 일정으로 업데이트
        // console.log(newSchedule);
        const filtered = newSchedule.filter(
            (place: WebSockPlace) => place.placeId === placeId
        );
        filtered.sort(
            (a: WebSockPlace, b: WebSockPlace) => a.tourDay - b.tourDay
        );
        // console.log(filtered);

        setThisPlaceVisit(filtered);
    };

    useEffect(() => {
        //selectedSchedule 변경
        thisPlaceVisit.map((place: WebSockPlace) => {
            if (
                place.placeId === selectedSchedule.placeId &&
                place.tourDay === selectedSchedule.tourDay
            ) {
                // console.log(place);
                setSelectedSchedule(place);
            }
        });
    }, [thisPlaceVisit]);

    // const makePhotoUrl = (original: string) => {
    //     const photoRefer = original.split("/")[3];
    //     const apiKey = import.meta.env.VITE_REACT_GOOGLE_MAPS_API_KEY;
    //     return `/maps/api/place/photo?maxwidth=400&photo_reference=${photoRefer}&key=${apiKey}`;
    // };

    const [urlList, setUrlList] = useState<string[]>([]);
    const makePhotoUrl = async (placeInfo: PlaceInfoDetail) => {
        try {
            const responses = await Promise.all(
                placeInfo.placePhotoList.map((photoRefer) =>
                    getPhotoUrl(photoRefer)
                )
            );
            const urls = responses
                .filter((response) => response.status === HttpStatusCode.Ok)
                .map((response) => response.data);
            setUrlList(urls);
        } catch (error) {
            console.error("Error fetching photo URLs:", error);
        }
    };

    /**활동 추가 모달 닫기 */
    const closeModal = () => {
        setIsActivityModal(false);
    };

    const addActivity = (
        placeId: string,
        tourDay: number,
        activity: string
    ) => {
        setIsLoading(true);
        if (wsClient) {
            wsClient.publish({
                destination: `/app/place/${tourId}`,
                body: JSON.stringify({
                    type: "ADD_ACTIVITY",
                    body: {
                        tourId: tourId,
                        placeId: placeId,
                        tourDay: tourDay,
                        activity: activity,
                    },
                }),
            });
        }
    };

    const deleteActivity = (tourPlaceId: string, activity: string) => {
        setIsLoading(true);
        if (wsClient) {
            wsClient.publish({
                destination: `/app/place/${tourId}`,
                body: JSON.stringify({
                    type: "DELETE_ACTIVITY",
                    body: {
                        tourPlaceId: tourPlaceId,
                        activity: activity,
                    },
                }),
            });
        }
    };

    const changeDate = (oldDate: number, newDate: number) => {
        setIsLoading(true);
        if (wsClient) {
            wsClient.publish({
                destination: `/app/place/${tourId}`,
                body: JSON.stringify({
                    type: "UPDATE_PLACE_DATE",
                    body: {
                        tourId: tourId,
                        placeId: placeId,
                        placeName: placeInfo.placeName,
                        oldTourDay: oldDate,
                        newTourDay: newDate,
                    },
                }),
            });
        }
        setIsDayChange(false);
    };

    const closeDayChange = () => {
        setIsDayChange(false);
    };

    return (
        <>
            {/* 활동 추가 모달 */}
            {isActivityModal ? (
                <ActivityAddModal
                    selectedSchedule={selectedSchedule}
                    closeModal={closeModal}
                    activityList={activityList}
                    addActivity={addActivity}
                    deleteActivity={deleteActivity}
                />
            ) : (
                <></>
            )}
            {/* 날짜 변경 모달 */}
            {isDayChange ? (
                <DayChangeModal
                    closeModal={closeDayChange}
                    startDate={tourInfo.startDate}
                    period={period}
                    selectedDate={selectedDate}
                    changeDate={changeDate}
                />
            ) : (
                <></>
            )}
            {isLoading ? <Loading /> : <></>}
            <section className="w-full h-full text-[#353535]">
                <HeaderBar />
                <div className="flex flex-col w-full h-[93%] overflow-y-scroll py-vw px-3vw">
                    {/* 이미지 */}
                    <div className="w-full h-[20%] flex overflow-x-auto mb-2vw">
                        {placeInfo.placePhotoList.map((original, index) => {
                            return (
                                <img
                                    crossOrigin="anonymous"
                                    className=" h-full flex-shrink-0 px-dot5vw border-rad-2vw"
                                    src={urlList[index]}
                                    key={index}
                                ></img>
                            );
                        })}
                    </div>

                    {/* 장소설명 */}
                    <div className="w-full text-7vw flex justify-between items-center mb-vw px-2vw">
                        <span className="weight-text-semibold">
                            {placeInfo.placeName}
                        </span>
                        {tourDetail?.isSelected ? (
                            <div
                                className="w-[20%] h-7vw text-4vw border-rad-2dot5vw color-bg-green-1 color-text-green-2 color-border-green-2 border-halfvw flex justify-center items-center weight-text-semibold"
                                onClick={() => {
                                    //여행 삭제
                                    if (wsClient)
                                        wsClient.publish({
                                            destination: `/app/place/${tourId}`,
                                            body: JSON.stringify({
                                                type: "DELETE_PLACE",
                                                body: {
                                                    tourId: tourId,
                                                    placeId:
                                                        tourDetail?.placeInfo
                                                            .placeId,
                                                    placeName:
                                                        tourDetail?.placeInfo
                                                            .placeName,
                                                    tourDay: tourDay,
                                                },
                                            }),
                                        });
                                }}
                            >
                                추가됨
                            </div>
                        ) : (
                            <div
                                className="w-[20%] h-7vw text-4vw border-rad-2dot5vw bg-white border-halfvw color-border-blue-6 color-text-blue-6 text-white flex justify-center items-center weight-text-semibold"
                                onClick={() => {
                                    //추가하는 요청 전송
                                    if (wsClient) {
                                        setIsLoading(true);

                                        wsClient.publish({
                                            destination: `/app/place/${tourId}`,
                                            body: JSON.stringify({
                                                type: "ADD_PLACE",
                                                body: {
                                                    tourId: tourId,
                                                    placeId:
                                                        tourDetail?.placeInfo
                                                            .placeId,
                                                    placeName:
                                                        tourDetail?.placeInfo
                                                            .placeName,
                                                    tourDay: tourDay,
                                                },
                                            }),
                                        });
                                    }
                                }}
                            >
                                추가
                            </div>
                        )}
                    </div>
                    <div className="w-full text-4vw mb-vw flex justify-start items-center px-vw ">
                        <PlaceMapIcon className="mr-vw w-5vw " />
                        <span>{placeInfo.placeAddress}</span>
                    </div>
                    <div className="w-full h-dot5vw bg-[#828282] mb-2vw"></div>

                    {/* 활동목록 */}
                    {tourDetail?.isSelected ? (
                        // 선택 되었으면 전체 일정 보여주고 수정 가능하게
                        <>
                            {thisPlaceVisit.map((place) => {
                                return (
                                    <div
                                        className="w-full text-6vw flex items-center"
                                        key={place.tourDay}
                                    >
                                        <div
                                            className="w-[6%] flex justify-center items-center"
                                            onClick={() => {
                                                //삭제 요청
                                                if (wsClient) {
                                                    setIsLoading(true);

                                                    wsClient.publish({
                                                        destination: `/app/place/${tourId}`,
                                                        body: JSON.stringify({
                                                            type: "DELETE_PLACE",
                                                            body: {
                                                                tourId: tourId,
                                                                placeId:
                                                                    place.placeId,
                                                                placeName:
                                                                    place.placeName,
                                                                tourDay:
                                                                    place.tourDay,
                                                            },
                                                        }),
                                                    });
                                                }
                                            }}
                                        >
                                            <CalcelIcon />
                                        </div>
                                        <div
                                            className="w-[50%] text-4vw border-[#B5B5B5] border-dot3vw flex justify-between p-vw m-vw border-rad-2vw"
                                            onClick={() => {
                                                setSelectedDate(place.tourDay);
                                                setIsDayChange(true);
                                            }}
                                        >
                                            <span>
                                                {dateToString(place.tourDay)}
                                            </span>
                                            <span className="material-symbols-outlined">
                                                calendar_today
                                            </span>
                                        </div>
                                        {place.activityList.length >= 1 &&
                                            place.activityList[0] && (
                                                <div className="w-[20%] text-4vw color-bg-blue-6 text-white color-border-blue-6 border-halfvw border-rad-2vw mr-1 py-dot5vw flex justify-center items-center">
                                                    {place.activityList.length >
                                                    1
                                                        ? place
                                                              .activityList[0] +
                                                          "+" +
                                                          (place.activityList
                                                              .length -
                                                              1)
                                                        : place.activityList[0]}
                                                </div>
                                            )}

                                        <div
                                            className="w-[20%] text-4vw color-text-blue-6 color-border-blue-6 border-halfvw border-rad-2vw px-vw  py-dot5vw flex justify-center items-center border-dotted"
                                            onClick={() => {
                                                //활동 추가 로직
                                                // console.log(place);
                                                setSelectedSchedule(place);
                                                setIsActivityModal(true);
                                            }}
                                        >
                                            +
                                        </div>
                                    </div>
                                );
                            })}
                        </>
                    ) : (
                        // 안되어 있으면 추가부터 하도록
                        <>
                            <div className="text-5vw weight-text-semibold">
                                이 장소에서 추천하는 활동
                            </div>
                            <div className="flex items-center text-5vw color-text-blue-6 weight-text-semibold">
                                {activityList.length > 0 ? (
                                    <span className="h-7vw px-3vw border-halfvw color-border-blue-6 border-rad-2dot5vw mr-2vw flex justify-center items-center">
                                        {activityList[0]}
                                    </span>
                                ) : (
                                    <></>
                                )}
                                {activityList.length > 1 ? (
                                    <span className="h-7vw px-3vw border-halfvw color-border-blue-6 border-rad-2dot5vw mr-2vw flex justify-center items-center">
                                        {activityList[1]}
                                    </span>
                                ) : (
                                    <></>
                                )}
                                {activityList.length > 2 ? (
                                    <span className="h-7vw px-3vw border-halfvw color-border-blue-6 border-rad-2dot5vw mr-2vw flex justify-center items-center">
                                        {activityList[2]}
                                    </span>
                                ) : (
                                    <></>
                                )}
                            </div>
                        </>
                    )}
                    <div className="w-full h-dot5vw bg-[#828282] my-2vw"></div>

                    {/* 장소 상세 정보 */}
                    <div className="flex flex-col items-start text-lg mb-2vw">
                        {/* 영업중 / 영업종료 */}
                        <div className="flex weight-text-semibold items-center mb-vw">
                            {placeInfo.placeOpenNow === null ? (
                                <></>
                            ) : placeInfo.placeOpenNow ? (
                                <>
                                    <ClockIcon className="w-5vw mr-vw pb-1" />
                                    <span className="text-[#28a71d]">
                                        영업 중
                                    </span>
                                </>
                            ) : (
                                <>
                                    <ClockIcon className="w-5vw mr-vw pb-1" />
                                    <span className="text-[#fa5d3b]">
                                        영업 종료
                                    </span>
                                </>
                            )}
                        </div>
                        {/* 영업 시간 */}
                        <div
                            className="flex flex-col text-lg"
                            style={{
                                wordSpacing: "min(1vw, 4.8px)",
                            }}
                        >
                            {placeInfo.placeWeekdayDescriptions ? (
                                placeInfo.placeWeekdayDescriptions.map(
                                    (weekInfo) => {
                                        return (
                                            <div className="mb-vw pl-6vw">
                                                {weekInfo}
                                            </div>
                                        );
                                    }
                                )
                            ) : (
                                <></>
                            )}
                        </div>
                        {/* 카드와 현금 */}
                        {placeInfo.placeAcceptCreditCards === null ? (
                            <></>
                        ) : (
                            <div className="flex weight-text-semibold items-center my-vw">
                                <CreditCardIcon className="w-5vw mr-vw" />
                                <div className="weight-text-semibold">
                                    카드 결제 :
                                    <>
                                        {placeInfo.placeAcceptCreditCards ? (
                                            <span> 가능</span>
                                        ) : (
                                            <span> 불가능</span>
                                        )}
                                    </>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
                <TabBarTour tourId={tourId} tourMode={2} type="schedule" />
                <WebSocket
                    tourId={tourId}
                    setWsClient={setWsClient}
                    update={update}
                />
            </section>
        </>
    );
}
