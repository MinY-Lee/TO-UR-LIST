import TabBarTour from '../../components/TabBar/TabBarTour';
import DaySelectBar from '../../components/SchedulePage/DaySelectBar';
import tourInfo from '../../dummy-data/get_tour_tourId.json';

export default function TourSchedulePage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 2];

    return (
        <>
            <h1>여행 일정 페이지</h1>
            <p>여행 일정을 정하는 페이지입니다.</p>
            <p>id : {tourId}</p>
            <DaySelectBar />
            <TabBarTour tabMode={2} tourMode={2} tourId={tourId} />
        </>
    );
}
