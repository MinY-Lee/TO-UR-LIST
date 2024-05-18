import { useEffect, useRef, useState } from "react";
import FeedIcon from "../../assets/svg/feedIcon";

interface PropType {
    tourMode: number;
    tourId: string;
    type?: string;
}

export default function TabBarTour(props: PropType) {
    const selected = `color-text-blue-6`;
    const [type, setType] = useState<string>("");

    useEffect(() => {
        if (props.type) {
            setType(props.type);
        }
    }, [props]);

    return (
        <>
            <div
                className={`w-[96%] h-[60px] grid grid-cols-4 absolute bottom-2 left-2 rounded-lg justify-around items-center border-2 drop-shadow-md bg-white z-10`}
            >
                <div
                    className={`hover:bg-[#d5e7f2] w-full h-full rounded-l-lg flex flex-col items-center justify-evenly text-neutral-500 ${
                        type == "tourMain" ? selected : ""
                    }`}
                    onClick={() => {
                        window.location.href = `/tour/${props.tourId}`;
                    }}
                >
                    <span className="material-symbols-outlined text-3xl h-[30px]">trip</span>
                    <div>여행</div>
                </div>
                <div
                    className="hover:bg-[#d5e7f2] w-full h-full"
                    onClick={() => {
                        if (props.tourMode !== 3) {
                            window.location.href = `/tour/${props.tourId}/checklist`;
                        }
                    }}
                >
                    <div
                        className={`${
                            type == "checklist" ? selected : ""
                        } h-full flex flex-col items-center  justify-evenly text-neutral-500`}
                    >
                        <span className="material-symbols-outlined text-3xl h-[30px]">
                            checklist_rtl
                        </span>
                        <div>체크리스트</div>
                    </div>
                </div>
                <div
                    className="hover:bg-[#d5e7f2] w-full h-full"
                    onClick={() => {
                        if (props.tourMode !== 2) {
                            window.location.href = `/tour/${props.tourId}/schedule`;
                        }
                    }}
                >
                    <div
                        className={`${
                            type == "schedule" ? selected : ""
                        } flex flex-col h-full  items-center  justify-evenly text-neutral-500`}
                    >
                        <span className="material-symbols-outlined  text-3xl h-[30px]">
                            calendar_month
                        </span>
                        <div>일정</div>
                    </div>
                </div>
                <div
                    className="hover:bg-[#d5e7f2] w-full h-full"
                    onClick={() => {
                        if (props.tourMode !== 1) {
                            window.location.href = `/tour/${props.tourId}/account`;
                        }
                    }}
                >
                    <div
                        className={`${
                            type == "account" ? selected : ""
                        } h-full justify-evenly flex flex-col items-center  text-neutral-500 `}
                    >
                        <span className="material-symbols-outlined  text-3xl h-[30px]">
                            receipt_long
                        </span>
                        <div>가계부</div>
                    </div>
                </div>
            </div>
        </>
    );
}
