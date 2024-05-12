import FeedSearchBar from '../../components/FeedPage/FeedSearchBar';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarMain from '../../components/TabBar/TabBarMain';

export default function FeedPage() {
    return (
        <>
            <section className="w-full h-full">
                <div className="w-full h-[90%] flex flex-col items-center overflow-y-scroll">
                    <HeaderBar />
                    <FeedSearchBar />
                </div>
                <TabBarMain tabMode={1} />
            </section>
        </>
    );
}
