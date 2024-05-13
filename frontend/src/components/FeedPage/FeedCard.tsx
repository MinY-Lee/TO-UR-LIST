import { Feed } from '../../types/types';
import Tag from './Tag';

interface PropType {
    feedInfo: Feed;
}

export default function FeedCard(props: PropType) {
    const feedInfo = props.feedInfo;

    /**date string -> YYYY.MM.DD */
    const dateStringToString = (val: string) => {
        const dSplit = val.split('-');
        const year = dSplit[0];
        const month = dSplit[1];
        const day = dSplit[2];
        return `${year}.${month.length >= 2 ? month : '0' + month}.${
            day.length >= 2 ? day : '0' + day
        }`;
    };

    return (
        <>
            <div
                className="w-[90%] my-[1vw] flex flex-col flex-grow-0 flex-shrink-0 justify-center border-b-[0.2vw] border-b-[#7E7E7E]"
                onClick={() => {
                    window.location.href = `/feed/${feedInfo.feedId}`;
                }}
            >
                <p className="text-5vw weight-text-semibold">
                    {feedInfo.feedTitle}
                </p>
                <div className="w-full flex justify-start text-4vw">
                    <span className="weight-text-semibold text-[#564EB3] mr-[1vw]">{`${
                        feedInfo.cityList[0].countryName
                    }, ${feedInfo.cityList[0].cityName} ${
                        feedInfo.cityList.length >= 2
                            ? '(+' + (feedInfo.cityList.length - 1) + ')'
                            : ''
                    } | `}</span>
                    <span>{`${dateStringToString(
                        feedInfo.startDate
                    )} ~ ${dateStringToString(feedInfo.endDate)}`}</span>
                </div>
                <p className="text-4vw whitespace-nowrap overflow-hidden overflow-ellipsis">
                    {feedInfo.feedContent}
                </p>
                <div className="w-full flex justify-between items-center mb-[1vw]">
                    <div className="flex">
                        {feedInfo.feedThemeTagList.map((tag) => {
                            return (
                                <Tag tagName={tag} tagType="theme" key={tag} />
                            );
                        })}
                        {feedInfo.feedMateTag ? (
                            <Tag
                                tagName={feedInfo.feedMateTag}
                                tagType="mate"
                            />
                        ) : (
                            <></>
                        )}
                    </div>
                    <p className="flex items-center">
                        <span className="material-symbols-outlined">
                            bookmark
                        </span>
                        {feedInfo.copyCount}
                    </p>
                </div>
            </div>
        </>
    );
}
