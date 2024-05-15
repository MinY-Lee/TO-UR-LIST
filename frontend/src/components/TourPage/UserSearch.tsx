import { useState } from "react";
import { MemberInfo, UserInfo } from "../../types/types";

import getUser from "../../dummy-data/get_user_userNickname.json";
import { searchUserByNickname } from "../../util/api/user";
import { HttpStatusCode } from "axios";
import SearchIcon from "../../assets/svg/searchIcon";
import { set } from "date-fns";

interface ChildProps {
    onChange: (updatedMember: MemberInfo) => void;
    memberList: MemberInfo[];
    isGhostHandle?: boolean;
}

interface SearchUser {
    userId: string;
    userName: string;
    userNickname: string;
}

export default function UserSearch(props: ChildProps) {
    const [query, setQuery] = useState<string>(""); // 검색어
    const [hasSearched, setHasSearched] = useState<boolean>(false);
    const [resultList, setResultList] = useState<SearchUser[]>([]); // 유저 검색 결과
    const [selectedMember, setSelectedMember] = useState<SearchUser>({
        userId: "",
        userNickname: "",
        userName: "",
    }); // 고스트 변경시에만 사용

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setQuery(event.target.value);
        if (event.target.value.trim() == "") {
            setHasSearched(false);
        }
    };

    const searchUser = () => {
        setHasSearched(true);
        if (query.trim() != "") {
            searchUserByNickname(query)
                .then((res) => {
                    let searchRes: SearchUser[] = [];
                    if (res.status == HttpStatusCode.Ok) {
                        setResultList(res.data.userInfoList);
                    }
                })
                .catch((err) => console.log(err));
        }
    };

    // 엔터로 검색
    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
            searchUser();
        }
    };

    const isOurTeamMember = (userNickname: string) => {
        return props.memberList
            .map((member) => member.userNickname)
            .includes(userNickname);
    };

    const handleAddMember = (member: SearchUser) => {
        let propItem = {
            userId: "",
            userName: "",
            userNickname: "",
            memberType: "",
        }; // 부모에게 보낼 요소

        if (props.isGhostHandle) {
            if (member != selectedMember) {
                setSelectedMember(member);
                propItem = {
                    userId: member.userId,
                    userNickname: member.userNickname,
                    userName: member.userName,
                    memberType: "guest",
                };
            } else {
                setSelectedMember({
                    userId: "",
                    userName: "",
                    userNickname: "",
                });
            }

            props.onChange(propItem);
        } else {
            props.onChange({
                userId: member.userId,
                userNickname: member.userNickname,
                userName: member.userName,
                memberType: "guest",
            });
        }
    };

    return (
        <div className="flex flex-col items-center color-bg-blue-4 rounded-lg p-2">
            <div className="flex w-[90%] rounded-lg border border-solid border-black bg-white">
                <input
                    type="search"
                    value={query}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown}
                    className="relative m-0 -mr-0 w-[1px] flex-auto bg-transparent bg-clip-padding pl-3 font-normal outline-none"
                    placeholder="닉네임을 검색하세요"
                    aria-label="Search"
                    aria-describedby="button-addon1"
                />

                <button
                    className="relative z-[2] flex items-center px-3 py-2.5 text-xs font-medium"
                    onClick={searchUser}
                    type="button"
                    id="button-addon1"
                >
                    <SearchIcon />
                </button>
            </div>
            <div className="m-2 h-[20vh] flex flex-col gap-2 overflow-scroll w-[90%]">
                {resultList.length == 0 ? (
                    <div className=" text-lg text-center text-gray-500">
                        {hasSearched ? "검색 결과가 없습니다." : ""}
                    </div>
                ) : (
                    resultList.map((res, index) => (
                        <div
                            onClick={
                                !isOurTeamMember(res.userNickname)
                                    ? () => handleAddMember(res)
                                    : undefined
                            }
                            key={index}
                            className={`${
                                selectedMember == res
                                    ? "color-bg-blue-3 m-0"
                                    : ""
                            } flex  justify-between px-2 items-center`}
                        >
                            <div className="text-lg">
                                {res.userNickname} ({res.userName})
                            </div>
                            <div
                                hidden={!isOurTeamMember(res.userNickname)}
                                className="color-text-blue-1"
                            >
                                멤버
                            </div>
                            <div
                                hidden={selectedMember != res}
                                className="text-white"
                            >
                                선택
                            </div>
                        </div>
                    ))
                )}
            </div>
            <div></div>
        </div>
    );
}
