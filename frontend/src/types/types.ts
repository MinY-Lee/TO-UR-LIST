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

//피드 관련
export interface Feed {
    feedId: number;
    feedTitle: string;
    feedContent: string;
    userNickname: string;
    feedThemeTagList: string[];
    feedMateTag: string;
    createdAt: string;
    startDate: string;
    endDate: string;
    cityList: FeedCity[];
    copyCount: number;
    likeCount: number;
}

export interface FeedCity {
    countryName: string;
    cityName: string;
}
