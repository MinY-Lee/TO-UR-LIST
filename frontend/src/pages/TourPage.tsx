export default function TourPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 1];

    return (
        <>
            <h1>투어 메인 페이지</h1>
            <p>기본적인 여행 정보가 담겨 있습니다.</p>
            <p>현재 아이디 : {tourId}</p>
        </>
    );
}
