package com.example.dubei.activity.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date yyyyMMddToDate(String yyyyMMdd) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        return sdf.parse(yyyyMMdd);
    }

    //datetime yyyy-MM-dd HH:mm:ss
    public static String formatDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String addDays(Date date,int days,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH,days);
        Date newTime = c.getTime();
        return sdf.format(newTime);
    }


    //将date中的时分秒格式化为0后，计算之间的天数
    public static int getDays(Date date1,Date date2){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        c1.set(Calendar.HOUR,0);
        c1.set(Calendar.MINUTE,0);
        c1.set(Calendar.SECOND,0);
        c1.set(Calendar.MILLISECOND,0);
        long time1 = c1.getTime().getTime();
        c2.set(Calendar.HOUR,0);
        c2.set(Calendar.MINUTE,0);
        c2.set(Calendar.SECOND,0);
        c2.set(Calendar.MILLISECOND,0);
        long time2 = c2.getTime().getTime();
        return (int)Math.abs((time1-time2))/1000/60/60/24;
    }

    public static String getCurDate(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String result = sdf.format(new Date());
        return result;
    }

}
