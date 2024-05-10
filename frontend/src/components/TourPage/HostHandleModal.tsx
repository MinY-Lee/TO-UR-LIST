import { useRef, useEffect, useState } from 'react';
import MyButton from '../Buttons/myButton';
import { MemberInfo, TourInfoDetail, UserInfo } from '../../types/types';

interface Proptype {
    selectedHostMember: MemberInfo;
    data: TourInfoDetail;
    closeHostHandleModal: () => void;
}

export default function HostHandleModal(props: Proptype) {
    const [topOffset, setTopOffset] = useState<number>(0);
    const [memberList, setMemberList] = useState<MemberInfo[]>([]);
    const [filteredMemberList, setFilteredMemberList] = useState<MemberInfo[]>([]);
    const [defaultHost, setDefaultHost] = useState<MemberInfo>({
        userId: '',
        userNickname: '',
        userName: '',
        memberType: '',
    });
    const [selectedUser, setSelectedUser] = useState<MemberInfo>({
        userId: '',
        userNickname: '',
        userName: '',
        memberType: '',
    });

    const divRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (divRef.current) {
            const height = divRef.current.clientHeight;
            const windowHeight = window.innerHeight;
            const calculatedTopOffset = (windowHeight - height) / 2;

            setTopOffset(calculatedTopOffset);
        }

        setMemberList(props.data.memberList);

        // 호스트 멤버 찾기
        memberList.forEach((member) => {
            if (member.memberType == 'host') {
                setDefaultHost(member);
            }
        });

        // 고스트 멤버 필터링
        const filtered = memberList.filter((member) => {
            return member.memberType != 'ghost';
        });
        setFilteredMemberList(filtered);
    }, [memberList]);

    const onChange = (updatedMember: MemberInfo) => {
        console.log(updatedMember);
        // 값 비어서 옴 = 호스트 변경 x
        if (updatedMember.userId != '') {
            setSelectedUser(updatedMember);
        }
    };

    const handleDone = () => {
        if (selectedUser.userId != '') {
            console.log('호스트 변경 완료');
            console.log(selectedUser);
        }

        props.closeHostHandleModal();
    };

    const handleHostMember = (member: MemberInfo) => {
        let item = {
            userId: '',
            userNickname: '',
            userName: '',
            memberType: '',
        };

        if (selectedUser.userId != '') {
            if (selectedUser != member) {
                item = member;
            }
        } else {
            item = member;
        }
        setSelectedUser(item);
    };

    return (
        <>
            <div className="absolute w-full h-full top-0 left-0 z-20 bg-black opacity-50"></div>
            <div
                ref={divRef}
                className={`absolute gap-3 p-5 w-[80%] left-[10%] z-30 bg-white flex flex-col justify-evenly items-center border-[0.5vw] color-border-blue-2`}
                style={{ borderRadius: '2vw', top: `${topOffset}px` }}
            >
                <div className="w-full ">
                    <div className="w-full flex flex-col justify-center text-2xl font-bold">호스트 위임하기</div>
                    <div className="w-full flex flex-col justify-center">*고스트는 호스트가 될 수 없어요.</div>
                </div>
                <div className="w-full rounded-lg color-bg-blue-4 p-5 max-h-[30vh] flex flex-col gap-2 overflow-y-scroll">
                    {filteredMemberList.map((member, index) => (
                        <div
                            key={index}
                            className={`grid grid-cols-2 px-2 ${selectedUser == member ? 'color-bg-blue-3' : ''}`}
                            onClick={() => handleHostMember(member)}
                        >
                            <div key={index} className={`font-semibold `}>
                                {member.userNickname} ({member.userName})
                            </div>

                            {defaultHost == member ? (
                                <div className="text-end text-neutral-500">현재 호스트(나)</div>
                            ) : (
                                ''
                            )}
                            {selectedUser == member ? <div className="text-end text-white">선택</div> : ''}
                        </div>
                    ))}
                </div>

                <div className="w-full">
                    <MyButton
                        isSelected={true}
                        onClick={() => handleDone()}
                        text="위임하기"
                        type="full"
                        className="font-medium py-2"
                    />
                </div>
            </div>
        </>
    );
}
