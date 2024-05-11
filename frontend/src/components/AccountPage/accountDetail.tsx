import { useState, useEffect, useRef } from 'react';
import { AccountInfo, PayMember, TourInfoDetail } from '../../types/types';

import CategoryToImg from './categoryToImg';

interface PropType {
    data: AccountInfo[];
    tourData: TourInfoDetail;
}

interface DataPerDayInfo {
    payDatetime: string;
    data: AccountInfo[];
}

export default function AccountDetail(props: PropType) {
    const [tourId, setTourId] = useState<string>('');
    const [startDate, setStartDate] = useState<Date | undefined>();
    const [endDate, setEndDate] = useState<Date | undefined>(new Date());
    const [rowData, setRowData] = useState<AccountInfo[]>([]);
    const [tabIdx, setTabIdx] = useState<number>(1);
    const [isClicked, setIsClicked] = useState<boolean>(false); // 드롭다운 클릭 여부
    const [groupedData, setGroupedData] = useState<DataPerDayInfo[]>([]);
    const [filter, setFilter] = useState<string>('전체 내역');

    const DataPerDay = (data: AccountInfo[]) => {
        // 결과를 저장할 배열
        const groupedData: DataPerDayInfo[] = [];

        const groupedByDate: { [date: string]: AccountInfo[] } = {};

        // data를 날짜별로 그룹화
        if (startDate) {
            const tempDate = new Date(startDate.getTime() - 24 * 60 * 60 * 1000).toISOString().split('T')[0];
            data.forEach((info: AccountInfo) => {
                const date = info.payDatetime;

                if (isPayMember('1234', info)) {
                    if (calcDay(new Date(date), startDate) <= 0) {
                        if (!groupedByDate[tempDate]) {
                            groupedByDate[tempDate] = [];
                        }
                        groupedByDate[tempDate] = [info, ...groupedByDate[tempDate]];
                    } else {
                        if (!groupedByDate[date]) {
                            groupedByDate[date] = [];
                        }
                        groupedByDate[date] = [info, ...groupedByDate[date]];
                    }
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
        groupedData.sort((a, b) => new Date(a.payDatetime).getTime() - new Date(b.payDatetime).getTime());

        setGroupedData(groupedData);
    };

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 2]);
        if (props.tourData.startDate) {
            setStartDate(new Date(props.tourData.startDate));
            setEndDate(new Date(props.tourData.endDate));
        }
        setRowData(props.data);
        // 데이터 날짜별로 그룹핑
        DataPerDay(rowData);
    }, [tourId, props, rowData]);

    const getTabClass = (idx: number) => {
        if (idx != tabIdx) {
            return '';
        }
        return 'border-transparent bg-gradient-to-t from-[#559bd9] to-[#94cef2] text-white';
    };

    const handleTypeChange = (type: string) => {
        if (type === 'all') {
            setFilter('전체 내역');
            getFilteredData(rowData);
        } else {
            // 개인 - 공동 지출 나누기
            let privateData: AccountInfo[] = [];
            let publicData: AccountInfo[] = [];
            rowData.forEach((item) => {
                if (item.payType == 'private' && item.payerId == '1234') {
                    privateData.push(item);
                }
                if (item.payType == 'public' && isPayMember('1234', item)) {
                    publicData.push(item);
                }
            });

            if (type == 'private') {
                setFilter('개인 지출');
                getFilteredData(privateData);
            }
            if (type == 'public') {
                setFilter('공동 지출');
                getFilteredData(publicData);
            }
        }

        setIsClicked(false);
    };

    const getFilteredData = (data: AccountInfo[]) => {
        DataPerDay(data);
    };

    const setDropdown = (isClicked: boolean) => {
        return isClicked ? '' : 'hidden';
    };

    const isPayMember = (userId: string, info: AccountInfo) => {
        if (info.payerId === userId) {
            return true;
        }
        info.payMemberList.forEach((member) => {
            if (member.userId === userId) {
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

    const getMyAmount = (info: AccountInfo): number => {
        let amount = 0;
        info.payMemberList.forEach((member) => {
            if (member.userId == '1234') {
                amount = member.payAmount;
            }
        });

        return amount;
    };

    const calcDay = (date1: Date, date2: Date) => {
        return (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24) + 1;
    };
    return (
        <div>
            <div className="px-5 flex justify-between items-center mt-3">
                {/* 내역 드롭다운 */}
                <button
                    onClick={() => setIsClicked(!isClicked)}
                    id="dropdown-button"
                    data-dropdown-toggle="dropdown"
                    className={`flex-shrink-0 z-10 inline-flex items-center py-2 px-3 text-center text-gray-900`}
                    type="button"
                >
                    {filter}
                    {/* 드롭다운 svg */}
                    <svg
                        className={`${isClicked ? 'rotate-180' : ''} w-2.5 h-2.5 ms-2.5`}
                        aria-hidden="true"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 10 6"
                    >
                        <path
                            stroke="currentColor"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth="2"
                            d="m1 1 4 4 4-4"
                        />
                    </svg>
                </button>

                <div
                    id="dropdown"
                    className={`${setDropdown(
                        isClicked
                    )} absolute top-[28%] z-10 bg-white divide-y divide-gray-100 shadow`}
                >
                    <ul className=" text-gray-700 " aria-labelledby="dropdown-button">
                        <li className="hover:bg-[#5faad9] px-5 py-2  border" onClick={() => handleTypeChange('all')}>
                            <div className="flex items-center">
                                <div className="">전체 내역</div>
                            </div>
                        </li>
                        <li className="hover:bg-[#5faad9] px-5 py-2 border" onClick={() => handleTypeChange('private')}>
                            <div className="flex items-center">
                                <div>개인 지출</div>
                            </div>
                        </li>
                        <li className="hover:bg-[#5faad9] px-5 py-2 border" onClick={() => handleTypeChange('public')}>
                            <div className="flex items-center">
                                <div>공동 지출</div>
                            </div>
                        </li>
                    </ul>
                </div>

                {/* 원화 현지화폐 토글 */}
                <ul className="grid grid-cols-2 w-[30vw] border rounded-full color-bg-blue-4">
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
                {/* 엑셀로 내보내기 */}
                <div className="text-neutral-500 underline">엑셀로 내보내기</div>
            </div>

            <div className="border-2 border-neutral-400 py-3 rounded-lg mx-8 mt-2">
                {groupedData.map((data, index) => (
                    <div key={index} className="px-5 mb-5">
                        <div className="border-b-2 text-lg text-neutral-500 mb-2">
                            DAY{' '}
                            {startDate && calcDay(new Date(data.payDatetime), startDate) <= 0
                                ? `- | ~`
                                : `${startDate && calcDay(new Date(data.payDatetime), startDate)} | `}
                            {data.payDatetime}
                        </div>
                        <div>
                            {data.data.map((item, index) => (
                                <div
                                    onClick={() => {
                                        window.location.href = `/tour/${tourId}/account/${item.payId}`;
                                    }}
                                    key={index}
                                    className="flex flex-col mb-2"
                                >
                                    <div className="flex justify-between">
                                        <div className="flex gap-2">
                                            <div className="bg-gray-100 p-1 rounded-full">
                                                {CategoryToImg(item.payCategory)}
                                            </div>
                                            <div>{item.payContent}</div>
                                        </div>
                                        {item.payType == 'private' ? (
                                            <div>
                                                {item.payAmount.toLocaleString()} {item.unit}
                                            </div>
                                        ) : (
                                            <div className="text-orange-500">
                                                {getMyAmount(item).toLocaleString()} {item.unit}
                                            </div>
                                        )}
                                    </div>
                                    {item.payType == 'public' ? (
                                        <div className="flex justify-between pl-10 text-sm">
                                            <div className="flex gap-2 items-center ">
                                                <svg
                                                    width="18"
                                                    height="18"
                                                    viewBox="0 0 22 18"
                                                    fill="none"
                                                    xmlns="http://www.w3.org/2000/svg"
                                                >
                                                    <path
                                                        d="M4.5 4.60416C4.5 4.28406 4.56305 3.9671 4.68554 3.67137C4.80804 3.37563 4.98758 3.10693 5.21393 2.88058C5.44027 2.65424 5.70898 2.4747 6.00471 2.3522C6.30044 2.2297 6.6174 2.16666 6.9375 2.16666C7.2576 2.16666 7.57456 2.2297 7.87029 2.3522C8.16602 2.4747 8.43473 2.65424 8.66107 2.88058C8.88742 3.10693 9.06696 3.37563 9.18946 3.67137C9.31195 3.9671 9.375 4.28406 9.375 4.60416C9.375 5.25062 9.11819 5.87061 8.66107 6.32773C8.20395 6.78485 7.58397 7.04166 6.9375 7.04166C6.29103 7.04166 5.67105 6.78485 5.21393 6.32773C4.75681 5.87061 4.5 5.25062 4.5 4.60416ZM6.9375 0.541656C5.86006 0.541656 4.82675 0.969669 4.06488 1.73154C3.30301 2.4934 2.875 3.52671 2.875 4.60416C2.875 5.6816 3.30301 6.71491 4.06488 7.47678C4.82675 8.23864 5.86006 8.66666 6.9375 8.66666C8.01494 8.66666 9.04825 8.23864 9.81012 7.47678C10.572 6.71491 11 5.6816 11 4.60416C11 3.52671 10.572 2.4934 9.81012 1.73154C9.04825 0.969669 8.01494 0.541656 6.9375 0.541656ZM15.0625 5.41666C15.0625 4.98568 15.2337 4.57235 15.5385 4.26761C15.8432 3.96286 16.2565 3.79166 16.6875 3.79166C17.1185 3.79166 17.5318 3.96286 17.8365 4.26761C18.1413 4.57235 18.3125 4.98568 18.3125 5.41666C18.3125 5.84763 18.1413 6.26096 17.8365 6.5657C17.5318 6.87045 17.1185 7.04166 16.6875 7.04166C16.2565 7.04166 15.8432 6.87045 15.5385 6.5657C15.2337 6.26096 15.0625 5.84763 15.0625 5.41666ZM16.6875 2.16666C15.8255 2.16666 14.9989 2.50907 14.3894 3.11856C13.7799 3.72805 13.4375 4.5547 13.4375 5.41666C13.4375 6.27861 13.7799 7.10526 14.3894 7.71475C14.9989 8.32425 15.8255 8.66666 16.6875 8.66666C17.5495 8.66666 18.3761 8.32425 18.9856 7.71475C19.5951 7.10526 19.9375 6.27861 19.9375 5.41666C19.9375 4.5547 19.5951 3.72805 18.9856 3.11856C18.3761 2.50907 17.5495 2.16666 16.6875 2.16666ZM0.4375 12.7292C0.4375 12.0827 0.694307 11.4627 1.15143 11.0056C1.60855 10.5485 2.22853 10.2917 2.875 10.2917H11C11.6465 10.2917 12.2665 10.5485 12.7236 11.0056C13.1807 11.4627 13.4375 12.0827 13.4375 12.7292V12.9112C13.4372 12.9857 13.4318 13.06 13.4212 13.1338C13.3289 13.9216 13.0357 14.6725 12.5697 15.3145C11.6809 16.5414 9.99087 17.6042 6.9375 17.6042C3.88413 17.6042 2.19575 16.5414 1.30362 15.3145C0.838295 14.6724 0.545658 13.9215 0.45375 13.1338C0.445786 13.0598 0.440366 12.9855 0.4375 12.9112V12.7292ZM2.0625 12.8754V12.8868L2.069 12.9664C2.13172 13.4695 2.32067 13.9486 2.61825 14.359C3.148 15.087 4.30175 15.9792 6.9375 15.9792C9.57325 15.9792 10.727 15.087 11.2567 14.359C11.5543 13.9486 11.7433 13.4695 11.806 12.9664L11.8125 12.8852V12.7292C11.8125 12.5137 11.7269 12.307 11.5745 12.1546C11.4222 12.0023 11.2155 11.9167 11 11.9167H2.875C2.65951 11.9167 2.45285 12.0023 2.30048 12.1546C2.1481 12.307 2.0625 12.5137 2.0625 12.7292V12.8754ZM16.6875 15.9792C15.732 15.9792 14.9487 15.8329 14.315 15.5924C14.5731 15.1141 14.7702 14.6053 14.9016 14.0779C15.3371 14.242 15.9156 14.3542 16.6875 14.3542C18.5059 14.3542 19.2517 13.7334 19.58 13.2719C19.7734 13.0035 19.8953 12.6904 19.9343 12.3619L19.9375 12.3164C19.9358 12.2098 19.8922 12.1081 19.8162 12.0333C19.7402 11.9586 19.6379 11.9166 19.5312 11.9167H14.9812C14.8608 11.3275 14.611 10.7725 14.25 10.2917H19.5312C20.6525 10.2917 21.5625 11.2017 21.5625 12.3229V12.3505C21.5614 12.4098 21.5571 12.4689 21.5495 12.5277C21.4824 13.1371 21.2597 13.7191 20.9027 14.2177C20.217 15.178 18.9316 15.9792 16.6875 15.9792Z"
                                                        fill="#363636"
                                                    />
                                                </svg>
                                                <div className="mr-3">{item.payMemberList.length}</div>
                                                <div className="flex">
                                                    {getPayMember(item).map((member) => (
                                                        <div
                                                            key={member.userId}
                                                            className="color-bg-blue-4 w-6 h-6 flex justify-center items-center -ml-2 rounded-full shadow-md"
                                                        >
                                                            {member.userId[0]}
                                                        </div>
                                                    ))}
                                                </div>
                                            </div>
                                            <div className=" text-neutral-500 ">
                                                {item.payAmount.toLocaleString()} {item.unit}
                                            </div>
                                        </div>
                                    ) : (
                                        ''
                                    )}
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
