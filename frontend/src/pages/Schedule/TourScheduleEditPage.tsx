export default function TourScheduleEditPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    return (
        <>
            <h1>스케줄 수정 페이지</h1>
            <p>여행 일정을 변경합니다.</p>
            <p>tourId : {tourId}</p>
        </>
    );
}
