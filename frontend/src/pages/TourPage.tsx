import { useEffect, useState } from 'react';
import HeaderBar from '../components/HeaderBar/HeaderBar';
import TourHeader from '../components/TourPage/TourHeader';
import TourBasicInfo from '../components/TourPage/TourBasicInfo';
import TourCheckList from '../components/TourPage/TourChecklist';

import { TourInfoDetail } from '../types/types';
// import TourDetail from '../dummy-data/get_tour_detail.json';
import TabBarTour from '../components/TabBar/TabBarTour';
import TourEditHeader from '../components/TourPage/TourEditHeader';
import { getTour } from '../util/api/tour';

export default function TourPage() {
    const [type, setType] = useState<string>('');
    const [flag, setFlag] = useState<boolean>(false); // 여행정보 변화 감지 플래그
    const [tourId, setTourId] = useState<string>('');
    const [data, setData] = useState<TourInfoDetail>({
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
        memberList: [],
    });

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 1]);

        // 데이터 불러오기
        if (tourId) {
            getTour(tourId)
                .then((res) => {
                    console.log(res.data.cityList);
                    setData({
                        tourTitle: res.data.tourTitle,
                        cityList: res.data.cityList,
                        startDate: res.data.startDate.split('T')[0],
                        endDate: res.data.endDate.split('T')[0],
                        memberList: res.data.memberList,
                    });
                })
                .catch((err) => {
                    console.log(err);
                });

            setType('default');
            setFlag(false);
        }
    }, [tourId, flag]);

    const onChange = (type: string) => {
        setType(type);
    };

    const onUpdate = (flag: boolean) => {
        setFlag(flag);
    };

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <div className="h-[85vh] overflow-y-scroll">
                <div>
                    {type == 'default' ? (
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
                <div>
                    <TourCheckList tourId={tourId} />
                </div>
            </div>
            <footer>
                <TabBarTour tourMode={0} tourId={tourId} />
            </footer>
        </>
    );
}
