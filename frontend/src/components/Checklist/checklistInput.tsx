import { useEffect, useState } from "react";
import { Item } from "../../types/types";
import PayTypeIcon from "../../assets/svg/payTypeIcon";
import DropdownIcon from "../../assets/svg/dropdownIcon";
import { addChecklist } from "../../util/api/checklist";
import { HttpStatusCode } from "axios";

interface ItemPerPlace {
    [placeId: string]: Item[];
}

interface ItemPerDayAndPlace {
    [day: number]: ItemPerPlace;
}

interface PropType {
    className?: string;
    tourId: string;
    checklist?: Item[];
    tourDay?: number;
    placeId?: string;
    checklistPerDay?: ItemPerDayAndPlace;
    onUpdate: (item: Item) => void;
    default?: Item;
}

export default function ChecklistInput(props: PropType) {
    const [isClicked, setIsClicked] = useState<boolean>(false);
    const [isPublicInput, setIsPublicInput] = useState<boolean>(false);
    const [itemInput, setItemInput] = useState<string>("");

    useEffect(() => {
        setIsPublicInput(props.default ? props.default.isPublic : false);
        setItemInput(props.default ? props.default.item : "");
    }, [props]);

    const setDropdown = (isClicked: boolean) => {
        return isClicked ? "" : "hidden";
    };

    const handleTypeChange = (type: string) => {
        type === "private" ? setIsPublicInput(false) : setIsPublicInput(true);
        setIsClicked(false);
    };

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setItemInput(event.target.value);
    };

    // 엔터로 add
    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter" && itemInput.trim() != "") {
            const existingItem = props.checklist?.find(
                (item) => item.item === itemInput
            );
            if (!existingItem) {
                if (!event.nativeEvent.isComposing) {
                    addItem();
                }
            } else {
                event.preventDefault();
            }
        }
    };

    const addItem = () => {
        console.log(itemInput);

        if (!props.default) {
            const newItem = {
                tourId: props.tourId,
                placeId: props.placeId || "",
                activity: "",
                item: itemInput.trim(),
                tourDay: props.tourDay || 0,
                isChecked: false,
            };
            // 데이터 추가 api
            addChecklist(isPublicInput ? "public" : "private", newItem)
                .then((res) => {
                    if (
                        res.status == HttpStatusCode.Ok &&
                        !res.data.isDuplicated
                    ) {
                        props.onUpdate({
                            tourId: props.tourId,
                            placeId: props.placeId || "",
                            activity: "",
                            item: itemInput,
                            tourDay: props.tourDay || 0,
                            isChecked: false,
                            isPublic: isPublicInput,
                        });
                        setItemInput("");
                    }
                })
                .catch((err) => console.log(err));
        } else {
            // 수정인 경우
            console.log("아이템 수정 : " + itemInput);

            props.onUpdate({
                tourId: props.tourId,
                placeId: props.default.placeId || "",
                activity: props.default.activity,
                item: itemInput,
                tourDay: props.default.tourDay,
                isChecked: props.default.isChecked,
                isPublic: props.default.isPublic,
            });
        }
    };

    const handleHelpText = () => {
        const existingItem = props.checklist?.find(
            (item) => item.item == itemInput
        );
        const existingItemPerDay =
            props.checklistPerDay && props.tourDay && props.placeId
                ? props.checklistPerDay[props.tourDay][props.placeId].find(
                      (item) => item.item == itemInput
                  )
                : null;
        return existingItem || existingItemPerDay ? "" : "hidden";
    };

    return (
        <>
            <div>
                <div className="flex relative items-start">
                    <button
                        disabled={props.default !== undefined}
                        onClick={() => setIsClicked(!isClicked)}
                        id="dropdown-button"
                        data-dropdown-toggle="dropdown"
                        className={`${props.className} flex-shrink-0 z-10 inline-flex items-center py-2.5 px-4 text-center text-gray-900`}
                        type="button"
                    >
                        <PayTypeIcon isPublic={isPublicInput} />
                        <DropdownIcon isClicked={isClicked} />
                    </button>

                    <div
                        id="dropdown"
                        className={`${setDropdown(
                            isClicked
                        )} absolute top-12 z-10 bg-white divide-y divide-gray-100 rounded-lg shadow`}
                    >
                        <ul
                            className=" text-gray-700 "
                            aria-labelledby="dropdown-button"
                        >
                            <li
                                className="hover:bg-[#5faad9] px-5 py-2 rounded-t-lg border"
                                onClick={() => handleTypeChange("private")}
                            >
                                <div className="flex gap-2 items-center">
                                    <PayTypeIcon isPublic={false} />
                                    <div className="">나에게만</div>
                                </div>
                            </li>
                            <li
                                className="hover:bg-[#5faad9] px-5 py-2 rounded-b-lg border"
                                onClick={() => handleTypeChange("public")}
                            >
                                <div className="flex gap-2 items-center">
                                    <PayTypeIcon isPublic={true} />
                                    <div>모두에게</div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div className="w-[70%]">
                        <input
                            onChange={handleInputChange}
                            onKeyDown={handleKeyDown} // 엔터 키 입력 감지
                            value={itemInput}
                            type="add"
                            id="add-inputbox"
                            className="p-2.5 w-full z-20 text-gray-900 border-b-2 text-lg"
                            placeholder={
                                props.default == undefined
                                    ? "추가하려는 항목을 입력하세요."
                                    : ""
                            }
                        />
                        <p
                            id="helper-text-explanation"
                            className={`${handleHelpText()} mt-1 text-sm color-text-blue-1`}
                        >
                            이미 존재하는 항목입니다.
                        </p>
                    </div>
                </div>
            </div>
        </>
    );
}
