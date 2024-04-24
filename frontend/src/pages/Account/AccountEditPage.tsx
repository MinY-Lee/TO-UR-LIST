export default function AccountEditPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];
    //지출 아이디 불러오기
    const payId: string = address[address.length - 1];

    return (
        <>
            <h1>지출 수정 페이지</h1>
            <p>지출을 수정합니다.</p>
            <p>tourId : {tourId}</p>
            <p>payId : {payId}</p>
        </>
    );
}
