import { useEffect, useRef, useState } from "react";
import FeedIcon from "../../assets/svg/feedIcon";

interface PropType {
    tourMode: number;
    tourId: string;
    type?: string;
}

export default function TabBarTour(props: PropType) {
    const selected = `color-text-blue-2`;
    const [type, setType] = useState<string>("");

    useEffect(() => {
        if (props.type) {
            setType(props.type);
        }
    }, [props]);

    return (
        <>
            <div
                className={`${
                    type == "feed" ? selected : ""
                } w-[96%] grid grid-cols-5 absolute bottom-2 left-2 rounded-lg justify-around items-center border-2 drop-shadow-md bg-white z-10`}
            >
                <div
                    className="hover:bg-[#d5e7f2] h-full rounded-l-lg flex flex-col items-center justify-evenly text-neutral-500"
                    onClick={() => {
                        window.location.href = `/feed`;
                    }}
                >
                    <FeedIcon />
                    <div>피드</div>
                </div>
                <div className="hover:bg-[#d5e7f2] h-full">
                    <div
                        className={`${
                            type == "checklist" ? selected : ""
                        } h-full flex flex-col items-center  justify-evenly text-neutral-500`}
                        onClick={() => {
                            if (props.tourMode !== 3) {
                                window.location.href = `/tour/${props.tourId}/checklist`;
                            }
                        }}
                    >
                        <span className="material-symbols-outlined text-3xl">
                            checklist_rtl
                        </span>
                        <div>체크리스트</div>
                    </div>
                </div>
                <div className="hover:bg-[#d5e7f2] h-full">
                    <div
                        className={`${
                            type == "schedule" ? selected : ""
                        } flex flex-col h-full  items-center  justify-evenly text-neutral-500`}
                    >
                        <span
                            className="material-symbols-outlined  text-3xl"
                            onClick={() => {
                                if (props.tourMode !== 2) {
                                    window.location.href = `/tour/${props.tourId}/schedule`;
                                }
                            }}
                        >
                            calendar_month
                        </span>
                        <div>일정</div>
                    </div>
                </div>
                <div className="hover:bg-[#d5e7f2] h-full">
                    <div
                        className={`${
                            type == "account" ? selected : ""
                        } h-full justify-evenly flex flex-col items-center  text-neutral-500 `}
                    >
                        <span
                            className="material-symbols-outlined  text-3xl"
                            onClick={() => {
                                if (props.tourMode !== 1) {
                                    window.location.href = `/tour/${props.tourId}/account`;
                                }
                            }}
                        >
                            receipt
                        </span>
                        <div>가계부</div>
                    </div>
                </div>
                <div className="hover:bg-[#d5e7f2] h-full rounded-r-lg">
                    <div
                        className={`${
                            type == "mypage" ? selected : ""
                        } h-full flex flex-col items-center justify-evenly text-neutral-500`}
                        onClick={() => {
                            window.location.href = `/mypage`;
                        }}
                    >
                        <span className="material-symbols-outlined  text-3xl ">
                            person
                        </span>
                        <div className="">마이페이지</div>
                    </div>
                </div>
            </div>
        </>
    );
}
