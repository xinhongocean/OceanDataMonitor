package net.xinhong.oceanmonitor.common.tools;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class NumModelTimeUtils {

    private static final Logger logger = Logger.getLogger(NumModelTimeUtils.class);
    private final static float GFS_DOWNLOAD_SPEED = 16.1f;      //gfs下载速度（6h全部,最低时间:10h）
    private final static DateTime DATE_TIME = new DateTime();   //规定统一的时间,防止查询过程中时间变化

    private NumModelTimeUtils() {
    }
    private static NumModelTimeUtils instance;
    public static NumModelTimeUtils getInstance() {
        if (instance == null) {
            synchronized (NumModelTimeUtils.class) {
                if (instance == null) {
                    instance = new NumModelTimeUtils();
                }
            }
        }
        return instance;
    }

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

    /**
     * 思路:按照6h下载97个文件,线性下载;
     * @param fileNum_real  实际文件个数
     * @param dateTime      要查看文件的时间
     * @return              [文件下载率,理论文件个数,文件小时数]
     */
    public static float[] getGFSDataRate(int fileNum_real , DateTime dateTime){
        float dataRate = 0;
        int fileNum = 0;
        int download_hour = 0;
        if (fileNum_real == 97 ) {                      //gfs数据下载个数(73+24)
            dataRate = 100;
            fileNum = 97;
        }else {
            String download_date = NumModelTimeUtils.getGFSDateTimeVTI(dateTime ,0);
            download_hour = Integer.parseInt(download_date.substring(download_date.length()-3));    //下载时间
            fileNum = (download_hour > 10)? 97 : (int)(download_hour * GFS_DOWNLOAD_SPEED) ;        //下载时间内的文件数
            dataRate =  (float) fileNum_real/ fileNum *100;
        }
        return new float[]{dataRate, fileNum ,(float)download_hour};
    }

    public static DateTime getDateTime() {
        return DATE_TIME;
    }
}
