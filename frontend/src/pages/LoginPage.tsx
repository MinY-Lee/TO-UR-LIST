import loginImage from '../assets/image/loginPage.jpg';
import googleLogin from '../assets/image/googleLogin.png';
import { useEffect } from 'react';
import { Cookies } from 'react-cookie';

export default function LoginPage() {
    const cookies = new Cookies();

    useEffect(() => {
        //로그인 되어 있으면 리다이렉트
        if (cookies.get('accessToken')) {
            window.location.href = '/main';
        }
    });

    return (
        <>
            <section
                style={{
                    backgroundImage: `url(${loginImage})`,
                    backgroundRepeat: `no-repeat`,
                    backgroundPosition: `center`,
                    backgroundSize: 'cover',
                    backgroundAttachment: 'fixed',
                }}
                className="w-full h-full"
            >
                <div
                    className="w-full h-full absolute top-0 left-0 bg-white opacity-[75%]"
                    style={{ zIndex: 0 }}
                ></div>
                <div className="z-10 absolute top-0 left-0 w-full h-full flex flex-col justify-center">
                    <div className="w-full h-[40%] flex flex-col justify-between items-center">
                        <div className="flex flex-col items-center">
                            <h1
                                className="color-text-blue-1 text-10vw"
                                style={{ fontFamily: 'TTHakgyoansimSamulhamR' }}
                            >
                                TO-UR-LIST
                            </h1>
                            <h5
                                className="color-text-blue-1 text-6vw"
                                style={{ fontFamily: 'TTHakgyoansimSamulhamR' }}
                            >
                                여행의 처음부터 끝까지!
                            </h5>
                        </div>
                        <div className="flex flex-col items-center">
                            <p className="text-[#565656] text-5vw">
                                구글 아이디로 편리하게 로그인하세요!
                            </p>
                            <div
                                className="w-[60%]"
                                onClick={() => {
                                    window.location.href =
                                        import.meta.env.VITE_REACT_GOOGLE_LOGIN_URL;
                                }}
                            >
                                <img src={`${googleLogin}`} />
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
}
