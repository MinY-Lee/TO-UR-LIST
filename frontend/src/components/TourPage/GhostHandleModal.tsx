import { BaseSyntheticEvent, useRef, useEffect, useState } from 'react';
import MyButton from '../Buttons/myButton';
import UserSearch from './UserSearch';
import { MemberInfo, TourInfoDetail, UserInfo } from '../../types/types';

interface Proptype {
    selectedGhostMember: MemberInfo;
    closeGhostHandleModal : ()=> void;
}

export default function GhostHandleModal(props: Proptype) {
    const [topOffset, setTopOffset] = useState<number>(0);
    const [memberList, setMemberList] = useState<MemberInfo[]>([]);
    const [ghostNickname, setGhostNickname] = useState<string>('');
    const [selectedUser, setSelectedUser] = useState<MemberInfo>({
        userId: "",
        userNickname: "",
        userName: "",
        memberType: ""
    });
    
    const divRef = useRef<HTMLDivElement>(null);

    useEffect(() => {

        if (divRef.current) {
            const height = divRef.current.clientHeight;
            const windowHeight = window.innerHeight;
            const calculatedTopOffset = (windowHeight - height) / 2;

            setTopOffset(calculatedTopOffset);
        }

        setGhostNickname(props.selectedGhostMember.userNickname);

    }, []);

    const onChange = (updatedMember: MemberInfo) => {
        // updatedMemberList에서 updatedMember의 id와 동일한 요소를 찾기
        // const existingMember = updatedMemberList.find(member => member.userId === updatedMember.userId);
      
        // // existingMember가 존재하면 기존 멤버를 업데이트하고, 존재하지 않으면 새로운 멤버 추가
        // const newMembers = existingMember
        //   ? updatedMemberList.map(member => (member.userId === updatedMember.userId ? updatedMember : member))
        //   : [...updatedMemberList, updatedMember];
      
        // setUpdatedMemberList(newMembers);
      }

      const handleInputChange = (event: BaseSyntheticEvent) => {
        setGhostNickname(event.target.value);
    };

    const handleDone = () => {
        if (selectedUser) {
            console.log("고스트 -> 게스트 변경 완료")
            console.log(selectedUser);
        } else {
            console.log("고스트 이름 변경 완료")
            console.log(ghostNickname);
        }
        
        props.closeGhostHandleModal();
    }
      
      
    return (
        <>
            <div className="absolute w-full h-full top-0 left-0 z-20 bg-black opacity-50"></div>
            <div
                ref={divRef}
                className={`absolute gap-2 p-5 w-[80%] left-[10%] top-[${topOffset}px] z-30 bg-white flex flex-col justify-evenly items-center border-[0.5vw] color-border-blue-2`}
                style={{ borderRadius: '2vw' }}
            >
                <div className="w-full flex flex-col justify-center text-2xl font-bold">
                    고스트 멤버 관리
                </div>
                <div className='w-[90%]'>
                    <div className='w-full'>
                        <div className='text-xl font-semibold'>고스트 이름 변경</div>
                        <div className='w-full text-center'>
                            <input
                                value={ghostNickname}
                                onChange={handleInputChange}
                                className="w-[90%] border-neutral-400 border py-2 m-0 mr-0.5 px-2 flex-auto bg-clip-padding outline-none"
                                aria-label="GhostNickname"
                                aria-describedby="button-addon1"
                                placeholder='새로운 고스트 닉네임을 지어주세요.'
                            />
                        </div>
                    </div>
                    <div className='w-full'>
                        <div className='text-xl font-semibold'>고스트를 게스트로 변경</div>
                        <div className=''>*변경해도 고스트의 기록은 남아있어요!</div>
                        <div className="w-full">
                            <UserSearch onChange={onChange} memberList={memberList} />
                        </div>
                    </div>
                </div>
                
                <div className="w-full">
                    <MyButton
                        isSelected={true}
                        onClick={() => handleDone()}
                        text="변경완료"
                        type="full"
                        className="font-medium py-2"
                    />
                </div>
            </div>
        </>
    );
}
