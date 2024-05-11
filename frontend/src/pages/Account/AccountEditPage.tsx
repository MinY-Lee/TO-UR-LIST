import { useEffect, useState } from 'react';

import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';

import AccountAddModify from '../../components/AccountPage/accountAddModify';
import { AccountInfo } from '../../types/types';

import getAccountDetail from '../../dummy-data/get_pay_detail_payId.json';

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

    useEffect(() => {
        // 투어 아이디 및 payId 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 3]);
        setPayId(address[address.length - 1]);

        // payId 로 디테일 가져와
        setData(getAccountDetail);
    }, [tourId]);

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <AccountAddModify isModify={true} data={data} />
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} />
            </footer>
        </>
    );
}
