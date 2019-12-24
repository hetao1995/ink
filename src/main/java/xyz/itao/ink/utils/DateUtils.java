package xyz.itao.ink.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * date操作相关工具类
 *
 * @author hetao
 * @date 2018-12-01 22:32
 */
public class DateUtils {


    public static Date dateFormat(String date, String dateFormat) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            try {
                return format.parse(date);
            } catch (Exception ignored) {
            }
            return null;
        }
    }


    public static String dateFormat(Date date, String dateFormat) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.format(date);
        }

        return "";
    }

    public static int getUnixTimeByDate(Date date) {
        return (int) (date.getTime() / 1000L);
    }


    public static String formatDateByUnixTime(long unixTime, String dateFormat) {
        return dateFormat(new Date(unixTime * 1000L), dateFormat);
    }

    public static Date toDate(long unixTime) {
        return Date.from(Instant.ofEpochSecond(unixTime));
    }

    public static Date getMonthsAfter(Date date, int months) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, months);
        return calender.getTime();
    }

    public static Date getNow() {
        return new Date();
    }
}
