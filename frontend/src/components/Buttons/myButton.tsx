import { useState } from 'react';

interface MyButtonProps {
    type: string; // 버튼 크기 타입
    text: string; // 버튼에 표시할 텍스트
}

const MyButton: React.FC<MyButtonProps> = ({ type, text }) => {
    return (

        <div className="">
            {type === 'full' ?
                <button
                    className='color-bg-blue-2 rounded-lg w-full py-3 text-white font-bold text-lg'>
                    {text}
                </button> :
                <button
                    className='color-bg-blue-5 rounded-xl px-5 text-white'>
                    {text}
                </button>
            }
        </div>
    );
}

export default MyButton;
