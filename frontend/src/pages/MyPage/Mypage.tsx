import MyInfoCard from '../../components/MyPage/MyInfoCard';
import TabBarMain from '../../components/TabBar/TabBarMain';

export default function MyPage() {
    return (
        <>
            <section className="w-full h-full p-[5vw] flex flex-col items-center">
                <MyInfoCard />
                <TabBarMain tabMode={2} />
            </section>
        </>
    );
}
