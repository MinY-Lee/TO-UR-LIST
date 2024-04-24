export default function FeedEditPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const feedId: string = address[address.length - 2];

    return (
        <>
            <h1>피드 수정 페이지</h1>
            <p>피드 수정을 합니다.</p>
            <p>feedId : {feedId}</p>
        </>
    );
}
