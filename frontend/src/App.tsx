//router
import { Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import InfoPage from './pages/InfoPage';
import TourPage from './pages/TourPage';
import TourCreatePage from './pages/TourCreatePage';
import MyPage from './pages/MyPage/Mypage';
import MypageInfo from './pages/MyPage/MypageInfo';
import MypageMyfeed from './pages/MyPage/MypageMyfeed';
import MypageLike from './pages/MyPage/MypageLike';
import TourSchedulePage from './pages/Schedule/TourSchedulePage';
import PlaceAddPage from './pages/Schedule/PlaceAddPage';
import TourScheduleEditPage from './pages/Schedule/TourScheduleEditPage';
import ChecklistPage from './pages/Checklist/ChecklistPage';
import ChecklistEditAllPage from './pages/Checklist/ChecklistEditAllPage';
import ChecklistEditDayPage from './pages/Checklist/ChecklistEditDayPage';
import ChecklistEditItemPage from './pages/Checklist/ChecklistEditItemPage';
import AccountPage from './pages/Account/AccountPage';
import AccountTotalPage from './pages/Account/AccountTotalPage';
import AccountAddPage from './pages/Account/AccountAddPage';
import AccountEditPage from './pages/Account/AccountEditPage';
import FeedPage from './pages/Feed/FeedPage';
import FeedDetailPage from './pages/Feed/FeedDetailPage';
import FeedWritePage from './pages/Feed/FeedWritePage';
import FeedEditPage from './pages/Feed/FeedEditPage';

//css
import './App.css';
import LoginCheck from './pages/AuthCheck/LoginCheck';
import TourScheduleEditDetailPage from './pages/Schedule/TourScheduleEditDetailPage';
import PlaceAddDetailPage from './pages/Schedule/PlaceAddDetailPage';

export default function App() {
    return (
        <Routes>
            {/* 로그인 페이지 */}
            <Route path="/" element={<LoginPage />} />

            {/* 로그인 여부 체크 */}
            <Route element={<LoginCheck />}>
                {/* 유저 메인 페이지 */}
                <Route path="/main" element={<MainPage />} />
                {/* 초기정보 세팅 페이지 */}
                <Route path="/info" element={<InfoPage />} />
                {/* 여행 정보 보기 페이지 */}
                <Route path="/tour/:tourid" element={<TourPage />} />
                {/* 여행 생성 페이지 */}
                <Route path="/create" element={<TourCreatePage />} />
                {/* 마이페이지 관련 */}
                {/* 마이페이지 */}
                <Route path="/mypage" element={<MyPage />} />
                {/* 개인정보 수정 페이지 */}
                <Route path="/mypage/info" element={<MypageInfo />} />
                {/* 내가 올린 피드 리스트 */}
                <Route path="/mypage/myfeed" element={<MypageMyfeed />} />
                {/* 내가 좋아요한 피드 리스트 */}
                <Route path="/mypage/like" element={<MypageLike />} />

                {/* 여행 일정 관련 */}
                {/* 여행 일정 페이지 */}
                <Route
                    path="/tour/:tourid/schedule"
                    element={<TourSchedulePage />}
                />
                {/* 장소 추가 페이지 */}
                <Route
                    path="/tour/:tourid/schedule/add"
                    element={<PlaceAddPage />}
                />
                {/* 장소 추가 상세보기 페이지 */}
                <Route
                    path="/tour/:tourid/schedule/add/detail"
                    element={<PlaceAddDetailPage />}
                />
                {/* 일정 변경 페이지 */}
                <Route
                    path="/tour/:tourid/schedule/edit"
                    element={<TourScheduleEditPage />}
                />
                {/* 일정 변경 상세보기 페이지 */}
                <Route
                    path="/tour/:tourid/schedule/edit/detail"
                    element={<TourScheduleEditDetailPage />}
                />

                {/* 체크리스트 관련 */}
                {/* 체크리스트 보기 */}
                <Route
                    path="/tour/:tourid/checklist"
                    element={<ChecklistPage />}
                />
                {/* 체크리스트 전체 수정 */}
                <Route
                    path="/tour/:tourid/checklist/all"
                    element={<ChecklistEditAllPage />}
                />
                {/* 체크리스트 일정별 보기 수정 */}
                <Route
                    path="/tour/:tourid/checklist/day"
                    element={<ChecklistEditDayPage />}
                />
                {/* 체크리스트 항목 수정 */}
                <Route
                    path="/tour/:tourid/checklist/edit"
                    element={<ChecklistEditItemPage />}
                />

                {/* 가계부 관련 */}
                {/* 가계부 보기 */}
                <Route path="/tour/:tourid/account" element={<AccountPage />} />
                {/* 가계부 정산 보기 */}
                <Route
                    path="/tour/:tourid/account/total"
                    element={<AccountTotalPage />}
                />
                {/* 여행 지출 추가 */}
                <Route
                    path="/tour/:tourid/account/add"
                    element={<AccountAddPage />}
                />
                {/* 여행 지출 수정 */}
                <Route
                    path="/tour/:tourid/account/:payid"
                    element={<AccountEditPage />}
                />

                {/* 피드 관련 */}
                {/* 피드 목록 페이지 */}
                <Route path="/feed" element={<FeedPage />} />
                {/* 피드 상세보기 페이지 */}
                <Route path="/feed/:feedid" element={<FeedDetailPage />} />
                {/* 피드 추가 페이지 */}
                <Route path="/feed/write" element={<FeedWritePage />} />
                {/* 피드 수정 페이지 */}
                <Route path="/feed/:feedid/edit" element={<FeedEditPage />} />
            </Route>
        </Routes>
    );
}
