import { useState } from "react";

interface PropType {
    searchEvent: (
        e: React.KeyboardEvent<HTMLInputElement>,
        searchValue: string
    ) => void;
    clickSearch: (searchValue: string) => void;
}

export default function SearchBar(props: PropType) {
    const [searchValue, setSearchValue] = useState<string>("");

    const valueChanged = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchValue(event.target.value);
    };

    return (
        <div
            className="w-[86%] h-[5%] absolute left-[7%] top-20vw bg-white border-rad-3vw px-2vw text-black text-5vw flex justify-between items-center px-3vw z-10"
            style={{ boxShadow: "0px 2px 6px 1px #929292" }}
        >
            <input
                className="w-[95%] h-full"
                onChange={valueChanged}
                value={searchValue}
                type="text"
                onKeyDown={(event) => props.searchEvent(event, searchValue)}
            ></input>
            <span
                className="material-symbols-outlined w-[5%] h-full flex justify-center items-center text-5vw mx-vw cursor-pointer"
                onClick={() => props.clickSearch(searchValue)}
            >
                search
            </span>
        </div>
    );
}
