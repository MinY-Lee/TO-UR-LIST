import { useState, useEffect, useRef } from "react";
import {
    AccountInfo,
    CurrencyInfo,
    PayMember,
    TourInfoDetail,
    UserInfo,
} from "../../types/types";

import CategoryToImg from "./categoryToImg";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import TrashIcon from "../../assets/svg/trashIcon";
import DropdownIcon from "../../assets/svg/dropdownIcon";
import CheckModal from "../CheckModal";
import { deleteAccount } from "../../util/api/pay";
import PayTypeIcon from "../../assets/svg/payTypeIcon";
import { HttpStatusCode } from "axios";
import GetISOStringKor from "./getISOStringKor";

interface PropType {
    data: AccountInfo[];
    tourData: TourInfoDetail;
    currency: CurrencyInfo;
    handleIsChanged: () => void;
}

interface DataPerDayInfo {
    payDatetime: string;
    data: AccountInfo[];
}

export default function AccountDetail(props: PropType) {
    const [tourId, setTourId] = useState<string>("");
    const [startDate, setStartDate] = useState<Date>(new Date());
    const [endDate, setEndDate] = useState<Date>(new Date());
    const [rowData, setRowData] = useState<AccountInfo[]>([]);
    const [tabIdx, setTabIdx] = useState<number>(1);
    const [isClicked, setIsClicked] = useState<boolean>(false); // 드롭다운 클릭 여부
    const [checkModalActive, setCheckModalActive] = useState<boolean>(false); // 아이템 삭제 클릭 여부
    const [deleteTarget, setDeleteTarget] = useState<AccountInfo>({
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

    const [groupedData, setGroupedData] = useState<DataPerDayInfo[]>([]);
    const [filter, setFilter] = useState<string>("전체 내역");

    const userInfo: UserInfo = useSelector((state: any) => state.userSlice);

    const navigate = useNavigate();

    const DataPerDay = (data: AccountInfo[]) => {
        // 결과를 저장할 배열
        const groupedData: DataPerDayInfo[] = [];

        const groupedByDate: { [date: string]: AccountInfo[] } = {};

        // data를 날짜별로 그룹화
        if (startDate) {
            const tempDate = GetISOStringKor(
                new Date(startDate.getTime() - 9 * 60 * 60 * 1000)
            ).split("T")[0];
            data.forEach((info: AccountInfo) => {
                const date = info.payDatetime;

                if (calcDay(new Date(date), new Date(tempDate)) < 0) {
                    if (!groupedByDate[tempDate]) {
                        groupedByDate[tempDate] = [];
                    }
                    groupedByDate[tempDate] = [
                        info,
                        ...groupedByDate[tempDate],
                    ];
                } else {
                    if (!groupedByDate[date.split("T")[0]]) {
                        groupedByDate[date.split("T")[0]] = [];
                    }
                    groupedByDate[date.split("T")[0]] = [
                        info,
                        ...groupedByDate[date.split("T")[0]],
                    ];
                }
            });
        }

        for (const date in groupedByDate) {
            if (groupedByDate.hasOwnProperty(date)) {
                const dataPerDay: DataPerDayInfo = {
                    payDatetime: date,
                    data: groupedByDate[date],
                };
                groupedData.push(dataPerDay);
            }
        }
        // 날짜 순으로 정렬
        groupedData.sort(
            (a, b) =>
                new Date(a.payDatetime).getTime() -
                new Date(b.payDatetime).getTime()
        );
        setGroupedData(groupedData);
    };

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split("/");
        setTourId(address[address.length - 2]);
        if (props.tourData.startDate) {
            setStartDate(new Date(props.tourData.startDate));
            setEndDate(new Date(props.tourData.endDate));
        }
        setRowData(props.data);
        // 데이터 날짜별로 그룹핑
        DataPerDay(props.data);
    }, [tourId, props, rowData]);

    const getTabClass = (idx: number) => {
        if (idx != tabIdx) {
            return "";
        }
        return "border-transparent bg-gradient-to-t from-[#559bd9] to-[#94cef2] text-white";
    };

    const handleTypeChange = (type: string) => {
        if (type === "all") {
            setFilter("전체 내역");
            getFilteredData(rowData);
        } else {
            // 개인 - 공동 지출 나누기
            let privateData: AccountInfo[] = [];
            let publicData: AccountInfo[] = [];
            rowData.forEach((item) => {
                if (
                    item.payType == "private" &&
                    item.payerId == userInfo.userId
                ) {
                    privateData.push(item);
                }
                if (
                    item.payType == "public" &&
                    isPayMember(userInfo.userId, item)
                ) {
                    publicData.push(item);
                }
            });

            if (type == "private") {
                setFilter("개인 지출");
                getFilteredData(privateData);
            }
            if (type == "public") {
                setFilter("공동 지출");
                getFilteredData(publicData);
            }
        }

        setIsClicked(false);
    };

    const getFilteredData = (data: AccountInfo[]) => {
        DataPerDay(data);
    };

    const setDropdown = (isClicked: boolean) => {
        return isClicked ? "" : "hidden";
    };

    const isPayMember = (userId: string, info: AccountInfo) => {
        if (info.payerId == userId) {
            return true;
        }
        info.payMemberList.forEach((member) => {
            console.log(userId);
            console.log(member.userId);

            if (member.userId == userId) {
                return true;
            }
        });
        return false;
    };

    const getPayMember = (info: AccountInfo): PayMember[] => {
        let memberList: PayMember[] = [];
        info.payMemberList.forEach((member) => {
            memberList.push(member);
        });

        return memberList;
    };

    const idToName = (memberId: string): string => {
        const member = props.tourData.memberList.find(
            (member) => member.userId === memberId
        );

        if (member) {
            return member.userName;
        } else {
            return "";
        }
    };

    const getMyAmount = (info: AccountInfo): number => {
        let amount = 0;
        info.payMemberList.forEach((member) => {
            if (member.userId == userInfo.userId) {
                amount = member.payAmount;
            }
        });

        return amount;
    };

    const deleteItem = () => {
        if (deleteTarget.payId) {
            deleteAccount(deleteTarget.payId, tourId, deleteTarget.payType)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        props.handleIsChanged();
                    }
                })
                .catch((err) => console.log(err));
            setCheckModalActive(false);
        }
    };

    const handleDelete = (
        item: AccountInfo,
        event: React.MouseEvent<HTMLDivElement>
    ) => {
        event.stopPropagation();
        setCheckModalActive(true);
        setDeleteTarget(item);
    };

    const closeModal = () => {
        setCheckModalActive(false);
    };

    const calcDay = (date1: Date, date2: Date) => {
        return Math.round(
            (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24)
        );
    };
    return (
        <>
            {checkModalActive ? (
                <CheckModal
                    mainText="정말 삭제하시겠습니까?"
                    subText="지운 항목은 되돌릴 수 없습니다."
                    OKText="삭제"
                    CancelText="취소"
                    clickOK={deleteItem}
                    clickCancel={closeModal}
                />
            ) : (
                <></>
            )}
            <div className="flex flex-col items-center">
                <div className="px-5 flex justify-between w-full items-center mt-3">
                    {/* 내역 드롭다운 */}
                    <button
                        onClick={() => setIsClicked(!isClicked)}
                        id="dropdown-button"
                        data-dropdown-toggle="dropdown"
                        className={`flex-shrink-0 z-10 inline-flex items-center py-2 px-3 text-center text-gray-900`}
                        type="button"
                    >
                        {filter}
                        <DropdownIcon isClicked={isClicked} />
                    </button>

                    <div
                        id="dropdown"
                        className={`${setDropdown(
                            isClicked
                        )} absolute top-[28%] z-10 bg-white divide-y divide-gray-100 shadow`}
                    >
                        <ul
                            className=" text-gray-700 "
                            aria-labelledby="dropdown-button"
                        >
                            <li
                                className="hover:bg-[#5faad9] px-5 py-2  border"
                                onClick={() => handleTypeChange("all")}
                            >
                                <div className="flex items-center">
                                    <div className="">전체 내역</div>
                                </div>
                            </li>
                            <li
                                className="hover:bg-[#5faad9] px-5 py-2 border"
                                onClick={() => handleTypeChange("private")}
                            >
                                <div className="flex items-center">
                                    <div>개인 지출</div>
                                </div>
                            </li>
                            <li
                                className="hover:bg-[#5faad9] px-5 py-2 border"
                                onClick={() => handleTypeChange("public")}
                            >
                                <div className="flex items-center">
                                    <div>공동 지출</div>
                                </div>
                            </li>
                        </ul>
                    </div>

                    {/* 원화 현지화폐 토글 */}
                    <ul className="grid grid-cols-2 w-[30vw] border rounded-full color-bg-blue-4">
                        <li
                            className="rounded-full h-6"
                            onClick={() => setTabIdx(1)}
                        >
                            <div
                                className={`${getTabClass(
                                    1
                                )} rounded-full h-6 flex justify-center items-center border-x-0 border-b-2 border-t-0 border-transparent leading-tight text-neutral-500 `}
                            >
                                KRW
                            </div>
                        </li>
                        <li
                            className="rounded-full h-6"
                            onClick={() => setTabIdx(2)}
                        >
                            <div
                                className={`${getTabClass(
                                    2
                                )} rounded-full h-6 flex justify-center items-center border-x-0 border-b-2 border-t-0 border-transparent leading-tight text-neutral-500 `}
                            >
                                원본
                            </div>
                        </li>
                    </ul>
                    {/* 엑셀로 내보내기 */}
                    <div className="text-neutral-500 underline">
                        엑셀로 내보내기
                    </div>
                </div>

                <div className="border-2 border-neutral-400 py-3 rounded-lg mt-2 w-[90%]">
                    {groupedData.length == 0 ? (
                        <div className="h-[50vh] flex justify-center items-center text-xl">
                            아직 지출 내역이 없습니다.
                        </div>
                    ) : (
                        <div className="">
                            {groupedData.map((data, index) => (
                                <div key={index} className="px-5 mb-5">
                                    <div className="border-b-2 text-lg text-neutral-500 mb-2">
                                        DAY{" "}
                                        {startDate &&
                                        calcDay(
                                            new Date(data.payDatetime),
                                            startDate
                                        ) < 0
                                            ? `- | ~`
                                            : `${
                                                  startDate &&
                                                  calcDay(
                                                      new Date(
                                                          data.payDatetime
                                                      ),
                                                      startDate
                                                  ) + 1
                                              } | `}
                                        {data.payDatetime}
                                    </div>
                                    <div>
                                        {data.data.map((item, index) => (
                                            <div
                                                onClick={() => {
                                                    navigate(
                                                        `/tour/${tourId}/account/${item.payId}`,
                                                        {
                                                            state: {
                                                                item: item,
                                                            },
                                                        }
                                                    );
                                                }}
                                                key={index}
                                                className="grid grid-cols-10 mb-2"
                                            >
                                                <div className="flex flex-col col-span-9">
                                                    <div className="flex justify-between">
                                                        <div className="flex gap-2">
                                                            <div className="bg-gray-100 p-1 rounded-full">
                                                                {CategoryToImg(
                                                                    item.payCategory
                                                                )}
                                                            </div>
                                                            <div>
                                                                {
                                                                    item.payContent
                                                                }
                                                            </div>
                                                        </div>
                                                        {item.payType ==
                                                        "private" ? (
                                                            <div>
                                                                {tabIdx == 1 ? (
                                                                    <>
                                                                        {item.payAmount.toLocaleString()}{" "}
                                                                        원
                                                                    </>
                                                                ) : (
                                                                    <>
                                                                        {item.payAmount.toLocaleString()}{" "}
                                                                        {
                                                                            item.unit
                                                                        }
                                                                    </>
                                                                )}
                                                            </div>
                                                        ) : (
                                                            <div className="text-orange-500">
                                                                {tabIdx == 1 ? (
                                                                    <>
                                                                        {getMyAmount(
                                                                            item
                                                                        ).toLocaleString()}{" "}
                                                                        원
                                                                    </>
                                                                ) : (
                                                                    <>
                                                                        {Math.ceil(
                                                                            getMyAmount(
                                                                                item
                                                                            ) /
                                                                                item.exchangeRate
                                                                        ).toLocaleString()}{" "}
                                                                        {
                                                                            item.unit
                                                                        }
                                                                    </>
                                                                )}
                                                            </div>
                                                        )}
                                                    </div>
                                                    {item.payType ==
                                                    "public" ? (
                                                        <div className="flex justify-between pl-10 text-sm">
                                                            <div className="flex gap-2 items-center ">
                                                                <PayTypeIcon
                                                                    isPublic={
                                                                        true
                                                                    }
                                                                />
                                                                <div className="mr-3">
                                                                    {
                                                                        item
                                                                            .payMemberList
                                                                            .length
                                                                    }
                                                                </div>
                                                                <div className="flex">
                                                                    {getPayMember(
                                                                        item
                                                                    ).map(
                                                                        (
                                                                            member
                                                                        ) => (
                                                                            <div
                                                                                key={
                                                                                    member.userId
                                                                                }
                                                                                className="color-bg-blue-4 w-6 h-6 flex justify-center items-center -ml-2 rounded-full shadow-md"
                                                                            >
                                                                                {
                                                                                    idToName(
                                                                                        member.userId
                                                                                    )[0]
                                                                                }
                                                                            </div>
                                                                        )
                                                                    )}
                                                                </div>
                                                            </div>
                                                            <div className=" text-neutral-500 ">
                                                                {tabIdx == 1 ? (
                                                                    <>
                                                                        {item.payAmount.toLocaleString()}{" "}
                                                                        원
                                                                    </>
                                                                ) : (
                                                                    <>
                                                                        {(
                                                                            item.payAmount /
                                                                            item.exchangeRate
                                                                        ).toLocaleString()}{" "}
                                                                        {
                                                                            props
                                                                                .currency
                                                                                .unit
                                                                        }
                                                                    </>
                                                                )}
                                                            </div>
                                                        </div>
                                                    ) : (
                                                        ""
                                                    )}
                                                </div>
                                                <div
                                                    className="flex justify-end items-start"
                                                    onClick={(event) =>
                                                        handleDelete(
                                                            item,
                                                            event
                                                        )
                                                    }
                                                >
                                                    <TrashIcon />
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}
