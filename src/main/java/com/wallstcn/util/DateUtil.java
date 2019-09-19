package com.wallstcn.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * @author xd
 *
 */
public abstract class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static ThreadLocal<Map<String,SimpleDateFormat>> threadLocal = new ThreadLocal<Map<String,SimpleDateFormat>>();

    public static String toDateStr(Date date, String pattern) {

        if (date == null || StringUtils.isBlank(pattern)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = localThreadSimpleDateObject(pattern);
        return simpleDateFormat.format(date);
    }

    public static Long getTimeStampAfterHours(Date date,Integer hour) {
        if (hour <= 0) {
            return date.getTime()/1000L;
        }

        return date.getTime()/1000L+60*60*hour;
    }

    public static Long getTimeStampAfterHours(Integer hour) {
        return getTimeStampAfterHours(new Date(), 2);
    }


    public static SimpleDateFormat getFormateDate(String pattern) {
        return localThreadSimpleDateObject(pattern);
    }

    private static SimpleDateFormat localThreadSimpleDateObject(String pattern) {

        Map<String, SimpleDateFormat> simpleDateFormatMap = threadLocal.get();
        if (simpleDateFormatMap == null) {
            simpleDateFormatMap = new HashMap<String, SimpleDateFormat>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            simpleDateFormatMap.put(pattern, simpleDateFormat);
            threadLocal.set(simpleDateFormatMap);
            return simpleDateFormatMap.get(pattern);
        } else {
            SimpleDateFormat simpleDateFormat = simpleDateFormatMap
                    .get(pattern);

            if (simpleDateFormat != null) {
                return simpleDateFormat;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            simpleDateFormatMap.put(pattern, dateFormat);
            return simpleDateFormatMap.get(pattern);
        }
    }

    public static Date daysAfter(Date baseDate, int increaseDate) {
        Calendar calendar = Calendar.getInstance(Locale.CHINESE);
        calendar.setTime(baseDate);
        calendar.add(Calendar.DATE, increaseDate);
        return calendar.getTime();
    }

    public static String toDatebyStr(Long timeStamp,String pattern) {
        SimpleDateFormat simpleDateFormat = localThreadSimpleDateObject(pattern);
        return simpleDateFormat.format(timeStamp);
    }

    //指定时间
    public static Date appointDate(String pattern,String date) {

        SimpleDateFormat simpleDateFormat = localThreadSimpleDateObject(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    public static void main(String[] args) {
        System.out.println(daysAfter(new Date(),1));
    }

}
