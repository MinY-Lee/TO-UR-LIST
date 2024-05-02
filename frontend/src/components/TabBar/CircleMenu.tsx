import { useEffect, useState } from 'react';

interface PropType {
    width: number;
    tourMode: number;
    tourId: string;
}

export default function CircleMenu(props: PropType) {
    const radius = props.width / 2;

    const [conicString, setConicString] = useState<string>('');

    //receipt
    const leftPos1 = radius + radius * 1.15 * Math.cos(Math.PI / 6);
    const topPos1 = -1 * (radius * 1.15 * Math.sin(Math.PI / 6));

    //calender
    const leftPos2 = radius + radius * 1.15 * Math.cos(Math.PI / 2);
    const topPos2 = -1 * (radius * 1.15 * Math.sin(Math.PI / 2));

    //checklist
    const leftPos3 = radius + radius * 1.15 * Math.cos((5 * Math.PI) / 6);
    const topPos3 = -1 * (radius * 1.15 * Math.sin((5 * Math.PI) / 6));

    useEffect(() => {
        if (props.tourMode === 1) {
            setConicString(
                'white 0deg, white 120deg, #a2dcf2 120deg, #a2dcf2 180deg, white 180deg, white 360deg'
            );
        } else if (props.tourMode === 2) {
            setConicString(
                'white 0deg, white 60deg, #a2dcf2 60deg, #a2dcf2 120deg, white 120deg, white 360deg'
            );
        } else if (props.tourMode === 3) {
            setConicString(
                '#a2dcf2 0deg, #a2dcf2 60deg, white 60deg, white 360deg'
            );
        }
        console.log(conicString);
    }, []);
    return (
        <>
            <div
                className="w-[150%] aspect-square rounded-[50%] -z-10 absolute left-[50%] border-[0.2vw] border-[#D9D9D9]"
                style={{
                    transform: `translate(-50%, -50%) rotate(270deg) scaleY(-1)`,
                    backgroundImage: `conic-gradient(${conicString})`,
                }}
            ></div>
            <div
                className="w-[85%] aspect-square rounded-[50%] -z-10 absolute left-[50%] bg-white"
                style={{ transform: `translate(-50%, -50%)` }}
            ></div>
            <span
                className="material-symbols-outlined absolute"
                style={{
                    transform: `translate(-50%, -50%)`,
                    left: `${leftPos1}px`,
                    top: `${topPos1}px`,
                }}
                onClick={() => {
                    if (props.tourMode !== 1) {
                        window.location.href = `/tour/${props.tourId}/account`;
                    }
                }}
            >
                receipt
            </span>
            <span
                className="material-symbols-outlined absolute"
                style={{
                    transform: `translate(-50%, -50%)`,
                    left: `${leftPos2}px`,
                    top: `${topPos2}px`,
                }}
                onClick={() => {
                    if (props.tourMode !== 2) {
                        window.location.href = `/tour/${props.tourId}/schedule`;
                    }
                }}
            >
                calendar_month
            </span>
            <span
                className="material-symbols-outlined absolute"
                style={{
                    transform: `translate(-50%, -50%)`,
                    left: `${leftPos3}px`,
                    top: `${topPos3}px`,
                }}
                onClick={() => {
                    if (props.tourMode !== 3) {
                        window.location.href = `/tour/${props.tourId}/checklist`;
                    }
                }}
            >
                checklist_rtl
            </span>
        </>
    );
}
