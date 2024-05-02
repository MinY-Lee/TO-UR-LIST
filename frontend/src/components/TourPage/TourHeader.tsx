import { useEffect, useState } from "react";
import { TourInfoDetail, MemberInfo } from "../../types/types";

interface PropType {
    tourInfo: TourInfoDetail;
}

export default function TourHeader(props: PropType) {
    const data = props.tourInfo;

    const [hoveredMember, setHoveredMember] = useState<MemberInfo | null>(null);

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

    return (
        <>
            <div className="w-full grid grid-cols-6 justify-between items-end p-5 bak">
                <div className="col-span-5">
                    <div className="text-[7vw] font-bold w-[100%] overflow-ellipsis overflow-hidden whitespace-nowrap">{data?.tourTitle}</div>
                    <div className="flex items-center mt-2">
                        {data?.memberList.map((member: MemberInfo, index: number) => (
                            <div key={index} className="relative" onMouseEnter={() => handleMouseEnter(member)} onMouseLeave={handleMouseLeave}>
                                <div className={`${isHost(member)}`}>
                                    <div className="shadow-lg m-1 font-bold text-3xl text-blue-500 bg-blue-200 w-12 h-12 rounded-full flex justify-center items-center">
                                        {member.userNickname[0]}
                                    </div>
                                </div>
                                {hoveredMember === member && (
                                    <div className="absolute whitespace-nowrap z-10 text-sm bottom-1 left-[40%] bg-gray-500 pl-1 pr-1 rounded-md text-white">
                                        {member.userNickname}
                                    </div>
                                )}
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
                <div className="flex justify-center">
                    <svg className="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 17 14">
                        <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M1 1h15M1 7h15M1 13h15"/>
                    </svg>
                </div>
            </div>
        </>
    );
}
