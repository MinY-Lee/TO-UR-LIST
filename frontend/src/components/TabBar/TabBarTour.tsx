import { useEffect, useRef, useState } from 'react';
import CircleMenu from './CircleMenu';

interface PropType {
    tabMode: number;
    tourMode: number;
    tourId: string;
}

export default function TabBarTour(props: PropType) {
    const [isMenuOpen, setIsMenuOpen] = useState<boolean>(false);

    //메인 화면 등 투어 화면이 아닌 곳에서 보이는 탭바
    const tabMode = props.tabMode;

    const [widthSize, setWidthSize] = useState<number>(0);

    useEffect(() => {
        if (widthRef.current) {
            setWidthSize(widthRef.current.offsetWidth);
        }
    }, []);
    const widthRef = useRef<HTMLDivElement>(null);

    return (
        <>
            <div
                className={`w-full h-[10%] absolute bottom-0 left-0 flex justify-center items-center border-t-[0.4vw] border-t-gray-400 bg-white z-10`}
            >
                <div
                    className={`w-[33%] h-full flex justify-center items-center bg-white`}
                    onClick={() => {
                        window.location.href = '/feed';
                    }}
                >
                    <span className="material-symbols-outlined">search</span>
                </div>
                <div className="w-[0.4vw] h-full bg-white flex justify-center items-center">
                    <div className="w-full h-[80%] bg-gray-400"></div>
                </div>
                <div
                    className={`w-[33%] h-full relative color-border-blue-2 border-t-[0.6vw] bg-white`}
                    ref={widthRef}
                >
                    {isMenuOpen ? (
                        <>
                            <CircleMenu
                                width={widthSize}
                                tourMode={props.tourMode}
                                tourId={props.tourId}
                            />
                            <div
                                className="w-[75%] aspect-square rounded-full color-bg-blue-5 absolute left-[50%] flex justify-center items-center"
                                style={{ transform: `translate(-50%, -50%)` }}
                                onClick={() => {
                                    setIsMenuOpen(false);
                                }}
                            >
                                <span className="material-symbols-outlined text-[8vw] text-white">
                                    close
                                </span>
                            </div>
                        </>
                    ) : (
                        <div
                            className="w-[75%] aspect-square rounded-full color-bg-blue-5 absolute left-[50%] flex justify-center items-center"
                            style={{ transform: `translate(-50%, -50%)` }}
                            onClick={() => {
                                setIsMenuOpen(true);
                            }}
                        >
                            <span className="material-symbols-outlined text-[8vw] text-white">
                                menu
                            </span>
                        </div>
                    )}
                </div>
                <div className="w-[0.4vw] h-full bg-white flex justify-center items-center">
                    <div className="w-full h-[80%] bg-gray-400"></div>
                </div>
                <div
                    className={`w-[33%] h-full flex justify-center items-center bg-white`}
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
