import { useState } from "react";

import { TourInfoDetail, MemberInfo } from "../../types/types";

import GhostProfile from "../../assets/image/ghostProfile.png";
import MapIcon from "../../assets/svg/mapIcon";

interface PropType {
    tourInfo: TourInfoDetail;
}

export default function TourScheduleHeader(props: PropType) {
    const [hoveredMember, setHoveredMember] = useState<MemberInfo | null>(null);

    const handleMouseEnter = (member: MemberInfo) => {
        setHoveredMember(member);
    };

    const handleMouseLeave = () => {
        setHoveredMember(null);
    };

    const isHost = (member: MemberInfo): string => {
        if (member.memberType === "host") {
            return "w-12 h-12 rounded-full flex border-gradient bg-gradient-to-b from-blue-500 via-blue-400 to-blue-100";
        }
        return "";
    };

    return (
        <div>
            <div className="grid grid-cols-6 justify-between items-end px-5vw py-2vw bak">
                <div className="col-span-5">
                    <div className="text-7vw font-bold w-[100%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                        {props.tourInfo.tourTitle}
                    </div>
                    <div className="text-4vw">{`${
                        props.tourInfo.startDate.split("T")[0]
                    }~${props.tourInfo.endDate.split("T")[0]}`}</div>

                    <div className="flex items-center w-full mt-3 overflow-x-scroll h-[40%]">
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
                                        {member.memberType !== "ghost" ? (
                                            <div className="drop-shadow-lg m-1 font-bold text-3xl text-blue-500 bg-blue-200 w-10 h-10 rounded-full flex justify-center items-center">
                                                {member.userNickname[0]}
                                            </div>
                                        ) : (
                                            <div className="drop-shadow-lg m-1 font-bold text-3xl p-2 bg-gray-400 w-10 h-10 rounded-full flex justify-center items-center">
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
                    <div className="text-5vw flex items-center mt-3">
                        <MapIcon />
                        <div className="flex flex-wrap">
                            {props.tourInfo.cityList.map((city, index) => (
                                <div key={index} className="whitespace-pre">
                                    {city.cityName}
                                    {index != props.tourInfo.cityList.length - 1
                                        ? ", "
                                        : ""}
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
