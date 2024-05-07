import { useEffect, useRef, useState } from 'react';
import { TourPlaceItem } from '../../types/types';

interface PropType {
    schedule: TourPlaceItem[][];
    selectedDate: number;
    tourId: string;
}

export default function Maps(props: PropType) {
    const googleMapRef = useRef<HTMLDivElement>(null);
    const [googleMap, setGoogleMap] = useState<google.maps.Map>();
    const [markers, setMarkers] = useState<google.maps.Marker[][]>([[]]);
    const [placesService, setPlacesService] =
        useState<google.maps.places.PlacesService>();
    const [placeList, setPlaceList] = useState<google.maps.LatLng[][]>([[]]);

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
                title: 'Seoul',
                label: '0일차 서울',
            });

            const arr: google.maps.Marker[][] = [[]];
            arr[0].push(marker);
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
                    markers[i].map((marker) => {
                        marker.setMap(null);
                    });
                }
            }

            //마커, 위치 정보 초기화
            const newMarkers: google.maps.Marker[][] = [];
            const newPlaces: google.maps.LatLng[][] = [];
            for (let i = 0; i < props.schedule.length; i++) {
                const arr1: google.maps.Marker[] = [];
                const arr2: google.maps.LatLng[] = [];
                newMarkers.push(arr1);
                newPlaces.push(arr2);
            }

            setMarkers(newMarkers);
            setPlaceList(newPlaces);

            //id 찾기
            props.schedule.map((daily) => {
                daily.map((place) => {
                    const cache = cacheMap.current;
                    console.log(cache);
                    //정보가 없으면 api 호출
                    if (!cache.get(place.placeId)) {
                        placesService?.getDetails(
                            { placeId: place.placeId },
                            (results) => {
                                console.log('api 호출');
                                const lat = results.geometry?.location.lat();
                                const lng = results.geometry?.location.lng();

                                if (lat && lng) {
                                    const pos = new google.maps.LatLng(
                                        lat,
                                        lng
                                    );

                                    const marker = new google.maps.Marker({
                                        position: { lat, lng },
                                        map: googleMap,
                                        title: place.placeName,
                                        label:
                                            place.tourDay +
                                            '일차 ' +
                                            place.placeName,
                                    });
                                    newMarkers[place.tourDay - 1].push(marker);
                                    newPlaces[place.tourDay - 1].push(pos);
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
                                    place.tourDay + '일차 ' + place.placeName,
                            });
                            newMarkers[place.tourDay - 1].push(marker);
                            newPlaces[place.tourDay - 1].push(pos);
                            googleMap.setCenter(pos);
                        }
                    }
                });
            });
        }
    }, [googleMap, props.schedule]);

    useEffect(() => {
        for (let i = 0; i < markers.length; i++) {
            if (props.selectedDate === -1 || i === props.selectedDate) {
                //마커 표시
                if (googleMap) {
                    markers[i].map((marker) => {
                        marker.setMap(googleMap);
                    });
                }
            } else {
                //마커 삭제
                if (googleMap) {
                    markers[i].map((marker) => {
                        marker.setMap(null);
                    });
                }
            }
        }
        if (placeList[props.selectedDate] && googleMap) {
            googleMap.setCenter(placeList[props.selectedDate][0]);
        }
    }, [props.selectedDate, googleMap, props.schedule]);

    return <div ref={googleMapRef} id="map" className="w-full h-[80%]"></div>;
}
