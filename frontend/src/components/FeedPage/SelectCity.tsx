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
        }
    }, [props.cityList]);

    return (
        <>
            <div
                className={`flex flex-col w-[40%] ${
                    isOpen ? 'h-[200%]' : 'h-full'
                } overflow-y-scroll relative`}
            >
                <div
                    onClick={() => setIsOpen((prev) => !prev)}
                    className={`${
                        isOpen ? '' : 'flex justify-center items-center h-full'
                    }`}
                >
                    {list[idx]}
                </div>
                {isOpen ? (
                    <>
                        {list.map((cityName, index) => {
                            return (
                                <div
                                    className={`${
                                        index === idx ? 'color-bg-blue-2' : ''
                                    }`}
                                    onClick={() => {
                                        setIdx(index);
                                        props.select(cityName);
                                    }}
                                >
                                    {cityName}
                                </div>
                            );
                        })}
                    </>
                ) : (
                    <></>
                )}
            </div>
        </>
    );
}
