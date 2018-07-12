package net.xinhong.oceanmonitor.common.tools;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class NumModelTimeUtils {

    private static final Logger logger = Logger.getLogger(NumModelTimeUtils.class);

    public static void main(String[] args) {

    }
    //todo add other type
    public static String getDateTimeVTI(DateTime inputDate, int minusHour, String type) {
        if (type.toLowerCase().equals("gfs.down")) {
            return getGFSDateTimeVTI(inputDate, minusHour);
        } else {
            logger.error("no this type,please check clz NumModelTimeUtils");
        }
        return null;
    }

    //todo add other type
    public static String getDateTime(DateTime inputDate, int minusHour, String type) {
        if (type.toLowerCase().equals("gfs")) {
            return getGFSDateTime(inputDate, minusHour);
        } else {
            logger.error("no this type,please check clz NumModelTimeUtils");
        }
        return null;
    }


    /**
     * 功能:前推10h,获取00和12世界时间
     * @param inputDate  new DateTime(),传入要查看的中国时间
     * @param minusHour  表示用更早的时间表示当前时间(允许延迟的时间,得到的结果再往前延minusHour)
     *                   eg:传入2018071111,0
     *                      输出2018071012015
     *                   eg:传入2018071111,10
     *                      输出2018071000027
     * @return
     */
    public static String getGFSDateTimeVTI(DateTime inputDate, int minusHour) {
        DateTime useDate = new DateTime(inputDate.getMillis());
        DateTimeFormatter dateformat = DateTimeFormat.forPattern("yyyyMMddHH");
        //转换为世界时
        useDate = useDate.minusHours(8);
        DateTime delayDate = useDate.minusHours(10);
        delayDate = delayDate.minusHours(minusHour);
        int shour = (delayDate.getHourOfDay() / 12) * 12;
        String ftimeStr = delayDate.toString(DateTimeFormat.forPattern("yyyyMMdd"))
                + String.format("%02d", shour);
        long disHour = (useDate.getMillis() - DateTime.parse(ftimeStr, dateformat).getMillis()) / 3600000l;
        String timeVTI = ftimeStr + String.format("%03d", disHour);
        return timeVTI;
    }

    /**
     * 功能:前推10h,获取00和12世界时间
     * @param inputDate  new DateTime(),传入要查看的中国时间
     * @param minusHour  允许延迟的时间,得到的结果再往前延minusHour
     *                   eg:传入2018071111
     *                      输出2018071012
     * @return
     */
    public static String getGFSDateTime(DateTime inputDate, int minusHour) {
        DateTime useDate = new DateTime(inputDate.getMillis());
        DateTimeFormatter dateformat = DateTimeFormat.forPattern("yyyyMMddHH");
        //转换为世界时
        useDate = useDate.minusHours(8);
        DateTime delayDate = useDate.minusHours(10);
        delayDate = delayDate.minusHours(minusHour);
        int shour = (delayDate.getHourOfDay() / 12) * 12;
        String ftimeStr = delayDate.toString(DateTimeFormat.forPattern("yyyyMMdd"))
                + String.format("%02d", shour);
        return ftimeStr;
    }

}
