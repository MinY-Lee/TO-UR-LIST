import { createSlice } from '@reduxjs/toolkit';
import { UserInfo } from '../../types/types';

const initialState: UserInfo = {
    userId: '',
    userNickname: '',
    userName: '',
    userBirth: '2001-01-01T00:00:00',
    userGender: 0,
    userProfileImageId: '',
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
        userProfileImageIdState: (state, action) => {
            state.userProfileImageId = action.payload;
        },
    },
});

export const {
    userIdState,
    userNicknameState,
    userNameState,
    userBirthState,
    userGenderState,
    userProfileImageIdState,
} = userSlice.actions;

export default userSlice.reducer;
