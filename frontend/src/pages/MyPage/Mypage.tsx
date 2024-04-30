import MyInfoCard from '../../components/MyPage/MyInfoCard';
import TabBarMain from '../../components/TabBar/TabBarMain';

import { useEffect, useState } from 'react';
import { Feed } from '../../types/types';

//dummy data
import publishedFeed from '../../dummy-data/get_feed_published.json';
import likedFeed from '../../dummy-data/get_feed_liked.json';
import FeedCard from '../../components/FeedPage/FeedCard';
import { useNavigate } from 'react-router';
import HeaderBar from '../../components/HeaderBar/HeaderBar';

export default function MyPage() {
    const [myPublishList, setMyPublishList] = useState<Feed[]>([]);
    const [myLikedList, setMyLikedList] = useState<Feed[]>([]);

    const navigate = useNavigate();

    useEffect(() => {
        //api요청 들어가야 함(일단 dummy data로 대체)

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
    }, []);

    return (
        <>
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
                <TabBarMain tabMode={2} />
            </section>
        </>
    );
}
