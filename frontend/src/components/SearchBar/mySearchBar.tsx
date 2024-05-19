import { useState } from "react";
import SearchIcon from "../../assets/svg/searchIcon";

interface ChildProps {
    onChange: (data: string) => void;
}

export default function searchBar(props: ChildProps) {
    const [query, setQuery] = useState<string>(""); // 검색어

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setQuery(event.target.value);
    };

    // 부모 컴포넌트로 검색어 보내기
    const searchCity = () => {
        props.onChange(query);
    };

    // 엔터로 검색
    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
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
                    id="button-addon1"
                >
                    <SearchIcon />
                </button>
            </div>
        </div>
    );
}
