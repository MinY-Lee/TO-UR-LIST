import { useEffect, useState } from "react";
import { CountryMapping, TourCardInfo } from "../../types/types";
import CountryCodeToName from "../TourPage/countryIdToName";
import MapIcon from "../../assets/svg/mapIcon";

interface PropType {
    tourInfo: TourCardInfo;
    className?: string;
    countryList: CountryMapping[];
}

export default function TourCard(props: PropType) {
    const [countryName, setCountryName] = useState<string>("");
    useEffect(() => {
        setCountryName(
            CountryCodeToName(
                props.tourInfo.cityList[0].countryCode,
                props.countryList
            )
        );
    }, [props, countryName]);

    //mode -> 진행중 : 0, 다가오는 : 1, 지난 : 2
    const nowTime = new Date();

    const now = new Date(
        `${nowTime.getFullYear()}-${
            nowTime.getMonth() + 1
        }-${nowTime.getDate()}`
    );

    const startDate = new Date(props.tourInfo.startDate);

    const endDate = new Date(props.tourInfo.endDate);
    let mode = 0;
    let dayElement = (
        <>
            <div className="text-4vw">Day</div>
            <div className="text-8vw">
                {Math.ceil(
                    (now.getTime() - startDate.getTime()) /
                        (1000 * 60 * 60 * 24)
                ) + 1}
            </div>
        </>
    );
    if (startDate > now) {
        mode = 1;
        dayElement = (
            <>
                <div className="text-6vw font-bold">
                    D-
                    {Math.floor(
                        (startDate.getTime() - now.getTime()) /
                            (1000 * 60 * 60 * 24)
                    )}
                </div>
            </>
        );
    } else if (endDate < now) {
        mode = 2;
        dayElement = <></>;
    }

    /**date string -> YYYY.MM.DD */
    const dateStringToString = (val: string) => {
        const date = val.split("T")[0];
        const dSplit = date.split("-");
        const year = dSplit[0];
        const month = dSplit[1];
        const day = dSplit[2];
        return `${year}.${month.length >= 2 ? month : "0" + month}.${
            day.length >= 2 ? day : "0" + day
        }`;
    };

    const Badge = (count: number) => {
        return (
            <span className="color-bg-blue-1 text-sm text-white font-bold flex ml-2 px-2 rounded-full">{`+${count}`}</span>
        );
    };

    return (
        <>
            <div
                className={`${
                    props.className
                } box-border w-[90%] m-2vw p-vw rounded-xl drop-shadow-lg flex items-center flex-shrink-0 ${
                    mode === 0
                        ? "color-bg-blue-3"
                        : mode === 1
                        ? "color-border-blue-3 border-2 bg-white"
                        : "border-[#DADADA] border-2 bg-white"
                }`}
                onClick={() => {
                    window.location.href = `/tour/${props.tourInfo.tourId}`;
                }}
            >
                {/* 날짜 표시 */}
                <div
                    className={`${
                        mode === 2 ? "" : "w-[30%]"
                    } h-full flex flex-col justify-center items-center p-[2vw]`}
                >
                    {dayElement}
                </div>
                <div
                    className={`${
                        mode === 2 ? "w-full" : "w-[70%]"
                    } h-full flex flex-col items-start justify-center p-[2vw]`}
                >
                    <p className="text-6vw weight-text-semibold">
                        {props.tourInfo.tourTitle}
                    </p>
                    <p className="text-4vw">{`${dateStringToString(
                        props.tourInfo.startDate
                    )}~${dateStringToString(props.tourInfo.endDate)}`}</p>
                    <div className="text-5vw flex items-center">
                        <MapIcon />
                        <p>{`${countryName}, ${props.tourInfo.cityList[0].cityName}`}</p>
                        <div>
                            {props.tourInfo.cityList.length >= 2
                                ? Badge(props.tourInfo.cityList.length - 1)
                                : ""}
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
