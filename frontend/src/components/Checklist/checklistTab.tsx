import { useState, useEffect, useRef } from "react";

import MyCheckList from "./myChecklist";
import ChecklistByDay from "./checklistByDay";

interface PropType {
    tourId: string;
}

export default function ChecklistTab(props: PropType) {
    const [tabIdx, setTabIdx] = useState<number>(1);
    // const [tabClass, setTabClass] = useState<string>("");
    

    const getTabClass = (idx : number) => {
        if (idx != tabIdx) {
            return "";
        }
        return "border-transparent bg-gradient-to-t from-[#559bd9] to-[#94cef2] text-white";
    
    }

    return (
        <div>
        <div className="flex justify-center mt-3">
            <ul className="grid grid-cols-2  w-[90%] border rounded-full color-bg-blue-4">
                <li className="rounded-full" onClick={() => setTabIdx(1)}>
                    <div 
                        className={`${getTabClass(1)} rounded-full text-lg text-center block border-x-0 border-b-2 border-t-0 border-transparent leading-tight text-neutral-500 focus:isolate focus:border-transparent data-[twe-nav-active]:border-primary data-[twe-nav-active]:text-primary`}
                    >전체보기</div>
                </li>
                <li className="rounded-full" onClick={() => setTabIdx(2)}>
                    <div className={`${getTabClass(2)} rounded-full text-lg text-center block border-x-0 border-b-2 border-t-0 border-transparent leading-tight text-neutral-500 focus:isolate focus:border-transparent data-[twe-nav-active]:border-primary data-[twe-nav-active]:text-primary`}
                    >일정별 보기</div>
                </li>
            </ul>
        </div>

        <div className="mb-6">
            {tabIdx == 1 ? <MyCheckList tourId={props.tourId}/> : <ChecklistByDay tourId={props.tourId}/>}
            
        
        </div>
        </div>
    );
}