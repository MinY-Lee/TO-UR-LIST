import { BaseSyntheticEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import MyButton from '../../components/Buttons/myButton';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';

import CategoryToImg from '../../components/AccountPage/categoryToImg';
import { AccountInfo, MemberInfo, PayMember, TourInfoDetail } from '../../types/types';

import TourDetail from '../../dummy-data/get_tour_detail.json';
import getCurrency from '../../dummy-data/get_pay_currency_countryCode_date.json';

interface PropType {
    isModify: boolean;
    data?: AccountInfo;
}

export default function AccountAddModify(props: PropType) {
    const navigate = useNavigate();

    const [tourId, setTourId] = useState<string>('');
    const [data, setData] = useState<TourInfoDetail>({
        tourId: '',
        tourTitle: '',
        cityList: [],
        startDate: '',
        endDate: '',
        memberList: [],
    });
    const [wonDropdownClick, setWonDropdownClick] = useState<boolean>(false);
    const [typeDropdownClick, setTypeDropdownClick] = useState<boolean>(false);
    const [typeDropdownPosition, setTypeDropdownPosition] = useState<string>('');
    const [payerDropdownClick, setPayerDropdownClick] = useState<boolean>(false);
    const [unit, setUnit] = useState<string>('₩');
    const [type, setType] = useState<string>('');
    const [amount, setAmount] = useState<number>(0);
    const [currency, setCurrency] = useState<number>(0);
    const [category, setCategory] = useState<string>('');
    const [content, setContent] = useState<string>('');
    const [isPublic, setIsPublic] = useState<boolean>(false);
    const [date, setDate] = useState<string>('');
    const [isValidDate, setIsValidDate] = useState<boolean>(true);
    const [payer, setPayer] = useState<string>('');
    const [payMember, setPayMember] = useState<PayMember[]>([]);

    useEffect(() => {
        // 투어 아이디 불러오기
        const address: string[] = window.location.href.split('/');
        setTourId(address[address.length - 3]);

        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const tourData = TourDetail.find((tour) => tour.tourId === tourId);
        if (tourData) {
            setData(tourData);
        }

        const currencyData = getCurrency;

        if (!props.isModify) {
            // payer 디폴트는 현재 가계부 작성하는 사람
            setPayer('김싸피');

            // 디폴트 환율 = 현재 환율
            setCurrency(currencyData.currencyRate);
        } else {
            if (props.data) {
                setAmount(props.data.payAmount);
                // 환율 지정 지금 안됨
                setCurrency(currencyData.currencyRate);
                setDate(props.data.payDatetime);
                setIsPublic(props.data.payType == 'public' ? true : false);
                setPayer(props.data.payerId); // 아이디 -> 이름 매칭 필요
                setPayMember(props.data.payMemberList);
                setCategory(props.data.payCategory);
                setContent(props.data.payContent);
                setType(props.data.payMethod);
            }
        }
    }, [tourId, data]);

    useEffect(() => {
        const dropdown = document.querySelector('#type-dropdown'); // 드롭다운 요소 선택
        const input = document.querySelector('#type-input'); // 인풋 요소 선택

        if (dropdown && input) {
            const inputRect = input.getBoundingClientRect();
            const topPosition = inputRect.top + inputRect.height;
            setTypeDropdownPosition(`${topPosition}px`);
        }
    }, [isPublic]);

    const handleUnit = (unit: string) => {
        setUnit(unit);
        setWonDropdownClick(false);
    };

    const handleAmount = (event: React.ChangeEvent<HTMLInputElement>) => {
        const inputValue = parseInt(event.target.value);
        if (!isNaN(inputValue)) {
            setAmount(inputValue);
        } else {
            setAmount(0);
        }
    };

    const handleCurrency = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (Number(event.target.value)) {
            setCurrency(Number(event.target.value));
        } else {
            setCurrency(0);
        }
    };
    const handleTypeChange = (type: string) => {
        setType(type);
        setTypeDropdownClick(false);
    };

    const categories = ['숙소', '교통', '식비', '쇼핑', '기타'];

    const setButtonProp = (input: string) => {
        if ((input == 'public' && !isPublic) || (input == 'private' && isPublic)) {
            return 'text-neutral-400 border-neutral-400';
        }
        return 'color-text-blue-2';
    };

    const handleDateChange = (event: BaseSyntheticEvent) => {
        setDate(event.target.value);
    };

    const handlePayerChange = (payer: string) => {
        setPayer(payer);
        setPayerDropdownClick(false);
    };

    const handleContent = (event: BaseSyntheticEvent) => {
        setContent(event.target.value);
    };

    const handlePayMember = (memberId: string) => {
        console.log(memberId);
        let updatedMember: PayMember[] = [];
        const payMemberIds = payMember.map((member) => member.userId);
        if (payMemberIds.includes(memberId)) {
            updatedMember = payMember.filter((item) => item.userId != memberId);
        } else {
            updatedMember = [...payMember, { userId: memberId, payAmount: 0 }];
        }

        setPayMember(updatedMember);
    };

    const handleSave = () => {
        const newAccountItem = {
            payType: isPublic,
            tourId: tourId,
            payAmount: amount,
            unit: unit,
            currencyCode: 'JPY',
            payMethod: type,
            payDatetime: date,
            payContent: content,
            payCategory: category,
            payerId: '1234',
            payMemberList: payMember,
        };
        // payId 리턴값으로 받아오기

        console.log(newAccountItem);
        // navigate(-1);
    };

    return (
        <>
            <div className="w-full flex flex-col items-center h-[75vh] mt-10 justify-between">
                <div className="w-[70%]">
                    <div>
                        <div className="flex border border-black rounded-lg items-center">
                            <div className="relative w-full">
                                <input
                                    value={amount == 0 ? '' : amount}
                                    onChange={handleAmount}
                                    type="number"
                                    className="block p-2.5 w-full z-20 text-sm text-gray-900 rounded-l-lg"
                                    placeholder="금액을 입력하세요"
                                />
                            </div>
                            <div className="w-0.5 h-7  bg-neutral-200"></div>
                            <button
                                id="dropdown-button"
                                className="flex-shrink-0 z-10 inline-flex items-center py-2.5 px-4 text-sm font-medium text-center text-gray-900 "
                                type="button"
                                onClick={() => setWonDropdownClick(!wonDropdownClick)}
                            >
                                {unit}
                                <svg
                                    className={`${wonDropdownClick ? 'rotate-180' : ''} w-2.5 h-2.5 ms-2.5`}
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
                                className={`${
                                    wonDropdownClick ? '' : 'hidden'
                                } absolute top-[14%] right-[15%] z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-16`}
                            >
                                <ul
                                    className="py-2 text-center text-sm text-gray-700"
                                    aria-labelledby="dropdown-button"
                                >
                                    {['₩', '￥'].map((unit) => (
                                        <li key={unit} onClick={() => handleUnit(unit)}>
                                            <div className="block px-4 py-2">{unit}</div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="w-[70%] flex flex-col gap-5 justify-start h-full m-10">
                    <div className="grid grid-cols-3">
                        <div className="col-span-1">적용환율</div>
                        <div className="col-span-2 flex gap-2 items-center">
                            <div>
                                <div>
                                    <input
                                        value={currency == 0 ? '' : currency}
                                        onChange={handleCurrency}
                                        type="number"
                                        className="block w-20 px-2 text-sm text-gray-900 border"
                                    />
                                </div>
                            </div>
                            <div>원 / 1 엔</div>
                        </div>
                    </div>
                    <div className="grid grid-cols-3">
                        <div className="col-span-1">결제날짜</div>
                        <div className="col-span-2">
                            <input
                                value={date}
                                onChange={handleDateChange}
                                type="date"
                                className="w-full text-sm text-gray-900 border py-1 px-2 rounded-lg"
                            />
                        </div>
                    </div>

                    <div className="grid grid-cols-3 items-center">
                        <div className="col-span-1">분류</div>
                        <div className="col-span-2 flex gap-1">
                            <MyButton
                                className={`${setButtonProp('private')} rounded-[8px]`}
                                isSelected={false}
                                type="small"
                                onClick={() => setIsPublic(false)}
                                text="개인"
                            ></MyButton>
                            <MyButton
                                className={`${setButtonProp('public')} rounded-[8px]`}
                                isSelected={false}
                                type="small"
                                onClick={() => setIsPublic(true)}
                                text="공동"
                            ></MyButton>
                        </div>
                    </div>
                    {isPublic ? (
                        <div className="flex flex-col gap-4">
                            <div className="grid grid-cols-3 items-center">
                                <div className="col-span-1">결제자</div>
                                <div className="col-span-2 grid grid-cols-4 gap-1 items-center">
                                    <div className="text-white col-span-1 color-bg-blue-2 w-7 h-7 flex justify-center shadow-lg items-center rounded-full">
                                        {payer[0]}
                                    </div>
                                    <button
                                        onClick={() => setPayerDropdownClick(!payerDropdownClick)}
                                        id="dropdown-button"
                                        className="flex col-span-3 items-center p-3 text-sm text-gray-900 border py-1 px-2 rounded-lg justify-between"
                                        type="button"
                                    >
                                        {payer} {payer == '김싸피' ? ' (나)' : ''}
                                        <svg
                                            className={`${payerDropdownClick ? 'rotate-180' : ''} w-2.5 h-2.5`}
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
                                        className={`${
                                            payerDropdownClick ? '' : 'hidden'
                                        } absolute top-[37%] right-[15%] z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-36`}
                                    >
                                        <ul
                                            className="py-2 text-sm text-gray-700 max-h-[30vh] overflow-y-scroll"
                                            aria-labelledby="dropdown-button"
                                        >
                                            {data.memberList.map((member, index) => (
                                                <li key={index} onClick={() => handlePayerChange(member.userName)}>
                                                    <div className="block px-4 py-2">{member.userName}</div>
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div className="grid grid-cols-3">
                                <div className="col-span-1">정산멤버</div>
                                <div className="col-span-2 flex max-h-[11vh] gap-2 overflow-y-scroll flex-col">
                                    {data.memberList.map((member, index) => (
                                        <div
                                            onClick={() => handlePayMember(member.userId)}
                                            key={index}
                                            className={`flex gap-2 ${
                                                payMember.map((member) => member.userId).includes(member.userId)
                                                    ? ''
                                                    : 'grayscale'
                                            } items-center`}
                                        >
                                            <div>
                                                {payMember.map((member) => member.userId).includes(member.userId)
                                                    ? 'x'
                                                    : '+'}
                                            </div>
                                            <div>
                                                <div className="text-white col-span-1 color-bg-blue-2 w-7 h-7 flex justify-center shadow-lg items-center rounded-full">
                                                    {member.userName[0]}
                                                </div>
                                            </div>
                                            {member.userName} {member.userName == '김싸피' ? ' (나)' : ''}
                                            <div></div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    ) : (
                        ''
                    )}

                    <div className="grid grid-cols-3 items-center">
                        <div className="col-span-1">카테고리</div>
                        <div className="grid grid-cols-5 col-span-2">
                            {categories.map((cat, index) => (
                                <div key={index} className="text-center" onClick={() => setCategory(cat)}>
                                    <div
                                        className={`${
                                            category !== cat ? 'bg-gray-200' : 'color-bg-blue-4 border-[#559bd9] border'
                                        } w-9 h-9 bg-gray-200 justify-center items-center rounded-full flex flex-col`}
                                    >
                                        {CategoryToImg(cat)}
                                    </div>
                                    <div className="text-sm">{cat}</div>
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="grid grid-cols-3">
                        <div className="col-span-1">내용</div>
                        <div className="w-full col-span-2">
                            <input
                                value={content}
                                onChange={handleContent}
                                type="text"
                                className="w-full text-sm text-gray-900 border py-1 px-2 rounded-lg"
                            />
                        </div>
                    </div>
                    <div className="grid grid-cols-3">
                        <div className="col-span-1">결제수단</div>
                        <div className="flex col-span-2">
                            <button
                                onClick={() => setTypeDropdownClick(!typeDropdownClick)}
                                id="type-input"
                                className={` flex items-center p-3 text-sm text-gray-900 border py-1 px-2 rounded-lg w-full justify-between`}
                                type="button"
                            >
                                {type || '선택하세요'}
                                <svg
                                    className={`${typeDropdownClick ? 'rotate-180' : ''} w-2.5 h-2.5`}
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
                                id="type-dropdown"
                                style={{ top: `${typeDropdownPosition}` }}
                                className={`${
                                    typeDropdownClick ? '' : 'hidden'
                                } absolute z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-48 dark:bg-gray-700`}
                            >
                                <ul
                                    className="py-2 text-sm text-gray-700 dark:text-gray-200"
                                    aria-labelledby="dropdown-button"
                                >
                                    <li onClick={() => handleTypeChange('카드')}>
                                        <div className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                                            카드
                                        </div>
                                    </li>
                                    <li onClick={() => handleTypeChange('현금')}>
                                        <div className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                                            현금
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="grid grid-cols-2 w-[90%] gap-2">
                    <MyButton
                        isSelected={true}
                        onClick={handleSave}
                        text="저장"
                        type="full"
                        className="py-1 text-white"
                    ></MyButton>
                    <MyButton
                        isSelected={true}
                        onClick={() => navigate(-1)}
                        text="취소"
                        type="full"
                        className="py-1 color-bg-blue-4 text-black"
                    ></MyButton>
                </div>
            </div>
        </>
    );
}
