import { useLocation } from 'react-router-dom';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import { useCallback, useEffect, useState } from 'react';
import TabBarTour from '../../components/TabBar/TabBarTour';
import { TourActivity, TourEditDetail, TourPlaceItem } from '../../types/types';

//dummy data
import TourDetailInfo from '../../dummy-data/get_tour_place_tourId_tourDay_placeId.json';
import WholeSchedule from '../../dummy-data/get_tour_place_tourId.json';
import TourInfo from '../../dummy-data/get_tour_tourId.json';

export default function TourScheduleEditDetailPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 4];

    const [schedule, setSchedule] = useState<TourPlaceItem>();

    const [tourDetail, setTourDetail] = useState<TourEditDetail>();
    const [activityList, setActivityList] = useState<TourPlaceItem[]>([]);
    const [startDate, setStartDate] = useState<Date>(new Date());

    /**스케줄 정보 불러오기 */
    const location = useLocation();
    useEffect(() => {
        if (location.state && location.state.scheduleInfo) {
            setSchedule(location.state.scheduleInfo);
        }
    }, []);

    useEffect(() => {
        if (schedule) {
            //관련 api 호출
            setTourDetail(TourDetailInfo);

            const filtered = WholeSchedule.filter(
                (place) => place.placeId === schedule.placeId
            );

            setActivityList(filtered);

            const today = new Date(TourInfo.startDate);
            setStartDate(today);
        }
    }, [schedule]);

    const dateToString = useCallback(
        (day: number) => {
            if (day === -1) return '날짜 없음';
            //ms
            const date = new Date(startDate.getTime() + day * 86400000);

            return `${date.getFullYear()}.${
                date.getMonth() + 1 >= 10
                    ? date.getMonth() + 1
                    : '0' + (date.getMonth() + 1)
            }.${date.getDate() >= 10 ? date.getDate() : '0' + date.getDate()}`;
        },
        [startDate]
    );

    return (
        <>
            <section className="w-full h-full">
                <div className="flex flex-col w-full h-[90%] overflow-y-scroll p-[1vw]">
                    <HeaderBar />
                    {/* 이미지 */}
                    <div className="w-full h-[20%] flex overflow-x-scroll mb-[2vw]">
                        {tourDetail?.placeInfo.placePhotoList.map(
                            (photoUrl) => {
                                return (
                                    <img
                                        className="w-[33%] h-full flex-shrink-0 px-[0.5vw] rounded-[2vw]"
                                        src={`${photoUrl}`}
                                    ></img>
                                );
                            }
                        )}
                    </div>

                    {/* 장소설명 */}
                    <div className="w-full text-[7vw] flex justify-between items-center mb-[1vw]">
                        <span>{tourDetail?.placeInfo.placeName}</span>
                        {tourDetail?.isSelected ? (
                            <div className="w-[20%] h-[7vw] text-[5vw] rounded-[3.5vw] bg-white color-text-blue-2 color-border-blue-2 border-[0.5vw] flex justify-center items-center">
                                추가됨
                            </div>
                        ) : (
                            <div
                                className="w-[20%] h-[7vw] text-[5vw] rounded-[3.5vw] color-bg-blue-2 text-white flex justify-center items-center"
                                onClick={() => {
                                    //추가하는 요청 전송
                                }}
                            >
                                추가
                            </div>
                        )}
                    </div>
                    <div className="w-full text-[4vw] mb-[1vw]">
                        주소 : {tourDetail?.placeInfo.placeAddress}
                    </div>
                    <div className="w-full h-[1vw] bg-[#828282]"></div>

                    {/* 활동목록 */}
                    {activityList.length > 0 ? (
                        <div className="w-full flex flex-col mt-[3vw]">
                            <p className="text-[6vw]">날짜 및 활동</p>
                            <p className="text-[4vw] text-[#B5B5B5]">
                                활동 추가 시 활동에 필요한 준비물이 체크리스트에
                                추가돼요.
                            </p>
                            {activityList.map((activity) => {
                                return (
                                    <div className="w-full text-[6vw] flex items-center">
                                        <div
                                            className="w-[10%] flex justify-center items-center"
                                            onClick={() => {
                                                //삭제 요청
                                            }}
                                        >
                                            X
                                        </div>
                                        <div className="w-[50%] text-[4vw] border-[#B5B5B5] border-[0.3vw] flex justify-between p-[1vw] m-[1vw] rounded-[2vw]">
                                            <span>
                                                {dateToString(activity.tourDay)}
                                            </span>
                                            <span className="material-symbols-outlined">
                                                calendar_today
                                            </span>
                                        </div>
                                        <div className="w-[20%] text-[4vw] color-text-blue-2 color-border-blue-2 border-[0.5vw] rounded-[2vw] m-[1vw] flex justify-center items-center">
                                            {activity.tourActivityList.length >
                                            1
                                                ? activity.tourActivityList[0]
                                                      .activity +
                                                  '+' +
                                                  (activity.tourActivityList
                                                      .length -
                                                      1)
                                                : activity.tourActivityList
                                                      .length === 1
                                                ? activity.tourActivityList[0]
                                                      .activity
                                                : '활동없음'}
                                        </div>
                                        <div
                                            className="w-[20%] text-[4vw] color-text-blue-2 color-border-blue-2 border-[0.5vw] rounded-[2vw] px-[1vw] flex justify-center items-center border-dotted"
                                            onClick={() => {
                                                //활동 추가 로직
                                            }}
                                        >
                                            +
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    ) : (
                        <div className="w-full flex flex-col">
                            <p className="text-[4vw] text-[#B5B5B5]">
                                활동 추가 시 활동에 필요한 준비물이 체크리스트에
                                추가돼요.
                            </p>
                        </div>
                    )}
                </div>
                <TabBarTour tourId={tourId} tourMode={2} />
            </section>
        </>
    );
}
