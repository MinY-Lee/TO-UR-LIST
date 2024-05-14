import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import MyButton from '../../components/Buttons/myButton';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import CheckModal from '../../components/CheckModal';
import ChecklistInput from '../../components/Checklist/checklistInput';
import TrashIcon from '../../assets/svg/trashIcon';
import { Item } from '../../types/types';

import TabBarTour from '../../components/TabBar/TabBarTour';
import { getChecklist } from '../../util/api/checklist';
import { HttpStatusCode } from 'axios';
import PayTypeIcon from '../../assets/svg/payTypeIcon';

interface Mapping {
    [key: string]: string[];
}

interface CountItem {
    [key: string]: number;
}

export default function ChecklistEditAllPage() {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');
    const navigate = useNavigate();

    const [data, setData] = useState<Item[]>([]);
    const [tourId, setTourId] = useState<string>('');
    const [checklist, setChecklist] = useState<Item[]>([]);
    const [filteredChecklist, setFilteredChecklist] = useState<Item[]>([]);
    const [filteredGroup, setFilteredGroup] = useState<CountItem>({});

    const [checkModalActive, setIsCheckModalActive] = useState<boolean>(false);
    const [deleteItem, setDeleteItem] = useState<Item>();

    useEffect(() => {
        setTourId(address[address.length - 3]);
    }, []);

    useEffect(() => {
        if (tourId != '') {
            getChecklist(tourId)
                .then((res) => {
                    if (res.status == HttpStatusCode.Ok) {
                        const checklistData = res.data;
                        setData(checklistData);
                        // 중복 횟수 카운트
                        const preparedData = prepareData(checklistData);
                        setFilteredGroup(preparedData);
                        // 중복 하나씩만 남김
                        const uniqueItems = filterUniqueItems(checklistData);
                        setFilteredChecklist(uniqueItems);
                    }
                })
                .catch((err) => console.log(err));
        }
    }, [tourId]);

    const handleEditChecklist = (item: Item) => {
        // state로 데이터 전달하며 페이지 이동
        navigate(`/tour/${tourId}/checklist/edit`, { state: { item: item } });
    };

    const mapping: Mapping = {
        walking: ['👣 산책', 'color-bg-blue-3'],
        shopping: ['🛒 쇼핑', 'bg-pink-100'],
    };

    // 활동 id 를 한글로 변환
    const ActivityToKor = (activity: string): string => {
        return mapping[activity][0];
    };

    // 활동 id 별 색상 부여
    const setColor = (activity: string): string => {
        return mapping[activity][1];
    };

    // 같은 체크리스트 아이템 처리
    const prepareData = (checklist: Item[]) => {
        const itemGroups: CountItem = {};

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

    const onUpdate = (item: Item) => {
        const updatedChecklist = [...filteredChecklist, item];
        setFilteredChecklist(updatedChecklist);
    };

    const handleDone = () => {
        navigate(-1);
    };

    const closeModal = () => {
        setIsCheckModalActive(false);
    };

    const handleDelete = () => {
        // 데이터 삭제 api
        const updatedChecklist = filteredChecklist.filter((currentItem) => currentItem !== deleteItem);
        setFilteredChecklist(updatedChecklist);
        setIsCheckModalActive(false);
    };

    const handleDeleteModal = (item: Item, event: React.MouseEvent<HTMLDivElement>) => {
        event.stopPropagation(); // 이벤트 버블링 중단

        setIsCheckModalActive(true);
        setDeleteItem(item);
    };

    return (
        <>
            {checkModalActive ? (
                <CheckModal
                    mainText="정말 삭제하시겠습니까?"
                    subText="지운 항목은 되돌릴 수 없습니다."
                    OKText="삭제"
                    CancelText="취소"
                    clickOK={handleDelete}
                    clickCancel={closeModal}
                />
            ) : (
                <></>
            )}

            <header>
                <HeaderBar />
            </header>
            <div className="m-5">
                <div className="flex justify-between items-center mb-5">
                    <h1 className="text-3xl font-bold">전체 체크리스트</h1>
                    <MyButton
                        className="text-xl font-medium text-white"
                        isSelected={true}
                        onClick={() => handleDone()}
                        text="완료"
                        type="small"
                    />
                </div>
                <div className="mb-5">
                    <ChecklistInput tourId={tourId} checklist={filteredChecklist} onUpdate={onUpdate} />
                </div>
                <div className="flex flex-col justify-start items-center h-[65vh]  overflow-y-scroll pt-2">
                    {filteredChecklist.length == 0 ? (
                        <div className="flex justify-center items-center h-[5vh] text-xl">
                            현재 체크리스트가 없습니다.
                        </div>
                    ) : (
                        <div className="w-[90%] flex flex-col gap-4">
                            {filteredChecklist.map((item, index) => (
                                <div
                                    key={index}
                                    className="grid grid-cols-6 justify-center"
                                    onClick={() => {
                                        handleEditChecklist(item);
                                    }}
                                >
                                    <div className="ml-2 col-span-3 flex items-center">
                                        <PayTypeIcon isPublic={item.isPublic} />
                                        <div className=" text-lg flex items-center ml-3">{item.item}</div>
                                    </div>
                                    <div className="col-span-3 grid grid-cols-3 justify-center">
                                        <div className="relative w-fit col-span-2">
                                            <div>
                                                {item.activity ? (
                                                    <span
                                                        className={`${setColor(
                                                            item.activity
                                                        )} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}
                                                    >
                                                        {ActivityToKor(item.activity)}
                                                    </span>
                                                ) : (
                                                    <span
                                                        className={` text-gray-500 border-2 border-dashed px-2.5 py-0.5 rounded`}
                                                    >
                                                        + 활동없음
                                                    </span>
                                                )}
                                            </div>
                                            <div>
                                                {item.activity && filteredGroup[item.item] > 1 ? (
                                                    <div>
                                                        <span className="sr-only">Notifications</span>
                                                        <div className="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white color-bg-blue-1 border-2 border-white rounded-full -top-2 -end-[20%]">
                                                            {filteredGroup[item.item]}
                                                        </div>
                                                    </div>
                                                ) : (
                                                    ''
                                                )}
                                            </div>
                                        </div>
                                        <div
                                            onClick={(event) => {
                                                handleDeleteModal(item, event);
                                            }}
                                            className="flex justify-end mr-2 items-center col-span-1"
                                        >
                                            <TrashIcon />
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
            <footer className="h-[]">
                <TabBarTour tourMode={1} tourId={tourId} />
            </footer>
        </>
    );
}
