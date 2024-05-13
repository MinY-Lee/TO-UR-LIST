import { useLocation } from 'react-router-dom';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import { useCallback, useEffect, useState } from 'react';
import TabBarTour from '../../components/TabBar/TabBarTour';
import { TourActivity, TourEditDetail, TourPlaceItem } from '../../types/types';

//dummy data
import TourDetailInfo from '../../dummy-data/get_tour_place_tourId_tourDay_placeId.json';
import WholeSchedule from '../../dummy-data/get_tour_place_tourId.json';
import TourInfo from '../../dummy-data/get_tour_tourId.json';
import { searchPlaceDetail } from '../../util/api/place';

export default function PlaceAddDetailPage() {
    const location = useLocation();

    const [tourDay, setTourDay] = useState<number>(0);
    const [tourId, setTourId] = useState<string>('');
    const [placeId, setPlaceId] = useState<string>('');

    console.log(location.state);

    /**state로부터 장소 정보 불러오기, 초기 세팅 */
    useEffect(() => {
        if (location.state) {
            setPlaceId(location.state.placeId);
            setTourDay(location.state.tourDay + 1);
            setTourId(location.state.tourId);
            /**장소 상세 조회 */
            searchPlaceDetail(
                location.state.tourId,
                location.state.tourDay + 1,
                location.state.placeId
            )
                .then((res) => {
                    console.log(res);
                })
                .catch((err) => {
                    console.log(err);
                });
        }
    }, []);

    return (
        <>
            <section className="w-full h-full">
                <div className="flex flex-col w-full h-[90%] overflow-y-scroll p-vw">
                    <HeaderBar />
                </div>
                <TabBarTour tourId={tourId} tourMode={2} />
            </section>
        </>
    );
}
