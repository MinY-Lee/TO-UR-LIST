import { useState } from "react";
import HeaderBar from "../../components/HeaderBar/HeaderBar";

export default function AccountPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const tourId: string = address[address.length - 2];

    const [tapIdx, setTabIdx] = useState<number>(1);

    const activeStyle = "font-bold color-bg-blue-3";

    return (
        <>
            <header>
                <HeaderBar/>
            </header>
            <div className="">
                <div id='tab-container' className="rounded-lg flex flex-col items-center">
                    <ul className="w-[90%] grid grid-cols-3 text-sm  text-center">
                        <li className="me-2" onClick={()=>setTabIdx(1)}>
                            <div className={`${tapIdx == 1 ? activeStyle : ""} inline-block w-full py-2  rounded-tl-lg`}>개인 지출</div>
                        </li>
                        <li className="me-2" onClick={()=>setTabIdx(2)}>
                            <div className={`${tapIdx == 2 ? activeStyle : ""} inline-block w-full py-2`}>공동 지출</div>
                        </li>
                        <li className="me-2"  onClick={()=>setTabIdx(3)}>
                            <div className={`${tapIdx == 3 ? activeStyle : ""} inline-block w-full py-2 rounded-tr-lg`}>환율</div>
                        </li>
                    </ul>
                    <div className="w-[90%] shadow-lg color-bg-blue-3 rounded-b-lg">
                        <div>랜딩</div>

                    </div>
                </div>
            </div>
            <p>tourId : {tourId}</p>
        </>
    );
}
