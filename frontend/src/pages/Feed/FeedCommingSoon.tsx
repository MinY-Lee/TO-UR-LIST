import HeaderBar from "../../components/HeaderBar/HeaderBar";
import TabBarMain from "../../components/TabBar/TabBarMain";

export default function FeedComingSoon() {
    return (
        <>
            <HeaderBar />
            <section className="w-full h-[80vh] flex flex-col justify-center items-center">
                <div className="text-12vw">👷‍♂️</div>
                <div className="text-5vw">/ \</div>
                <div className="text-5vw">
                    피드 게시판은 지금 공사중이에요 🚧
                </div>
                <TabBarMain tabMode={0} type="feed" />
            </section>
        </>
    );
}
