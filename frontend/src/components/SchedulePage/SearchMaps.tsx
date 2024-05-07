import { useEffect, useRef, useState } from 'react';
import { PlaceInfo } from '../../types/types';

interface PropType {
    searchedPlaces: PlaceInfo[];
}

export default function SearchMaps(props: PropType) {
    const googleMapRef = useRef<HTMLDivElement>(null);
    const [googleMap, setGoogleMap] = useState<google.maps.Map>();
    const [markers, setMarkers] = useState<google.maps.Marker[]>([]);

    useEffect(() => {
        if (googleMapRef.current) {
            const initMap = new window.google.maps.Map(googleMapRef.current, {
                center: { lat: 37.5, lng: 127 },
                zoom: 13,
            });

            const arr: google.maps.Marker[] = [];
            setMarkers(arr);

            setGoogleMap(initMap);
        }
    }, []);

    useEffect(() => {
        //검색된 장소 변화 시 마커, 중앙 다르게
        if (googleMap) {
            //기존 마커 초기화
            if (markers) {
                markers.map((marker) => marker.setMap(null));
            }

            const newMarkers: google.maps.Marker[] = [];

            props.searchedPlaces.map((place) => {
                const lat = place.placeLatitude;
                const lng = place.placeLongitude;

                if (lat && lng) {
                    const pos = new google.maps.LatLng(lat, lng);

                    const marker = new google.maps.Marker({
                        position: { lat, lng },
                        map: googleMap,
                        title: place.placeName,
                        label: place.placeName,
                    });

                    newMarkers.push(marker);
                    googleMap.setCenter(pos);
                }
            });
        }
    }, [props.searchedPlaces, googleMap]);

    return <div ref={googleMapRef} id="map" className="w-full h-full"></div>;
}
