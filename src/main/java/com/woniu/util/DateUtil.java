package com.woniu.util;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//该类为date类型的转换类   用于计算时间等
@Data
public class DateUtil {

    //时间对象转字符串
    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }

    //字符串转时间
    public static Date stringToDate(String date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date transDate;
        try{
            transDate = simpleDateFormat.parse(date);
        }catch (ParseException e){
            return null;
        }
        return transDate;
    }

    //用于结算两个时间的时间  按小时向上取整
    public static Integer maxTime(String beginTime,String endTime){
        //将开始时间转化为date对象
        Date beginT = stringToDate(beginTime);
        //获取开始时间的时间戳
        long timeB = beginT.getTime();
        //获取结束时间的date对象
        Date endT = stringToDate(endTime);
        //获取结束时间的时间戳
        long timeE = endT.getTime();
       return getMaxHour(timeB,timeE);
    }

    //方法重载  通过时间对象获取差值
    public static Integer maxTime(Date beginTime,Date endTime){
        long beginT=beginTime.getTime();
        long endT=endTime.getTime();
        return getMaxHour(beginT, endT);
    }

    //私有的计算时间差方法  需要两个long值  分别为开始时间  结束时间
    private static int getMaxHour(Long begin,Long end){
        int l = (int)(end - begin);
        //计算时间差  不足1小时按1小时计算
        if(l%(1000*60*60)!=0){
            //模60有余数 证明小时除不尽
            return l/(1000*60*60)+1;
        }
        return l/(1000*60*60);
    }
}
