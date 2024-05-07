import { BaseSyntheticEvent, useState, useEffect } from 'react';

interface PropType {
    onChangeTitle: (data: string) => void;
}

export default function SetTitle(props: PropType) {
    const { onChangeTitle } = props;
    const [title, setTitle] = useState<string>('');

    useEffect(() => {
        // 부모 컴포넌트에 보내기
        onChangeTitle(title);
    }, [title]);

    const handleInputChange = (event: BaseSyntheticEvent) => {
        setTitle(event.target.value);
    };

    return (
        <div className=" flex flex-col items-center">
            <div className="m-3">
                <div className="text-2xl font-bold">
                    여행의 제목을 지어주세요
                </div>
                <div className="text-lg">
                    * 생략시 기본 이름으로 설정됩니다.{' '}
                </div>
            </div>
            <div
                id="input-container"
                className="w-[90%] rounded-lg border border-solid border-neutral-300 text-xl"
            >
                <input
                    value={title}
                    onChange={handleInputChange}
                    className="relative m-0 -mr-0.5 w-[90%] flex-auto bg-clip-padding py-3 outline-none"
                    aria-label="Title"
                    aria-describedby="button-addon1"
                />
            </div>
        </div>
    );
}
