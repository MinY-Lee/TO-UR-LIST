import { useEffect, useState } from "react";

import HeaderBar from "../../components/HeaderBar/HeaderBar";
import TabBarTour from "../../components/TabBar/TabBarTour";

import AccountAddModify from "../../components/AccountPage/accountAddModify";
import { AccountInfo, TourInfoDetail } from "../../types/types";

import { getAccount, getAccountList } from "../../util/api/pay";
import { getTour } from "../../util/api/tour";
import { useLocation } from "react-router-dom";

export default function AccountAddPage() {
    const location = useLocation();

    const [tourId, setTourId] = useState<string>("");
    const [payId, setPayId] = useState<string>("");
    const [data, setData] = useState<AccountInfo>({
        payId: "",
        payType: "",
        tourId: "",
        payAmount: 0,
        exchangeRate: 0,
        unit: "",
        currencyCode: "",
        payMethod: "",
        payDatetime: "",
        payContent: "",
        payCategory: "",
        payerId: "",
        payMemberList: [],
    });
    const [tourData, setTourData] = useState<TourInfoDetail>({
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: [],
    });

    useEffect(() => {
        console.log(location.state);
        // 투어 아이디 및 payId 불러오기
        const address: string[] = window.location.href.split("/");
        setTourId(address[address.length - 3]);
        setPayId(address[address.length - 1]);

        // 데이터 불러오기
        if (tourId != "" && location.state) {
            getTour(tourId)
                .then((res) => {
                    setTourData(res.data);
                })
                .catch((err) => console.log(err));

            getAccount(payId, tourId, location.state.item.payType)
                .then((res) => {
                    console.log(res.data);
                    setData(res.data);
                })
                .catch((err) => console.log(err));
        }
    }, [tourId]);

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <AccountAddModify
                tourId={tourId}
                tourData={tourData}
                isModify={true}
                data={data}
            />
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} type="account" />
            </footer>
        </>
    );
}
