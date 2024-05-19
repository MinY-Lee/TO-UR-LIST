import { useNavigate } from "react-router-dom";
import MyButton from "../Buttons/myButton";

interface PropType {
    handleSave: () => void;
}

export default function ButtonGroup(props: PropType) {
    const navigate = useNavigate();

    return (
        <>
            <MyButton
                isSelected={true}
                onClick={props.handleSave}
                text="저장"
                type="full"
                className="py-1 text-white"
            ></MyButton>
            <MyButton
                isSelected={false}
                onClick={() => navigate(-1)}
                text="취소"
                type="full"
                className="py-1  color-text-blue-2"
            ></MyButton>
        </>
    );
}
