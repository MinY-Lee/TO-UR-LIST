import { useEffect, useState } from 'react';

interface PropType {
    cityList: string[];
    select: (countryName: string) => void;
}

export default function SelectCity(props: PropType) {
    const [idx, setIdx] = useState<number>(0);
    const [list, setList] = useState<string[]>(['도시 없음']);
    const [isOpen, setIsOpen] = useState<boolean>(false);

    useEffect(() => {
        setIdx(0);
        const newList: string[] = ['도시 없음'];
        if (props.cityList) {
            setList([...newList, ...props.cityList]);
            if (props.cityList.length !== 0) {
                setIsOpen(true);
            }
        }
    }, [props.cityList]);

    return (
        <>
            <div
                className={`flex flex-col w-[40%] h-full relative border-halfvw color-border-blue-2 border-rad-2vw px-2vw`}
            >
                <div
                    onClick={() => setIsOpen((prev) => !prev)}
                    className={`w-full h-full flex items-center justify-between`}
                >
                    <span>{list[idx]}</span>
                    {isOpen ? <span>▲</span> : <span>▼</span>}
                </div>
                {isOpen ? (
                    <div className="absolute top-[100%] z-10 h-[300%] w-full overflow-y-scroll cursor-pointer">
                        {list.map((cityName, index) => {
                            return (
                                <div
                                    className={`${
                                        index === idx ? 'color-bg-blue-2' : ''
                                    }`}
                                    onClick={() => {
                                        setIdx(index);
                                        props.select(cityName);
                                        setIsOpen((prev) => !prev);
                                    }}
                                >
                                    {cityName}
                                </div>
                            );
                        })}
                    </div>
                ) : (
                    <></>
                )}
            </div>
        </>
    );
}
