// 날짜 가져오기
import { useState } from 'react';
import { getDaysInMonth } from 'date-fns';

// const DATE_MONTH_FIXER: number = 1;
const CALENDER_LENGTH: number = 35;
const DEFAULT_TRASH_VALUE: number = 0;
const DAY_OF_WEEK: number = 7;
const DAY_LIST: string[] = ['일', '월', '화', '수', '목', '금', '토'];

const useCalendar = () => {
  const [currentDate, setCurrentDate] = useState<Date>(new Date());
  const totalMonthDays = getDaysInMonth(currentDate);

  const getFirstDayOfMonth = (date: Date): Date => {
    const newDate = new Date(date); // 입력된 날짜의 복사본 생성
    newDate.setDate(1); // 입력된 날짜를 해당 월의 1일로 설정
    return newDate;
};


  const prevDayList: number[] = Array.from({
    length: Math.max(0, getFirstDayOfMonth(currentDate).getDay()),
  }).map(() => DEFAULT_TRASH_VALUE);

  const currentDayList: number[] = Array.from({ length: totalMonthDays }).map(
    (_, i) => i + 1,
  );

  const nextDayList: number[] = Array.from({
    length: CALENDER_LENGTH - currentDayList.length - prevDayList.length,
  }).map(() => DEFAULT_TRASH_VALUE);

  const currentCalendarList: number[] = prevDayList.concat(currentDayList, nextDayList);

  const weekCalendarList: number[][] = currentCalendarList.reduce(
    (acc: number[][], cur, idx) => {
      const chunkIndex = Math.floor(idx / DAY_OF_WEEK);
      if (!acc[chunkIndex]) {
        acc[chunkIndex] = [];
      }
      acc[chunkIndex].push(cur);
      return acc;
    },
    [],
  );

  return {
    weekCalendarList: weekCalendarList,
    currentDate: currentDate,
    setCurrentDate: setCurrentDate,
    weekDayList : DAY_LIST,
  };
};

export default useCalendar;