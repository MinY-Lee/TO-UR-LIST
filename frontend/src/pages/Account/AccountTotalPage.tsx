export default function AccountTotalPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 3];

    return (
        <>
            <h1>정산 페이지</h1>
            <p>여행에 대한 정산을 봅니다.</p>
            <p>tourId : {tourId}</p>
        </>
    );
}
