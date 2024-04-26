export default function PlaceAddPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    return (
        <>
            <h1>장소 추가 페이지</h1>
            <p>장소를 검색해서 추가할 수 있습니다.</p>
            <p>tourId : {tourId}</p>
        </>
    );
}
