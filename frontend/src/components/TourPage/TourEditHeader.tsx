import { BaseSyntheticEvent, useEffect, useState } from "react";
import { TourInfoDetail, MemberInfo } from "../../types/types";
import MyButton from "../Buttons/myButton";
import CheckModal from "../CheckModal";

interface PropType {
    tourInfo: TourInfoDetail;
    onChange: (type: string) => void;
}

export default function TourEditHeader(props: PropType) {

    const [data, setData] = useState<TourInfoDetail>();
    const [hostChangeModal, setHostChangeModal] = useState<boolean>(false);
    const [hoveredMember, setHoveredMember] = useState<MemberInfo | null>(null);
    
    const [title, setTitle] = useState<string>('');

    useEffect(() => {
        setData(props.tourInfo);
        setTitle(data ? data.tourTitle : "");
    }, [data])

    const isHost = (member: MemberInfo): string => {
        if (member.memberType === "host") {
            return "w-14 h-14 rounded-full flex border-gradient bg-gradient-to-b from-blue-500 via-blue-400 to-blue-100";
        }
        return "";
    };

    const handleMouseEnter = (member: MemberInfo) => {
        setHoveredMember(member);
    };

    const handleMouseLeave = () => {
        setHoveredMember(null);
    };

    const handleTypeChange = (type: string) => {
        // 수정에서 여행 정보로 돌아가
        //////////////////////
        // 그 전에 수정 api 먹이기
        console.log(title);
        props.onChange(type);
    };

    const handleInputChange = (event: BaseSyntheticEvent) => {
        setTitle(event.target.value);
    };

    const handleHost = () => {
        setHostChangeModal(true)
    }
    

    return (
        <div>
            {/* {hostChangeModal ? (
                <CheckModal
                    mainText="로그아웃 하시겠습니까?"
                    subText=""
                    OKText="확인"
                    CancelText="취소"
                    clickOK={logoutProceed}
                    clickCancel={closeModal}
                />
            ) : (
                <></>
            )} */}

            

            <div className="w-full grid grid-cols-6 justify-between items-end p-5 bak">
                <div className="col-span-5">
                    <div className="w-[90%] rounded-lg border border-solid border-neutral-300 text-3xl font-bold pl-5">
                        <input
                            value={title}
                            onChange={handleInputChange}
                            className="relative m-0 -mr-0.5 w-[90%] flex-auto bg-clip-padding py-1 outline-none"
                            aria-label="Title"
                            aria-describedby="button-addon1"
                        />

                    </div>
                    <div className="flex items-center mt-2">
                        {data?.memberList.map((member: MemberInfo, index: number) => (
                            <div key={index} className="relative" onMouseEnter={() => handleMouseEnter(member)} onMouseLeave={handleMouseLeave}>
                                <div className={`${isHost(member)}`} onClick={member.memberType == "host" ? handleHost : undefined }>
                                    <div className="shadow-lg m-1 font-bold text-3xl text-blue-500 bg-blue-200 w-12 h-12 rounded-full flex justify-center items-center">
                                        {member.userNickname[0]}
                                    </div>
                                </div>
                                {hoveredMember === member && (
                                    <div className="absolute whitespace-nowrap z-10 text-sm bottom-1 left-[40%] bg-gray-500 pl-1 pr-1 rounded-md text-white">
                                        {member.userNickname}
                                    </div>
                                )}
                                {member.memberType != "host"
                                    ? <div className="absolute top-[5%] left-[70%] bg-black text-white flex justify-center items-center z-10 w-[40%] h-[40%] rounded-full">x</div>
                                    : ""
                                }
                            </div>
                        ))}
                    </div>
                    <div className="text-[4vw]">{`${data?.startDate}~${data?.endDate}`}</div>
                    <div className="text-[5vw] flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6 m-1">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M15 10.5a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
                            <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1 1 15 0Z" />
                        </svg>

                        <div>{`${data?.cityList[0].cityName} ${
                            data?.cityList.length >= 2
                                ? '(+' + (data?.cityList.length - 1) + ')'
                                : ''
                        }`}</div>
                    </div>
                </div>
                <MyButton isSelected={true} text="완료" type="full" className="" onClick={() => handleTypeChange('default')}/>
                  
            </div>
        </div>
    );
}
