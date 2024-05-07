interface PropType {
    isSelected: number;
    select: (a: number) => void;
    selectBoxMode: number;
    setSelectBoxMode: React.Dispatch<React.SetStateAction<number>>;
}

export default function GenderSelectBox(props: PropType) {
    const data = ['기입 안함', '남성', '여성'];

    return (
        <>
            <div
                className="w-full h-full z-10 flex justify-between items-center bg-white px-[2vw] mt-[1vw] border-[0.3vw] border-[#929292]"
                style={{ borderRadius: '1vw' }}
                onClick={() => {
                    props.setSelectBoxMode((prev) => (prev + 1) % 2);
                }}
            >
                <div>{data[props.isSelected]}</div>
                {props.selectBoxMode === 0 ? (
                    <span className="material-symbols-outlined">
                        expand_more
                    </span>
                ) : (
                    <span className="material-symbols-outlined">
                        expand_less
                    </span>
                )}
            </div>
            {props.selectBoxMode === 1 ? (
                data.map((value, index) => {
                    return (
                        <div
                            key={value}
                            className={`w-full h-full flex justify-start items-center bg-white px-[2vw] border-x-[0.3vw] border-[#929292] ${
                                index === 0
                                    ? 'border-t-[0.3vw]'
                                    : index === data.length - 1
                                    ? 'border-b-[0.3vw]'
                                    : ''
                            }
                            ${
                                index === props.isSelected
                                    ? 'color-bg-blue-2 text-white'
                                    : ''
                            }
                            
                            `}
                            style={{
                                borderRadius:
                                    index === 0
                                        ? '1vw 1vw 0 0'
                                        : index === data.length - 1
                                        ? '0 0 1vw 1vw'
                                        : '',
                            }}
                            onClick={() => {
                                props.select(index);
                                props.setSelectBoxMode(0);
                            }}
                        >
                            {value}
                        </div>
                    );
                })
            ) : (
                <></>
            )}
        </>
    );
}
