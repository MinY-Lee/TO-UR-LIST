export default function ChecklistEditAllPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    return (
        <>
            <h1>체크리스트 전체보기 수정 페이지</h1>
            <p>체크리스트 전체보기에서 수정하는 페이지입니다.</p>
            <p>tourId: {tourId}</p>
        </>
    );
}
