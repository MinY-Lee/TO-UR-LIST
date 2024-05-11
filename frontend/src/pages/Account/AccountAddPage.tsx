import { useEffect, useState } from 'react';

import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';

import AccountAddModify from '../../components/AccountPage/accountAddModify';

export default function AccountAddPage() {
    const [tourId, setTourId] = useState<string>('');

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 3]);
    }, [tourId]);

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <AccountAddModify isModify={false} />
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} />
            </footer>
        </>
    );
}
