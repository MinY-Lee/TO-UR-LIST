import { useRef, useEffect, useState } from 'react';
import MyButton from "../Buttons/myButton";
import UserSearch from "../TourPage/UserSearch";
import { MemberInfo, TourInfoDetail, UserInfo } from '../../types/types';

interface Proptype {
    data: TourInfoDetail;
}

export default function AddMemberModal(props: Proptype) {
    const [topOffset, setTopOffset] = useState(0);
    const [memberList, setMemberList] = useState<MemberInfo[]>([]);
    const [updatedMemberList, setUpdatedMemberList] = useState<MemberInfo[]>([]);
    const divRef = useRef(null);

    useEffect(() => {
        setMemberList(props.data.memberList);

        if (divRef.current) {
          const height = divRef.current.clientHeight;
          const windowHeight = window.innerHeight;
          const calculatedTopOffset = (windowHeight - height) / 2;
        
          setTopOffset(calculatedTopOffset);
        }

      }, [topOffset]);

    const onChange = (updatedMember: MemberInfo) => {
        console.log("멤버 변동 : " + updatedMember.userNickname);

        let newMembers: MemberInfo[] = [...updatedMemberList];
        !memberList.includes(updatedMember) ? [...newMembers, updatedMember] : ""
        

        setUpdatedMemberList(newMembers);

    }

    return (
        <>
            <div className="absolute w-full h-full top-0 left-0 z-20 bg-black opacity-50"></div>
            <div
                ref={divRef}
                className={`absolute gap-2 p-5 w-[80%] left-[10%] top-[${topOffset}px] z-30 bg-white flex flex-col justify-evenly items-center border-[0.5vw] color-border-blue-2`}
                style={{ borderRadius: '2vw' }}
            >
                <div className="w-full flex flex-col justify-center text-xl font-bold">
                    멤버 추가하기
                </div>
                <div className='w-full'>
                    <UserSearch onChange={onChange} memberList={memberList} />
                </div>
                <div className='flex gap-2 w-full px-2 flex-wrap max-h-10vh] overflow-scroll' >
                    {updatedMemberList.map((member: MemberInfo) => (
                        <div key={member.userId} className={`flex px-2 gap-3 ${member.memberType == "ghost" ? "bg-gray-300" : "color-bg-blue-3"} rounded-full`}>
                            <div>x</div>
                            <div className='whitespace-nowrap'>{member.userNickname} ({member.userName})</div>
                        </div>
                    ))}
                </div>
                <div className="border-dashed border-black border-2 w-[90%] text-center py-3">
                    + 고스트멤버 추가하기 👻
                </div>
                <div className="w-[90%]">
                    <div className="font-bold">*고스트멤버란?</div>
                    <div className="whitespace-pre-line">
                        {`TO-UR-LIST 회원은 아니지만, 정확한 정산을 위해 임시로 넣어놓은 멤버를 말해요!\n추후 실제 회원으로 전환이 가능해요. `}
                    </div>
                </div>
                <div className="w-full">    
                    <MyButton isSelected={true} onClick={() => {}} text="추가하기" type="full" className="font-medium py-2"/>
                </div>
            </div>
        </>
    );
}
