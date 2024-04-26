export default function AccountPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 2];

    return (
        <>
            <h1>가계부 페이지</h1>
            <p>여행에 대한 가계부를 봅니다.</p>
            <p>tourId : {tourId}</p>
        </>
    );
}
