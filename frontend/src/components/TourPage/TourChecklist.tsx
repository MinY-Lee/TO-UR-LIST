import { useEffect, useState } from "react";
import { TourInfoDetail, MemberInfo } from "../../types/types";

export default function TourCheckList(props: TourInfoDetail) {
    const data = props.data;

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
            <div className="w-full justify-between items-end p-5 bak">
                    
                <div className="text-xl font-bold">
                    전체 체크리스트
                </div>
                <div>

                </div>
                
                
            </div>
        </>
    );
}
