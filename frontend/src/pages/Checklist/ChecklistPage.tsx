import { useState, useEffect } from "react";

import HeaderBar from "../../components/HeaderBar/HeaderBar";
import ChecklistTab from '../../components/Checklist/checklistTab'
import TourHeader from "../../components/TourPage/TourHeader";

import { TourInfoDetail } from "../../types/types";

import TourDetail from '../../dummy-data/get_tour_detail.json';
import TourBasicInfo from "../../components/TourPage/TourBasicInfo";
import TabBarTour from "../../components/TabBar/TabBarTour";
  
export default function ChecklistPage() {
    const [data, setData] = useState<TourInfoDetail>({
        tourId: "",
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: []
    });
    const [tourId, setTourId] = useState<string>("");

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 2]);
    
        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const tourData = TourDetail.find(tour => tour.tourId === tourId);
        if (tourData) {
            setData(tourData);
        }
        
    }, [tourId]);

    
    return (
        <div>
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
                <div className="">
                    <ChecklistTab tourId={tourId}/>
                    
                </div>
                
            </div>
            <footer>
                <TabBarTour tourMode={1} tourId={tourId} />
            </footer>
        </div>
    );
}
