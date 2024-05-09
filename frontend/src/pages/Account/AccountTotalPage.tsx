import { useEffect, useState } from 'react';

import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';

import AccountAddModify from '../../components/AccountPage/accountAddModify';

export default function AccountAddPage() {
    const [tourId, setTourId] = useState<string>('');
    const [tabIdx, setTabIdx] = useState<number>(1);

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 3]);
    }, [tourId]);

    const getTabClass = (idx: number) => {
        if (idx != tabIdx) {
            return '';
        }
        return 'border-transparent bg-gradient-to-t from-[#559bd9] to-[#94cef2] text-white';
    };

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <div className="flex flex-col items-center h-[80vh] p-5 overflow-y-scroll">
                {/* 원화 현지화폐 토글 */}
                <ul className="grid grid-cols-2 w-[30vw] border rounded-full color-bg-blue-4 mb-5">
                    <li className="rounded-full" onClick={() => setTabIdx(1)}>
                        <div
                            className={`${getTabClass(
                                1
                            )} rounded-full text-center block border-x-0 border-b-2 border-t-0 border-transparent leading-tight text-neutral-500 `}
                        >
                            원화
                        </div>
                    </li>
                    <li className="rounded-full" onClick={() => setTabIdx(2)}>
                        <div
                            className={`${getTabClass(
                                2
                            )} rounded-full text-center block border-x-0 border-b-2 border-t-0 border-transparent leading-tight text-neutral-500 `}
                        >
                            현지화폐
                        </div>
                    </li>
                </ul>
                <div className="flex flex-col w-full p-3 gap-10">
                    <div>
                        <div className="font-bold text-2xl">보낼 금액 💸</div>
                        <div className="border-t-2 border-neutral-500"></div>
                    </div>
                    <div>
                        <div className="font-bold text-2xl">받을 금액 💰</div>
                        <div className="border-t-2 border-neutral-500"></div>
                    </div>
                </div>
            </div>
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} />
            </footer>
        </>
    );
}
