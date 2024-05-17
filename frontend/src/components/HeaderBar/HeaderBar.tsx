import { useNavigate } from "react-router-dom";

export default function HeaderBar() {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    };

    return (
        <>
            <div className="px-5 pt-2 flex w-full justify-between items-center">
                <div className="h-10vw flex justify-start items-center flex-shrink-0">
                    <span
                        className="material-symbols-outlined h-full flex justify-center items-center cursor-pointer"
                        onClick={goBack}
                    >
                        west
                    </span>
                    <span className="text-5vw">TO-UR-LIST</span>
                </div>
                <div
                    className="underline"
                    onClick={() => {
                        window.location.href = `/`;
                    }}
                >
                    여행메인
                </div>
            </div>
        </>
    );
}
