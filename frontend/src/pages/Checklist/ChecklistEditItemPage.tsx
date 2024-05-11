import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Item } from '../../types/types';
import HeaderBar from '../../components/HeaderBar/HeaderBar';
import TabBarTour from '../../components/TabBar/TabBarTour';
import MyButton from '../../components/Buttons/myButton';
import ChecklistInput from '../../components/Checklist/checklistInput';

import Checklist from '../../dummy-data/get_checklist.json';
import CheckModal from '../../components/CheckModal';
import SelectModal from '../../components/SelectModal';

interface PropType {}

export default function ChecklistEditItemPage(props: PropType) {
    // 투어 아이디 불러오기
    const address: string[] = window.location.href.split('/');

    const [tourId, setTourId] = useState<string>('');
    const [editItem, setEditItem] = useState<Item>();
    const [data, setData] = useState<Item[]>();
    const [filteredData, setFilteredData] = useState<Item[]>();
    const [checkModalActive, setCheckModalActive] = useState<boolean>(false);
    const [selectModalActive, setSelectModalActive] = useState<boolean>(false);
    const [deleteItem, setDeleteItem] = useState<Item>();

    const location = useLocation();
    const state = location.state;
    const navigate = useNavigate();

    useEffect(() => {
        setTourId(address[address.length - 3]);
        // 전달받은 아이템
        setEditItem(state.item);

        // 투어 아이디로 더미데이터에서 데이터 찾기 (임시)
        const checkListData: Item[] = Checklist.filter((checklist) => checklist.tourId === tourId);
        if (checkListData) {
            setData(checkListData);
        }
    }, [tourId]);

    useEffect(() => {
        filterItem();
    }, [data, editItem]);

    const handleDone = () => {
        ///////////////////
        // 수정된 상태 띄우기

        navigate(-1);
    };

    const closeModal = () => {
        setCheckModalActive(false);
    };

    const closeSelectModal = () => {
        setSelectModalActive(false);
    };

    const onUpdate = (item: Item) => {
        console.log('edit 페이지 도착 : ' + item.item);
    };

    interface Mapping {
        [key: string]: string[];
    }

    const mapping: Mapping = {
        walking: ['👣 산책', 'color-bg-blue-3'],
        shopping: ['🛒 쇼핑', 'bg-pink-100'],
    };

    // 해당 아이템이 사용되는 장소/활동 필터링
    const filterItem = () => {
        const dataList = data?.filter(
            (item) => item.placeId != '' && item.tourActivityId != '' && item.item == editItem?.item
        );
        setFilteredData(dataList);
    };

    const formatNumberToTwoDigits = (num: number): string => {
        return `${num < 10 && num > 0 ? '0' : ''}${num}`;
    };

    const handleDelete = () => {
        // 데이터 삭제 api
        const updatedActivity = filteredData?.filter((item) => item !== deleteItem);
        setFilteredData(updatedActivity);
        setCheckModalActive(false);
    };

    const handleDeleteModal = (item: Item) => {
        setCheckModalActive(true);
        setDeleteItem(item);
    };

    const handleEdit = () => {
        // 장소 및 활동 수정 api

        setSelectModalActive(false);
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

            {selectModalActive ? (
                <SelectModal tourId={tourId} clickOK={handleEdit} clickCancel={closeSelectModal} />
            ) : (
                <></>
            )}

            <header>
                <HeaderBar />
            </header>
            <div className="m-5">
                <div className="flex justify-between items-center mb-5">
                    <h1 className="text-3xl font-bold">항목 수정</h1>
                    <MyButton
                        className="text-xl text-white font-medium"
                        isSelected={true}
                        onClick={() => handleDone()}
                        text="완료"
                        type="small"
                    />
                </div>
                <div className="mb-5">
                    <ChecklistInput tourId={tourId} onUpdate={onUpdate} default={editItem} />
                </div>
                <div>
                    <div className="text-xl">사용되는 장소/활동</div>
                    <div>
                        {filteredData && filteredData.length > 0 ? (
                            filteredData.map((item, index) => (
                                <div
                                    key={index}
                                    className="grid grid-cols-5 border-2 m-2 p-3 rounded-lg border-[#5faad9]"
                                >
                                    <div className="col-span-1 color-text-blue-2 text-lg">
                                        Day{formatNumberToTwoDigits(item.tourDay)}
                                    </div>
                                    <div className="col-span-3 text-lg">
                                        {item.placeId} / {mapping[item.tourActivityId][0].slice(2)}
                                    </div>
                                    <div
                                        className="col-span-1 text-end text-lg"
                                        onClick={() => handleDeleteModal(item)}
                                    >
                                        x
                                    </div>
                                </div>
                            ))
                        ) : (
                            <div className="grid border-dashed border m-2 p-3 rounded-lg border-[#5faad9] color-text-blue-2 text-center">
                                사용되는 장소/활동이 없습니다
                            </div>
                        )}
                    </div>
                </div>
                <div
                    onClick={() => setSelectModalActive(true)}
                    className="underline text-center text-neutral-500 mt-10"
                >
                    장소 / 활동 추가
                </div>
            </div>
            <footer className="h-[]">
                <TabBarTour tourMode={1} tourId={tourId} />
            </footer>
        </>
    );
}
