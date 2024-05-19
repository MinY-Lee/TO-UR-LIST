import { memo, useEffect, useState } from "react";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import TabBarTour from "../../components/TabBar/TabBarTour";
import {
    AccountInfo,
    PayMember,
    TourInfoDetail,
    UserInfo,
} from "../../types/types";
import { getAccountList } from "../../util/api/pay";
import { HttpStatusCode } from "axios";
import { useSelector } from "react-redux";
import { getTour } from "../../util/api/tour";

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
    const [tourData, setTourData] = useState<TourInfoDetail>({
        tourTitle: "",
        cityList: [],
        startDate: "",
        endDate: "",
        memberList: [],
    });
    const [acceptData, setAcceptData] = useState<MemberCharge>({});
    const [sendData, setSendData] = useState<MemberCharge>({});

    const userInfo: UserInfo = useSelector((state: any) => state.userSlice);

    useEffect(() => {
        const address: string[] = window.location.href.split("/");
        const id = address[address.length - 3];
        setTourId(id);
    }, []);

    useEffect(() => {
        if (tourId) {
            const fetchTourData = async () => {
                try {
                    const tourRes = await getTour(tourId);
                    if (tourRes.status === HttpStatusCode.Ok) {
                        setTourData(tourRes.data);
                    }

                    const accountRes = await getAccountList(tourId);
                    if (accountRes.status === HttpStatusCode.Ok) {
                        setData(accountRes.data);
                        calcAccept(accountRes.data);
                        calcSend(accountRes.data);
                    }
                } catch (err) {
                    console.log(err);
                }
            };

            fetchTourData();
        }
    }, [tourId]);

    const idToName = (memberId: string): string => {
        const member = tourData.memberList.find(
            (member) => member.userId === memberId
        );
        return member ? member.userName : "";
    };

    const calcAccept = (accountData: AccountInfo[]) => {
        const acceptList: MemberCharge = {};
        accountData.forEach((item) => {
            if (item.payType === "public" && item.payerId === userInfo.userId) {
                item.payMemberList.forEach((member: PayMember) => {
                    const memberId: string = member.userId;
                    if (memberId !== userInfo.userId) {
                        if (!acceptList[memberId]) {
                            acceptList[memberId] = [];
                        }
                        acceptList[memberId].push({
                            payContent: item.payContent,
                            payAmount: member.payAmount,
                        });
                    }
                });
            }
        });
        setAcceptData(acceptList);
    };

    const calcSend = (accountData: AccountInfo[]) => {
        const sendList: MemberCharge = {};
        accountData.forEach((item) => {
            if (item.payType === "public" && item.payerId !== userInfo.userId) {
                item.payMemberList.forEach((member: PayMember) => {
                    const memberId: string = member.userId;
                    if (memberId === userInfo.userId) {
                        if (!sendList[memberId]) {
                            sendList[memberId] = [];
                        }
                        sendList[memberId].push({
                            payContent: item.payContent,
                            payAmount: member.payAmount,
                        });
                    }
                });
            }
        });
        setSendData(sendList);
    };

    const getTabClass = (idx: number) => {
        return idx === tabIdx
            ? "border-transparent bg-gradient-to-t from-[#559bd9] to-[#94cef2] text-white"
            : "";
    };

    const calcTotal = (isAccept: boolean, member: string) => {
        const totalList = isAccept ? acceptData[member] : sendData[member];
        return totalList.reduce((total, item) => total + item.payAmount, 0);
    };

    return (
        <>
            <header>
                <HeaderBar />
            </header>
            <div className="flex flex-col items-center h-[80vh] p-5 overflow-y-scroll">
                <div className="flex flex-col w-full p-3 gap-10">
                    <div>
                        <div className="font-bold text-2xl">보낼 금액 💸</div>
                        <div className="border-t-2 border-neutral-500 flex flex-col text-lg">
                            {Object.keys(sendData).length !== 0 ? (
                                Object.keys(sendData).map((member, index) => (
                                    <div
                                        key={index}
                                        className="grid grid-cols-3 mt-5 justify-center gap-2"
                                    >
                                        <div className="grid grid-cols-3">
                                            <div className="col-span-1 color-bg-blue-4 rounded-full text-white shadow-md font-bold w-8 h-8 justify-center items-center flex">
                                                {idToName(member)[0]}
                                            </div>
                                            <div className="col-span-2 font-bold color-text-blue-1">
                                                {idToName(member)}
                                            </div>
                                        </div>
                                        <div className="text-neutral-500 text-center">
                                            {sendData[member].length > 1
                                                ? `${
                                                      sendData[member][0]
                                                          .payContent
                                                  } 외 ${
                                                      sendData[member].length -
                                                      1
                                                  }건`
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
                                ))
                            ) : (
                                <div className="text-center m-10 text-lg">
                                    보낼 금액이 없습니다!
                                </div>
                            )}
                        </div>
                    </div>
                    <div>
                        <div className="font-bold text-2xl">받을 금액 💰</div>
                        <div className="border-t-2 border-neutral-500 flex flex-col text-lg">
                            {Object.keys(acceptData).length !== 0 ? (
                                Object.keys(acceptData).map((member, index) => (
                                    <div
                                        key={index}
                                        className="grid grid-cols-3 mt-5"
                                    >
                                        <div className="grid grid-cols-3 justify-center gap-2">
                                            <div className="col-span-1 color-bg-blue-4 rounded-full text-white shadow-md font-bold w-8 h-8 justify-center items-center flex">
                                                {idToName(member)[0]}
                                            </div>
                                            <div className="col-span-2 font-bold color-text-blue-1">
                                                {idToName(member)}
                                            </div>
                                        </div>
                                        <div className="text-neutral-500 text-center">
                                            {acceptData[member].length > 1
                                                ? `${
                                                      acceptData[member][0]
                                                          .payContent
                                                  } 외 ${
                                                      acceptData[member]
                                                          .length - 1
                                                  }건`
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
                                ))
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
                <TabBarTour tourId={tourId} tourMode={3} type="account" />
            </footer>
        </>
    );
}
