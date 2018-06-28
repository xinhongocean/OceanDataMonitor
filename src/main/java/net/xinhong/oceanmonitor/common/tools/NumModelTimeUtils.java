package net.xinhong.oceanmonitor.common.tools;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class NumModelTimeUtils {

    private static final Logger logger = Logger.getLogger(NumModelTimeUtils.class);


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
