import { useEffect, useState } from "react";
import MyButton from '../../components/Buttons/myButton'
import { TourInfoDetail, MemberInfo } from "../../types/types";

import Checklist from '../../dummy-data/get_checklist.json'

export default function TourCheckList(props: TourInfoDetail) {
    const data = props.data;

    const [hoveredMember, setHoveredMember] = useState<MemberInfo | null>(null);

    const isHost = (member: MemberInfo): string => {
        if (member.memberType === "host") {
            return "w-14 h-14 rounded-full flex border-gradient bg-gradient-to-b from-blue-500 via-blue-400 to-blue-100";
        }
        return "";
    };

    // 활동 id 를 한글로 변환
    const ActivityIdToKor = (tourActivityId) => {
        const mapping = {
            'walking' : '👣 산책',
            'shopping' : '🛒 쇼핑'
        }
        return mapping[tourActivityId];
    }

    return (
        <>
            <div className="w-full justify-between items-end p-5 bak">
                    
                <div className="text-xl font-bold">
                    전체 체크리스트
                </div>
                <div>
                    <div className=" border-2 border-blue-200 rounded-2xl p-3">
                        <div className="flex w-full justify-end">
                            <MyButton type="small" text="편집" onClick={""}/>
                        </div>
                        <div className="flex flex-col">
                            {Checklist.map((item, index) => 
                                (
                                    <div key={index} className="grid grid-cols-2 justify-center m-1">
                                        <div className="flex items-center">
                                            <input id="default-checkbox" type="checkbox" value="" className="w-5 h-5 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"/>
                                            <label for="default-checkbox" class="ms-2">{item.item}</label>
                                        </div>
                                        <div>
                                            {item.tourActivityId ?
                                                <span className="color-bg-blue-3 text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded">{ActivityIdToKor(item.tourActivityId)}</span> : 
                                                ""}
                                            
                                        </div>
                                    </div>
                                )
                            )}
                        </div>
                    </div>
                </div>
                
                
            </div>
        </>
    );
}
