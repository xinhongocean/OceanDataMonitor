package net.xinhong.oceanmonitor.common;

/**
 * Description:城镇精细化预报要素 <br>
 * Company: <a href=www.xinhong.net>新宏高科</a><br>
 *
 * @author 作者 <a href=mailto:liusofttech@sina.com>刘晓昌</a>
 * @version 创建时间：2016/3/11.
 */
public enum ElemCityFC {
    /**
     * 时要素
     */
    TT("TT", "气温"),
    RH("RH", "相对湿度"),
    WD("WD", "风向"),
    WDF("WDF", "风向中文编码(16风向)"),
    WS("WS", "风速"),
    RN("RN", "降水量"),
    CN("CN", "总云量"),
    CNL("CNL", "低云量"),
    WW("WW", "天气现象"),
    VIS("VIS", "能见度"),
   // PR("PR", "海平面气压"),
    /**
     * 日要素
     * 最高，最大以MX开头
     * 最低，最小以MI开头
     */
    MXT("MXT", "最高气温"),
    MIT("MIT", "最低气温"),
    MXRH("MXRH", "最大相对湿度"),
    MIRH("MIRH", "最小相对湿度"),
    RN24("RN24", "24小时降水量"),
    RN12("RN12", "12小时降水量"),
    CN12("CN12", "12小时总云量"),
    CNL12("CNL12", "12小时低云量"),
    //// TODO: 2016/7/22  修改为 WXT
    WW12("WW12", "12小时天气现象"),
    WF12("WF12", "12小时天气现象编码"),
    WD12("WD12", "12小时风向"),
    WS12("WS12", "12小时风速");

    private String ename, cname;

    private ElemCityFC(String ename, String cname) {
        this.ename = ename;
        this.cname = cname;
    }

    public static ElemCityFC fromValue(String v) {
        for (ElemCityFC c: ElemCityFC.values()) {
            if (c.ename.equals(v)) {
                return c;
            }
        }
        return null;
    }

    public String getEname() {
        return this.ename;
    }

    public String getCname() {
        return this.cname;
    }


    public boolean isDay() {
        return this.equals(MXT) || this.equals(MIT) || this.equals(MXRH) || this.equals(MIRH)
                || this.equals(RN24) || this.equals(RN12) || this.equals(RN24) || this.equals(CN12)
                || this.equals(CN12) || this.equals(CNL12) || this.equals(WW12) || this.equals(WF12)
                || this.equals(WD12) || this.equals(WS12);
    }
}
