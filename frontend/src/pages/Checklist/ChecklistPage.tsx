export default function ChecklistPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 2];

    return (
        <>
            <h1>체크리스트 페이지</h1>
            <p>여행 체크리스트를 보는 페이지입니다.</p>
            <p>tourId : {tourId}</p>
        </>
    );
}
