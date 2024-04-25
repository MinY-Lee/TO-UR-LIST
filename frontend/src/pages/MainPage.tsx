import { useEffect, useState } from 'react';
import TourCard from '../components/MainPage/TourCard';
import TabBarMain from '../components/TabBar/TabBarMain';

//dummy data(api완료 시 삭제 요망)
import tourList from '../dummy-data/get_tour.json';
import { TourCardInfo } from '../types/types';

export default function MainPage() {
    const [nowTourList, setNowTourList] = useState<TourCardInfo[]>([]);
    const [comingTourList, setComingTourList] = useState<TourCardInfo[]>([]);
    const [passTourList, setPassTourList] = useState<TourCardInfo[]>([]);

    useEffect(() => {
        const tempNow: TourCardInfo[] = [];
        const tempCome: TourCardInfo[] = [];
        const tempPass: TourCardInfo[] = [];

        //startDate 빠른 순으로 정렬
        tourList.sort((a, b) => {
            const date1 = new Date(a.startDate);
            const date2 = new Date(b.startDate);
            return date1.getTime() - date2.getTime();
        });

        tourList.map((tour) => {
            const now = new Date();
            const startDate = new Date(tour.startDate);
            const endDate = new Date(tour.endDate);
            if (startDate > now) {
                //시작 날짜가 오늘 뒤 -> 아직 시작 안한 여행
                tempCome.push(tour);
            } else if (endDate < now) {
                //끝난 날짜가 오늘 이전 -> 지나간 여행
                tempPass.push(tour);
            } else {
                //나머지는 현재 진행중
                tempNow.push(tour);
            }
        });

        setNowTourList(tempNow);
        setComingTourList(tempCome);
        setPassTourList(tempPass);
    }, []);

    return (
        <section className="w-full h-[90%] overflow-y-scroll flex flex-col flex-nowrap items-center">
            <p className="w-[90%] text-[5vw] my-[0.5vh]">진행 중인 여행</p>
            {nowTourList.map((tour) => {
                return <TourCard key={tour.tourId} tourInfo={tour} />;
            })}
            <p className="w-[90%] text-[5vw] my-[0.5vh]">다가오는 여행</p>
            {comingTourList.map((tour) => {
                return <TourCard key={tour.tourId} tourInfo={tour} />;
            })}
            <p className="w-[90%] text-[5vw] my-[0.5vh]">지난 여행</p>
            {passTourList.map((tour) => {
                return <TourCard key={tour.tourId} tourInfo={tour} />;
            })}
            <TabBarMain />
        </section>
    );
}
