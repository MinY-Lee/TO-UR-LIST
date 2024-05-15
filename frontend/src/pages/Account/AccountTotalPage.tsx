import { memo, useEffect, useState } from "react";

import HeaderBar from "../../components/HeaderBar/HeaderBar";
import TabBarTour from "../../components/TabBar/TabBarTour";

import getPayTourId from "../../dummy-data/get_pay_tourId.json";
import { AccountInfo, PayMember } from "../../types/types";
import { getAccountList } from "../../util/api/pay";
import { HttpStatusCode } from "axios";

interface MemberCharge {
    [memberId: string]: {
        payContent: string;
        payAmount: number;
    }[];
}

export default function AccountAddPage() {
    const [tourId, setTourId] = useState<string>("");
    const [tabIdx, setTabIdx] = useState<number>(1);
    const [data, setData] = useState<AccountInfo[]>([]);
    const [acceptData, setAcceptData] = useState<MemberCharge>({});
    const [sendData, setSendData] = useState<MemberCharge>({});

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split("/");
        setTourId(address[address.length - 3]);

        // 데이터 세팅
        if (tourId != "") {
            getAccountList(tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        setData(res.data);
                        // 받을 금액 판별
                        calcAccept();
                        // 보낼 금액 판별
                        calcSend();
                    }
                })
                .catch((err) => console.log(err));
        }
    }, [tourId]);

    const calcAccept = () => {
        let acceptList: MemberCharge = {};
        data.forEach((item) => {
            if (item.payType == "public" && item.payerId == "1234") {
                item.payMemberList.forEach((member: PayMember) => {
                    const memberId: string = member.userId;
                    if (memberId != "1234") {
                        if (!acceptList[memberId]) {
                            acceptList[memberId] = [];
                        }

                        let payAmountWon = 0;
                        if (item.currencyCode == "JPY") {
                            payAmountWon = member.payAmount * 8.9;
                        } else {
                            payAmountWon = member.payAmount;
                        }

                        acceptList[memberId].push({
                            payContent: item.payContent,
                            payAmount: payAmountWon,
                        });
                    }
                });
            }
        });

        setAcceptData(acceptList);
    };

    const calcSend = () => {
        let sendList: MemberCharge = {};
        data.forEach((item) => {
            if (item.payType == "public" && item.payerId != "1234") {
                item.payMemberList.forEach((member: PayMember) => {
                    const memberId: string = member.userId;
                    if (memberId == "1234") {
                        if (!sendList[memberId]) {
                            sendList[memberId] = [];
                        }

                        let payAmountWon = 0;
                        if (item.currencyCode == "JPY") {
                            payAmountWon = member.payAmount * 8.9;
                        } else {
                            payAmountWon = member.payAmount;
                        }

                        sendList[memberId].push({
                            payContent: item.payContent,
                            payAmount: payAmountWon,
                        });
                    }
                });
            }
        });

        setSendData(sendList);
    };

    const getTabClass = (idx: number) => {
        if (idx != tabIdx) {
            return "";
        }
        return "border-transparent bg-gradient-to-t from-[#559bd9] to-[#94cef2] text-white";
    };

    const calcTotal = (isAccept: boolean, member: string) => {
        let total = 0;
        if (isAccept) {
            acceptData[member].map((item) => (total += item.payAmount));
        } else {
            // 보낼 금액 계산
        }
        return total;
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
                        <div className="border-t-2 border-neutral-500 flex flex-col gap-3 text-lg">
                            {Object.keys(sendData).length !== 0 ? (
                                <div>
                                    {Object.keys(sendData).map(
                                        (member, index) => (
                                            <div
                                                key={index}
                                                className="grid grid-cols-3 mt-5"
                                            >
                                                <div className="grid grid-cols-3">
                                                    <div className="col-span-1 color-bg-blue-4 rounded-full text-white shadow-md font-bold w-8 h-8 justify-center items-center flex">
                                                        {member[0]}
                                                    </div>
                                                    <div className="font-bold color-text-blue-1">
                                                        {member}
                                                    </div>
                                                </div>
                                                <div className="text-neutral-500">
                                                    {sendData[member].length > 1
                                                        ? `${
                                                              sendData[
                                                                  member
                                                              ][0].payContent
                                                          } (+${
                                                              sendData[member]
                                                                  .length - 1
                                                          })`
                                                        : sendData[member][0]
                                                              .payContent}
                                                </div>
                                                <div className="text-end">
                                                    {calcTotal(
                                                        false,
                                                        member
                                                    ).toLocaleString()}{" "}
                                                    원
                                                </div>
                                            </div>
                                        )
                                    )}
                                </div>
                            ) : (
                                <div className="text-center m-10 text-lg">
                                    보낼 금액이 없습니다!
                                </div>
                            )}
                        </div>
                    </div>
                    <div>
                        <div className="font-bold text-2xl">받을 금액 💰</div>
                        <div className="border-t-2 border-neutral-500 flex flex-col gap-3 text-lg">
                            {Object.keys(acceptData).length !== 0 ? (
                                <div>
                                    {Object.keys(acceptData).map(
                                        (member, index) => (
                                            <div
                                                key={index}
                                                className="grid grid-cols-3 mt-5"
                                            >
                                                <div className="grid grid-cols-3">
                                                    <div className="col-span-1 color-bg-blue-4 rounded-full text-white shadow-md font-bold w-8 h-8 justify-center items-center flex">
                                                        {member[0]}
                                                    </div>
                                                    <div className="font-bold color-text-blue-1">
                                                        {member}
                                                    </div>
                                                </div>
                                                <div className="text-neutral-500">
                                                    {acceptData[member].length >
                                                    1
                                                        ? `${
                                                              acceptData[
                                                                  member
                                                              ][0].payContent
                                                          } (+${
                                                              acceptData[member]
                                                                  .length - 1
                                                          })`
                                                        : acceptData[member][0]
                                                              .payContent}
                                                </div>
                                                <div className="text-end">
                                                    {calcTotal(
                                                        true,
                                                        member
                                                    ).toLocaleString()}{" "}
                                                    원
                                                </div>
                                            </div>
                                        )
                                    )}
                                </div>
                            ) : (
                                <div className="text-center m-10 text-lg">
                                    받을 금액이 없습니다!
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
            <footer>
                <TabBarTour tourId={tourId} tourMode={3} />
            </footer>
        </>
    );
}
