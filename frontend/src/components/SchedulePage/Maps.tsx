import { useEffect, useRef, useState } from "react";
import { PlaceInfoDetail, WebSockPlace } from "../../types/types";
import { searchPlaceDetail } from "../../util/api/place";

interface PropType {
    schedule: WebSockPlace[][];
    selectedDate: number;
    tourId: string;
    selectedSchedule: WebSockPlace | undefined;
}

export default function Maps(props: PropType) {
    const googleMapRef = useRef<HTMLDivElement>(null);
    const [googleMap, setGoogleMap] = useState<google.maps.Map>();
    const [markers, setMarkers] = useState<google.maps.Marker[]>([]);

    useEffect(() => {
        if (googleMapRef.current) {
            const initMap = new window.google.maps.Map(googleMapRef.current, {
                center: { lat: 37.5, lng: 127 },
                zoom: 13,
                disableDefaultUI: true,
            });

            const marker = new google.maps.Marker({
                position: { lat: 37.5, lng: 127 },
                map: initMap,
                title: "Seoul",
            });

            const arr: google.maps.Marker[] = [];
            arr.push(marker);
            setMarkers(arr);

            setGoogleMap(initMap);
        }
    }, []);

    useEffect(() => {
        if (googleMap) {
            //마커 초기화
            if (markers) {
                for (let i = 0; i < markers.length; i++) {
                    markers.map((marker) => {
                        marker.setMap(null);
                    });
                }
            }

            //마커, 위치 정보 초기화
            const newMarkers: google.maps.Marker[] = [];

            setMarkers(newMarkers);

            //id 찾기
            props.schedule.map((daily) => {
                daily.map((place) => {
                    //선택 날짜에만 마커 표시
                    if (
                        props.selectedDate + 1 === place.tourDay ||
                        props.selectedDate === -1
                    ) {
                        // console.log(cache);
                        //정보가 없으면 api 호출
                        searchPlaceDetail(
                            props.tourId,
                            place.tourDay,
                            place.placeId
                        ).then((res) => {
                            const result: PlaceInfoDetail = res.data.placeInfo;

                            //선택된 지역이 있는가?
                            if (props.selectedSchedule) {
                                const lat = result.placeLatitude;
                                const lng = result.placeLongitude;
                                if (lat && lng) {
                                    //존재 시 추가
                                    const pos = new google.maps.LatLng(
                                        lat,
                                        lng
                                    );

                                    //존재 시, 그리고 현재 위치가 우리가 찾던 위치
                                    if (
                                        props.selectedSchedule.placeId ===
                                        result.placeId
                                    ) {
                                        //특별한 마커로
                                        const marker = new google.maps.Marker({
                                            position: { lat, lng },
                                            map: googleMap,
                                            title: place.placeName,
                                        });

                                        newMarkers.push(marker);
                                        //중앙으로 맞추기
                                        googleMap.setCenter(pos);
                                    } else {
                                        //마커 (선택 안된 애들, 투명하게)
                                        const marker = new google.maps.Marker({
                                            position: { lat, lng },
                                            map: googleMap,
                                            title: place.placeName,
                                            opacity: 0.3,
                                        });

                                        newMarkers.push(marker);
                                    }
                                }
                            } else {
                                //없으면 그냥 다 보여주기

                                const lat = result.placeLatitude;
                                const lng = result.placeLongitude;
                                if (lat && lng) {
                                    //존재 시 추가
                                    const pos = new google.maps.LatLng(
                                        lat,
                                        lng
                                    );

                                    //마커
                                    const marker = new google.maps.Marker({
                                        position: { lat, lng },
                                        map: googleMap,
                                        title: place.placeName,
                                    });

                                    newMarkers.push(marker);

                                    googleMap.setCenter(pos);
                                }
                            }
                        });
                    }
                });
            });
        }
    }, [googleMap, props.schedule, props.selectedDate, props.selectedSchedule]);

    return <div ref={googleMapRef} id="map" className="w-full h-[80%]"></div>;
}
