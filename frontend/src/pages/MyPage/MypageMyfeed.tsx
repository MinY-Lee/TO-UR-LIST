import { useEffect, useState } from 'react';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarMain from '../../components/TabBar/TabBarMain';
import { Feed } from '../../types/types';
import { useLocation } from 'react-router';
import FeedCard from '../../components/FeedPage/FeedCard';

export default function MypageMyfeed() {
    const [myPublishList, setMyPublishList] = useState<Feed[]>([]);
    const { state } = useLocation();

    useEffect(() => {
        //넘어온 리스트 있으면 그걸로, 아니면 새로 api호출
        if (state.length !== 0) {
            setMyPublishList(state);
        } else {
            //api호출
        }
    }, []);

    return (
        <>
            <section className="w-full h-[90%] py-vw overflow-y-scroll flex flex-col items-center flex-grow-0 flex-shrink-0">
                <HeaderBar />
                <h1 className="text-7vw my-2vw w-[90%] weight-text-semibold">
                    내가 게시한 여행
                </h1>
                {myPublishList.map((feed) => {
                    return <FeedCard feedInfo={feed} key={feed.feedId} />;
                })}
            </section>
            <TabBarMain tabMode={2} />
        </>
    );
}
