import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
export default function SearchSlideBar() {
    const navigate = useNavigate();

    const [mode, setMode] = useState<number>(1);

    const swipeRef = useRef<HTMLDivElement>(null);

    /**드래그 업, 스와이프 업 하면 적용 */
    const goUpMode = () => {
        if (mode == 1) {
            setMode(0);
        } else if (mode == 2) {
            setMode(1);
        }
    };

    /**드래그 다운, 스와이프 다운 하면 적용 */
    const goDownMode = () => {
        if (mode == 0) {
            setMode(1);
        } else if (mode == 1) {
            setMode(2);
        }
    };

    let dragStartPosY = 0;
    let touchStartPosY = 0;

    return (
        <>
            <div
                className={`w-full bottom-0 absolute rounded-[2vw] border-[0.5vw] border-[#D9D9D9] z-10 bg-white ${
                    mode === 0 ? 'h-full' : mode === 1 ? 'h-[50%]' : 'h-[20%]'
                }`}
                style={{ transitionDuration: '1s' }}
            >
                {/* 스크롤 */}
                <div
                    className="w-full h-[10vw] flex justify-center items-center"
                    draggable={true}
                    onDragStart={(e) => {
                        dragStartPosY = e.clientY;

                        if (swipeRef.current) {
                            swipeRef.current.classList.add('color-bg-blue-4');
                        }
                    }}
                    onDragEnd={(e) => {
                        if (swipeRef.current) {
                            swipeRef.current.classList.remove(
                                'color-bg-blue-4'
                            );
                        }

                        if (e.clientY < dragStartPosY) {
                            //드래그 업
                            goUpMode();
                        } else if (e.clientY > dragStartPosY) {
                            //드래그 다운
                            goDownMode();
                        }
                    }}
                    onTouchStart={(e) => {
                        touchStartPosY = e.touches[0].clientY;

                        if (swipeRef.current) {
                            swipeRef.current.classList.add('color-bg-blue-4');
                        }
                    }}
                    onTouchEnd={(e) => {
                        if (swipeRef.current) {
                            swipeRef.current.classList.remove(
                                'color-bg-blue-4'
                            );
                        }

                        if (e.changedTouches[0].clientY < touchStartPosY) {
                            //스와이프 업
                            goUpMode();
                        } else if (
                            e.changedTouches[0].clientY > touchStartPosY
                        ) {
                            //스와이프 다운
                            goDownMode();
                        }
                    }}
                    ref={swipeRef}
                >
                    <div className="w-[20%] h-[1vw] bg-[#929292] rounded-[0.5vw]"></div>
                </div>
                {/* 일정 정보 표시 */}
                <div className="w-full h-[85%] overflow-y-scroll flex flex-col"></div>
            </div>
        </>
    );
}
