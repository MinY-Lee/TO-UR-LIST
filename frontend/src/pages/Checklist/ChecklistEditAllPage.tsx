import { useState, useEffect } from "react";

import MyButton from "../../components/Buttons/myButton";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import { Item } from "../../types/types";

import Checklist from '../../dummy-data/get_checklist.json'


export default function ChecklistEditAllPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    
    const [data, setData] = useState<Item[]>([]);
    const [tourId, setTourId] = useState<string>("");
    
    
    useEffect(() => {
        setTourId(address[address.length - 3]);

        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const checkListData : Item[] = Checklist.filter(checklist => checklist.tourId === tourId);
        if (checkListData) {
            setData(checkListData);
        }

    }, [data]);

    const handleDone = () => {

    }

    return (
        <>
            <header>
                <HeaderBar/>
            </header>
            <div className="m-5">
                <div className="flex justify-between items-center">
                    <h1 className="text-3xl font-bold">전체 체크리스트</h1>
                    <MyButton className="text-2xl" isSelected={true} onClick={() => handleDone()} text="완료" type="small"/>

                </div>
                <div>
                    {data.map((item, index) => (
                        <div>
                            {item.item}
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}
