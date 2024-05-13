import { useState } from 'react';
import FeedSearchBar from '../../components/FeedPage/FeedSearchBar';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarMain from '../../components/TabBar/TabBarMain';

export default function FeedPage() {
    const [isDetailActive, setIsDetailActive] = useState<boolean>(false);

    return (
        <>
            <section className="w-full h-full">
                <div className="w-full h-[90%] flex flex-col items-center overflow-y-scroll">
                    <HeaderBar />
                    <FeedSearchBar isDetailActive={isDetailActive} />

                    <div className="w-full p-[2vw] flex justify-between">
                        <div>정렬</div>
                        <div
                            className="text-5vw underline"
                            onClick={() => {
                                setIsDetailActive((prev) => !prev);
                            }}
                        >
                            고급검색
                        </div>
                    </div>
                </div>
                <TabBarMain tabMode={1} />
            </section>
        </>
    );
}
