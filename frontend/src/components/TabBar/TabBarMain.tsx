interface PropType {
    tabMode: number;
}

export default function TabBarMain(props: PropType) {
    //메인 화면 등 투어 화면이 아닌 곳에서 보이는 탭바
    const tabMode = props.tabMode;

    return (
        <>
            <div
                className={`w-full h-[7%] absolute bottom-0 left-0 flex justify-center items-center border-t-dot3vw border-t-gray-400 bg-white cursor-pointer`}
            >
                <div
                    className={`w-[33%] h-full flex justify-center items-center ${
                        tabMode === 0
                            ? " color-border-blue-2 border-t-halfvw"
                            : ""
                    }`}
                    onClick={() => {
                        window.location.href = "/feed";
                    }}
                >
                    <span className="material-symbols-outlined">search</span>
                </div>
                <div className="w-dot5vw h-[80%] bg-gray-400"></div>
                <div
                    className={`w-[33%] h-full relative ${
                        tabMode === 1
                            ? " color-border-blue-2 border-t-halfvw"
                            : "flex justify-center items-center"
                    }`}
                    onClick={() => {
                        if (tabMode !== 1) {
                            window.location.href = "/main";
                        }
                    }}
                >
                    {tabMode === 1 ? (
                        <div
                            className="w-[60%] aspect-square rounded-[50%] color-bg-blue-5 absolute left-[50%] flex justify-center items-center"
                            style={{ transform: `translate(-50%, -50%)` }}
                            onClick={() => {
                                window.location.href = "/create";
                            }}
                        >
                            <p className="text-white text-12vw">+</p>
                        </div>
                    ) : (
                        <span className="material-symbols-outlined">home</span>
                    )}
                </div>
                <div className={`w-dot5vw h-[80%] bg-gray-400`}></div>
                <div
                    className={`w-[33%] h-full flex justify-center items-center ${
                        tabMode === 2
                            ? " color-border-blue-2 border-t-halfvw"
                            : ""
                    }`}
                    onClick={() => {
                        window.location.href = "/mypage";
                    }}
                >
                    <span className="material-symbols-outlined">person</span>
                </div>
            </div>
        </>
    );
}
