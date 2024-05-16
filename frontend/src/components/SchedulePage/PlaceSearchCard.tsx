import { Client } from "@stomp/stompjs";
import { PlaceInfo } from "../../types/types";

interface PropType {
    placeInfo: PlaceInfo;
    goDetail: (placeId: string) => void;
    visitedCache: Set<string>;
    wsClient: Client;
    tourId: string;
    tourDay: number;
    setIsLoading: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function PlaceSearchCard(props: PropType) {
    const photoReference =
        props.placeInfo.placePhotoList.length >= 1
            ? props.placeInfo.placePhotoList[0].split("/")[3]
            : "";
    const photoUrl = `/maps/api/place/photo?maxwidth=400&photo_reference=${photoReference}&key=${
        import.meta.env.VITE_REACT_GOOGLE_MAPS_API_KEY
    }`;

    const wsClient = props.wsClient;
    const tourId = props.tourId;

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
                    {props.visitedCache.has(props.placeInfo.placeId) ? (
                        //이미 있는 장소면 추가됨
                        <div
                            className="w-[20%] h-8vw text-5vw border-rad-3vw bg-white color-text-blue-2 color-border-blue-2 border-halfvw flex justify-center items-center"
                            onClick={(event) => {
                                //여행 삭제
                                if (wsClient) {
                                    props.setIsLoading(true);
                                    wsClient.publish({
                                        destination: `/app/place/${tourId}`,
                                        body: JSON.stringify({
                                            type: "DELETE_PLACE",
                                            body: {
                                                tourId: tourId,
                                                placeId:
                                                    props.placeInfo.placeId,
                                                placeName:
                                                    props.placeInfo.placeName,
                                                tourDay: props.tourDay,
                                            },
                                        }),
                                    });
                                }
                                //전파 차단
                                event.stopPropagation();
                            }}
                        >
                            추가됨
                        </div>
                    ) : (
                        <div
                            className="w-[20%] h-8vw text-5vw border-rad-3vw color-bg-blue-2 text-white flex justify-center items-center"
                            onClick={(event) => {
                                //추가하는 요청 전송
                                if (wsClient) {
                                    props.setIsLoading(true);
                                    wsClient.publish({
                                        destination: `/app/place/${tourId}`,
                                        body: JSON.stringify({
                                            type: "ADD_PLACE",
                                            body: {
                                                tourId: tourId,
                                                placeId:
                                                    props.placeInfo.placeId,
                                                placeName:
                                                    props.placeInfo.placeName,
                                                tourDay: props.tourDay,
                                            },
                                        }),
                                    });
                                }
                                //전파 차단
                                event.stopPropagation();
                            }}
                        >
                            추가
                        </div>
                    )}
                </div>
                <div className="w-full h-[70%] flex items-center">
                    {photoReference !== "" ? (
                        <img
                            crossOrigin="anonymous"
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
