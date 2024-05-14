import { Cookies } from 'react-cookie';
import { Navigate, Outlet } from 'react-router-dom';

export default function LoginCheck() {
    const cookies = new Cookies();

    //로그인 여부 체크, 없으면 리다이렉트
    if (!cookies.get('accessToken')) {
        return <Navigate to={'/'} />;
    } else {
        //서비스워커 실행
        if ('serviceWorker' in navigator) {
            //register service worker
            navigator.serviceWorker
                .register('/sw.js')
                .then((registration) => {
                    console.log(
                        'Service worker registration succeeded:',
                        registration
                    );
                })
                .catch((err) => {
                    console.log('Service worker registration failed:', err);
                });
        } else {
            console.log('Service workers are not supported');
        }

        //outlet
        return <Outlet />;
    }
}
