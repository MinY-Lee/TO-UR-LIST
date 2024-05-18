import { useState, useEffect } from "react";
import HeaderBar from "../../components/HeaderBar/HeaderBar";

import {
    AccountInfo,
    CurrencyInfo,
    PayMember,
    TourInfoDetail,
    UserInfo,
} from "../../types/types";
import MyButton from "../../components/Buttons/myButton";
import TabBarTour from "../../components/TabBar/TabBarTour";
import AccountDetail from "../../components/AccountPage/accountDetail";

import { getAccountList, getCurrency } from "../../util/api/pay";
import { getCountry, getTour } from "../../util/api/tour";
import { useSelector } from "react-redux";

export default function AccountPage() {
    const [tourId, setTourId] = useState<string>("");
    const [data, setData] = useState<AccountInfo[]>([]);
    const [tourData, setTourData] = useState<TourInfoDetail>({
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: [],
    });
    const [currency, setCurrency] = useState<CurrencyInfo>({
        currencyCode: "",
        currencyRate: 0,
        unit: "",
    });

    const [tapIdx, setTabIdx] = useState<number>(1);

    const userInfo: UserInfo = useSelector((state: any) => state.userSlice);

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split("/");
        setTourId(address[address.length - 2]);

        // 데이터 불러오기
        if (tourId != "") {
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

    useEffect(() => {
        // 환율 정보 가져오기
        if (tourData.tourTitle != "") {
            getCurrency(
                tourData.cityList[0].countryCode,
                new Date().toISOString().split("T")[0]
            )
                .then((res) => {
                    setCurrency({
                        unit: res.data.unit,
                        currencyCode: res.data.currencyCode,
                        currencyRate: calcCurrency(res.data.currencyRate), // 1원당 가격 -> 화폐단위당 가격
                    });
                })
                .catch((err) => console.log(err));
        }
    }, [tourData]);

    const calcCurrency = (currencyRate: number) => {
        return Number((1 / currencyRate).toFixed(2));
    };

    const calcPrivateTotal = () => {
        let total = 0;
        // 개인 총 지출 계산
        data.forEach((item: AccountInfo) => {
            if (item.payType == "private" && item.payerId == userInfo.userId) {
                total += item.payAmount;
            }
        });

        return total;
    };

    const calcPublicTotal = () => {
        let total = 0;
        data.forEach((item: AccountInfo) => {
            if (item.payType == "public") {
                item.payMemberList.map((payMember: PayMember) => {
                    if (payMember.userId == userInfo.userId) {
                        total += payMember.payAmount;
                    }
                });
            }
        });

        return total;
    };

    const activeStyle = "font-bold color-bg-blue-4";

    const tapComponent = (index: number) => {
        if (index == 1) {
            return (
                <div>
                    <div className="text-xl">총 지출</div>
                    <div className="font-bold text-3xl">
                        {calcPrivateTotal().toLocaleString()} 원
                    </div>
                </div>
            );
        }
        if (index == 2) {
            return (
                <div>
                    <div className="text-xl">총 지출</div>
                    <div className="text-3xl font-bold">
                        {calcPublicTotal().toLocaleString()} 원
                    </div>
                    <MyButton
                        isSelected={true}
                        onClick={() => {
                            window.location.href = `/tour/${tourId}/account/total`;
                        }}
                        text="정산하기"
                        type="full"
                        className="font-medium py-1 mt-3 shadow-lg color-bg-blue-2 text-white"
                    />
                </div>
            );
        }
        if (index == 3) {
            return (
                <div>
                    <div className="text-xl">오늘의 환율</div>
                    <div className="font-bold text-2xl">
                        1{currency.unit} = {currency.currencyRate.toString()} 원
                    </div>
                    <div className="mt-3">
                        *지출 내역 클릭시 환율 상세 지정이 가능합니다.
                    </div>
                </div>
            );
        }
    };

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <div className="flex flex-col items-center h-[75vh] overflow-y-scroll">
                <div
                    id="tab-container"
                    className="w-[90%] flex flex-col items-center"
                >
                    <ul className="w-full grid grid-cols-3 text-sm text-center rounded-t-2xl">
                        <li
                            className="border-r-black"
                            onClick={() => setTabIdx(1)}
                        >
                            <div
                                className={`${
                                    tapIdx == 1 ? activeStyle : ""
                                } border-r-2 color-border-blue-4 inline-block w-full py-2  rounded-tl-lg`}
                            >
                                개인 지출
                            </div>
                        </li>
                        <li className="" onClick={() => setTabIdx(2)}>
                            <div
                                className={`${
                                    tapIdx == 2 ? activeStyle : ""
                                } inline-block w-full py-2`}
                            >
                                공동 지출
                            </div>
                        </li>
                        <li className="" onClick={() => setTabIdx(3)}>
                            <div
                                className={`${
                                    tapIdx == 3 ? activeStyle : ""
                                } border-l-2 color-border-blue-4 inline-block w-full py-2 rounded-tr-lg`}
                            >
                                환율
                            </div>
                        </li>
                    </ul>
                    <div className="w-full shadow-lg color-bg-blue-4 rounded-b-2xl">
                        <div className="p-8 text-center">
                            {tapComponent(tapIdx)}
                        </div>
                    </div>
                </div>
                <div className="w-full">
                    <AccountDetail data={data} tourData={tourData} />
                    <div className="h-[10vh]"></div>
                </div>

                <div className="w-[90%] absolute bottom-28">
                    <MyButton
                        isSelected={true}
                        onClick={() => {
                            window.location.href = `/tour/${tourId}/account/add`;
                        }}
                        text="기록하기"
                        type="full"
                        className="py-3 shadow-lg text-white text-xl"
                    />
                </div>
            </div>
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} type="account" />
            </footer>
        </>
    );
}
