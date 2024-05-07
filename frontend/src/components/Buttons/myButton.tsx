import { useState } from 'react';

interface MyButtonProps {
    className?: string; // 추가 커스텀 사항
    type: string; // 버튼 크기 타입
    text: string; // 버튼에 표시할 텍스트
    isSelected: boolean; // 상태
    onClick: () => void;
}

const MyButton: React.FC<MyButtonProps> = ({ className, type, text, isSelected, onClick}) => {
    // const [buttonColor, setButtonColor] = useState<String>(isSelected ? 'color-bg-blue-1 text-white text-bold' : 'color-bg-blue-5 text-white');
    const buttonColor = (isSelected ? 'color-bg-blue-1 text-white font-bold' : 'box-border border-[#5faad9] border-2 color-text-blue-2');
    return (

        <div className="">
            {type === 'full' ?
                <button
                    className={`${className} ${buttonColor} rounded-lg w-full text-white font-bold text-lg`}
                    onClick={onClick}
                >
                    {text}
                </button> :
                <button
                    className={`${className || ""} ${buttonColor} rounded-xl px-5`}
                    onClick={onClick}
                >
                    {text}
                    
                </button>
            }
        </div>
    );
}

export default MyButton;
