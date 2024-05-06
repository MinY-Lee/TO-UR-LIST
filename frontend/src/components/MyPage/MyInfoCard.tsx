import { useDispatch, useSelector } from 'react-redux';
import { UserInfo } from '../../types/types';
import { logout } from '../../util/api/auth';
import { httpStatusCode } from '../../util/api/http-status';
import CheckModal from '../CheckModal';
import { useState } from 'react';
import { userWholeState } from '../../util/reduxSlices/userSlice';

export default function MyInfoCard() {
    const userInfo: UserInfo = useSelector((state: any) => state.userSlice);
    const [checkModalActive, setIsCheckModalActive] = useState<boolean>(false);

    const dispatch = useDispatch();

    let gender: string = '';
    switch (userInfo.userGender) {
        case 0:
            gender = '미지정';
            break;
        case 1:
            gender = '남성';
            break;
        case 2:
            gender = '여성';
            break;
        case 3:
            gender = '기타';
            break;
    }

    const logoutProceed = () => {
        logout().then((res) => {
            if (res.status === httpStatusCode.OK) {
                //redux에서 유저 정보 초기화
                const userInfo: UserInfo = {
                    userId: '',
                    userNickname: '',
                    userName: '',
                    userBirth: '',
                    userGender: 0,
                };
                dispatch(userWholeState(userInfo));
                window.location.href = '/';
            }
        });
    };

    const closeModal = () => {
        setIsCheckModalActive(false);
    };

    return (
        <>
            {checkModalActive ? (
                <CheckModal
                    mainText="로그아웃 하시겠습니까?"
                    subText=""
                    OKText="확인"
                    CancelText="취소"
                    clickOK={logoutProceed}
                    clickCancel={closeModal}
                />
            ) : (
                <></>
            )}
            <div
                className="w-[90%] h-[30%] flex flex-col items-center px-[1vw] py-[4vw] my-[1vw] border-[0.5vw] border-black"
                style={{
                    borderRadius: '7vw',
                }}
            >
                {/* 닉네임 */}
                <p className="text-[6vw] h-[20%] weight-text-semibold mb-[1vw]">
                    <span className="text-[7vw] mr-[1vw]">
                        {userInfo.userNickname}
                    </span>
                    님
                </p>
                <div className="w-[90%] h-[0.5vw] bg-[#929292]"></div>
                <div className="w-[90%] h-[60%] flex">
                    <div className="w-[30%] h-full weight-text-semibold text-[5vw] flex flex-col justify-center items-start">
                        <p>이름</p>
                        <p>생년월일</p>
                        <p>성별</p>
                    </div>
                    <div className="w-[70%] h-full text-[5vw] flex flex-col justify-center items-start">
                        <p>{userInfo.userName}</p>
                        <p>
                            {userInfo.userBirth
                                ? userInfo.userBirth.replaceAll('-', '.')
                                : '미지정'}
                        </p>
                        <p>{gender}</p>
                    </div>
                </div>
                <div className="w-[90%] h-[20%] flex justify-between items-center">
                    <div
                        className="w-[45%] h-full color-bg-blue-2 text-white text-[5vw] weight-text-semibold flex justify-center items-center"
                        style={{ borderRadius: '3vw' }}
                        onClick={() => {
                            window.location.href = '/mypage/info';
                        }}
                    >
                        정보수정
                    </div>
                    <div
                        className="w-[45%] h-full bg-[#D9D9D9] text-[#646464] text-[5vw] weight-text-semibold flex justify-center items-center"
                        style={{ borderRadius: '3vw' }}
                        onClick={() => {
                            setIsCheckModalActive(true);
                        }}
                    >
                        로그아웃
                    </div>
                </div>
            </div>
        </>
    );
}
