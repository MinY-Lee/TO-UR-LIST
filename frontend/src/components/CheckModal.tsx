interface Proptype {
    mainText: string;
    subText: string;
    OKText: string;
    CancelText: string;
    clickOK: () => void;
    clickCancel: () => void;
    className?: string;
}

export default function CheckModal(props: Proptype) {
    return (
        <>
            <div className="fixed w-full h-full top-0 left-0 z-30 bg-black opacity-50"></div>
            <div
                className="fixed check h-[20%] top-[40%] z-40 bg-white flex flex-col justify-evenly items-center border-halfvw color-border-blue-2"
                style={{ borderRadius: "min(2vw, 9.6px)" }}
            >
                <div className="w-full h-[30%] flex flex-col justify-center items-center">
                    <p
                        className={`text-5vw weight-text-semibold ${props.className}`}
                    >
                        {props.mainText}
                    </p>
                    <p
                        className={`text-3vw w-[90%] text-center ${props.className}`}
                    >
                        {props.subText}
                    </p>
                </div>
                <div className="w-[60%] h-[20%] flex justify-between items-center">
                    <div
                        className="w-[48%] h-full flex justify-center items-center color-bg-blue-2 text-white"
                        style={{ borderRadius: "min(2vw, 9.6px)" }}
                        onClick={props.clickOK}
                    >
                        {props.OKText}
                    </div>
                    <div
                        className="w-[48%] h-full flex justify-center items-center bg-white border-halfvw color-border-blue-2 color-text-blue-2"
                        style={{ borderRadius: "min(2vw, 9.6px)" }}
                        onClick={props.clickCancel}
                    >
                        {props.CancelText}
                    </div>
                </div>
            </div>
        </>
    );
}
