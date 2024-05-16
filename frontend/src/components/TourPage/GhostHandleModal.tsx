import { BaseSyntheticEvent, useRef, useEffect, useState } from "react";
import MyButton from "../Buttons/myButton";
import UserSearch from "./UserSearch";
import { MemberInfo, TourInfoDetail, UserInfo } from "../../types/types";
import { changeGhostNickname, changeGhostToGuest } from "../../util/api/tour";
import { HttpStatusCode } from "axios";

interface Proptype {
    selectedGhostMember: MemberInfo;
    memberList: MemberInfo[];
    tourId: string;
    onUpdate: (updatedMember: MemberInfo, ghostId?: string) => void;
    closeGhostHandleModal: () => void;
}

export default function GhostHandleModal(props: Proptype) {
    const [topOffset, setTopOffset] = useState<number>(0);
    const [memberList, setMemberList] = useState<MemberInfo[]>([]);
    const [ghostNickname, setGhostNickname] = useState<string>("");
    const [selectedUser, setSelectedUser] = useState<MemberInfo>({
        userId: "",
        userNickname: "",
        userName: "",
        memberType: "",
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
        setMemberList(props.memberList);
    }, []);

    const onChange = (updatedMember: MemberInfo) => {
        console.log(updatedMember);
        // 값 비어서 옴 = 고스트 변경 x
        if (updatedMember.userId != "") {
            setSelectedUser(updatedMember);
        }
    };

    const handleInputChange = (event: BaseSyntheticEvent) => {
        setGhostNickname(event.target.value);
    };

    const handleDone = () => {
        if (selectedUser.userId != "") {
            if (props.tourId) {
                changeGhostToGuest({
                    tourId: props.tourId,
                    ghostId: props.selectedGhostMember.userId,
                    ghostNickname: props.selectedGhostMember.userNickname,
                    userId: selectedUser.userId,
                    userNickname: selectedUser.userNickname,
                })
                    .then((res) => {
                        if (res.status == HttpStatusCode.Ok) {
                            props.onUpdate(
                                {
                                    userId: selectedUser.userId,
                                    userNickname: selectedUser.userNickname,
                                    userName: selectedUser.userName,
                                    memberType: "guest",
                                },
                                props.selectedGhostMember.userId
                            );
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }
        } else {
            if (props.tourId) {
                changeGhostNickname({
                    tourId: props.tourId,
                    ghostId: props.selectedGhostMember.userId,
                    ghostNickname: ghostNickname,
                })
                    .then((res) => {
                        if (res.status == HttpStatusCode.Ok) {
                            props.onUpdate({
                                userId: props.selectedGhostMember.userId,
                                userNickname: ghostNickname,
                                userName: props.selectedGhostMember.userName,
                                memberType: "ghost",
                            });
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }
        }

        props.closeGhostHandleModal();
    };

    return (
        <>
            <div className="absolute w-full h-full top-0 left-0 z-20 bg-black opacity-50"></div>
            <div
                ref={divRef}
                className={`absolute gap-3 p-5 w-[80%] left-[10%] z-30 bg-white flex flex-col justify-evenly items-center border-[0.5vw] color-border-blue-2`}
                style={{ borderRadius: "2vw", top: `${topOffset}px` }}
            >
                <div className="w-full flex flex-col justify-center text-2xl font-bold">
                    고스트 멤버 관리
                </div>
                <div className="w-[90%] flex flex-col gap-5">
                    <div className="w-full flex flex-col gap-2">
                        <div className="text-xl font-semibold">
                            고스트 이름 변경
                        </div>
                        <div className="w-full text-center">
                            <input
                                value={ghostNickname}
                                onChange={handleInputChange}
                                className="w-[90%] border-neutral-400 border py-2 m-0 mr-0.5 px-2 flex-auto bg-clip-padding outline-none"
                                aria-label="GhostNickname"
                                aria-describedby="button-addon1"
                                placeholder="새로운 고스트 닉네임을 지어주세요."
                            />
                        </div>
                    </div>
                    <div className="w-full  flex flex-col gap-2">
                        <div>
                            <div className="text-xl font-semibold">
                                고스트를 게스트로 변경
                            </div>
                            <div className="">
                                *변경해도 고스트의 기록은 남아있어요!
                            </div>
                        </div>
                        <div className="w-full">
                            <UserSearch
                                onChange={onChange}
                                memberList={memberList}
                                isGhostHandle={true}
                            />
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
