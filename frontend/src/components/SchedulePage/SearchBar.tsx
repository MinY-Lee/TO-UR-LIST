import { useState } from 'react';

interface PropType {
    searchEvent: (
        e: React.KeyboardEvent<HTMLInputElement>,
        searchValue: string
    ) => void;
}

export default function SearchBar(props: PropType) {
    const [searchValue, setSearchValue] = useState<string>('');

    const valueChanged = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchValue(event.target.value);
    };

    return (
        <input
            className="w-[80%] h-[7%] absolute left-[10%] top-[12%] bg-white border-[#929292] border-[0.3vw] rounded-[3vw] px-[2vw] text-black text-[6vw] flex justify-between items-center z-10"
            onChange={valueChanged}
            value={searchValue}
            type="search"
            onKeyDown={(event) => props.searchEvent(event, searchValue)}
        ></input>
    );
}
