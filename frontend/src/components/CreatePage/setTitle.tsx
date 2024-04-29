import { useState, useEffect } from "react";

export default function SetTitle(props) {
  
  // const { onChangeDate } = props;

  // const [startDate, setStartDate] = useState<Date>();
  // const [endDate, setEndDate] = useState<Date>();

  // useEffect(() => {
  //     // 부모 컴포넌트에 보내기
  //     onChangeDate([startDate, endDate]);

  // }, [startDate, endDate]);

  // // datepicker 로 부터 데이터 받기
  // const handleDataFromChild = (data: Date[]) => {
  //     setStartDate(data[0]);
  //     setEndDate(data[1]);
  // };

  const [title, setTitle] = useState<String>(""); 

  const handleInputChange = (event) => {
    setTitle(event.target.value);
  };

  return (
    <div>
      <div>
        <div className="text-2xl font-bold">여행의 제목을 지어주세요</div>
        <div className="text-lg">* 생략시 기본 이름으로 설정됩니다. </div>
      </div>
      <div id="input-container" className="flex w-full rounded-lg border border-solid border-neutral-300">
        <input
            value={title}
            onChange={handleInputChange}
            className="relative m-0 -mr-0.5 w-[1px] flex-auto bg-transparent bg-clip-padding px-3 py-[0.25rem] font-normal outline-none"
            aria-label="Title"
            aria-describedby="button-addon1"
        />
      </div>
    </div>
  );
}
