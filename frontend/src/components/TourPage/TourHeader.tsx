import { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { TourInfoDetail, MemberInfo } from '../../types/types';
import CheckModal from '../CheckModal';

import GhostProfile from '../../assets/image/ghostProfile.png';
import MapIcon from '../../assets/svg/mapIcon';
import { deleteTour, quitTour } from '../../util/api/tour';
import MenuIcon from '../../assets/svg/menuIcon';
import { HttpStatusCode } from 'axios';
import OutIcon from '../../assets/svg/outIcon';
import PencilIcon from '../../assets/svg/pencilIcon';
import TrashIcon from '../../assets/svg/trashIcon';

interface PropType {
    tourId: string;
    tourInfo: TourInfoDetail;
    onChange: (type: string) => void;
}

export default function TourHeader(props: PropType) {
    const navigate = useNavigate();

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
            if (
                dropdownElement &&
                !dropdownElement.contains(event.target as Node)
            ) {
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
        quitTour(props.tourId)
            .then((res) => {
                if (res.status == HttpStatusCode.Ok) {
                    setOutModal(false);
                    window.location.href = `/`;
                }
            })
            .catch((err) => console.log(err));
    };

    const closeOutModal = () => {
        setOutModal(false);
    };

    const handleDeleteTour = () => {
        deleteTour(props.tourId)
            .then((res) => {
                if (res.status == HttpStatusCode.Ok) {
                    setDeleteModal(false);
                    window.location.href = `/`;
                }
            })
            .catch((err) => console.log(err));
    };

    const closeDeleteModal = () => {
        setDeleteModal(false);
    };

    const titleEllipsis = (text: string) => {
        if (text.length > 10) {
            return text.substring(0, 10) + '...';
        }
        return text;
    };
    return (
        <div>
            {outModal ? (
                <CheckModal
                    mainText={`[ ${titleEllipsis(
                        props.tourInfo.tourTitle
                    )} ] 에서\n나가시겠습니까?`}
                    subText="나의 여행 내역에서 삭제됩니다."
                    OKText="나가기"
                    CancelText="취소"
                    clickOK={handleOutTour}
                    clickCancel={closeOutModal}
                    className="whitespace-pre-wrap text-center"
                />
            ) : (
                <></>
            )}

            {deleteModal ? (
                <CheckModal
                    mainText="여행을 삭제하시겠습니까?"
                    subText={`여행 삭제시 모든 멤버에게도 삭제됩니다.\n여행 내역 삭제는 여행 나가기를 이용해주세요.`}
                    OKText="삭제"
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
                    <div className="text-7vw font-bold w-[100%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                        {props.tourInfo.tourTitle}
                    </div>
                    <div className="text-4vw">{`${
                        props.tourInfo.startDate.split('T')[0]
                    }~${props.tourInfo.endDate.split('T')[0]}`}</div>

                    <div className="flex items-center w-full overflow-x-scroll h-[40%]">
                        {props.tourInfo.memberList.map(
                            (member: MemberInfo, index: number) => (
                                <div
                                    key={index}
                                    className="relative"
                                    onMouseEnter={() =>
                                        handleMouseEnter(member)
                                    }
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
                            )
                        )}
                    </div>
                    <div className="text-5vw flex items-center mt-3 ">
                        <MapIcon />
                        <div className="flex flex-wrap">
                            {props.tourInfo.cityList.map((city, index) => (
                                <div key={index} className="whitespace-pre">
                                    {city.cityName}
                                    {index != props.tourInfo.cityList.length - 1
                                        ? ', '
                                        : ''}
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
                <div
                    ref={dropdownRef}
                    className="flex justify-center"
                    onClick={() => setIsClicked(!isClicked)}
                >
                    <MenuIcon />

                    <div
                        id="dropdown"
                        className={`${setDropdown(
                            isClicked
                        )} absolute top-[25%] right-[10%] z-10 bg-white divide-y divide-gray-100 rounded-lg shadow`}
                    >
                        <ul
                            className=" text-gray-700"
                            aria-labelledby="dropdown-button"
                        >
                            <li
                                className="hover:bg-[#94cef2] py-2 px-5 rounded-t-lg border"
                                onClick={() => handleTypeChange('edit')}
                            >
                                <div className="flex gap-2 items-center">
                                    <PencilIcon />
                                    <div className="">여행 편집</div>
                                </div>
                            </li>
                            <li
                                className="hover:bg-[#94cef2] px-5 py-2 rounded-b-lg border"
                                onClick={() => handleTypeChange('out')}
                            >
                                <div className="flex gap-2 items-center">
                                    <OutIcon />
                                    <div>여행 나가기</div>
                                </div>
                            </li>
                            <li
                                className="hover:bg-[#94cef2] px-5 py-2 rounded-b-lg border "
                                onClick={() => handleTypeChange('delete')}
                            >
                                <div className="flex gap-2 items-center">
                                    <TrashIcon />
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
