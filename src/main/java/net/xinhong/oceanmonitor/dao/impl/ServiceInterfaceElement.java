package net.xinhong.oceanmonitor.dao.impl;

import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2018/6/26.
 */
public enum  ServiceInterfaceElement {

//**** 实况资料 ************************************************************************/
    SKSJ1("http://weather.xinhong.net/stationdata_surf/datafromcode?code=54511",
        "实况资料", "根据站号查询地面实况数据"),
    SKSJ2("http://weather.xinhong.net/stationdata_surf/datafromlatlng?lat=39.962927&lng=116.317251&hour=08",
            "实况资料", "根据经纬度查询地面实况数据"),
    SKSJ3("http://weather.xinhong.net/stationdata_surf/datafromcname?cname=上海",
            "实况资料", "根据站名查询地面实况数据"),
    SKSJ4("http://weather.xinhong.net/stationdata_surf/seqdatafromcode?code=54511&elem=PR",
            "实况资料", "根据站号查询地面实况序列数据"),
    SKSJ5("http://weather.xinhong.net/stationdata_surf/aqidatafromcode?code=54511",
            "实况资料", "根据站号查询空气质量"),
    SKSJ6("http://weather1.xinhong.net/stationdata_surf/aqidistriblist?dlevel=1",
            "实况资料", "空气质量分布图"),
    SKSJ7("http://weather1.xinhong.net/stationdata_surf/aqidatafromcityid?cityid=CN101010100",
            "实况资料", "按照行政区划查询AQI"),
    SKSJ8("http://weather.xinhong.net/stationdata_surf/isolinedata?year=2018&day=20&month=06&hour=20&level=9999&elem=PR",
            "实况资料", "地面实况等值线(要素是TT PR DP03)"),
    SKSJ9("http://weather.xinhong.net/stationdata_surf/isosurfacedata?year=2017&day=22&month=06&hour=08&level=9999&elem=RAIN24",
            "实况资料", "地面实况等值线(降水)"),
    SKSJ10("https://weather.xinhong.net/stationdata_surf/datafromselectedstations",
            "实况资料", "地面实况标注"),
    SKSJ11("https://weather.xinhong.net/stationdata_high/datafromselectedstations",
            "实况资料", "高空实况标注"),
    SKSJ12("http://weather.xinhong.net/stationdata_high/isolinedata?year=2017&day=20&month=06&hour=20&level=0500&elem=HH",
            "实况资料", "高空实况等值线(要素是TT HH TD)"),
    SKSJ13("http://weather.xinhong.net/stationdata_high/datafromcode?code=54511",
            "实况资料", "根据站号查询高空实况数据"),
    SKSJ14("http://weather.xinhong.net/stationdata_high/indexfromcode?code=54511",
            "实况资料", "根据站号查询强对流指数"),
    SKSJ15("http://weather.xinhong.net/stationdata_high/timeprofilefromcode?code=54511",
            "实况资料", "根据站号高空时序图"),

//**** 站点城市预报资料 ************************************************************************/
    ZDCSYBZL1("http://weather.xinhong.net/stationdata_cityfc/datafromlatlng?lat=32&lng=118&hour=20&elem=TT,RH,WW,RN,MXT",
        "站点城市预报资料", "根据经纬度查询最近站点预报数据"),
    ZDCSYBZL2("http://weather.xinhong.net/stationdata_cityfc/datafromcname?cname=上海&elem=TT,RH,WW,RN,MXT",
            "站点城市预报资料", "根据站名查询站点预报数据"),
    ZDCSYBZL3("http://weather.xinhong.net/stationdata_cityfc/datafromcode?code=54511&elem=TT,RH,WW,RN,MXT",
            "站点城市预报资料", "根据站号查询站点预报数据"),

//**** 站点信息 ************************************************************************/
    ZDXX1("http://weather.xinhong.net/station/infofrompynamecode?param=54",
        "站点信息", "根据站号模糊查询气象站点信息"),
    ZDXX2("http://weather.xinhong.net/station/infofrompynamecode?param=BJ",
            "站点信息", "根据拼音模糊查询气象站点信息"),
    ZDXX3("http://weather.xinhong.net/station/infofrompynamecode?param=朝阳",
            "站点信息", "根据中文名模糊查询气象站点信息"),
    ZDXX4("http://weather.xinhong.net/station/infofromlatlng?lat=39&lng=115",
            "站点信息", "根据中经纬度查询气象站点信息"),
    ZDXX5("http://weather.xinhong.net/station/nearesthighinfofromlatlng?lat=42&lng=114",
            "站点信息", "根据经纬度查询最近的高空站点信息"),
    ZDXX6("http://weather.xinhong.net/station/nearesthighinfofromcode?code=54399",
            "站点信息", "根据中站号查询最近的高空气象站点信息"),

//**** 航班及航线信息 ************************************************************************/
    HBJHXXX1("http://weather.xinhong.net/airline/flightfromumber?number=CA1315&year=2015&month=09&day=11",
        "航班及航线信息", "根据航班号查询航班信息"),
    HBJHXXX2("http://weather.xinhong.net/airline/flightfromdeptarr?dept=ZBAA&arr=ZJHK&year=2015&month=09&day=11",
            "航班及航线信息", "根据起落机场四字码查询航班信息"),
    HBJHXXX3("http://weather.xinhong.net/airline/airlinelistfromdeptarr?dept=ZBAA&arr=ZJHK",
            "航班及航线信息", "根据起落机场四字码查询航班信息"),
    HBJHXXX4("http://weather.xinhong.net/airline/airlinefromname?name=ZBAAZJHK001",
            "航班及航线信息", "根据航班号查询航班信息"),
    HBJHXXX5("http://weather1.xinhong.net/airport/waypointfromname?name=JAK",
            "航班及航线信息", "模糊查询航路点信息，最多只返回10条"),
    HBJHXXX6("http://weather1.xinhong.net/airport/waypointsfromrouteidenty?identy=A1",
            "航班及航线信息", "根据航线identy查询航路点"),

//**** 民航实况及预报报文查询 ************************************************************************/
    MHSKJYBBWCX1("http://weather1.xinhong.net/airportdata_surf/sigmentdataindexs",
        "民航实况及预报报文查询", "全球机场信息及机场天气现象"),
    MHSKJYBBWCX2("http://weather1.xinhong.net/airportdata_surf/sigmentdataindexslevel",
            "民航实况及预报报文查询", "全球机场信息及机场天气现象（根据地图缩放分级别显示）"),
    MHSKJYBBWCX3("http://weather1.xinhong.net/airportdata_surf/sigmentdatafromcode?year=2016&month=11&day=14&hour=09&minute=47&code=KLAX&sigmenttype=SOOT",
            "民航实况及预报报文查询", "根据返回的机场天气现象查询机场报文"),

//**** 台风及火山灰 ************************************************************************/
    TFJHSH1("http://weather.xinhong.net/typhdata?year=2017&month=07&day=04&hour=07&minute=24&daynum=90&isshowfinish=true",
        "台风及火山灰", "查询当前台风"),
    TFJHSH2("weather.xinhong.net/typhtime",
            "台风及火山灰", "查询台风最新的编报时间"),
    TFJHSH3("http://weather.xinhong.net/volcadata",
            "台风及火山灰", "查询当前火山灰"),

//**** 云图，雷达，日本传真，欧洲预报，pm2.5 ************************************************************************/
    YTLD1("http://weather.xinhong.net/cloudmap/info",
        "云图，雷达，日本传真，欧洲预报，pm2.5", "云图列表"),
    YTLD2("http://weather.xinhong.net/radarmap/info",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "雷达图列表"),
    YTLD3("http://weather.xinhong.net/wxfaxmap/info",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "传真图列表"),
    YTLD4("http://weather.xinhong.net/ecmwfmap/info",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "欧洲传真图"),
    YTLD5("http://weather1.xinhong.net//jppm2dot5fc/info",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "PM2.5趋势预报"),
    YTLD6("https://weather.xinhong.net/stationradarmap/info",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "固定的5个站查询"),
    YTLD7("https://weather.xinhong.net/stationradarmap/info?radarIDs=az9591,AZ9516",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "单站雷达指定站号查询，多个站号用英文逗号分隔"),
    YTLD8("https://weather.xinhong.net/stationradarmap/distribinfo",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "国内雷达地理位置信息列表"),
    YTLD9("http://weather1.xinhong.net/neareststationradarmap/info?lat=44&lng=115",
            "云图，雷达，日本传真，欧洲预报，pm2.5", "查询给定经纬度最近的五个雷达图像数据"),

//**** 气候产品 ************************************************************************/
    QHCP1("http://weather.xinhong.net/stationdata_stat/datafromcode?code=54511",
        "气候产品", "地面气候产品"),

//**** GFS ************************************************************************/
    GFS1("http://weather1.xinhong.net/gfs/pointdata?lat=35.002&lng=128.76",
        "GFS", "全球任意点预报数据"),
    GFS2("http://weather1.xinhong.net/gfs/pointsdata?lat=35.002,-20.5,37.2,40&lng=128.76,115,122.3,135",
            "GFS", "全球多点(最多100个)预报数据"),
    GFS3("http://weather1.xinhong.net/gfs/spaceprofiledata?lat=35.002,-20.5,37.2,40&lng=128.76,115,122.3,135",
            "GFS", "全球多点当前时次空间剖面图"),
    GFS4("http://weather1.xinhong.net/gfs/timeprofiledata?lat=35.002&lng=112",
            "GFS", "全球单点未来24时次空间剖面图"),
    GFS5("http://weather1.xinhong.net/gfs/isolinedata?level=0500&elem=HH",
            "GFS", "等高线(HH)等温线(TT)海平面气压（PR）等风速（WS）低空数据level=9999"),
    GFS6("http://weather1.xinhong.net/gfs/isosurfacedata?level=9999&elem=RN",
            "GFS", "降水填充"),
    GFS7("http://weather1.xinhong.net/gfs/isosurfacedata?level=0500&elem=RH",
            "GFS", "湿度填充"),
    GFS8("http://weather1.xinhong.net/gfs/isosurfacedata?level=0500&elem=TURB",
            "GFS", "颠簸填充"),
    GFS9("http://weather1.xinhong.net/gfs/isosurfacedata?level=0500&elem=ICE",
            "GFS", "积冰填充"),
    GFS10("http://weather1.xinhong.net/gfs/isosurfacedata?level=9999&elem=CTH",
            "GFS", "云顶高"),
    GFS11("http://weather1.xinhong.net/gfs/isosurfacedata?level=9999&elem=TS",
            "GFS", "雷暴"),
    GFS12("http://weather1.xinhong.net/gfs/isosurfacedata?level=9999&elem=WSI",
            "GFS", "大风"),
    GFS13("http://weather1.xinhong.net/gfs/isosurfacedata?level=9999&elem=VIS",
            "GFS", "能见度"),
    GFS14("http://weather1.xinhong.net/gfs/isosurfacedata?level=9999&elem=CBH",
            "GFS", "云底高"),
    GFS15("http://weather1.xinhong.net/gfs/areadata?level=0500&elem=WS",
            "GFS", "高空风速"),
    GFS16("http://weather1.xinhong.net/gfs/areadata?level=0500&elem=WD",
            "GFS", "高空风向"),

//**** 葵花8相关服务接口 ************************************************************************/
    KH1("http://weather1.xinhong.net/himawari8l2map/info?month=02&day=22&hour=04&channel=clth",
        "葵花8相关服务接口", "葵花8二级产品图像索引查询(给定时间最近3小时)"),
    KH2("http://weather1.xinhong.net/himawari8l1map/info?month=02&day=27&hour=04&channel=ir",
            "葵花8相关服务接口", "葵花8一级产品图像索引查询(给定时间最近3小时)"),
    KH3("http://weather1.xinhong.net/himawari8l2/pointspacedata?month=02&day=22&hour=11&minute=10&lat=27.046,26.26,22.149&lng=133.5,134.94,132.461",
            "葵花8相关服务接口", "葵花8二级产品多点同一时间数据查询（空间剖面图，每一个点一条记录）"),
    KH4("http://weather1.xinhong.net/himawari8l2/pointtimedata?month=02&day=22&hour=11&minute=17&lat=27.046&lng=133.52",
            "葵花8相关服务接口", "葵花8二级产品单点多时次数据查询（时间剖面图，给定时间最近3小时每10分钟一条记录）"),
    KH5("http://weather1.xinhong.net/himawari8l2/pointspacedata?month=03&day=16&hour=07&minute=17&lat=24.55,30.61,31.9,40&lng=106.744,109.80,112.3,115&interploate=0",
            "葵花8相关服务接口", "葵花8二级产品多点同一时间数据查询（空间剖面图，interploate设置为false或0时不插值，否则插值，不设置则插值。如插值则返回插值后多个点的数据，否则返回给定点的数据）"),

//**** 图例图片 ************************************************************************/
    TLTP1("http://weather1.xinhong.net/images/colorlegend/legend_himawari8l2_clot.png",
        "图例图片", "云厚度"),
    TLTP2("http://weather1.xinhong.net/images/colorlegend/legend_himawari8l2_cltt.png",
            "图例图片", "云顶温"),
    TLTP3("http://weather1.xinhong.net/images/colorlegend/legend_himawari8l2_clth.png",
            "图例图片", "云顶高"),
    TLTP4("http://weather1.xinhong.net/images/colorlegend/legend_himawari8l2_cltype.png",
            "图例图片", "云类型"),
    TLTP5("http://weather1.xinhong.net/images/colorlegend/legend_gfs_rh.png",
            "图例图片", "GFS相对湿度"),
    TLTP6("http://weather1.xinhong.net/images/colorlegend/legend_gfs_rn06.png",
            "图例图片", "GFS6小时降水"),
    TLTP7("http://weather1.xinhong.net/images/colorlegend/legend_jp_pm25.png",
            "图例图片", "PM2.5预报"),
    TLTP8("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_ctb.png",
            "图例图片", "解释应用云底高"),
    TLTP9("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_cth.png",
            "图例图片", "解释应用云顶高"),
    TLTP10("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_ice.png",
            "图例图片", "解释应用积冰"),
    TLTP11("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_ts.png",
            "图例图片", "解释雷暴概率"),
    TLTP12("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_turb.png",
            "图例图片", "解释应用颠簸"),
    TLTP13("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_vis.png",
            "图例图片", "解释应用能见度"),
    TLTP14("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_vws.png",
            "图例图片", "解释应用垂直风切变"),
    TLTP15("http://weather1.xinhong.net/images/colorlegend/legend_jsyb_wsi.png",
            "图例图片", "解释应用下击暴流"),
    TLTP16("http://weather1.xinhong.net/images/colorlegend/legend_radar.png",
            "图例图片", "雷达图（单站及拼图）"),
    TLTP17("http://weather1.xinhong.net/images/colorlegend/legend_real_rn06.png",
            "图例图片", "实况6小时累积降水"),
    TLTP18("http://weather1.xinhong.net/images/colorlegend/legend_real_rn24.png",
            "图例图片", "实况24小时累积降水"),

//**** 等值线颜色属性查询 ************************************************************************/
    DZXYSSXCX1("http://weather.xinhong.net/isolineattr/color?elem=WS&level=0700",
        "等值线颜色属性查询", "http://weather.xinhong.net/isolineattr/color?elem=WS&level=0700"),
    DZXYSSXCX2("http://weather.xinhong.net/isolineattr/color?elem=PR&level=9999",
            "等值线颜色属性查询", "http://weather.xinhong.net/isolineattr/color?elem=PR&level=9999"),
    DZXYSSXCX3("http://weather.xinhong.net/isolineattr/color?elem=TT&level=0850",
            "等值线颜色属性查询", "http://weather.xinhong.net/isolineattr/color?elem=TT&level=0850"),
    DZXYSSXCX4("http://weather.xinhong.net/isolineattr/color?elem=HH&level=0500",
            "等值线颜色属性查询", "http://weather.xinhong.net/isolineattr/color?elem=HH&level=0500"),
    DZXYSSXCX5("http://weather.xinhong.net/isolineattr/color?elem=HH&level=0700",
            "等值线颜色属性查询", "http://weather.xinhong.net/isolineattr/color?elem=HH&level=0700"),
    DZXYSSXCX6("http://weather.xinhong.net/isolineattr/color?elem=RH&level=9999",
            "等值线颜色属性查询", "http://weather.xinhong.net/isolineattr/color?elem=RH&level=9999"),

//**** 其他 ************************************************************************/
    QT1("http://weather1.xinhong.net/tools/sunrisesetfromlatlng?lat=43.78&lng=87.62",
            "其他" , "查询日出日落时间"),

//**** redis信息查询 ************************************************************************/
    REDIS1("http://weather.xinhong.net/redis/keys?type=city",
        "redis信息查询", "查询redid中目前的某类key及field数量"),
    REDIS2("http://weather.xinhong.net/redis/info",
            "redis信息查询", "查询redid节点信息"),
    REDIS3("http://weather.xinhong.net/redis/clusterinfo",
            "redis信息查询", "查询redid集群信息"),

//**** hy1suo HY1SUO************************************************************************/
    HY1SUO1("https://ocean.xinhong.net/hy1Suo/img/?depth=1&eles=TT" ,
            "HY1SUO" , "hy1suo图片(要素TT UU VV SAL SWH MWD MWP)"),
    HY1SUO2("https://ocean.xinhong.net/hy1Suo/pointdata/?lat=30&lng=130" ,
            "HY1SUO" , "hy1suo单点数据"),
    HY1SUO3("https://ocean.xinhong.net/hy1Suo/spaceprofiledata/?lat=30,38&lng=130,145" ,
            "HY1SUO" , "hy1suo 空间剖面"),
    HY1SUO4("https://ocean.xinhong.net/hy1Suo/timeprofiledata/?lat=30&lng=130" ,
            "HY1SUO" , "hy1suo时间剖面"),
    HY1SUO5("https://ocean.xinhong.net//hy1Suo/isolinedata/?elem=WH" ,
            "HY1SUO" , "hy1Suo等值线（elem：WH）"),
    HY1SUO6("https://ocean.xinhong.net/hy1Suo/areadata/" ,
            "HY1SUO" , "hy1Suo区域(elem：MWH,MWD,WS,WD)"),

//**** wavewatch3 ************************************************************************/
    WAVEWATCH1("https://ocean.xinhong.net/wavewatch/pointdata/?lat=30&lng=130" ,
            "WAVEWATCH" , "wavewatch3单点数据"),
    WAVEWATCH2("https://ocean.xinhong.net/wavewatch/spaceprofiledata/?lat=30,38&lng=130,145" ,
            "WAVEWATCH" , "wavewatch3 空间剖面"),
    WAVEWATCH3("https://ocean.xinhong.net/wavewatch/timeprofiledata/?lat=30&lng=130" ,
            "WAVEWATCH" , "wavewatch3时间剖面"),
    WAVEWATCH4("https://ocean.xinhong.net/wavewatch/img/?eles=Wind_direction_from_which_blowing_surface" ,
            "WAVEWATCH" , "wavewatch3图片"),
    WAVEWATCH5("https://ocean.xinhong.net//wavewatch/isolinedata/?elem=WH" ,
            "WAVEWATCH" , "wavewatch3等值线（elem：WH SH SWH）"),
    WAVEWATCH6("https://ocean.xinhong.net/wavewatch/areadata/" ,
            "WAVEWATCH" , "wavewatch3主浪向"),

    ;

    private String url;//连接
    private String type;
    private String cName;//中文描述

    /**
     * 私有构造,防止被外部调用
     * @param url
     */
    private ServiceInterfaceElement(String url, String type ,String cName) {
        this.url = url;
        this.type = type;
        this.cName = cName;

    }

    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     *
     * @return
     */
    public String geturl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getcName() {
        return cName;
    }

}
