import { Item } from "../../types/types";

interface CountItem {
    [key: string]: number;
}

interface PropType {
    filteredChecklist: Item[];
    filteredGroup: CountItem;
    item: Item;
    hadNoContent: boolean; // + 활동없음 띄울지 말지
}

interface Mapping {
    [key: string]: string;
}

export default function ActivityBadge(props: PropType) {
    const mapping: Mapping = {
        산책: "color-bg-blue-3",
        쇼핑: "bg-pink-100",
    };

    // 활동 id 별 색상 부여
    const setColor = (activity: string): string => {
        if (mapping[activity]) {
            return mapping[activity];
        }
        return "color-bg-green-1";
    };

    return (
        <>
            <div>
                {props.item.activity ? (
                    <span
                        className={`${setColor(
                            props.item.activity
                        )} text-gray-500 drop-shadow-md px-2.5 py-0.5 rounded`}
                    >
                        {props.item.activity}
                    </span>
                ) : (
                    ""
                )}
            </div>
            <div>
                {props.item.activity &&
                props.filteredGroup[props.item.item] > 1 ? (
                    <div className="relative">
                        <span className="sr-only">Notifications</span>
                        <div className="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white color-bg-blue-1 border-2 border-white rounded-full -top-8  -end-3 z-10">
                            {props.filteredGroup[props.item.item]}
                        </div>
                    </div>
                ) : (
                    ""
                )}
            </div>
        </>
    );
}
