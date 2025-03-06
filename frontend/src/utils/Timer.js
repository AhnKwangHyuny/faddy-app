import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

const TimerText = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2em; /* 글자 크기 */
  color: #ff0000; /* 글자 색상 */
  margin-top: 20px; /* 상단 여백 */
`;

function Timer({ initialSeconds, onTimerEnd }) {
  const [seconds, setSeconds] = useState(initialSeconds);

 useEffect(() => {
   let timerId;
   if (seconds > 0) {
     timerId = setTimeout(() => {
       setSeconds(seconds - 1);
     }, 1000);
   } else if (seconds === 0 && onTimerEnd) {
     onTimerEnd();
   }

   return () => {
     clearTimeout(timerId);
   };
 }, [seconds, onTimerEnd]);


  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;

  return (
    <TimerText>
      {`${minutes}:${remainingSeconds < 10 ? `0${remainingSeconds}` : remainingSeconds}`}
    </TimerText>
  );
}

export default Timer;

/**
 * 시간을 '오전/오후 hh:mm' 형식으로 포맷하는 함수
 * @param {number[]} timeArray - [년, 월, 일, 시, 분, 초, 나노초] 배열
 * @returns {string} 포맷된 시간 문자열
 */
export const formatTime = (timeArray) => {
    const [year, month, day, hours, minutes, seconds, nanoseconds] = timeArray;
    const date = new Date(year, month - 1, day, hours, minutes, seconds, nanoseconds / 1000000); // 월은 0부터 시작하므로 -1 필요

    const hours12 = date.getHours();
    const mins = date.getMinutes();
    const ampm = hours12 >= 12 ? '오후' : '오전';
    const formattedHours = hours12 % 12 === 0 ? 12 : hours12 % 12;
    const formattedMinutes = mins < 10 ? `0${mins}` : mins;

    return `${ampm} ${formattedHours}:${formattedMinutes}`;
};

/**
 * 시간을 '오전/오후 hh:mm' 형식으로 포맷하는 함수
 * @param {string} isoTime - ISO 8601 형식의 시간 문자열
 * @returns {string} 포맷된 시간 문자열
 */
export const formatTimeToIso = (isoTime) => {
    const date = new Date(isoTime);

    const hours = date.getHours();
    const minutes = date.getMinutes();
    const ampm = hours >= 12 ? '오후' : '오전';
    const formattedHours = hours % 12 === 0 ? 12 : hours % 12;
    const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;

    return `${ampm} ${formattedHours}:${formattedMinutes}`;
};


