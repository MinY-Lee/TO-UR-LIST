export default function FeedDetailPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const feedId: string = address[address.length - 1];

    return (
        <>
            <h1>피드 상세보기 페이지</h1>
            <p>피드에 대한 상세 정보를 봅니다.</p>
            <p>feedId : {feedId}</p>
        </>
    );
}
