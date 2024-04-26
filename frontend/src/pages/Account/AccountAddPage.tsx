export default function AccountAddPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    return (
        <>
            <h1>지출 입력 페이지</h1>
            <p>이번 지출을 등록합니다.</p>
            <p>tourId : {tourId}</p>
        </>
    );
}
