import { useState, useEffect } from "react";

import HeaderBar from "../../components/HeaderBar/HeaderBar";
import ChecklistTab from "../../components/Checklist/checklistTab";
import TourHeader from "../../components/TourPage/TourHeader";

import { TourInfoDetail } from "../../types/types";

import TourDetail from "../../dummy-data/get_tour_detail.json";
import TourBasicInfo from "../../components/TourPage/TourBasicInfo";
import TabBarTour from "../../components/TabBar/TabBarTour";
import { getTour } from "../../util/api/tour";
import { httpStatusCode } from "../../util/api/http-status";
import TourEditHeader from "../../components/TourPage/TourEditHeader";

export default function ChecklistPage() {
    const [data, setData] = useState<TourInfoDetail>({
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: [],
    });
    const [tourId, setTourId] = useState<string>("");
    const [type, setType] = useState<string>("default");
    const [flag, setFlag] = useState<boolean>(false); // 여행정보 변화 감지 플래그

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split("/");
        setTourId(address[address.length - 2]);

        if (tourId != "") {
            getTour(tourId)
                .then((res) => {
                    if (res.status === httpStatusCode.OK) {
                        setData({
                            tourTitle: res.data.tourTitle,
                            cityList: res.data.cityList,
                            startDate: res.data.startDate.split("T")[0],
                            endDate: res.data.endDate.split("T")[0],
                            memberList: res.data.memberList,
                        });
                    }
                })
                .catch((err) => console.log(err));
        }
    }, [tourId]);

    //헤더 타입 변환
    const onChange = (type: string) => {
        setType(type);
    };

    const onUpdate = (flag: boolean) => {
        setFlag(flag);
    };

    return (
        <div>
            <header>
                <HeaderBar />
            </header>
            <div className="h-[85vh] overflow-y-scroll">
                <div>
                    {type == "default" ? (
                        <TourHeader
                            tourId={tourId}
                            tourInfo={data}
                            onChange={(type) => onChange(type)}
                        />
                    ) : (
                        <TourEditHeader
                            tourId={tourId}
                            tourInfo={data}
                            onChange={(type) => onChange(type)}
                            onUpdate={onUpdate}
                        />
                    )}
                </div>
                <div>
                    <TourBasicInfo tourInfo={data} />
                </div>
                <div className="">
                    <ChecklistTab tourId={tourId} />
                </div>
            </div>
            <footer>
                <TabBarTour tourMode={1} tourId={tourId} type="checklist" />
            </footer>
        </div>
    );
}
