import { useEffect, useState } from 'react';

import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';

import AccountAddModify from '../../components/AccountPage/accountAddModify';
import { AccountInfo, TourInfoDetail } from '../../types/types';

import getAccountDetail from '../../dummy-data/get_pay_detail_payId.json';
import { getAccountList } from '../../util/api/pay';
import { getTour } from '../../util/api/tour';

export default function AccountAddPage() {
    const [tourId, setTourId] = useState<string>('');
    const [payId, setPayId] = useState<string>('');
    const [data, setData] = useState<AccountInfo>({
        payId: '',
        payType: '',
        tourId: '',
        payAmount: 0,
        unit: '',
        currencyCode: '',
        payMethod: '',
        payDatetime: '',
        payContent: '',
        payCategory: '',
        payerId: '',
        payMemberList: [],
    });
    const [tourData, setTourData] = useState<TourInfoDetail>({
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
        memberList: [],
    });

    useEffect(() => {
        // 투어 아이디 및 payId 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 3]);
        setPayId(address[address.length - 1]);

        // 데이터 불러오기
        if (tourId != '') {
            getAccountList(tourId)
                .then((res) => {
                    setData(res.data);
                })
                .catch((err) => console.log(err));
            getTour(tourId)
                .then((res) => {
                    setTourData(res.data);
                })
                .catch((err) => console.log(err));
        }
    }, [tourId]);

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <AccountAddModify tourId={tourId} tourData={tourData} isModify={true} data={data} />
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} />
            </footer>
        </>
    );
}
