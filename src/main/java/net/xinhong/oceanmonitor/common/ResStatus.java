package net.xinhong.oceanmonitor.common;

/**
 * Description: 返回到前段JSON中状态信息描述<br>
 * Company: <a href=www.xinhong.net>新宏高科</a><br>
 *
 * @author 作者 <a href=mailto:liusofttech@sina.com>刘晓昌</a>
 * @version 创建时间：2016/3/11.
 */
public enum ResStatus {
    /**
     * 查询正常处理，有查询结果
     */
    SUCCESSFUL(0, "查询成功"),
    /**
     * 查询正常处理，无查询结果
     */
    NORESULT(1, "正常处理，无结果"),
    /**
     * 传入参数错误
     */
    PARAM_ERROR(301, "请求参数不对"),
    /**
     * 查询时发生异常，无查询结果
     */
    SEARCH_ERROR(302, "查询时发生异常"),

    /**
     * 等值线追踪失败
     */
    ISO_ERROR(303,"等值线追踪异常"),

    /**
     * 传入参数错误
     */
    KEY_ERROR(-1, "指定的Key不正确"),
    /**
     * 传入参数错误
     */
    KEY_INVALID(-2, "指定的Key已失效"),


    /**
     * 已注册用户,且版本正常
     */
    VERIFIED_VALIDVERSION(1000, "已注册用户,且版本最新"),

    /**
     * 已注册用户,版本不是最新,提示更新
     */
    VERIFIED_OLDVALIDVERSION(1001, "已注册用户,版本不是最新,提示更新"),

    /**
     * 已注册用户,版本与后台服务不兼容,需强制更新
     */
    VERIFIED_INVALIDVERSION(1002, "已注册用户,版本与后台服务不兼容,需强制更新"),

    /**
     * 未注册用户,版本最新
     */
    UNVERIFIED_VALIDVERSION(1100, "未注册用户,版本为最新"),
    /**
     * 未注册用户,版本不是最新,提示更新
     */
    UNVERIFIED_OLDVALIDVERSION(1101, "未注册用户,版本不是最新,提示更新"),

    /**
     * 未注册用户,版本与后台服务不兼容,需强制更新
     */
    UNVERIFIED_INVALIDVERSION(1102, "未注册用户,版本与后台服务不兼容,需强制更新");


    private int statusCode;
    private String message;
    private ResStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
