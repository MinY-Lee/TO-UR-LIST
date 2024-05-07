import MyInfoCard from '../../components/MyPage/MyInfoCard';
import TabBarMain from '../../components/TabBar/TabBarMain';

import { useEffect, useState } from 'react';
import { Feed, UserInfo } from '../../types/types';
import FeedCard from '../../components/FeedPage/FeedCard';
import { useNavigate } from 'react-router';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import { getUserInfo, withdraw } from '../../util/api/user';
import { useDispatch } from 'react-redux';
import { userWholeState } from '../../util/reduxSlices/userSlice';
//dummy data
import publishedFeed from '../../dummy-data/get_feed_published.json';
import likedFeed from '../../dummy-data/get_feed_liked.json';
import CheckModal from '../../components/CheckModal';
import { httpStatusCode } from '../../util/api/http-status';
import { Cookies } from 'react-cookie';

export default function MyPage() {
    const [myPublishList, setMyPublishList] = useState<Feed[]>([]);
    const [myLikedList, setMyLikedList] = useState<Feed[]>([]);
    const [user, setUser] = useState<UserInfo>({
        userId: '',
        userNickname: '',
        userName: '',
        userBirth: '',
        userGender: 0,
    });

    const cookies = new Cookies();

    const [isWithdrawalProceeding, setIsWithdrawalProceeding] =
        useState<boolean>(false);

    const navigate = useNavigate();
    const dispatch = useDispatch();

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

                dispatch(userWholeState(userInfo));
            })
            .catch((err) => {
                if (err.response.status === 400) {
                    window.location.href = '/info';
                }
            });
    }, []);

    useEffect(() => {
        //최신순 정렬
        publishedFeed.sort((a, b) => {
            const dateA = new Date(a.createdAt);
            const dateB = new Date(b.createdAt);
            return dateB.getTime() - dateA.getTime();
        });
        likedFeed.sort((a, b) => {
            const dateA = new Date(a.createdAt);
            const dateB = new Date(b.createdAt);
            return dateB.getTime() - dateA.getTime();
        });

        setMyPublishList(publishedFeed);
        setMyLikedList(likedFeed);
    }, [user]);

    const proceedWithDrawal = () => {
        setIsWithdrawalProceeding(true);
    };

    const continueWithdraw = () => {
        withdraw().then((res) => {
            if (res.status === httpStatusCode.OK) {
                setIsWithdrawalProceeding(false);
                cookies.remove('accessToken', { path: '/' });
                window.location.href = '/';
            }
        });
    };

    const cancelWithdraw = () => {
        setIsWithdrawalProceeding(false);
    };

    return (
        <>
            {isWithdrawalProceeding ? (
                <CheckModal
                    mainText="회원탈퇴 하시겠습니까?"
                    subText="탈퇴 후 정보는 복구할 수 없습니다."
                    OKText="회원탈퇴"
                    CancelText="취소"
                    clickOK={continueWithdraw}
                    clickCancel={cancelWithdraw}
                />
            ) : (
                <></>
            )}
            <section className="w-full h-[90%] py-[1vw] flex flex-col items-center flex-grow-0 flex-shrink-0 overflow-y-scroll">
                <HeaderBar />
                <MyInfoCard />
                <div className="w-[90%] border-b-[0.5vw] border-b-[#7E7E7E] flex justify-between items-end py-[1vw] weight-text-semibold">
                    <p className="text-[6vw] color-bg-blue-4 px-[0.5vw]">
                        내가 게시한 여행
                    </p>
                    <p
                        className="text-[5vw]"
                        onClick={() => {
                            navigate(`/mypage/myfeed`, {
                                state: myPublishList,
                            });
                        }}
                    >
                        더보기
                    </p>
                </div>
                {myPublishList.map((feed, index) => {
                    if (index > 1) {
                        return <></>;
                    }
                    return <FeedCard feedInfo={feed} key={feed.feedId} />;
                })}
                <div className="w-[90%] mt-[2vw] border-b-[0.5vw] border-b-[#7E7E7E] flex justify-between items-end py-[1vw] weight-text-semibold">
                    <p className="text-[6vw] color-bg-blue-4 px-[0.5vw]">
                        내가 좋아요한 여행
                    </p>
                    <p
                        className="text-[5vw]"
                        onClick={() => {
                            navigate(`/mypage/like`, {
                                state: myLikedList,
                            });
                        }}
                    >
                        더보기
                    </p>
                </div>
                {myLikedList.map((feed, index) => {
                    if (index > 1) {
                        return <></>;
                    }
                    return <FeedCard feedInfo={feed} key={feed.feedId} />;
                })}
                <p
                    className="text-red-400 underline mt-[1vw]"
                    onClick={proceedWithDrawal}
                >
                    회원탈퇴
                </p>
                <TabBarMain tabMode={2} />
            </section>
        </>
    );
}
