import { TourPlaceItem } from '../../types/types';

interface PropType {
    schedule: TourPlaceItem;
}

export default function PlaceCard(props: PropType) {
    const bgColor = ['color-bg-blue-5', 'bg-[#FFD4D4]', 'color-bg-blue-2`'];

    return (
        <>
            <div className="w-full p-[1vw] my-[0.5vw] rounded-[2vw] border-[0.5vw] border-[#D9D9D9]">
                <p className="text-[6vw]">{props.schedule.placeName}</p>
                <div className="w-full flex">
                    {props.schedule.tourActivityList.map((activity, index) => {
                        return (
                            <p
                                className={`text-[4vw] px-[1vw] mx-[0.5vw] rounded-[2vw] ${
                                    index <= 2 ? bgColor[index] : bgColor[0]
                                }`}
                            >
                                {activity.activity}
                            </p>
                        );
                    })}
                </div>
            </div>
        </>
    );
}
