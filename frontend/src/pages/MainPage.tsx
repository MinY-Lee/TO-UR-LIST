import { useEffect, useState } from "react";
import TourCard from "../components/MainPage/TourCard";
import TabBarMain from "../components/TabBar/TabBarMain";

import { CountryMapping, TourCardInfo, UserInfo } from "../types/types";
import { useDispatch } from "react-redux";
import { userWholeState } from "../util/reduxSlices/userSlice";
import { getUserInfo } from "../util/api/user";
import { getMyTourList } from "../util/api/tour";
import { GetCountryList } from "../util/api/country";
import { HttpStatusCode } from "axios";
import Loading from "../components/Loading";

export default function MainPage() {
    const [nowTourList, setNowTourList] = useState<TourCardInfo[]>([]);
    const [comingTourList, setComingTourList] = useState<TourCardInfo[]>([]);
    const [passTourList, setPassTourList] = useState<TourCardInfo[]>([]);
    const [countryList, setCountryList] = useState<CountryMapping[]>([]);

    const [isLoading, setIsLoading] = useState<boolean>(false);

    const [today, setToday] = useState<string>("1111-11-11");

    const dispatch = useDispatch();

    const [user, setUser] = useState<UserInfo>({
        userId: "",
        userNickname: "",
        userName: "",
        userBirth: "",
        userGender: 0,
    });

    //유저 정보 불러오기
    //redux에 저장 + main페이지에서는 새로 불러오기
    useEffect(() => {
        setIsLoading(true);

        //체크
        getUserInfo()
            .then((res) => {
                // console.log(res);

                if (res.data.userId !== "") {
                    const userInfo: UserInfo = {
                        userId: "",
                        userNickname: "",
                        userName: "",
                        userBirth: "",
                        userGender: 0,
                    };
                    //불러와서 저장
                    userInfo.userId = res.data.userId;
                    userInfo.userNickname = res.data.userNickname;
                    userInfo.userName = res.data.userName;
                    if (res.data.userBirth) {
                        userInfo.userBirth = res.data.userBirth.split("T")[0];
                    }
                    userInfo.userGender = res.data.userGender;
                    // console.log(userInfo);

                    setUser(userInfo);
                    // if(user.userProfileImageId){
                    //     userInfo.userProfileImageId = user.userProfileImageId
                    // }
                    dispatch(userWholeState(userInfo));
                } else {
                    window.location.href = "/info";
                }
            })
            .catch((err) => {
                if (err.response) {
                    if (err.response.status === 400) {
                        window.location.href = "/info";
                    }
                }
            })
            .finally(() => {
                setIsLoading(false);
            });
    }, []);

    //시간 설정, 여행 정보 불러오기
    useEffect(() => {
        getMyTourList().then((res) => {
            const tourList: TourCardInfo[] = res.data;

            const tempNow: TourCardInfo[] = [];
            const tempCome: TourCardInfo[] = [];
            const tempPass: TourCardInfo[] = [];

            //startDate 빠른 순으로 정렬
            tourList.sort((a, b) => {
                const date1 = new Date(a.startDate);
                const date2 = new Date(b.startDate);
                return date1.getTime() - date2.getTime();
            });

            tourList.map((tour) => {
                const nowTime = new Date();

                const now = new Date(
                    `${nowTime.getFullYear()}-${
                        nowTime.getMonth() + 1
                    }-${nowTime.getDate()}`
                );

                const startDate = new Date(tour.startDate);
                const endDate = new Date(tour.endDate);
                if (startDate > now) {
                    //시작 날짜가 오늘 뒤 -> 아직 시작 안한 여행
                    tempCome.push(tour);
                } else if (endDate < now) {
                    //끝난 날짜가 오늘 이전 -> 지나간 여행
                    tempPass.push(tour);
                } else {
                    //나머지는 현재 진행중
                    tempNow.push(tour);
                }
            });

            setNowTourList(tempNow);
            setComingTourList(tempCome);
            setPassTourList(tempPass);

            //set now date
            const today = new Date();
            setToday(dateToString(today));
        });
    }, []);

    // 나라 코드 -> 나라 이름 매핑 위해 데이터 불러오기
    useEffect(() => {
        GetCountryList()
            .then((res) => {
                if (res.status === HttpStatusCode.Ok) {
                    setCountryList(res.data);
                }
            })
            .catch((err) => {
                console.log(err);
            });
    }, []);

    /**date -> YYYY-MM-DD */
    const dateToString = (val: Date) => {
        const year = val.getFullYear();
        const month = val.getMonth() + 1;
        const day = val.getDate();
        return `${year}-${month >= 10 ? month : "0" + month}-${
            day >= 10 ? day : "0" + day
        }`;
    };

    return (
        <section className="w-full h-[90%] overflow-y-scroll flex flex-col flex-nowrap items-center">
            {isLoading ? <Loading /> : <></>}
            <div className="w-[90%] h-35vw flex items-center justify-between py-2vw ">
                <div className="text-6vw h-full flex flex-col justify-center items-start">
                    <p>
                        <span className="text-7vw weight-text-semibold mr-vw">
                            {user.userNickname}
                        </span>
                        님의
                    </p>
                    <p>TO-UR-LIST</p>
                </div>
                <div className="text-[#5B5B5B] text-4vw h-full flex flex-col justify-center items-end">
                    <p>TODAY</p>
                    <p>{today}</p>
                </div>
            </div>
            {nowTourList.length > 0 ? (
                <>
                    <p className="w-[90%] text-5vw my-vw">진행 중인 여행</p>
                    <div className="w-full flex flex-col gap-2 items-center">
                        {nowTourList.map((tour) => {
                            return (
                                <TourCard
                                    key={tour.tourId}
                                    tourInfo={tour}
                                    countryList={countryList}
                                />
                            );
                        })}
                    </div>
                </>
            ) : (
                <div></div>
            )}
            {comingTourList.length > 0 ? (
                <>
                    <p className="w-[90%] text-5vw my-vw">다가오는 여행</p>
                    <div className="w-full flex flex-col gap-2 items-center">
                        {comingTourList.map((tour) => {
                            return (
                                <TourCard
                                    key={tour.tourId}
                                    tourInfo={tour}
                                    countryList={countryList}
                                />
                            );
                        })}
                    </div>
                </>
            ) : (
                <></>
            )}
            {passTourList.length > 0 ? (
                <>
                    <p className="w-[90%] text-5vw my-vw">지난 여행</p>
                    <div className="w-full flex flex-col gap-2 items-center">
                        {passTourList.map((tour) => {
                            return (
                                <TourCard
                                    key={tour.tourId}
                                    tourInfo={tour}
                                    countryList={countryList}
                                />
                            );
                        })}
                    </div>
                </>
            ) : (
                <></>
            )}

            {nowTourList.length == 0 &&
            comingTourList.length == 0 &&
            passTourList.length == 0 ? (
                <>
                    <div className="text-md h-[65vh] w-[90%] flex flex-col justify-center items-center border">
                        <div className="text-2xl">나의 여행이 없습니다.</div>
                        <div>아래 + 버튼을 눌러 여행을 추가해보세요!</div>
                    </div>
                </>
            ) : (
                <></>
            )}
            <div className="h-16"></div>
            <TabBarMain tabMode={1} type="main" />
        </section>
    );
}
