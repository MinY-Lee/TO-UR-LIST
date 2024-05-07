import { useEffect, useState } from 'react';
import TourCard from '../components/MainPage/TourCard';
import TabBarMain from '../components/TabBar/TabBarMain';

//dummy data(api완료 시 삭제 요망)
import tourList from '../dummy-data/get_tour.json';
// import user from '../dummy-data/get_user.json';

import { TourCardInfo, UserInfo } from '../types/types';
import { useDispatch } from 'react-redux';
import { userWholeState } from '../util/reduxSlices/userSlice';
import { getUserInfo } from '../util/api/user';

export default function MainPage() {
    const [nowTourList, setNowTourList] = useState<TourCardInfo[]>([]);
    const [comingTourList, setComingTourList] = useState<TourCardInfo[]>([]);
    const [passTourList, setPassTourList] = useState<TourCardInfo[]>([]);

    const [today, setToday] = useState<string>('1111-11-11');

    const dispatch = useDispatch();

    const [user, setUser] = useState<UserInfo>({
        userId: '',
        userNickname: '',
        userName: '',
        userBirth: '',
        userGender: 0,
    });

    //유저 정보 불러오기
    //redux에 저장 + main페이지에서는 새로 불러오기
    useEffect(() => {
        //체크
        getUserInfo()
            .then((res) => {
                console.log(res);

                const userInfo: UserInfo = {
                    userId: '',
                    userNickname: '',
                    userName: '',
                    userBirth: '',
                    userGender: 0,
                };
                //불러와서 저장
                userInfo.userId = res.data.userId;
                userInfo.userNickname = res.data.userNickname;
                userInfo.userName = res.data.userName;
                userInfo.userBirth = res.data.userBirth.split('T')[0];
                userInfo.userGender = res.data.userGender;

                setUser(userInfo);
                // if(user.userProfileImageId){
                //     userInfo.userProfileImageId = user.userProfileImageId
                // }
                dispatch(userWholeState(userInfo));
            })
            .catch((err) => {
                if (err.response.status === 400) {
                    window.location.href = '/info';
                }
            });
    }, []);

    //시간 설정, 여행 정보 불러오기
    useEffect(() => {
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
    }, []);

    /**date -> YYYY-MM-DD */
    const dateToString = (val: Date) => {
        const year = val.getFullYear();
        const month = val.getMonth() + 1;
        const day = val.getDate();
        return `${year}-${month >= 10 ? month : '0' + month}-${
            day >= 10 ? day : '0' + day
        }`;
    };

    return (
        <section className="w-full h-[90%] overflow-y-scroll flex flex-col flex-nowrap items-center">
            <div className="w-[90%] h-[30%] flex items-center justify-between py-[1vh]">
                <div className="text-[6vw] h-full flex flex-col justify-center items-start">
                    <p>
                        <span className="text-[7vw] weight-text-semibold mr-[1vw]">
                            {user.userNickname}
                        </span>
                        님의
                    </p>
                    <p>TO-UR-LIST</p>
                </div>
                <div className="text-[#5B5B5B] text-[4vw] h-full flex flex-col justify-center items-end">
                    <p>TODAY</p>
                    <p>{today}</p>
                </div>
            </div>
            <p className="w-[90%] text-[5vw] my-[0.5vh]">진행 중인 여행</p>
            {nowTourList.map((tour) => {
                return <TourCard key={tour.tourId} tourInfo={tour} />;
            })}
            <p className="w-[90%] text-[5vw] my-[0.5vh]">다가오는 여행</p>
            {comingTourList.map((tour) => {
                return <TourCard key={tour.tourId} tourInfo={tour} />;
            })}
            <p className="w-[90%] text-[5vw] my-[0.5vh]">지난 여행</p>
            {passTourList.map((tour) => {
                return <TourCard key={tour.tourId} tourInfo={tour} />;
            })}
            <TabBarMain tabMode={1} />
        </section>
    );
}
