import { useEffect, useState } from "react";
import MyButton from "../../components/Buttons/myButton";

import Checklist from "../../dummy-data/get_checklist.json";
import { Item } from "../../types/types";
import { HttpStatusCode } from "axios";
import { getChecklist } from "../../util/api/checklist";

interface PropType {
  tourId: string;
}
interface Mapping {
  [key: string]: string[];
}

interface CountItem {
  [key: string]: number;
}

export default function TourCheckList(props: PropType) {
  const [checklist, setChecklist] = useState<Item[]>([]);
  const [filteredChecklist, setFilteredChecklist] = useState<Item[]>([]);
  const [filteredGroup, setFilteredGroup] = useState<CountItem>({});

  useEffect(() => {
    if (props.tourId) {
      getChecklist(props.tourId)
        .then((res) => {
          if (res.status == HttpStatusCode.Ok) {
            console.log(res.data);
            setChecklist(res.data);
            // 중복 횟수 카운트
            setFilteredGroup(prepareData(checklist));
            // 중복 하나씩만 남김
            setFilteredChecklist(filterUniqueItems(checklist));
          }
        })
        .catch((err) => console.log(err));
    }
  }, [props]);

  const mapping: Mapping = {
    walking: ["👣 산책", "color-bg-blue-3"],
    shopping: ["🛒 쇼핑", "bg-pink-100"],
  };

  // 활동 id 를 한글로 변환
  const ActivityIdToKor = (tourActivityId: string): string => {
    return mapping[tourActivityId][0];
  };

  // 활동 id 별 색상 부여
  const setColor = (tourActivityId: string): string => {
    return mapping[tourActivityId][1];
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

  const handleCheckbox = (index: number) => {
    const updatedChecklist = [...filteredChecklist];
    // 나중에 실제로 api 로 반영하기
    updatedChecklist[index].isChecked = !updatedChecklist[index].isChecked;
    setFilteredChecklist(updatedChecklist);
  };

  return (
    <>
      <div className="w-full  justify-between items-end p-5 bak">
        <div className="text-xl font-bold">전체 체크리스트</div>
        <div>
          <div className=" border-2 border-blue-200 rounded-2xl p-3">
            <div className="flex w-full justify-end">
              <MyButton
                type="small"
                text="편집"
                isSelected={true}
                onClick={() => {
                  window.location.href = `/tour/${props.tourId}/checklist/all`;
                }}
                className="text-white"
              />
            </div>
            <div className="flex flex-col">
              {filteredChecklist.length == 0 ? (
                <div className="flex justify-center items-center h-[40vh] text-xl">
                  현재 체크리스트가 없습니다.
                </div>
              ) : (
                <div>
                  {filteredChecklist.map((item, index) => (
                    <div
                      key={index}
                      className="grid grid-cols-2 justify-center m-1"
                    >
                      <div className="flex items-center">
                        <input
                          id="default-checkbox"
                          type="checkbox"
                          onChange={() => handleCheckbox(index)}
                          checked={item.isChecked}
                          className="w-5 h-5 bg-gray-100 border-gray-300 rounded "
                        />
                        <label className="ms-2">{item.item}</label>
                      </div>
                      <div className="relative w-fit">
                        <div>
                          {item.tourActivityId ? (
                            <span
                              className={`${setColor(
                                item.tourActivityId
                              )} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}
                            >
                              {ActivityIdToKor(item.tourActivityId)}
                            </span>
                          ) : (
                            ""
                          )}
                        </div>
                        <div>
                          {item.tourActivityId &&
                          filteredGroup[item.item] > 1 ? (
                            <div>
                              <span className="sr-only">Notifications</span>
                              <div className="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white color-bg-blue-1 border-2 border-white rounded-full -top-2 -end-[20%]">
                                {filteredGroup[item.item]}
                              </div>
                            </div>
                          ) : (
                            ""
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
