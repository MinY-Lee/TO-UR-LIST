import { useEffect, useState } from 'react';

import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';

import AccountAddModify from '../../components/AccountPage/accountAddModify';
import { TourInfoDetail } from '../../types/types';
import { getTour } from '../../util/api/tour';

export default function AccountAddPage() {
    const [tourId, setTourId] = useState<string>('');
    const [tourData, setTourData] = useState<TourInfoDetail>({
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
        memberList: [],
    });

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 3]);

        getTour(tourId)
            .then((res) => {
                setTourData({
                    tourTitle: res.data.tourTitle,
                    cityList: res.data.cityList,
                    startDate: res.data.startDate,
                    endDate: res.data.endDate,
                    memberList: res.data.memberList,
                });
            })
            .catch((err) => console.log(err));
    }, [tourId]);

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <AccountAddModify tourId={tourId} tourData={tourData} isModify={false} />
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} />
            </footer>
        </>
    );
}
