import { BaseSyntheticEvent, KeyboardEventHandler, useRef } from 'react';

export default function FeedSearchBar() {
    const inputRef = useRef<HTMLInputElement>(null);

    const searchEvent = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.keyCode === 13 && inputRef.current) {
            console.log(inputRef.current.value);
        }
    };

    return (
        <>
            <input
                type="search"
                className="w-[90%] h-[10%] text-[7vw] mt-[2vw] p-[2vw] border-[0.5vw] border-[#929292] rounded-[5vw]"
                placeholder="원하시는 여행을 검색해 보세요!"
                onKeyDown={searchEvent}
                ref={inputRef}
            ></input>
        </>
    );
}
