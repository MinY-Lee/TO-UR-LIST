import { useNavigate } from 'react-router-dom';

export default function HeaderBar() {
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    };

    return (
        <>
            <div className="  w-full h-[10vw] flex justify-start items-center flex-shrink-0">
                <span
                    className="material-symbols-outlined w-[10%] h-full flex justify-center items-center"
                    onClick={goBack}
                >
                    west
                </span>
                <span className="text-[5vw]">TO-UR-LIST</span>
            </div>
        </>
    );
}
