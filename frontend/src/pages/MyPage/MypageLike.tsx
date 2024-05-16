import { useEffect, useState } from "react";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import TabBarMain from "../../components/TabBar/TabBarMain";
import { Feed } from "../../types/types";
import { useLocation } from "react-router";
import FeedCard from "../../components/FeedPage/FeedCard";
import { getLikedFeed } from "../../util/api/feed";
import { httpStatusCode } from "../../util/api/http-status";

export default function MypageLike() {
    const [myLikedList, setMyLikedList] = useState<Feed[]>([]);
    const { state } = useLocation();

    useEffect(() => {
        //넘어온 리스트 있으면 그걸로, 아니면 새로 api호출
        if (state.length !== 0) {
            setMyLikedList(state);
        } else {
            //api호출
            getLikedFeed().then((res) => {
                if (res.status === httpStatusCode.OK) {
                    const likedFeed: Feed[] = res.data;
                    likedFeed.sort((a, b) => {
                        const dateA = new Date(a.createdAt);
                        const dateB = new Date(b.createdAt);
                        return dateB.getTime() - dateA.getTime();
                    });

                    setMyLikedList(likedFeed);
                }
            });
        }
    }, []);

    return (
        <>
            <section className="w-full h-[90%] py-vw overflow-y-scroll flex flex-col items-center flex-grow-0 flex-shrink-0">
                <HeaderBar />
                <h1 className="text-7vw my-2vw w-[90%] weight-text-semibold">
                    내가 좋아요한 여행
                </h1>
                {myLikedList.length > 0 ? (
                    myLikedList.map((feed) => {
                        return <FeedCard feedInfo={feed} key={feed.feedId} />;
                    })
                ) : (
                    <div className="w-full h-[10%] flex justify-center items-center text-5vw text-[#aeaeae]">
                        좋아요한 피드가 없습니다.
                    </div>
                )}
            </section>
            <TabBarMain tabMode={2} type="feed" />
        </>
    );
}
