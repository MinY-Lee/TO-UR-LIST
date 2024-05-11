import { useState } from 'react';
import { MemberInfo } from '../../types/types';
import GhostProfile from '../../assets/image/ghostProfile.png';
import CancelIcon from '../../assets/svg/cancelIcon';

interface PropType {
    setAddModalClicked: (clicked: boolean) => void;
    handleGhostModal: (member: MemberInfo) => void;
    handleMemberDeleteModal: (member: MemberInfo, event: React.MouseEvent<HTMLDivElement>) => void;
    handleHost: () => void;
    memberList: MemberInfo[];
}

export default function EditMemberList(props: PropType) {
    const [hoveredMember, setHoveredMember] = useState<MemberInfo | null>(null);

    const handleMouseEnter = (member: MemberInfo) => {
        setHoveredMember(member);
    };

    const handleMouseLeave = () => {
        setHoveredMember(null);
    };

    const isHost = (member: MemberInfo): string => {
        if (member.memberType === 'host') {
            return 'w-14 h-14 rounded-full flex border-gradient bg-gradient-to-b from-blue-500 via-blue-400 to-blue-100';
        }
        return '';
    };
    return (
        <>
            <div
                onClick={() => props.setAddModalClicked(true)}
                className="border-dashed border-2 color-text-blue-2 border-[#5faad9] m-1 font-bold text-3xl w-12 h-12 rounded-full flex justify-center items-center"
            >
                +
            </div>
            {props.memberList &&
                props.memberList?.length >= 1 &&
                props.memberList.map((member: MemberInfo, index: number) => (
                    <div
                        key={index}
                        className="relative"
                        onClick={member.memberType == 'ghost' ? () => props.handleGhostModal(member) : () => {}}
                        onMouseEnter={() => handleMouseEnter(member)}
                        onMouseLeave={handleMouseLeave}
                    >
                        <div
                            className={`${isHost(member)}`}
                            onClick={member.memberType == 'host' ? props.handleHost : undefined}
                        >
                            {member.memberType !== 'ghost' ? (
                                <div className="drop-shadow-lg rounded-full m-1 font-bold text-3xl text-blue-500 bg-blue-200 w-12 h-12 flex justify-center items-center">
                                    {member.userNickname[0]}
                                </div>
                            ) : (
                                <div className="m-1 drop-shadow-lg font-bold text-3xl p-2 bg-gray-400 w-12 h-12 rounded-full flex justify-center items-center">
                                    <img src={GhostProfile}></img>
                                </div>
                            )}
                        </div>
                        {hoveredMember === member && (
                            <div className="absolute whitespace-nowrap z-10 text-sm bottom-1 left-[40%] bg-gray-500 pl-1 pr-1 rounded-md text-white">
                                {member.userName}
                            </div>
                        )}
                        {member.memberType != 'host' ? (
                            <div
                                onClick={(event) => props.handleMemberDeleteModal(member, event)}
                                className="absolute top-[5%] left-[70%] bg-black text-white flex justify-center items-center z-10 w-5 h-5 rounded-full"
                            >
                                <CancelIcon />
                            </div>
                        ) : (
                            ''
                        )}
                    </div>
                ))}
        </>
    );
}
