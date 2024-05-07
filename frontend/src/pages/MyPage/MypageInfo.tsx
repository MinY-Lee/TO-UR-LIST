import { useSelector } from 'react-redux';
import { UserInfo } from '../../types/types';
import { BaseSyntheticEvent, useEffect, useState } from 'react';
import GenderSelectBox from '../../components/MyPage/GenderSelectBox';
import TabBarMain from '../../components/TabBar/TabBarMain';
import { useNavigate } from 'react-router-dom';
import {
    changeBirthDay,
    changeGender,
    changeName,
    changeNickname,
    checkDuplicatedNick,
} from '../../util/api/user';
import { httpStatusCode } from '../../util/api/http-status';

export default function MypageInfo() {
    const userInfo: UserInfo = useSelector((state: any) => state.userSlice);

    const [userName, setUserName] = useState<string>(userInfo.userName);
    const [userNickname, setUserNickname] = useState<string>(
        userInfo.userNickname
    );
    const [userBirthDay, setUserBirthDay] = useState<string>(
        userInfo.userBirth ? userInfo.userBirth.replaceAll('-', '.') : ''
    );

    const [userGender, setUserGender] = useState<number>(userInfo.userGender);
    //0 : selectbox close, 1: selectbox open
    const [selectBoxMode, setSelectBoxMode] = useState<number>(0);

    //유효성 검사
    const [isValidName, setIsValidName] = useState<boolean>(true);
    const [isValidNickname, setIsValidNickname] = useState<boolean>(true);
    const [isNicknameDupleChecked, setIsNicknameDupleChecked] =
        useState<boolean>(true);
    const [nicknameMsg, setNicknameMsg] = useState<string>('');
    const [isValidBirthday, setIsValidBirthDay] = useState<boolean>(true);

    const [isChangePossible, setIsChangePossible] = useState<boolean>(true);

    //navigator
    const navigate = useNavigate();

    useEffect(() => {
        if (userName.length >= 2 && userName.length <= 8) {
            setIsValidName(true);
        } else {
            setIsValidName(false);
        }
    }, [userName]);

    useEffect(() => {
        setNicknameMsg('닉네임은 최소 2자, 최대 15자로 구성되어야 합니다.');
        if (userNickname.length >= 2 && userNickname.length <= 15) {
            if (userNickname !== '(알 수 없음)') {
                setNicknameMsg('닉네임이 중복되었는지 확인해 주세요.');
                setIsValidNickname(true);
                return;
            }
            //길이는 맞으나 예약어
            setNicknameMsg('예약어는 사용 불가능합니다.');
        }
        setIsValidNickname(false);
    }, [userNickname]);

    useEffect(() => {
        //길이 제한
        if (userBirthDay.length !== 10) {
            setIsValidBirthDay(false);
            return;
        }
        //날짜 유효성 검사
        const now = new Date();
        const [yearS, monthS, dayS] = userBirthDay.split('.');
        const year = Number(yearS);
        if (year < 1901 || year > now.getFullYear()) {
            //생년이 1901년 이전이거나 오늘 이후다.
            setIsValidBirthDay(false);
            return;
        }

        const month = Number(monthS);
        if (
            month <= 0 ||
            month > 12 ||
            (year === now.getFullYear() && month > now.getMonth() + 1)
        ) {
            //생월이 이상하다
            setIsValidBirthDay(false);
            return;
        }

        const dayOfMon = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        //윤년 판별
        if (year % 400 === 0) {
            dayOfMon[2] = 29;
        } else if (year % 100 !== 0 && year % 4 === 0) {
            dayOfMon[2] = 29;
        }

        const day = Number(dayS);
        if (
            day <= 0 ||
            day > dayOfMon[month] ||
            (year === now.getFullYear() &&
                month == now.getMonth() + 1 &&
                day > now.getDate())
        ) {
            setIsValidBirthDay(false);
            return;
        }

        setIsValidBirthDay(true);
    }, [userBirthDay]);

    //전체 유효성 검사
    useEffect(() => {
        setIsChangePossible(
            isNicknameDupleChecked &&
                isValidBirthday &&
                isValidName &&
                isValidNickname
        );
    }, [isNicknameDupleChecked, isValidBirthday, isValidName, isValidNickname]);

    /**
    nameChanged(event)
    이름의 변경을 감지해 업데이트 해주는 함수다.
    */
    const nameChanged = (event: BaseSyntheticEvent) => {
        setUserName(event.target.value);
    };

    /**
     * nicknameChanged(event)
     * 닉네임 변경을 감지합니다.
     * */
    const nicknameChanged = (event: BaseSyntheticEvent) => {
        setIsNicknameDupleChecked(false);
        setUserNickname(event.target.value);
    };

    /**
     * checkDupleNickname()
     * 닉네임 중복 여부 확인
     */
    const checkDupleNickname = () => {
        checkDuplicatedNick(userNickname)
            .then((res) => {
                if (res.data.isDuplicated) {
                    setNicknameMsg('중복된 닉네임입니다.');
                } else {
                    setNicknameMsg('유효하지 않은 닉네임입니다.'); //오류 메시지 초기화
                    setIsNicknameDupleChecked(true);
                }
            })
            .catch((err) => {
                console.log(err);
            });
    };

    const birtyDayChanged = (event: BaseSyntheticEvent) => {
        const dateString = event.target.value.replaceAll('.', '');
        //숫자만 입력 가능
        if (isNaN(dateString)) {
            return;
        }
        if (dateString.length > 8) {
            return;
        }

        let tempString = '';
        for (let i = 0; i < dateString.length; i++) {
            if (i === 4 || i === 6) {
                tempString += '.';
            }
            tempString += dateString[i];
        }

        setUserBirthDay(tempString);
    };

    /**selectGender(a : number) 현재 선택된 성별을 수정한다.*/
    const selectGender = (a: number) => {
        setUserGender(a);
    };

    /**submitChange() 수정을 완료한다. */
    const submitChange = async () => {
        //api호출
        const res1 = await changeName({
            userName: userName,
        });
        console.log(userNickname);
        const res2 = await changeNickname({
            userNickname: userNickname,
        });

        const sendBirthday = userBirthDay.replaceAll('.', '-');
        const res3 = await changeBirthDay({
            userBirth: sendBirthday + 'T00:00:00',
        });

        const res4 = await changeGender({
            userGender: userGender,
        });

        if (
            res1.status === httpStatusCode.OK &&
            res2.status === httpStatusCode.OK &&
            res3.status === httpStatusCode.OK &&
            res4.status === httpStatusCode.OK
        ) {
            navigate(`/mypage`);
        } else {
            console.log(res1);
        }

        //생일은 검증 필요
        // const sendBirthday = userBirthDay.replaceAll('.', '-');
        // changeBirthDay(sendBirthday + 'T00:00:00')
        //     .then((res) => {
        //         if (res.status === httpStatusCode.OK) {
        //             navigate(`/mypage`);
        //         } else {
        //             console.log(res);
        //         }
        //     })
        //     .catch((err) => {
        //         console.log(err);
        //     });
    };

    return (
        <>
            <section
                className="w-full h-full py-[1vw] flex flex-col justify-between items-center"
                onClick={() => {
                    if (selectBoxMode === 1) {
                        setSelectBoxMode(0);
                    }
                }}
            >
                {/* 제목 */}
                <div className="w-[90%] flex flex-col items-start pt-[3vw] flex-grow-0 flex-shrink-0">
                    <h1 className="text-[7vw] weight-text-semibold">
                        개인정보 수정
                    </h1>
                    <p className="text-[4vw]">*는 필수 입력사항입니다.</p>
                </div>

                {/* 입력폼 */}
                <div className="w-[90%] h-[80%] flex flex-col flex-grow-0 flex-shrink-0 text-[4vw]">
                    {/* 이름 */}
                    <div className="w-full h-[17vw] flex justify-between items-start">
                        <div className="w-[20%] my-[1vw]">이름 *</div>
                        <div className="w-[80%] flex flex-col">
                            <input
                                className="px-[2vw] my-[1vw] border-[0.3vw] border-[#929292]"
                                style={{ borderRadius: '1vw' }}
                                value={userName}
                                placeholder="이름을 입력하세요."
                                onChange={nameChanged}
                            ></input>
                            {isValidName ? (
                                <div className="color-text-blue-2 text-[4vw] px-[2vw]">
                                    사용 가능한 이름입니다.
                                </div>
                            ) : (
                                <div className="text-red-400 text-[4vw] px-[2vw]">
                                    이름은 최소 2자, 최대 8자로 구성되어야
                                    합니다.
                                </div>
                            )}
                        </div>
                    </div>
                    {/* 닉네임 */}
                    <div className="w-full h-[17vw] flex justify-between items-start">
                        <div className="w-[20%] my-[1vw]">닉네임 *</div>
                        <div className="w-[80%] flex flex-col">
                            <div className="w-full flex">
                                <input
                                    className="px-[2vw] my-[1vw] border-[0.3vw] border-[#929292] w-[70%] mr-[1vw]"
                                    style={{ borderRadius: '1vw' }}
                                    value={userNickname}
                                    placeholder="닉네임을 입력하세요."
                                    onChange={nicknameChanged}
                                ></input>
                                {/* 닉네임 확인은 유효한 닉만 */}
                                {isValidNickname ? (
                                    <div
                                        className="w-[28%] my-[1vw] color-bg-blue-2 text-white flex justify-center items-center"
                                        style={{ borderRadius: '1vw' }}
                                        onClick={checkDupleNickname}
                                    >
                                        중복 체크
                                    </div>
                                ) : (
                                    <div
                                        className="w-[28%] my-[1vw] bg-[#929292] text-white flex justify-center items-center"
                                        style={{ borderRadius: '1vw' }}
                                    >
                                        중복 체크
                                    </div>
                                )}
                            </div>
                            {isValidNickname && isNicknameDupleChecked ? (
                                <div className="color-text-blue-2 text-[4vw] px-[2vw]">
                                    사용 가능한 닉네임입니다.
                                </div>
                            ) : (
                                <div className="text-red-400 text-[4vw] px-[2vw]">
                                    {nicknameMsg}
                                </div>
                            )}
                        </div>
                    </div>
                    {/* 생년월일 */}
                    <div className="w-full h-[17vw] flex justify-between items-start">
                        <div className="w-[20%] my-[1vw]">생년월일 *</div>
                        <div className="w-[80%] flex flex-col">
                            <input
                                className="px-[2vw] my-[1vw] border-[0.3vw] border-[#929292]"
                                style={{ borderRadius: '1vw' }}
                                value={userBirthDay}
                                placeholder="YYYY.MM.DD"
                                onChange={birtyDayChanged}
                            ></input>
                            {isValidBirthday ? (
                                <div className="color-text-blue-2 text-[4vw] px-[2vw]">
                                    정확한 형식입니다.
                                </div>
                            ) : (
                                <div className="text-red-400 text-[4vw] px-[2vw]">
                                    생년월일이 올바르지 않습니다.
                                </div>
                            )}
                        </div>
                    </div>

                    {/* 성별 설정 */}
                    <div className="w-full h-[17vw] flex justify-between items-start">
                        <div className="w-[20%] my-[1vw]">성별 *</div>
                        <div className="w-[80%] relative">
                            <GenderSelectBox
                                isSelected={userGender}
                                select={selectGender}
                                selectBoxMode={selectBoxMode}
                                setSelectBoxMode={setSelectBoxMode}
                            />
                        </div>
                    </div>

                    {/* 버튼 */}
                    <div className="w-full flex justify-between items-center">
                        {isChangePossible ? (
                            <div
                                className="w-[48%] color-bg-blue-2 text-white flex justify-center items-center py-[2vw]"
                                style={{ borderRadius: '1vw' }}
                                onClick={submitChange}
                            >
                                수정완료
                            </div>
                        ) : (
                            <div
                                className="w-[48%] bg-[#D9D9D9] text-[#646464] flex justify-center items-center py-[2vw]"
                                style={{ borderRadius: '1vw' }}
                            >
                                수정 조건 미충족
                            </div>
                        )}

                        <div
                            className="w-[48%] bg-[#D9D9D9] text-[#646464] flex justify-center items-center py-[2vw]"
                            style={{ borderRadius: '1vw' }}
                            onClick={() => {
                                navigate(`/mypage`);
                            }}
                        >
                            취소
                        </div>
                    </div>
                </div>

                <TabBarMain tabMode={2} />
            </section>
        </>
    );
}
