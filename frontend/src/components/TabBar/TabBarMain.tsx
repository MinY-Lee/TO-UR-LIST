import { useEffect, useState } from "react";
import FeedIcon from "../../assets/svg/feedIcon";

interface PropType {
    tabMode: number;
    type: string;
}

export default function TabBarMain(props: PropType) {
    //메인 화면 등 투어 화면이 아닌 곳에서 보이는 탭바
    const selected = `color-text-blue-2`;

    const [type, setType] = useState<string>("");

    useEffect(() => {
        if (props.type) {
            setType(props.type);
        }
    }, [props]);

    return (
        <>
            <div
                className={`${
                    type == "feed" ? selected : ""
                }  w-[96%] grid grid-cols-3 absolute bottom-3 left-2  text-neutral-500 rounded-lg  justify-around items-center border-2 drop-shadow-md bg-white z-10`}
            >
                <div
                    className="hover:bg-[#d5e7f2] h-full rounded-l-lg flex pt-1 flex-col items-center justify-evenly"
                    onClick={() => {
                        window.location.href = `/feed`;
                    }}
                >
                    <FeedIcon />
                    <div>피드</div>
                </div>
                <div className="hover:bg-[#d5e7f2] h-full  ">
                    {props.tabMode == 2 ? (
                        <div
                            className={`${
                                props.type == "main" ? selected : ""
                            } mt-0 h-full  flex flex-col items-center justify-evenly rounded-full py-1 text-neutral-500`}
                            onClick={() => {
                                window.location.href = `/`;
                            }}
                        >
                            <span className="material-symbols-outlined text-4xl ">
                                home
                            </span>
                            <div>메인</div>
                        </div>
                    ) : (
                        <div
                            className={`${
                                type == "create" ? selected : ""
                            } h-full  flex flex-col items-center justify-evenly text-neutral-500 rounded-full py-1`}
                            onClick={() => {
                                window.location.href = `/create`;
                            }}
                        >
                            <span className="flex justify-center items-center text-4xl font-bold">
                                +
                            </span>
                            <div>여행추가</div>
                        </div>
                    )}
                </div>
                <div className=" hover:bg-[#d5e7f2] h-full">
                    <div
                        className={`${
                            type == "mypage" ? selected : ""
                        } h-full flex flex-col pt-1 rounded-r-lg items-center justify-evenly text-neutral-500`}
                        onClick={() => {
                            window.location.href = `/mypage`;
                        }}
                    >
                        <span className="material-symbols-outlined  text-3xl ">
                            person
                        </span>
                        <div className="">마이페이지</div>
                    </div>
                </div>
            </div>
        </>
    );
}
