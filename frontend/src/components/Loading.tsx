import { useEffect, useState } from "react";

export default function Loading() {
    const [count, setCount] = useState<number>(0);

    useEffect(() => {
        const interval = setInterval(() => {
            setCount((prev) => (prev + 1) % 3);
        }, 300);

        return () => clearInterval(interval);
    }, []);

    return (
        <>
            <div className="fixed w-full h-full top-0 left-0 z-30 bg-black opacity-50 flex justify-center items-center text-white text-7vw"></div>
            <div className="fixed w-full h-full top-0 left-0 z-40 flex justify-center items-center text-white text-7vw">
                <p>로딩 중입니다{".".repeat(count + 1)}</p>
            </div>
        </>
    );
}
