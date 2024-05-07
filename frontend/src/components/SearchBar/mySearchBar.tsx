import { useState } from 'react';

interface ChildProps {
    onChange: (data: string) => void;
}

export default function searchBar(props: ChildProps) {

    const [query, setQuery] = useState<string>(""); // 검색어

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setQuery(event.target.value);
      };

    // 부모 컴포넌트로 검색어 보내기
    const searchCity = (() => {
        props.onChange(query);
    });

    // 엔터로 검색
    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Enter') {
          searchCity();
        }
      };


    return (
        <div className="">
            <div className="flex w-full rounded-lg border border-solid border-neutral-300">
                <input
                    type="search"
                    value={query}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown} // 엔터 키 입력 감지
                    className="relative m-0 -mr-0.5 w-[1px] flex-auto bg-transparent bg-clip-padding px-3 py-[0.25rem] font-normal outline-none"
                    placeholder="나라 이름을 검색해보세요"
                    aria-label="Search"
                    aria-describedby="button-addon1"
                />

                <button
                    className="relative z-[2] flex items-center px-3 py-2.5 text-xs font-medium"
                    onClick={searchCity}
                    type="button"
                    id="button-addon1">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 20 20"
                        fill="black"
                        className="h-5 w-5">
                        <path
                            fillRule="evenodd"
                            d="M9 3.5a5.5 5.5 0 100 11 5.5 5.5 0 000-11zM2 9a7 7 0 1112.452 4.391l3.328 3.329a.75.75 0 11-1.06 1.06l-3.329-3.328A7 7 0 012 9z"
                            clipRule="evenodd" />
                    </svg>
                </button>
            </div>
        </div>
    );
}
