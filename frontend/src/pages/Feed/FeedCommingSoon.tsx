import HeaderBar from "../../components/HeaderBar/HeaderBar";
import TabBarMain from "../../components/TabBar/TabBarMain";

export default function FeedComingSoon() {
    return (
        <>
            <HeaderBar />
            <section className="w-full h-[93%] flex flex-col justify-center items-center">
                <div className="text-7vw">
                    피드 기능은 추후 제공 예정입니다.
                </div>
                <TabBarMain tabMode={0} />
            </section>
        </>
    );
}
