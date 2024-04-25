export default function TabBarMain() {
    //메인 화면 등 투어 화면이 아닌 곳에서 보이는 탭바

    return (
        <>
            <div className="w-full h-[10%] absolute bottom-0 left-0 flex justify-center items-center border-t-[0.4vw] border-t-gray-400 bg-white">
                <div
                    className="w-[33%] h-full flex justify-center items-center"
                    onClick={() => {
                        window.location.href = '/feed';
                    }}
                >
                    <span className="material-symbols-outlined">search</span>
                </div>
                <div className="w-[0.4vw] h-[80%] bg-gray-400"></div>
                <div className="w-[33%] h-full color-border-blue-2 border-t-[0.6vw] relative">
                    <div
                        className="w-[75%] aspect-square rounded-full color-bg-blue-5 absolute left-[50%] flex justify-center items-center"
                        style={{ transform: `translate(-50%, -50%)` }}
                        onClick={() => {
                            window.location.href = '/create';
                        }}
                    >
                        <p className="text-white text-[12vw]">+</p>
                    </div>
                </div>
                <div className="w-[0.4vw] h-[80%] bg-gray-400"></div>
                <div
                    className="w-[33%] h-full flex justify-center items-center"
                    onClick={() => {
                        window.location.href = '/mypage';
                    }}
                >
                    <span className="material-symbols-outlined">person</span>
                </div>
            </div>
        </>
    );
}
