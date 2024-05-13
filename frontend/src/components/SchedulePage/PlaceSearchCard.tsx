import { PlaceInfo } from '../../types/types';

interface PropType {
    placeInfo: PlaceInfo;
    goDetail: (placeId: string) => void;
}

export default function PlaceSearchCard(props: PropType) {
    const photoReference =
        props.placeInfo.placePhotoList.length >= 1
            ? props.placeInfo.placePhotoList[0].split('/')[3]
            : '';
    const photoUrl = `https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${photoReference}&key=${
        import.meta.env.VITE_REACT_GOOGLE_MAPS_API_KEY
    }`;

    return (
        <div className="w-full h-35vw flex justify-center items-center flex-shrink-0 p-vw">
            <div
                className="w-[95%] p-vw h-full color-border-blue-2 border-halfvw border-rad-2vw flex flex-col"
                onClick={() => props.goDetail(props.placeInfo.placeId)}
            >
                <div className="w-full h-[30%] flex justify-between items-center">
                    <span className="w-[80%] text-5vw text-ellipsis text-nowrap overflow-hidden">
                        {props.placeInfo.placeName}
                    </span>
                    <div className="w-[20%] h-[80%] border-rad-5vw color-bg-blue-2 text-white flex justify-center items-center">
                        추가
                    </div>
                </div>
                <div className="w-full h-[70%] flex items-center">
                    {photoReference !== '' ? (
                        <img
                            src={photoUrl}
                            className="w-[30%] h-full aspect-square border-rad-2vw"
                        />
                    ) : (
                        <div className="w-[30%] h-full aspect-square bg-white border-black border-dot3vw"></div>
                    )}
                    <div className="w-[70%] h-full p-vw text-4vw">
                        {props.placeInfo.placeAddress}
                    </div>
                </div>
            </div>
        </div>
    );
}
