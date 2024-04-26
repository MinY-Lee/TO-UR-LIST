import SearchBar from "../components/SearchBar/mySearchBar";
import MyCalendar from "../components/Calendar/myCalendar";
import MyButton from "../components/Buttons/myButton";
import HeaderBar from "../components/HeaderBar/HeaderBar";

export default function TourCreatePage() {
    const resultList: string[] = ["일본, 오사카", "일본, 후쿠오카", "일본, 오키나와", "대만, 가오슝"];
    return (
        <section className="m-5 flex flex-col justify-between h-[95vh]">
            
            <header className="">
                <HeaderBar/>
                <h1 className="m-3 text-3xl font-bold">
                    여행 만들기
                </h1>
            </header>
            <div className="m-5 gap-3 flex flex-col justify-center items-center">
                <div className="text-2xl font-bold">어디로 떠나시나요?</div>
                <div id='search-container' className="w-full shadow-md border border-black rounded-lg">
                    <SearchBar/>
                </div>
                <div id='search-result-container' className="h-[40vh] overflow-scroll w-[90%] ">
                    {resultList.map((res) => (
                        <div key={res} className="flex justify-between m-2">
                            <div className="text-lg">{res}</div>
                            <MyButton type="small" text="선택"/>
                        </div>
                    ))}
                </div>
            </div>
            <MyButton type="full" text="선택완료"/>
        </section>
    );
}
