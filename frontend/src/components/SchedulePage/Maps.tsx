import { TourPlaceItem } from '../../types/types';

interface PropType {
    schedule: TourPlaceItem[][];
    selectedDate: number;
}

export default function Maps(props: PropType) {
    const pos = { lat: 37.5, lng: 127 };

    return <></>;
}
