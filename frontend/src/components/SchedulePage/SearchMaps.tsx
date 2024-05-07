import { useEffect, useRef, useState } from 'react';

export default function SearchMaps() {
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

            const arr: google.maps.Marker[][] = [[]];
            setMarkers(arr);

            const googleService = new google.maps.places.PlacesService(initMap);
            setPlacesService(googleService);

            setGoogleMap(initMap);
        }
    }, []);

    return <div ref={googleMapRef} id="map" className="w-full h-full"></div>;
}
