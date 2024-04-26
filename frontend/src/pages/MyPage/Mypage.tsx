import MyInfoCard from '../../components/MyPage/MyInfoCard';
import TabBarMain from '../../components/TabBar/TabBarMain';

import { useEffect, useState } from 'react';
import { Feed } from '../../types/types';

//dummy data
import publishedFeed from '../../dummy-data/get_feed_published.json';
import likedFeed from '../../dummy-data/get_feed_liked.json';
import FeedCard from '../../components/FeedPage/FeedCard';

export default function MyPage() {
    const [myPublishList, setMyPublishList] = useState<Feed[]>([]);
    const [myLikedList, setMyLikedList] = useState<Feed[]>([]);

    useEffect(() => {
        //api요청 들어가야 함(일단 dummy data로 대체)
        setMyPublishList(publishedFeed);
        setMyLikedList(likedFeed);
    }, []);

    return (
        <>
            <section className="w-full h-full p-[5vw] flex flex-col items-center">
                <MyInfoCard />
                {myPublishList.map((feed) => {
                    return <FeedCard feedInfo={feed} key={feed.feedId} />;
                })}
                {myLikedList.map((feed) => {
                    return <FeedCard feedInfo={feed} key={feed.feedId} />;
                })}
                <TabBarMain tabMode={2} />
            </section>
        </>
    );
}
