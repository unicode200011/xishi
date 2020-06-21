/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.xishi.user.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @author jeeplus
 * @version 2017-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static String getFormatDateTime(Long time, Object... pattern) {
        Date date = new Date();
        date.setTime(time);
        return formatDate(date, pattern);
    }

    public static Date getDateByTime(Long time) {
        Date date = new Date();
        date.setTime(time);
        return date;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Object date) {
        if(date == null)return "";
        if(date instanceof Date){
            return formatDate((Date) date, "yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }
    /**
     * 得到指定日期星期字符串 格式（E）星期几
     */
    public static String parseWeek(Object str) {
        Date forParse = parseDate(str);
        return formatDate(forParse, "E");
    }
    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 获取过去的秒数
     *
     * @param date
     * @return
     */
    public static long pastMillis(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param currentDate
     * @param oldDate
     * @return
     */
    public static String getDatePoor(Date currentDate, Date oldDate) {
        Long timeMillis = currentDate.getTime() - oldDate.getTime();
        if (timeMillis < 0) {
            return "00:00";
        }
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return (day > 0 ? day + "," : "") + (hour > 0 ? hour + ":" : "") + (min > 0 ? (min < 10 ? "0" + min : min) : "00") + ":" + (s > 0 ? (s < 10 ? "0" + s : s) : "00");
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        if(before == null|| after == null){
            return 0;
        }
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        long l = (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
        if(l < 1 && l>0){
            return 1;
        }
        return l;
    }
    /**
     * 获取两个日期之间的月份数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getMonthOfTwoDate(Date before, Date after) {
        int year = before.getYear();
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 字符串时间转LONG
     *
     * @param sdate
     * @return
     */
    public static long string2long(String sdate) {
        if (sdate.length() < 11) {
            sdate = sdate + " 00:00:00";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        Date dt2 = null;
        try {
            dt2 = sdf.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //继续转换得到秒数的long型
        long lTime = dt2.getTime();
        return lTime;
    }

    /**
     * LONG时间转字符串
     *
     * @param ldate
     * @return
     */
    public static String long2string(Object ldate) {
        if (null == ldate) {
            return "";
        }
        String time= String.valueOf(ldate);
        if(time.length() == 10){
            time = String.valueOf(Long.valueOf(time) * 1000);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //前面的ldate是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(Long.valueOf(time));
        String sDateTime = sdf.format(dt);  //得到精确到秒的表示
        if (sDateTime.endsWith("00:00:00")) {
            sDateTime = sDateTime.substring(0, 10);
        }
        return sDateTime;
    }
    /**
     * LONG时间转字符串
     *
     * @param ldate
     * @return
     */
    public static String long2string(Object ldate,Object temp) {
        if (null == ldate) {
            return "";
        }
        String time= String.valueOf(ldate);
        if(time.length() == 10){
            time = String.valueOf(Long.valueOf(time) * 1000);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(temp.toString());
        //前面的ldate是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(Long.valueOf(time));
        String sDateTime = sdf.format(dt);  //得到精确到秒的表示
        if (sDateTime.endsWith("00:00:00")) {
            sDateTime = sDateTime.substring(0, 10);
        }
        return sDateTime;
    }

    /***
     * Description: 计算已过去的时间
     * @author: lx
     * @date: 2019/1/28 14:31
     */
    public static String parsePastTime(Date date) {
        long pastMillis = DateUtils.pastMillis(date);//过去的秒数
        long pastMinutes = DateUtils.pastMinutes(date);//过去的分钟数
        long pastHour = DateUtils.pastHour(date);//过去的小时数
        long pastDays = DateUtils.pastDays(date);//过去的天数
        if (pastMillis < 60) {
            return pastMillis + "秒前";
        }
        if (pastMillis > 59 && pastMinutes < 59) {
            return pastMinutes + "分钟前";
        }
        if (pastMinutes > 59 && pastHour < 24) {
            return formatDate(date,"HH:mm");
        }
        if (pastHour > 24 && pastDays < 5) {
            return pastDays + "天前";
        }
        return formatDate(date);
    }

    /***
     * Description:localDate转 date
     * @author: lx
     * @date: 2019/1/28 14:27
     */
    public static Date parseLocalDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static Date parseLocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }
    public static LocalDateTime parseDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    /***
     * Description:date转 localDate
     * @author: lx
     * @date: 2019/1/28 14:27
     */
    public static LocalDate parseDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    /***
     * Description: 根据经纬度获取地址
     * @author: lx
     * @date: 2019/1/28 15:24
     */
    public static String getAdd(String log, String lat) {
        //lat 小  log  大
        //参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + "," + log + "&type=010";
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
            return "";
        }
        System.out.println(res);
        return res;
    }

    /**
     * 根据已知经纬度获取附近指定范围的经纬度范围
     * 维度 :latitude
     * 经度 :longitude
     * 半径：raidus 单位米
     */
    public Map<String,Object> getAroundPosition(Double latitude, Double longitude, Double raidus){
        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minlatitude = latitude - radiusLat;
        Double maxlatitude = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minlongitude = longitude - radiusLng;
        Double maxlongitude = longitude + radiusLng;
        Map<String,Object> param = new HashMap();
        param.put("minlatitude", minlatitude);
        param.put("minlongitude", minlongitude);
        param.put("maxlatitude", maxlatitude);
        param.put("maxlongitude", maxlongitude);
        return param;
    }

    /***
     * Description: 获取两值之间的随机数
     * @author: lx
     * @date: 2019/1/30 9:58
     */
    public static BigDecimal getBigdecimalBetweenMaxToMin(double max, double min) {
        double v = Math.random() * max + min;
        BigDecimal bigDecimal = new BigDecimal(v);
        return bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_UP);
    }

    /***
     * Description: 获取当前时间戳
     * @author: lx
     * @date: 2019/2/19 10:29
     */
    public static long getTimeTro() {
        long now = new Date().getTime() / 1000;
        return now;
    }

    /***
     * Description: 根据传入时间获取间戳
     * @author: lx
     * @date: 2019/2/19 10:29
     */
    public static Integer getTimeTroByDate(Date date) {
        long time = date.getTime() / 1000;
        return (int) time;
    }

    /***
     * Description: 根据传入时间字符串获取间戳
     * @author: lx
     * @date: 2019/2/19 10:29
     */
    public static Integer getTimeTroByDateStr(Object str) {
        long time = parseDate(str).getTime() / 1000;
        return (int) time;
    }

    /**
     * unicode转码
     * @param s
     * @return
     */
    public static String getUnicodeString(String s){
        char[] chars = s.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            builder.append("\\u").append(Integer.toString(c, 16));
        }
        return builder.toString();
    }

    public static String getTomarrodata(int i) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String sdate = DateUtils.getDate();
        //过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, i);
        Date m = c.getTime();
        String mon = format.format(m);
        return  mon;
    }

    /***
    * Description: 传入时间戳
    * @author: lx
    * @date: 2019/3/6 16:45
    */
    public static String formatTimeToString(Integer time,String pattern) {
        Date date = new Date();
        date.setTime(time*1000L);
        String s = DateUtils.formatDate(date,pattern);
        return s;
    }

    /**
     * 每几天执行一次的corn表达式
     * @param date
     * @return
     */
    public static String getDayCornStr(Date date,int days){
        String cron = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        cron = cron+ second + " ";
        cron = cron + minute + " ";
        cron = cron + hour + " ";
        cron =  cron + "1/"+days + " ";

        cron = cron + "* * ?";
        return cron;

    }

 /**
     * 每几小时执行一次的corn表达式
     * @param date
     * @return
     */
    public static String getHourCornStr(Date date,int hours){
        //0 0 0/1 * * ? 每小时执行一次
       String cron = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);

        cron = cron+ second + " ";
        cron = cron + minute + " ";
        cron =  cron + "0/"+hours + " ";

        cron = cron + "* * ?";
        return cron;
    }
    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTime2Date( LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        Date date = Date.from(zdt.toInstant());
         return date;
    }

    /**
     * 获取金额拆分 个位十位百位千位万位等
     * @param num
     * @return
     */
    public static Map<String,String> getNumDetail(String num){
        Map result = new HashMap();
        List<String> danWei = Arrays.asList("fen", "jiao", "yuan", "shi", "bai", "qian", "wan", "shiWan","baiWan","qianWan");

        BigDecimal inNum = new BigDecimal(num).setScale(2,BigDecimal.ROUND_UP);
        //将金额转换为分
        BigDecimal fenNum = inNum.multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_DOWN);
        //转换为long
        long fen = Long.parseLong(fenNum + "");
        String fenStr = fen+"";
        for(int i = 0;i<fenStr.length();i++){
            int weishu = (int)Math.pow(10, i);
            if(i < 1){
                long fenVal = fen % 10;
                result.put("fen",fenVal);
                result.put("bigfen",getBigNumHan(Integer.parseInt(fenVal+"")));
            }else {
                String key = danWei.get(i);
                long val = fen / weishu % 10;
                result.put(key,val);
                result.put("big"+key,getBigNumHan(Integer.parseInt(val+"")));
            }
        }
        return result;
    }

    /**
     * 数字转中文大写
     * @param Num
     * @return
     */
    public static  String getBigNumHan(Integer Num){
         final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆","伍", "陆", "柒", "捌", "玖" };
         return CN_UPPER_NUMBER[Num];
    }

    /**
     * 时间戳转日期字符串
     */
    public static String formatTimeTroToStr(Long time){
        if(time == null){
            return  "";
        }
        if(time.toString().length()==10){
            time = time * 1000;
        }
        Date date = new Date(time);
        return formatDate(date);
    }


    /**
     * echart柱状图本周数据转换
     * @param mapList
     * @return
     */
    public static Map<String,Object> getWeekOption(List<Map<String, Object>> mapList){
        String zero = "0";
        String[] arr = {zero,zero,zero,zero,zero,zero,zero};
        for(Map map : mapList){
            int weekNum = 0;
            String weekE = DateUtils.parseWeek(map.get("dateTime"));
            switch (weekE){
                case "星期一":
                    weekNum = 0;
                    break;
                case "星期二":
                    weekNum = 1;
                    break;
                case "星期三":
                    weekNum = 2;
                    break;
                case "星期四":
                    weekNum = 3;
                    break;
                case "星期五":
                    weekNum = 4;
                    break;
                case "星期六":
                    weekNum = 5;
                    break;
                case "星期日":
                    weekNum = 6;
                    break;
                case "Sun":
                    weekNum = 6;
                    break;
                case "Mon":
                    weekNum = 0;
                    break;
                case "Tue":
                    weekNum = 1;
                    break;
                case "Wed":
                    weekNum = 2;
                    break;
                case "Thu":
                    weekNum = 3;
                    break;
                case "Fri":
                    weekNum = 4;
                    break;
                case "sat":
                    weekNum = 5;
                    break;
            }
            String weekRecord = map.get("weekRecord").toString();
            arr[weekNum] = weekRecord;
        }
        Map map = new HashMap();
        map.put("data",arr);
        return map;
    }


    public static String getFileSize(MultipartFile file){
        double size = (double)file.getSize();
        if (size < 1024) {
            return size+"B";
        } else if (size < 1048576) {
            double l =size / 1024;
            BigDecimal val = BigDecimal.valueOf(l).setScale(2,BigDecimal.ROUND_DOWN);
            return val+"K";
        }else if (size < 1073741824) {
            double l =size / 1048576;
            BigDecimal val = BigDecimal.valueOf(l).setScale(2,BigDecimal.ROUND_DOWN);
            return val+"M";
        }else if (size > 1073741824) {
            double l =size / 1073741824;
            BigDecimal val = BigDecimal.valueOf(l).setScale(2,BigDecimal.ROUND_DOWN);
            return val+"G";
        }
        return "";
    }

    /**
     * 获取图片尺寸
     * @return
     */
    public static String getImageSize(InputStream inputStream){
        BufferedImage sourceImg = null;
        try {
            sourceImg = ImageIO.read(inputStream);
        } catch (IOException e) {
           return "";
        }
        double width = sourceImg.getWidth();
        double height = sourceImg.getHeight();
        return width+"*"+height;
    }

    /**
     * 获取本月所有日期
     * @param date
     * @return
     */
    public static List<Date> getAllTheDateOftheMonth(Date date) {
        List<Date> list = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        int month = cal.get(Calendar.MONTH);
        while(cal.get(Calendar.MONTH) == month){
            list.add(cal.getTime());
            cal.add(Calendar.DATE, 1);
        }
        return list;
    }
    /**
     * 获取两个日期之间的日期
     * @param start 开始日期
     * @param end 结束日期
     * @return 日期集合
     */
    public static List<Date> getBetweenDates(Date start, Date end) {
        List<Date> list = new ArrayList<Date>(); //保存日期集合
        Calendar cd = Calendar.getInstance();//用Calendar 进行日期比较判断
        while (start.getTime()<=end.getTime()){
            list.add(start);
            cd.setTime(end);
            cd.add(Calendar.DATE, 1);//增加一天 放入集合
            start=cd.getTime();
        }
        return list;
    }
    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @return
     */
    public static List<String> getBetweenDates(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, 1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }


    /**
     * html转pdf
     *
     * @param srcPath
     *            html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath
     *            pdf保存路径
     *@param toPdfTool
     *            wkhtmltopdf在系统中的路径
     * @return 转换成功返回true
     */
    public static boolean convert(String srcPath, String destPath,String toPdfTool) {
        File file = new File(destPath);
        File parent = file.getParentFile();
        // 如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(toPdfTool);
        cmd.append(" ");
        cmd.append(" \"");
        cmd.append(srcPath);
        cmd.append("\" ");
        cmd.append(" ");
        cmd.append(destPath);

        System.out.println(cmd.toString());
        boolean result = true;
        OutputStream out = null;
        try {
//            out =  new FileOutputStream(file);
            Process proc = Runtime.getRuntime().exec(cmd.toString());
//            IOUtils.write(IOUtils.toByteArray(proc.getInputStream()), out);
            proc.waitFor();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    public static Process getPdfProcess(String srcPath, String destPath,String toPdfTool) {
        File file = new File(destPath);
        File parent = file.getParentFile();
        // 如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(toPdfTool);
        cmd.append(" ");
//        cmd.append(" \"");
        cmd.append(srcPath);
//        cmd.append("\" ");
        cmd.append(" ");
        cmd.append(destPath);

        System.out.println(cmd.toString());
        boolean result = true;
        OutputStream out = null;
        Process proc = null;
        try {
//            out =  new FileOutputStream(file);
            proc =  Runtime.getRuntime().exec(cmd.toString());
//            IOUtils.write(IOUtils.toByteArray(proc.getInputStream()), out);
            proc.waitFor();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return proc;
    }

    /**
     * 获取最近N天的日期集合
     * @param
     */
    public static List<String> getLastDaysDateStr(Date date,int days){
        List<String> dayList = new ArrayList<String>();
        long start=date.getTime();
        long end = getBeforeOrAfterDate(date,days).getTime();
        if(days > 0){
            do {
                dayList.add(DateUtils.parseDateToLocalDate(new Date(start)).toString());
                start = start + (24 * 60 * 60 * 1000);
            } while (start <= end);
        }else {
            do {
                dayList.add(DateUtils.parseDateToLocalDate(new Date(start)).toString());
                start = start - (24 * 60 * 60 * 1000);
            } while (start >= end);
        }
        return dayList;
    }

    /**
     * 获取距离date num天的日期
     * @param date
     * @param num
     * @return
     */
    public static Date getBeforeOrAfterDate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();//获取日历
        calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
        calendar.add(Calendar.DATE, num);
        Date d = calendar.getTime();//把日历转换为Date
        return d;
    }

    public static String getHttpGetParam(boolean mark,String url,Map param){
        StringBuffer query = new StringBuffer();
        Iterator var5 = param.entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry<String, String> kv = (Map.Entry)var5.next();
            try {
                query.append(URLEncoder.encode((String)kv.getKey(), "UTF-8") + "=");
                query.append(URLEncoder.encode((String)kv.getValue(), "UTF-8") + "&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (query.lastIndexOf("&") > 0) {
            query.deleteCharAt(query.length() - 1);
        }
        String urlNameString = mark?url + "?" + query.toString():query.toString();
        return urlNameString;
    }

    public static String getNowTimeTro8(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String yyyymmdd = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        return yyyymmdd;
    }

    public static void main(String[] args) {
        String url = "www.baidu.com";
        Map map = new HashMap();
        map.put("userName","kkkkk");
        map.put("userId","123");
        map.put("age","2");
        String httpGetParam = DateUtils.getHttpGetParam(false,url, map);
        System.out.println(httpGetParam);
    }

}
