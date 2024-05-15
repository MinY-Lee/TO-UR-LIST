import { useEffect, useRef, useState } from "react";
import { WebSockPlace } from "../../types/types";

interface PropType {
    schedule: WebSockPlace[][];
    selectedDate: number;
    tourId: string;
}

export default function Maps(props: PropType) {
    const googleMapRef = useRef<HTMLDivElement>(null);
    const [googleMap, setGoogleMap] = useState<google.maps.Map>();
    const [markers, setMarkers] = useState<google.maps.Marker[]>([]);
    const [placesService, setPlacesService] =
        useState<google.maps.places.PlacesService>();
    const [placeList, setPlaceList] = useState<google.maps.LatLng[]>([]);

    const cacheMap = useRef<Map<string, google.maps.LatLng>>(new Map());

    useEffect(() => {
        if (googleMapRef.current) {
            const initMap = new window.google.maps.Map(googleMapRef.current, {
                center: { lat: 37.5, lng: 127 },
                zoom: 13,
            });

            const marker = new google.maps.Marker({
                position: { lat: 37.5, lng: 127 },
                map: initMap,
                title: "Seoul",
                label: "0일차 서울",
            });

            const arr: google.maps.Marker[] = [];
            arr.push(marker);
            setMarkers(arr);

            const googleService = new google.maps.places.PlacesService(initMap);
            setPlacesService(googleService);

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
            const newPlaces: google.maps.LatLng[] = [];

            setMarkers(newMarkers);
            setPlaceList(newPlaces);

            //id 찾기
            props.schedule.map((daily) => {
                daily.map((place) => {
                    //선택 날짜에만 마커 표시
                    if (
                        props.selectedDate + 1 === place.tourDay ||
                        props.selectedDate === -1
                    ) {
                        const cache = cacheMap.current;
                        // console.log(cache);
                        //정보가 없으면 api 호출
                        if (!cache.get(place.placeId)) {
                            placesService?.getDetails(
                                { placeId: place.placeId },
                                (results) => {
                                    // console.log("api 호출");
                                    const lat =
                                        results.geometry?.location.lat();
                                    const lng =
                                        results.geometry?.location.lng();

                                    if (lat && lng) {
                                        const pos = new google.maps.LatLng(
                                            lat,
                                            lng
                                        );

                                        if (place.tourDay !== 0) {
                                            const marker =
                                                new google.maps.Marker({
                                                    position: { lat, lng },
                                                    map: googleMap,
                                                    title: place.placeName,
                                                    label:
                                                        place.tourDay +
                                                        "일차 " +
                                                        place.placeName,
                                                });

                                            newMarkers.push(marker);
                                        } else {
                                            const marker =
                                                new google.maps.Marker({
                                                    position: { lat, lng },
                                                    map: googleMap,
                                                    title: place.placeName,
                                                    label:
                                                        "날짜없음 " +
                                                        place.placeName,
                                                });

                                            newMarkers.push(marker);
                                        }
                                        newPlaces.push(pos);
                                        googleMap.setCenter(pos);

                                        cache.set(place.placeId, pos);
                                    }
                                }
                            );
                        } else {
                            //정보가 있으면 불러오기
                            const pos = cache.get(place.placeId);
                            if (pos) {
                                const marker = new google.maps.Marker({
                                    position: pos,
                                    map: googleMap,
                                    title: place.placeName,
                                    label:
                                        place.tourDay +
                                        "일차 " +
                                        place.placeName,
                                });
                                newMarkers.push(marker);
                                newPlaces.push(pos);
                                googleMap.setCenter(pos);
                            }
                        }
                    }
                });
            });
        }
    }, [googleMap, props.schedule, props.selectedDate]);

    return <div ref={googleMapRef} id="map" className="w-full h-[80%]"></div>;
}
