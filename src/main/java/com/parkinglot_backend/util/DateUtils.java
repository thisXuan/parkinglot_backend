package com.parkinglot_backend.util;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author: HeYuxin
 * @CreateTime: 2025-01-27
 * @Description:
 */


public class DateUtils {
    public static Date getLastDayOfMonth() {
        YearMonth yearMonth = YearMonth.now();
        LocalDate lastDay = yearMonth.atEndOfMonth();
        return Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
