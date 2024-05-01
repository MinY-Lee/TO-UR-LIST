import { useEffect, useState } from "react";
import HeaderBar from "../components/HeaderBar/HeaderBar";
import TourHeader from "../components/TourPage/TourHeader";
import TourBasicInfo from "../components/TourPage/TourBasicInfo";
import TourCheckList from "../components/TourPage/TourChecklist";

import { TourInfoDetail } from "../types/types";
import TourDetail from '../dummy-data/get_tour_detail.json';

export default function TourPage() {

    const [data, setData] = useState<TourInfoDetail>();

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        const tourId: string = address[address.length - 1];
    
        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const data = TourDetail.find(tour => tour.tourId === tourId);
        if (data) {
            setData(data);
        }
        
    }, []);


    return (
        <>
            <header>
                <HeaderBar/>
            </header>
            <div>
                <div>
                    <TourHeader tourInfo={data}/>
                </div>
                <div>
                    <TourBasicInfo tourInfo={data}/>
                </div>
                 <div>
                    <TourCheckList tourId={data?.tourId}/>
                </div>
            </div>
        </>
    );
}
