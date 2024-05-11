import { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { TourInfoDetail, MemberInfo } from '../../types/types';
import CheckModal from '../CheckModal';

import GhostProfile from '../../assets/image/ghostProfile.png';
import MapIcon from '../../assets/svg/mapIcon';

interface PropType {
    tourInfo: TourInfoDetail;
    onChange: (type: string) => void;
}

export default function TourHeader(props: PropType) {
    const navigate = useNavigate();

    const data = props.tourInfo;

    const [outModal, setOutModal] = useState<boolean>(false);
    const [deleteModal, setDeleteModal] = useState<boolean>(false);

    const [isClicked, setIsClicked] = useState<boolean>(false);
    const [hoveredMember, setHoveredMember] = useState<MemberInfo | null>(null);

    const dropdownRef = useRef(null);

    const isHost = (member: MemberInfo): string => {
        if (member.memberType === 'host') {
            return 'w-14 h-14 rounded-full flex border-gradient bg-gradient-to-b from-blue-500 via-blue-400 to-blue-100';
        }
        return '';
    };

    const handleMouseEnter = (member: MemberInfo) => {
        setHoveredMember(member);
    };

    const handleMouseLeave = () => {
        setHoveredMember(null);
    };

    const setDropdown = (isClicked: boolean) => {
        return isClicked ? '' : 'hidden';
    };

    const handleTypeChange = (type: string) => {
        if (type == 'edit') {
            props.onChange(type);
        }
        if (type == 'out') {
            setOutModal(true);
        }
        if (type == 'delete') {
            setDeleteModal(true);
        }
    };

    const handleClickOutside = (event: Event) => {
        if (dropdownRef.current) {
            const dropdownElement = dropdownRef.current as HTMLElement;
            if (dropdownElement && !dropdownElement.contains(event.target as Node)) {
                // 드롭다운 영역 외부를 클릭했을 때 드롭다운 닫기
                setIsClicked(false);
            }
        }
    };

    useEffect(() => {
        // 컴포넌트가 마운트될 때 전체 document에 클릭 이벤트 리스너 추가
        document.addEventListener('click', handleClickOutside);

        return () => {
            // 컴포넌트가 언마운트될 때 클릭 이벤트 리스너 제거
            document.removeEventListener('click', handleClickOutside);
        };
    }, []);

    const handleOutTour = () => {
        // 여행 나가기 처리

        setOutModal(false);
        window.location.href = `/`;
    };

    const closeOutModal = () => {
        setOutModal(false);
    };

    const handleDeleteTour = () => {
        // 여행 삭제 처리

        setDeleteModal(false);
        window.location.href = `/`;
    };

    const closeDeleteModal = () => {
        setDeleteModal(false);
    };

    const titleEllipsis = (text: string) => {
        console.log(text.length);
        if (text.length > 10) {
            return text.substring(0, 10) + '...';
        }
        return text;
    };

    return (
        <div>
            {outModal ? (
                <CheckModal
                    mainText={`[ ${titleEllipsis(data?.tourTitle)} ]에서\n나가시겠습니까?`}
                    subText="나의 여행 내역에서 삭제됩니다."
                    OKText="나가기"
                    CancelText="취소"
                    clickOK={handleOutTour}
                    clickCancel={closeOutModal}
                    className="whitespace-pre-wrap"
                />
            ) : (
                <></>
            )}

            {deleteModal ? (
                <CheckModal
                    mainText="여행을 삭제하시겠습니까?"
                    subText={`여행 삭제시 모든 멤버에게도 삭제됩니다.\n나의 내역에서만 지우고 싶다면 여행 나가기를 이용해주세요.`}
                    OKText="나가기"
                    CancelText="취소"
                    clickOK={handleDeleteTour}
                    clickCancel={closeDeleteModal}
                    className="whitespace-pre-wrap pt-1"
                />
            ) : (
                <></>
            )}
            <div className="w-full grid grid-cols-6 justify-between items-end p-5 bak">
                <div className="col-span-5">
                    <div className="text-[7vw] font-bold w-[100%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                        {data?.tourTitle}
                    </div>
                    <div className="text-[4vw]">{`${data?.startDate}~${data?.endDate}`}</div>

                    <div className="flex items-center w-[90vw] overflow-x-scroll h-[8vh]">
                        {data?.memberList.map((member: MemberInfo, index: number) => (
                            <div
                                key={index}
                                className="relative"
                                onMouseEnter={() => handleMouseEnter(member)}
                                onMouseLeave={handleMouseLeave}
                            >
                                <div className={`${isHost(member)}`}>
                                    {member.memberType !== 'ghost' ? (
                                        <div className="drop-shadow-lg m-1 font-bold text-3xl text-blue-500 bg-blue-200 w-12 h-12 rounded-full flex justify-center items-center">
                                            {member.userNickname[0]}
                                        </div>
                                    ) : (
                                        <div className="drop-shadow-lg m-1 font-bold text-3xl p-2 bg-gray-400 w-12 h-12 rounded-full flex justify-center items-center">
                                            <img src={GhostProfile}></img>
                                        </div>
                                    )}
                                </div>
                                {hoveredMember === member && (
                                    <div className="absolute whitespace-nowrap z-10 text-sm bottom-1 left-[40%] bg-gray-500 pl-1 pr-1 rounded-md text-white">
                                        {member.userNickname}
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                    <div className="text-[5vw] flex items-center mt-3 ">
                        <MapIcon />

                        <div className="flex flex-wrap">
                            {data.cityList.map((city, index) => (
                                <div key={index} className="whitespace-pre">
                                    {city.cityName}
                                    {index != data.cityList.length - 1 ? ', ' : ''}
                                </div>
                            ))}
                            {/* {data.cityList.length >= 1
                                ? `${data?.cityList[0].cityName} ${
                                      data?.cityList.length >= 2 ? '(+' + (data?.cityList.length - 1) + ')' : ''
                                  }`
                                : ``} */}
                        </div>
                    </div>
                </div>
                <div ref={dropdownRef} className="flex justify-center" onClick={() => setIsClicked(!isClicked)}>
                    <svg
                        className="w-6 h-6 text-gray-800"
                        aria-hidden="true"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 17 14"
                    >
                        <path
                            stroke="currentColor"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth="2"
                            d="M1 1h15M1 7h15M1 13h15"
                        />
                    </svg>

                    <div
                        id="dropdown"
                        className={`${setDropdown(
                            isClicked
                        )} absolute top-[25%] right-[10%] z-10 bg-white divide-y divide-gray-100 rounded-lg shadow`}
                    >
                        <ul className=" text-gray-700" aria-labelledby="dropdown-button">
                            <li
                                className="hover:bg-[#94cef2] py-2 px-5 rounded-t-lg border"
                                onClick={() => handleTypeChange('edit')}
                            >
                                <div className="flex gap-2 items-center">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                        strokeWidth="2"
                                        stroke="currentColor"
                                        className="w-5 h-5"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L6.832 19.82a4.5 4.5 0 0 1-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 0 1 1.13-1.897L16.863 4.487Zm0 0L19.5 7.125"
                                        />
                                    </svg>

                                    <div className="">여행 편집</div>
                                </div>
                            </li>
                            <li
                                className="hover:bg-[#94cef2] px-5 py-2 rounded-b-lg border"
                                onClick={() => handleTypeChange('out')}
                            >
                                <div className="flex gap-2 items-center">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                        strokeWidth="1.5"
                                        stroke="currentColor"
                                        className="w-5 h-5"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            d="M15.75 9V5.25A2.25 2.25 0 0 0 13.5 3h-6a2.25 2.25 0 0 0-2.25 2.25v13.5A2.25 2.25 0 0 0 7.5 21h6a2.25 2.25 0 0 0 2.25-2.25V15m3 0 3-3m0 0-3-3m3 3H9"
                                        />
                                    </svg>
                                    <div>여행 나가기</div>
                                </div>
                            </li>
                            <li
                                className="hover:bg-[#94cef2] px-5 py-2 rounded-b-lg border "
                                onClick={() => handleTypeChange('delete')}
                            >
                                <div className="flex gap-2 items-center">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                        strokeWidth="1.5"
                                        stroke="currentColor"
                                        className="w-5 h-5"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"
                                        />
                                    </svg>

                                    <div>여행 삭제</div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
}
