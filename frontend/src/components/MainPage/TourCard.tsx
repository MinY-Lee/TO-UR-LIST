import { useEffect, useState } from "react";
import { CountryMapping, TourCardInfo } from "../../types/types";
import { GetCountryList } from "../../util/api/country";
import { HttpStatusCode } from "axios";
import CountryCodeToName from "../TourPage/countryIdToName";

interface PropType {
    tourInfo: TourCardInfo;
    className?: string;
    countryList: CountryMapping[];
}

export default function TourCard(props: PropType) {
    const tour = props.tourInfo;
    // const [countryList, setCountryList] = useState<CountryMapping[]>([]);

    // useEffect(() => {
    //     GetCountryList()
    //         .then((res) => {
    //             if (res.status === HttpStatusCode.Ok) {
    //                 setCountryList(res.data);
    //             }
    //         })
    //         .catch((err) => {
    //             console.log(err);
    //         });
    // }, []);

    //mode -> 진행중 : 0, 다가오는 : 1, 지난 : 2
    const nowTime = new Date();

    const now = new Date(
        `${nowTime.getFullYear()}-${
            nowTime.getMonth() + 1
        }-${nowTime.getDate()}`
    );

    const startDate = new Date(tour.startDate);

    const endDate = new Date(tour.endDate);
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
            <span className="border border-black text-sm flex ml-2 px-2 rounded-full">{`+${count}`}</span>
        );
    };

    return (
        <>
            <div
                className={`${
                    props.className
                } box-border w-[90%] h-[15%] m-2vw p-vw border-rad-2vw flex items-center flex-shrink-0 ${
                    mode === 0
                        ? "color-bg-blue-3"
                        : mode === 1
                        ? "color-bg-blue-4"
                        : "bg-[#DADADA]"
                }`}
                style={{
                    boxShadow: "#EBEBEB 1vw 1vw ",
                }}
                onClick={() => {
                    window.location.href = `/tour/${tour.tourId}`;
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
                        {tour.tourTitle}
                    </p>
                    <p className="text-4vw">{`${dateStringToString(
                        tour.startDate
                    )}~${dateStringToString(tour.endDate)}`}</p>
                    <div className="text-5vw flex items-center">
                        <span className="material-symbols-outlined mr-vw">
                            location_on
                        </span>
                        <p>{`${CountryCodeToName(
                            tour.cityList[0].countryCode,
                            props.countryList
                        )}, ${tour.cityList[0].cityName}`}</p>
                        <div>
                            {tour.cityList.length >= 2
                                ? Badge(tour.cityList.length - 1)
                                : ""}
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
