import { useState } from 'react';
import MyButton from '../../components/Buttons/myButton'

import Checklist from '../../dummy-data/get_checklist.json'
import { Item } from '../../types/types';

interface PropType {
    tourId: string;
}

export default function MyCheckList(props: PropType) {
    const id = props.tourId;


    interface Mapping {
        [key: string]: string[];
    }

    const mapping: Mapping = {
        'walking' : ['👣 산책', 'color-bg-blue-3'],
        'shopping' : ['🛒 쇼핑', 'bg-pink-100'],
    }

    // 활동 id 를 한글로 변환
    const ActivityIdToKor = (tourActivityId : string): string => {
        return mapping[tourActivityId][0];
    }

    // 활동 id 별 색상 부여
    const setColor = (tourActivityId : string): string => {
        return mapping[tourActivityId][1];
    }

    interface CountItem {
        [key: string]: number;
    }

    // 같은 체크리스트 아이템 처리
    const prepareData = (checklist : Item[]) => {
        const itemGroups : CountItem = {};

        checklist.forEach((item) => {
            const itemName = item.item;
            if (itemName) {
                if (!itemGroups[itemName]) {
                    itemGroups[itemName] = 0;
                }
                itemGroups[itemName]++;
            }
        });

        return itemGroups;
    };

    // 같은 항목 리스트에 여러 번 띄우지 않게 처리
    const filterUniqueItems = (checklist: Item[]): Item[] => {
        const seenItems = new Set<string>();
        const uniqueItems: Item[] = [];

        checklist.forEach((item) => {
            const itemName = item.item;
            if (itemName && !seenItems.has(itemName)) {
                seenItems.add(itemName);
                uniqueItems.push(item);
            }
        });

        return uniqueItems;
    };

    const filteredGroups = prepareData(Checklist); // 중복 횟수 카운트
    
    // 중복 하나씩만 남김
    const [filteredChecklist, setFilteredChecklist] = useState<Item[]>(filterUniqueItems(Checklist));

    const handleCheckbox = (index : number) => {
        const updatedChecklist = [...filteredChecklist];
        updatedChecklist[index].isChecked = !updatedChecklist[index].isChecked;
        setFilteredChecklist(updatedChecklist);
    }

    return (
        <>
            <div className="w-full  justify-between items-end p-5 bak">
                <div>
                    <div className=" border-2 border-blue-200 rounded-2xl p-3">
                        <div className="flex w-full justify-end">
                            <MyButton 
                                type="small" 
                                text="편집" 
                                isSelected={true} 
                                onClick={() => {
                                    window.location.href = `/tour/${id}/checklist/all`;
                                }}/>
                        </div>
                        <div className="flex flex-col">
                            {filteredChecklist.map((item, index) => 
                                (
                                    <div key={index} className="grid grid-cols-3 justify-center m-1">
                                        <div className="flex items-center col-span-2">
                                            <input id="default-checkbox" type="checkbox" onChange={() => handleCheckbox(index)} checked={item.isChecked} className="w-5 h-5 bg-gray-100 border-gray-300 rounded "/>
                                            <div className='ml-2'>
                                                {item.isPublic
                                                    ? <svg width="22" height="18" viewBox="0 0 22 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                            <path d="M4.5 4.60416C4.5 4.28406 4.56305 3.9671 4.68554 3.67137C4.80804 3.37563 4.98758 3.10693 5.21393 2.88058C5.44027 2.65424 5.70898 2.4747 6.00471 2.3522C6.30044 2.2297 6.6174 2.16666 6.9375 2.16666C7.2576 2.16666 7.57456 2.2297 7.87029 2.3522C8.16602 2.4747 8.43473 2.65424 8.66107 2.88058C8.88742 3.10693 9.06696 3.37563 9.18946 3.67137C9.31195 3.9671 9.375 4.28406 9.375 4.60416C9.375 5.25062 9.11819 5.87061 8.66107 6.32773C8.20395 6.78485 7.58397 7.04166 6.9375 7.04166C6.29103 7.04166 5.67105 6.78485 5.21393 6.32773C4.75681 5.87061 4.5 5.25062 4.5 4.60416ZM6.9375 0.541656C5.86006 0.541656 4.82675 0.969669 4.06488 1.73154C3.30301 2.4934 2.875 3.52671 2.875 4.60416C2.875 5.6816 3.30301 6.71491 4.06488 7.47678C4.82675 8.23864 5.86006 8.66666 6.9375 8.66666C8.01494 8.66666 9.04825 8.23864 9.81012 7.47678C10.572 6.71491 11 5.6816 11 4.60416C11 3.52671 10.572 2.4934 9.81012 1.73154C9.04825 0.969669 8.01494 0.541656 6.9375 0.541656ZM15.0625 5.41666C15.0625 4.98568 15.2337 4.57235 15.5385 4.26761C15.8432 3.96286 16.2565 3.79166 16.6875 3.79166C17.1185 3.79166 17.5318 3.96286 17.8365 4.26761C18.1413 4.57235 18.3125 4.98568 18.3125 5.41666C18.3125 5.84763 18.1413 6.26096 17.8365 6.5657C17.5318 6.87045 17.1185 7.04166 16.6875 7.04166C16.2565 7.04166 15.8432 6.87045 15.5385 6.5657C15.2337 6.26096 15.0625 5.84763 15.0625 5.41666ZM16.6875 2.16666C15.8255 2.16666 14.9989 2.50907 14.3894 3.11856C13.7799 3.72805 13.4375 4.5547 13.4375 5.41666C13.4375 6.27861 13.7799 7.10526 14.3894 7.71475C14.9989 8.32425 15.8255 8.66666 16.6875 8.66666C17.5495 8.66666 18.3761 8.32425 18.9856 7.71475C19.5951 7.10526 19.9375 6.27861 19.9375 5.41666C19.9375 4.5547 19.5951 3.72805 18.9856 3.11856C18.3761 2.50907 17.5495 2.16666 16.6875 2.16666ZM0.4375 12.7292C0.4375 12.0827 0.694307 11.4627 1.15143 11.0056C1.60855 10.5485 2.22853 10.2917 2.875 10.2917H11C11.6465 10.2917 12.2665 10.5485 12.7236 11.0056C13.1807 11.4627 13.4375 12.0827 13.4375 12.7292V12.9112C13.4372 12.9857 13.4318 13.06 13.4212 13.1338C13.3289 13.9216 13.0357 14.6725 12.5697 15.3145C11.6809 16.5414 9.99087 17.6042 6.9375 17.6042C3.88413 17.6042 2.19575 16.5414 1.30362 15.3145C0.838295 14.6724 0.545658 13.9215 0.45375 13.1338C0.445786 13.0598 0.440366 12.9855 0.4375 12.9112V12.7292ZM2.0625 12.8754V12.8868L2.069 12.9664C2.13172 13.4695 2.32067 13.9486 2.61825 14.359C3.148 15.087 4.30175 15.9792 6.9375 15.9792C9.57325 15.9792 10.727 15.087 11.2567 14.359C11.5543 13.9486 11.7433 13.4695 11.806 12.9664L11.8125 12.8852V12.7292C11.8125 12.5137 11.7269 12.307 11.5745 12.1546C11.4222 12.0023 11.2155 11.9167 11 11.9167H2.875C2.65951 11.9167 2.45285 12.0023 2.30048 12.1546C2.1481 12.307 2.0625 12.5137 2.0625 12.7292V12.8754ZM16.6875 15.9792C15.732 15.9792 14.9487 15.8329 14.315 15.5924C14.5731 15.1141 14.7702 14.6053 14.9016 14.0779C15.3371 14.242 15.9156 14.3542 16.6875 14.3542C18.5059 14.3542 19.2517 13.7334 19.58 13.2719C19.7734 13.0035 19.8953 12.6904 19.9343 12.3619L19.9375 12.3164C19.9358 12.2098 19.8922 12.1081 19.8162 12.0333C19.7402 11.9586 19.6379 11.9166 19.5312 11.9167H14.9812C14.8608 11.3275 14.611 10.7725 14.25 10.2917H19.5312C20.6525 10.2917 21.5625 11.2017 21.5625 12.3229V12.3505C21.5614 12.4098 21.5571 12.4689 21.5495 12.5277C21.4824 13.1371 21.2597 13.7191 20.9027 14.2177C20.217 15.178 18.9316 15.9792 16.6875 15.9792Z" fill="#363636"/>
                                                        </svg>
                                                    : <svg width="22" height="18" viewBox="0 0 15 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                            <path d="M12.75 9.00666C13.3467 9.00666 13.919 9.22158 14.341 9.60416C14.7629 9.98673 15 10.5056 15 11.0467V11.7267C15 14.4072 12.21 17.1667 7.5 17.1667C2.79 17.1667 0 14.4072 0 11.7267V11.0467C0 10.5056 0.237053 9.98673 0.65901 9.60416C1.08097 9.22158 1.65326 9.00666 2.25 9.00666H12.75ZM12.75 10.3667H2.25C2.05109 10.3667 1.86032 10.4383 1.71967 10.5658C1.57902 10.6933 1.5 10.8663 1.5 11.0467V11.7267C1.5 13.6823 3.648 15.8067 7.5 15.8067C11.352 15.8067 13.5 13.6823 13.5 11.7267V11.0467C13.5 10.8663 13.421 10.6933 13.2803 10.5658C13.1397 10.4383 12.9489 10.3667 12.75 10.3667ZM7.5 0.166656C8.59402 0.166656 9.64323 0.560691 10.4168 1.26208C11.1904 1.96346 11.625 2.91475 11.625 3.90666C11.625 4.89857 11.1904 5.84985 10.4168 6.55124C9.64323 7.25262 8.59402 7.64666 7.5 7.64666C6.40598 7.64666 5.35677 7.25262 4.58318 6.55124C3.8096 5.84985 3.375 4.89857 3.375 3.90666C3.375 2.91475 3.8096 1.96346 4.58318 1.26208C5.35677 0.560691 6.40598 0.166656 7.5 0.166656ZM7.5 1.52666C6.80381 1.52666 6.13613 1.77741 5.64384 2.22374C5.15156 2.67008 4.875 3.27544 4.875 3.90666C4.875 4.53787 5.15156 5.14323 5.64384 5.58957C6.13613 6.03591 6.80381 6.28666 7.5 6.28666C8.19619 6.28666 8.86387 6.03591 9.35616 5.58957C9.84844 5.14323 10.125 4.53787 10.125 3.90666C10.125 3.27544 9.84844 2.67008 9.35616 2.22374C8.86387 1.77741 8.19619 1.52666 7.5 1.52666Z" fill="#363636"/>
                                                        </svg>
                                                    }
                                            </div>
                                            <label 
                                                className="ms-2 w-[70%] overflow-ellipsis overflow-hidden whitespace-nowrap">
                                                {item.item}
                                            </label>
                                        </div>
                                        
                                        <div className='relative w-fit'>
                                            <div>
                                                {item.tourActivityId ?
                                                    <span className={`${setColor(item.tourActivityId)} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}>{ActivityIdToKor(item.tourActivityId)}</span>
                                                    : ""}
                                            </div>
                                            <div>
                                                {item.tourActivityId && filteredGroups[item.item] > 1 ?
                                                <div>
                                                    <span className="sr-only">Notifications</span>
                                                    <div className="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white color-bg-blue-1 border-2 border-white rounded-full -top-2 -end-[20%]">
                                                        {filteredGroups[item.item]}
                                                    </div>
                                                </div>
                                                : ""}
                                            </div>
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
