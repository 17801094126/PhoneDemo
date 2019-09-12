package com.example.phone;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换工具
 */
public class TimeUtils {
    /**
     * 时间戳转为时年月日
     *
     * @param cc_time 时间
     * @return
     */
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        Date date=new Date();
        //同理也可以转为其它样式的时间格式.例如�yyyy/MM/dd HH:mm"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;
    }
    /**
     * Date类型转换0位时间戳
     * @param time
     * @return
     */
    public static Integer DateToTimestamp(Date time){
        Timestamp ts = new Timestamp(time.getTime());
        return (int) ((ts.getTime())/1000);
    }
    /**
     * 10位时间戳转换为时间
     */
    public static String getTime(long str_num){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(new Date(str_num*1000));
            return date;
    }
    /**
     * 13位时间戳转换为时间
     */
    public static String getYearTime(long str_num){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date(str_num));
        return date;
    }
    /**
     * 13位时间戳转换位年月日
     * @param str_num
     */
    public static String getYearTime(String str_num){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(toLong(str_num)));
        return date;
    }
    public static long toLong(String obj){
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

        /**
         * 时间转换为时间戳
         *
         * @param timeStr 时间 例如: 2016-03-09
         * @param format  时间对应格式  例如: yyyy-MM-dd
         * @return
         */
    public static long getTimeStamp(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 时间戳转换成字符�
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        if(String.valueOf(milSecond).length() == 10)
        {
            milSecond = milSecond * 1000;
        }
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    /**
     * 将字符串转为时间�
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 13位时间戳转换�0位时间戳
     * @return
     */
    public static long getLongTime(long time){
        String timestamp = String.valueOf(time/1000);
        return Integer.valueOf(timestamp);
    }
    /*
     * 将时间转换为时间�
     */
    public static Long dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }
    /**
     * 每隔半个小时请求一次数�
     */
    /*public static void interval(OnNextListener listener)
    {
     *//*   Observable.interval(60*30, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (listener != null) {
                            listener.success(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (listener != null) {
                            listener.error(e.toString());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });*//*
    }*/
}
