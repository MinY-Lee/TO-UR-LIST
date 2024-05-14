// 유저정보
export interface UserInfo {
    userId: string;
    userNickname: string;
    userName: string;
    userBirth: string;
    userGender: number;
    // userProfileImageId: string;
}

// 여행 정보
export interface TourCardInfo {
    tourId: string;
    tourTitle: string;
    cityList: City[];
    startDate: string;
    endDate: string;
}

export interface MemberInfo {
    userId: string;
    userNickname: string;
    userName: string;
    memberType: string;
}

export interface TourInfoDetail {
    tourId?: string; // 나중에는 없어도 됨
    tourTitle: string;
    cityList: City[];
    startDate: string;
    endDate: string;
    memberList: MemberInfo[];
}

export interface City {
    countryCode: string;
    cityName: string;
}

export interface Country {
    countryCode: string;
    cityList: City[];
}

export interface CountryMapping {
    countryCode: string;
    countryName: string;
}

export interface CountryInfo {
    climate: string;
    currencyUnit: string;
    kst: number;
    language: string;
    plug_type: string;
    voltage: string;
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

// api 로 보낼 체크리스트 아이템 정보
export interface ItemApi {
    tourId: string;
    placeId: string;
    tourActivityId: string;
    item: string;
    tourDay: number;
    isChecked: boolean;
}

// 체크리스트 아이템 정보
export interface Item {
    tourId: string;
    placeId: string;
    tourActivityId: string;
    item: string;
    tourDay: number;
    isChecked: boolean;
    isPublic: boolean;
}

//일정 관련
export interface TourPlaceItem {
    placeId: string;
    placeName: string;
    tourDay: number;
    activityList: TourActivity[];
}

export interface TourActivity {
    tourActivityId: string;
    activity: string;
}

export interface Position {
    lat: number;
    lng: number;
}

//장소 정보 상세
export interface PlaceInfo {
    placeId: string;
    placeName: string;
    placePrimaryType: string | null;
    placeLatitude: number;
    placeLongitude: number;
    placeAddress: string;
    placePhotoList: string[];
}

//장소 정보 상세
export interface PlaceInfoDetail {
    placeId: string;
    placeName: string;
    placePrimaryType: string;
    placeLatitude: number;
    placeLongitude: number;
    placeAddress: string;
    placePhotoList: string[];
}

//여행 편집 화면
export interface TourEditDetail {
    placeInfo: PlaceInfoDetail;
    tourActivityList: TourActivity[];
    isSelected: boolean;
}

// 가계부
export interface PayMember {
    userId: string;
    payAmount: number;
}

export interface AccountInfo {
    payId?: string;
    payType: string;
    tourId: string;
    payAmount: number;
    exchangeRate: number;
    unit: string;
    currencyCode: string;
    payMethod: string;
    payDateStr?: string;
    payDatetime: Date;
    payContent: string;
    payCategory: string;
    payerId: string;
    payMemberList: PayMember[];
}

export interface CurrencyInfo {
    unit: string;
    currencyRate: number;
    currencyCode: string;
}

// 피드
export interface Filter {
    filterType: string;
    filterValue: string;
}

//웹소켓
export interface WebSockPlace {
    activityList: string[];
    placeId: string;
    placeName: string;
    tourDay: number;
    tourPlaceId: string;
}
