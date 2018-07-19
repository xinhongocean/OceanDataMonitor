package net.xinhong.oceanmonitor.common;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @Autor rongxiaokun
 * @Date 2018/7/13  15:12
 */
public class DataCheckUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataCheckUtil.class);
    static DecimalFormat decimalFormat000 = new DecimalFormat("000");

    /**
     * 处理gfs的业务下载业务逻辑
     * @param fileNum       理论文件个数        (73+24)6h下载完成
     * @param fileNames     实际文件            list<2018051712/2018051712_gfs.t12z.pgrb2.0p25.f049>
     * @param type          数据类型            GFS
     * @param json          要写入的json
     * @param date          传入的日期:         2018071112
     */
    public static void GFSDownloadCheck(int fileNum , List<String> fileNames , String type ,JSONObject json ,String date){
        if (fileNum >73) {
            for (int i = 0; i < 73; i++) {
                write2json(fileNames , i ,type ,json ,date);
            }
            for (int i = 75; i < fileNum; i++) {
                write2json(fileNames , (75 + (i-73) * 3) , type ,json ,date);
            }
        }else {
            for (int i = 0; i < fileNum; i++) {
                write2json(fileNames , i , type ,json ,date);
            }
        }
    }
    //遍历获取的文件名列表,不存在即写入json, GFSDownloadCheck的补充方法
    private static void write2json(List<String> fileNames ,int i ,String type ,JSONObject json ,String date) {
        String fileName = null;
        if(type.contains("GFS_download") || type.equals("GFS")) {
            fileName = date + "_gfs.t12z.pgrb2.0p25.f" + decimalFormat000.format(i);
        }
        if(type.contains("GFS_parse")) {
            fileName = "XHGFS_G_DB_"+date + decimalFormat000.format(i) +".dat";
        }
        boolean flag = false;
        for (String name : fileNames) {
            if (name.equals(fileName)) {
                flag = true;
                break;
            }
        }
        if (flag == false)
            json.put("lossFile--" + fileName, 0);
    }
}
