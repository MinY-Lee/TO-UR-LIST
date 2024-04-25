// 유저정보
export interface UserInfo {
    userId: string;
    userNickname: string;
    userName: string;
    userBirth: string;
    userGender: number;
    userProfileImageId: string;
}

// 여행 정보
export interface TourCardInfo {
    tourId: string;
    tourTitle: string;
    cityList: City[];
    startDate: string;
    endDate: string;
}

export interface City {
    countryCode: string;
    cityName: string;
}
