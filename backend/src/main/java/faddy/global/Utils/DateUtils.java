package faddy.global.Utils;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class DateUtils {

    // LocalDateTime을 받아서 "오후 12:40" 형태로 변환
    public static String convertLocalDateTimeToChatTimeFormat(LocalDateTime localDateTime) {
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();

        String timeFormat = "오전 ";
        if (hour >= 12) {
            timeFormat = "오후 ";
            if (hour > 12) {
                hour -= 12;
            }
        } else if (hour == 0) {
            hour = 12; // 자정 처리
        }

        // 시간과 분이 한 자리 수일 때 앞에 0을 추가합니다.
        String formattedHour = String.format("%02d", hour);
        String formattedMinute = String.format("%02d", minute);

        return timeFormat + formattedHour + ":" + formattedMinute;
    }
}
