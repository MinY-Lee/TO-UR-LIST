import { useState, useEffect, useRef } from 'react';
import { AccountInfo, PayMember } from '../../types/types';

interface PropType {
    data: AccountInfo[];
}

interface DataPerDay {
    payDatetime: string;
    data: AccountInfo[];
}

export default function AccountDetail(props: PropType) {
    const [tourId, setTourId] = useState<string>('');

    const [tabIdx, setTabIdx] = useState<number>(1);
    const [isClicked, setIsClicked] = useState<boolean>(false); // 드롭다운 클릭 여부
    const [groupedData, setGroupedData] = useState<DataPerDay[]>([]);

    const DataPerDay = () => {
        // 결과를 저장할 배열
        const groupedData: DataPerDay[] = [];

        const groupedByDate: { [date: string]: AccountInfo[] } = {};

        // data를 날짜별로 그룹화
        props.data.forEach((info: AccountInfo) => {
            const date = info.payDatetime;
            if (isPayMember('1234', info)) {
                if (!groupedByDate[date]) {
                    groupedByDate[date] = [];
                }
                groupedByDate[date].push(info);
            }
        });

        for (const date in groupedByDate) {
            if (groupedByDate.hasOwnProperty(date)) {
                const dataPerDay: DataPerDay = {
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

        // 데이터 날짜별로 정리
        DataPerDay();
    }, [props.data]);

    const getTabClass = (idx: number) => {
        if (idx != tabIdx) {
            return '';
        }
        return 'border-transparent bg-gradient-to-t from-[#559bd9] to-[#94cef2] text-white';
    };

    const handleTypeChange = (type: string) => {
        // type === "private" ? setIsPublicInput(false) : setIsPublicInput(true);
        // setIsClicked(false);
        console.log(type);
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

    const TypeToImg = (type: string) => {
        if (type == '숙소') {
            return (
                <svg
                    width="22"
                    height="22"
                    viewBox="0 0 22 22"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    xmlnsXlink="http://www.w3.org/1999/xlink"
                >
                    <rect x="0.343536" y="0.577148" width="21.3134" height="21.3134" fill="url(#pattern0_288_6137)" />
                    <defs>
                        <pattern id="pattern0_288_6137" patternContentUnits="objectBoundingBox" width="1" height="1">
                            <use xlinkHref="#image0_288_6137" transform="scale(0.0078125)" />
                        </pattern>
                        <image
                            id="image0_288_6137"
                            width="128"
                            height="128"
                            xlinkHref="data:image/png;base64,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"
                        />
                    </defs>
                </svg>
            );
        }
        if (type == '교통') {
            return (
                <svg
                    width="22"
                    height="23"
                    viewBox="0 0 22 23"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    xmlnsXlink="http://www.w3.org/1999/xlink"
                >
                    <rect x="0.507431" y="0.795654" width="21.3134" height="21.3134" fill="url(#pattern0_288_6491)" />
                    <defs>
                        <pattern id="pattern0_288_6491" patternContentUnits="objectBoundingBox" width="1" height="1">
                            <use xlinkHref="#image0_288_6491" transform="scale(0.0078125)" />
                        </pattern>
                        <image
                            id="image0_288_6491"
                            width="128"
                            height="128"
                            xlinkHref="data:image/png;base64,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"
                        />
                    </defs>
                </svg>
            );
        }
        if (type == '식비') {
            return (
                <svg
                    width="22"
                    height="23"
                    viewBox="0 0 22 23"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    xmlnsXlink="http://www.w3.org/1999/xlink"
                >
                    <rect x="0.343277" y="0.960083" width="21.3134" height="21.3134" fill="url(#pattern0_288_6496)" />
                    <defs>
                        <pattern id="pattern0_288_6496" patternContentUnits="objectBoundingBox" width="1" height="1">
                            <use xlinkHref="#image0_288_6496" transform="scale(0.0078125)" />
                        </pattern>
                        <image
                            id="image0_288_6496"
                            width="128"
                            height="128"
                            xlinkHref="data:image/png;base64,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"
                        />
                    </defs>
                </svg>
            );
        }
        if (type == '쇼핑') {
            return (
                <svg
                    width="22"
                    height="23"
                    viewBox="0 0 22 23"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    xmlnsXlink="http://www.w3.org/1999/xlink"
                >
                    <rect x="0.343277" y="0.960083" width="21.3134" height="21.3134" fill="url(#pattern0_288_6501)" />
                    <defs>
                        <pattern id="pattern0_288_6501" patternContentUnits="objectBoundingBox" width="1" height="1">
                            <use xlinkHref="#image0_288_6501" transform="scale(0.0078125)" />
                        </pattern>
                        <image
                            id="image0_288_6501"
                            width="128"
                            height="128"
                            xlinkHref="data:image/png;base64,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"
                        />
                    </defs>
                </svg>
            );
        }
        if (type == '기타') {
            return (
                <svg
                    width="18"
                    height="19"
                    viewBox="0 0 18 19"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    xmlnsXlink="http://www.w3.org/1999/xlink"
                >
                    <rect y="0.616821" width="18" height="18" fill="url(#pattern0_288_6506)" />
                    <defs>
                        <pattern id="pattern0_288_6506" patternContentUnits="objectBoundingBox" width="1" height="1">
                            <use xlinkHref="#image0_288_6506" transform="scale(0.0078125)" />
                        </pattern>
                        <image
                            id="image0_288_6506"
                            width="128"
                            height="128"
                            xlinkHref="data:image/png;base64,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"
                        />
                    </defs>
                </svg>
            );
        }
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
                    전체 내역
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

            <div className="">
                {groupedData.map((data, index) => (
                    <div key={index} className="px-10 mb-3">
                        <div className="border-b-2 text-lg text-neutral-500 mb-2">DAY | {data.payDatetime}</div>
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
                                                {TypeToImg(item.payCategory)}
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
