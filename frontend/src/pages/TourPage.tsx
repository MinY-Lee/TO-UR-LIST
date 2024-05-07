import { useEffect, useState } from "react";
import HeaderBar from "../components/HeaderBar/HeaderBar";
import TourHeader from "../components/TourPage/TourHeader";
import TourBasicInfo from "../components/TourPage/TourBasicInfo";
import TourCheckList from "../components/TourPage/TourChecklist";

import { TourInfoDetail } from "../types/types";
import TourDetail from '../dummy-data/get_tour_detail.json';
import TabBarTour from "../components/TabBar/TabBarTour";

export default function TourPage() {

    const [data, setData] = useState<TourInfoDetail>({
        tourId: "",
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: []
    });

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        const tourId: string = address[address.length - 1];
    
        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const tourData = TourDetail.find(tour => tour.tourId === tourId);
        if (tourData) {
            setData(tourData);
        }
        
    }, []);


    return (
        <>
            <header>
                <HeaderBar/>
            </header>
            <div className="h-[85vh] overflow-y-scroll">
                <div>
                    <TourHeader tourInfo={data}/>
                </div>
                <div>
                    <TourBasicInfo tourInfo={data}/>
                </div>
                 <div>
                    <TourCheckList tourId={data ? data.tourId : ""}/>
                </div>
            </div>
            <footer>
                <TabBarTour tourMode={0} tourId={data ? data.tourId : ""} />
            </footer>
            
        </>
    );
}
