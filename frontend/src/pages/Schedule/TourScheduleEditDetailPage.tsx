import { useLocation } from 'react-router-dom';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import { useEffect } from 'react';
import TabBarTour from '../../components/TabBar/TabBarTour';

export default function TourScheduleEditDetailPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 4];

    const location = useLocation();
    useEffect(() => {}, []);

    return (
        <>
            <section className="w-full h-full">
                <HeaderBar />
                <TabBarTour tourId={tourId} tourMode={2} />
            </section>
        </>
    );
}
