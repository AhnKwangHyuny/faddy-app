import React from 'react';
import { formatTime, formatTimeToIso } from 'utils/Timer';

const SentTime = ({ createdAt }) => {
  let formattedTime;

  if (Array.isArray(createdAt)) {
    // [년, 월, 일, 시, 분, 초, 나노초] 배열인 경우
    formattedTime = formatTime(createdAt);
  } else {
    // ISO 8601 형식으로 간주하고 처리
    formattedTime = formatTimeToIso(createdAt);
  }

  return (
    <div className="sentTime">
      <span className="time">{formattedTime}</span>
    </div>
  );
};

export default SentTime;