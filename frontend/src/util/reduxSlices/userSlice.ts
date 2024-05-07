import { createSlice } from '@reduxjs/toolkit';
import { UserInfo } from '../../types/types';

const initialState: UserInfo = {
    userId: '',
    userNickname: '',
    userName: '',
    userBirth: '2001-01-01T00:00:00',
    userGender: 0,
};

export const userSlice = createSlice({
    name: 'userSlice',
    initialState,
    reducers: {
        //id설정
        userIdState: (state, action) => {
            state.userId = action.payload;
        },
        //닉네임 설정
        userNicknameState: (state, action) => {
            state.userNickname = action.payload;
        },
        //이름 설정
        userNameState: (state, action) => {
            state.userName = action.payload;
        },
        //생일 설정
        userBirthState: (state, action) => {
            state.userBirth = action.payload;
        },
        //성별 설정
        userGenderState: (state, action) => {
            state.userGender = action.payload;
        },
        //프로필 이미지 설정

        //유저 정보 전체 저장
        userWholeState: (state, action) => {
            state.userId = action.payload.userId;
            state.userNickname = action.payload.userNickname;
            state.userName = action.payload.userName;
            state.userBirth = action.payload.userBirth;
            state.userGender = action.payload.userGender;
        },
    },
});

export const {
    userIdState,
    userNicknameState,
    userNameState,
    userBirthState,
    userGenderState,
    userWholeState,
} = userSlice.actions;

export default userSlice.reducer;
