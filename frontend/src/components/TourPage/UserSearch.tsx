import { useState } from 'react';
import { MemberInfo, UserInfo } from '../../types/types';

import getUser from '../../dummy-data/get_user_userNickname.json';

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
    const [query, setQuery] = useState<string>(''); // 검색어
    const [hasSearched, setHasSearched] = useState<boolean>(false);
    const [resultList, setResultList] = useState<SearchUser[]>([]); // 유저 검색 결과
    const [selectedMember, setSelectedMember] = useState<SearchUser>({
        userId: '',
        userNickname: '',
        userName: '',
    }); // 고스트 변경시에만 사용

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setQuery(event.target.value);
    };

    const escapeRegExp = (text: string) => {
        return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&');
    };

    const searchUser = () => {
        setHasSearched(true);
        // 유저 검색 api
        // 임시로 더미데이터에서 찾기
        const result: SearchUser[] = [];
        getUser.forEach((user: SearchUser) => {
            const text = user.userNickname;
            // searchText를 정규 표현식으로 변환
            const regexText = escapeRegExp(query);
            const regex = new RegExp(regexText, 'i'); // "i" 플래그는 대소문자 구분 없이 검색

            if (regex.test(text)) {
                result.push(user);
            }
        });

        setResultList(result);
    };

    // 엔터로 검색
    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Enter') {
            searchUser();
        }
    };

    const isOurTeamMember = (userNickname: string) => {
        return props.memberList.map((member) => member.userNickname).includes(userNickname);
    };

    const handleAddMember = (member: SearchUser) => {
        let propItem = {
            userId: '',
            userName: '',
            userNickname: '',
            memberType: '',
        }; // 부모에게 보낼 요소

        if (props.isGhostHandle) {
            if (member != selectedMember) {
                setSelectedMember(member);
                propItem = {
                    userId: member.userId,
                    userNickname: member.userNickname,
                    userName: member.userName,
                    memberType: 'guest',
                };
            } else {
                setSelectedMember({
                    userId: '',
                    userName: '',
                    userNickname: '',
                });
            }

            props.onChange(propItem);
        } else {
            props.onChange({
                userId: member.userId,
                userNickname: member.userNickname,
                userName: member.userName,
                memberType: 'guest',
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
                    placeholder="유저 닉네임을 검색해보세요"
                    aria-label="Search"
                    aria-describedby="button-addon1"
                />

                <button
                    className="relative z-[2] flex items-center px-3 py-2.5 text-xs font-medium"
                    onClick={searchUser}
                    type="button"
                    id="button-addon1"
                >
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="black" className="h-5 w-5">
                        <path
                            fillRule="evenodd"
                            d="M9 3.5a5.5 5.5 0 100 11 5.5 5.5 0 000-11zM2 9a7 7 0 1112.452 4.391l3.328 3.329a.75.75 0 11-1.06 1.06l-3.329-3.328A7 7 0 012 9z"
                            clipRule="evenodd"
                        />
                    </svg>
                </button>
            </div>
            <div className="m-2 h-[20vh] flex flex-col gap-2 overflow-scroll w-[90%]">
                {hasSearched && resultList.length === 0 ? (
                    <div className=" text-lg text-center text-gray-500">검색 결과가 없습니다.</div>
                ) : (
                    resultList.map((res, index) => (
                        <div
                            onClick={!isOurTeamMember(res.userNickname) ? () => handleAddMember(res) : undefined}
                            key={index}
                            className={`${
                                selectedMember == res ? 'color-bg-blue-3 m-0' : ''
                            } flex  justify-between px-2 items-center`}
                        >
                            <div className="text-lg">
                                {res.userNickname} ({res.userName})
                            </div>
                            <div hidden={!isOurTeamMember(res.userNickname)} className="color-text-blue-1">
                                멤버
                            </div>
                            <div hidden={selectedMember != res} className="text-white">
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
