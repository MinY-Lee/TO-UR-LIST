import MyInfoCard from "../../components/MyPage/MyInfoCard";
import TabBarMain from "../../components/TabBar/TabBarMain";

import { useEffect, useState } from "react";
import { Feed, UserInfo } from "../../types/types";
import FeedCard from "../../components/FeedPage/FeedCard";
import { useNavigate } from "react-router";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import { getUserInfo, withdraw } from "../../util/api/user";
import { useDispatch } from "react-redux";
import { userWholeState } from "../../util/reduxSlices/userSlice";

import CheckModal from "../../components/CheckModal";
import { httpStatusCode } from "../../util/api/http-status";
import { Cookies } from "react-cookie";
import { getLikedFeed, getPublishedFeed } from "../../util/api/feed";
import Loading from "../../components/Loading";

export default function MyPage() {
    const [myPublishList, setMyPublishList] = useState<Feed[]>([]);
    const [myLikedList, setMyLikedList] = useState<Feed[]>([]);
    const [user, setUser] = useState<UserInfo>({
        userId: "",
        userNickname: "",
        userName: "",
        userBirth: "",
        userGender: 0,
    });

    const [isLoading, setIsLoading] = useState<boolean>(false);

    const cookies = new Cookies();

    const [isWithdrawalProceeding, setIsWithdrawalProceeding] =
        useState<boolean>(false);

    const navigate = useNavigate();
    const dispatch = useDispatch();

    //유저 정보 불러오기
    //redux에 저장 + main페이지에서는 새로 불러오기
    useEffect(() => {
        setIsLoading(true);
        //체크
        getUserInfo()
            .then((res) => {
                // console.log(res);

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
                userInfo.userBirth = res.data.userBirth.split("T")[0];
                userInfo.userGender = res.data.userGender;
                setUser(userInfo);

                dispatch(userWholeState(userInfo));
            })
            .catch((err) => {
                if (err.response.status === 400) {
                    window.location.href = "/info";
                }
            })
            .finally(() => {
                setIsLoading(false);
            });
    }, []);

    // useEffect(() => {
        //최신순 정렬
        // getPublishedFeed().then((res) => {
        //     if (res.status === httpStatusCode.OK) {
        //         const publishedFeed: Feed[] = res.data;
        //         publishedFeed.sort((a, b) => {
        //             const dateA = new Date(a.createdAt);
        //             const dateB = new Date(b.createdAt);
        //             return dateB.getTime() - dateA.getTime();
        //         });
        //
        //         setMyPublishList(publishedFeed);
        //     }
        // });

        // getLikedFeed().then((res) => {
        //     if (res.status === httpStatusCode.OK) {
        //         const likedFeed: Feed[] = res.data;
        //         likedFeed.sort((a, b) => {
        //             const dateA = new Date(a.createdAt);
        //             const dateB = new Date(b.createdAt);
        //             return dateB.getTime() - dateA.getTime();
        //         });
        //
        //         setMyLikedList(likedFeed);
        //     }
        // });
    // }, [user]);

    const proceedWithDrawal = () => {
        setIsWithdrawalProceeding(true);
    };

    const continueWithdraw = () => {
        withdraw().then((res) => {
            if (res.status === httpStatusCode.OK) {
                setIsWithdrawalProceeding(false);
                cookies.remove("accessToken", { path: "/" });

                //delete service worker
                if ("serviceWorker" in navigator) {
                    navigator.serviceWorker.ready
                        .then((registration) => {
                            registration.unregister();
                        })
                        .catch((err) => {
                            console.log(err);
                        });
                }

                window.location.href = "/";
            }
        });
    };

    const cancelWithdraw = () => {
        setIsWithdrawalProceeding(false);
    };

    return (
        <>
            {isLoading ? <Loading /> : <></>}
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
            <section className="w-full h-[90%] py-vw flex flex-col items-center flex-grow-0 flex-shrink-0 overflow-y-scroll">
                <HeaderBar />
                <MyInfoCard />
                <div className="w-[90%] border-b-dot5vw border-b-[#7E7E7E] flex justify-between items-end py-vw weight-text-semibold">
                    <p className="text-6vw color-bg-blue-4 px-dot5vw">
                        내가 게시한 여행
                    </p>
                    <p
                        className="text-5vw cursor-pointer"
                        onClick={() => {
                            navigate(`/mypage/myfeed`, {
                                state: myPublishList,
                            });
                        }}
                    >
                        더보기
                    </p>
                </div>
                {myPublishList.length > 0 ? (
                    myPublishList.map((feed, index) => {
                        if (index > 1) {
                            return <></>;
                        }
                        return <FeedCard feedInfo={feed} key={feed.feedId} />;
                    })
                ) : (
                    <div className="w-full h-[10%] flex justify-center items-center text-4vw text-[#aeaeae]">
                        게시한 피드가 없습니다.
                    </div>
                )}
                <div className="w-[90%] mt-2vw border-b-dot5vw border-b-[#7E7E7E] flex justify-between items-end py-vw weight-text-semibold">
                    <p className="text-6vw color-bg-blue-4 px-dot5vw">
                        내가 좋아요한 여행
                    </p>
                    <p
                        className="text-5vw cursor-pointer"
                        onClick={() => {
                            navigate(`/mypage/like`, {
                                state: myLikedList,
                            });
                        }}
                    >
                        더보기
                    </p>
                </div>
                {myLikedList.length > 0 ? (
                    myLikedList.map((feed, index) => {
                        if (index > 1) {
                            return <></>;
                        }
                        return <FeedCard feedInfo={feed} key={feed.feedId} />;
                    })
                ) : (
                    <div className="w-full h-[10%] flex justify-center items-center text-4vw text-[#aeaeae]">
                        좋아요한 피드가 없습니다.
                    </div>
                )}
                <p
                    className="text-red-400 underline mt-vw cursor-pointer"
                    onClick={proceedWithDrawal}
                >
                    회원탈퇴
                </p>
                <TabBarMain tabMode={2} type="mypage" />
            </section>
        </>
    );
}
