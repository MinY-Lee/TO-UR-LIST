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
            // ? props.placeInfo.placePhotoList[0].split("/")[3]
            ? props.placeInfo.placePhotoList[0]
            : "";
    const photoUrl = `/v1/${photoReference}/media?key=${
        import.meta.env.VITE_REACT_GOOGLE_MAPS_API_KEY
    }`;

    const wsClient = props.wsClient;
    const tourId = props.tourId;

    return (
        <div className="w-full h-38vw flex justify-center items-center flex-shrink-0 p-vw mb-2vw">
            <div
                className="w-[95%] p-2vw h-full flex flex-col border-rad-2vw border-[#cecece]"
                style={{ boxShadow: "0px 2px 6px 1px #cecece" }}
                onClick={() => props.goDetail(props.placeInfo.placeId)}
            >
                <div className="w-full h-[30%] flex justify-between items-center">
                    <span className="w-[80%] text-5vw text-ellipsis text-nowrap overflow-hidden">
                        {props.placeInfo.placeName}
                    </span>
                    {props.visitedCache.has(props.placeInfo.placeId) ? (
                        //이미 있는 장소면 추가됨
                        <div
                            className="w-[20%] h-7vw text-4vw border-rad-2dot5vw color-bg-green-1 color-text-green-2 color-border-green-2 border-halfvw flex justify-center items-center"
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
                            className="w-[20%] h-7vw text-4vw border-rad-2dot5vw bg-white border-halfvw color-border-blue-6 color-text-blue-6 text-white flex justify-center items-center"
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
                <div className="w-full h-[70%] flex items-center justify-between">
                    {photoReference !== "" ? (
                        <img
                            crossOrigin="anonymous"
                            src={photoUrl}
                            className="w-[30%] h-full aspect-square border-rad-2vw"
                        />
                    ) : (
                        <div className="w-[30%] h-full aspect-square bg-white border-black border-dot3vw"></div>
                    )}
                    <div className="w-[68%] h-full p-vw text-4vw flex flex-col justify-between">
                        <p>{props.placeInfo.placeAddress}</p>
                        <p className="text-3dot5vw text-[#7c7c7c]">
                            {props.placeInfo.placePrimaryType !== ""
                                ? props.placeInfo.placePrimaryType?.replaceAll(
                                      "_",
                                      " "
                                  )
                                : " "}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}
