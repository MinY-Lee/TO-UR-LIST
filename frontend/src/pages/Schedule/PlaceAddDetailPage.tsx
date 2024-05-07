import { useLocation } from 'react-router-dom';

export default function PlaceAddDetailPage() {
    const location = useLocation();

    console.log(location.state);

    return <></>;
}
