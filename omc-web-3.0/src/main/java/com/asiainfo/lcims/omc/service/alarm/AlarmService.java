package com.asiainfo.lcims.omc.service.alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmHisInfo;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmStatisInfo;
import com.asiainfo.lcims.omc.model.monitor.ChartData;
import com.asiainfo.lcims.omc.model.monitor.ChartWithGraph;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.page.Page;

@Service(value = "alarmService")
public class AlarmService {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmService.class);

    @Inject
    private MdAlarmInfoDao mdAlarmInfoDao;

    @Inject
    MenuService menuService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;


    public List<MdAlarmInfo> getAlarmMessageList(String username) {
//        String urlList = getUrlListString();
        List urlList = getUrlList();

        List<MdAlarmInfo> result = mdAlarmInfoDao.getAlarmInfoListWithIndex(urlList);
        return result;
    }

    public List<MdAlarmHisInfo> getDetailAlarmHisInfo(String id) {
        List<MdAlarmHisInfo> alarmInfoList = mdAlarmInfoDao.getAlarmHisInfoById(id);
        return alarmInfoList;
    }

    public List<MdAlarmInfo> getDetailAlarmInfo(String alarmId) {
        List<MdAlarmInfo> alarmInfos = mdAlarmInfoDao.getAlarmInfoById(alarmId);
        return alarmInfos;
    }

    public Page getAlarmInfoList(MdAlarmInfo mdAlarmInfo,Map<String, Object> params, int pageNumber){

        Page page = new Page(0);

        try {
            page.setPageNumber(pageNumber);
            String wholeUrl = getWholeUrl(params);
            mdAlarmInfo.setUrl(wholeUrl);
//        String urlList = getUrlListString();
            List urlList = getUrlList();

            //查询总数
            int totalCount = mdAlarmInfoDao.getAlarmInfoCount(mdAlarmInfo, urlList);

            page.setTotalCount(totalCount);
            //查询分页数据
            if (totalCount > 0) {
                List<MdAlarmInfo> data = mdAlarmInfoDao.getAlarmInfoList(mdAlarmInfo, urlList, page);
                page.setPageList(data);
            }
        }catch (Exception e){
            LOG.info("get alarminfolist error ! because : " + e.getMessage(), e);
        }
        return page;
    }

    public Page getAlarmHisInfoList(MdAlarmInfo mdAlarmInfo,Map<String, Object> params, int pageNumber){
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        String wholeUrl = getWholeUrl(params);
        mdAlarmInfo.setUrl(wholeUrl);
//        String urlList = getUrlListString();
        List urlList = getUrlList();

        //查询总数
        int totalCount = mdAlarmInfoDao.getAlarmHisInfoCount(mdAlarmInfo, urlList);
        page.setTotalCount(totalCount);
        //查询分页数据
        if(totalCount>0) {
            List<MdAlarmHisInfo> data = mdAlarmInfoDao.getAlarmHisInfoList(mdAlarmInfo, urlList, page);
            page.setPageList(data);
        }
        return page;
    }

    public WebResult confirmAlarmInfo(String username, String[] alarmArray) {
        String logDesc = "";
        if (alarmArray != null && alarmArray.length != 0) {
            String clearflag = CommonInit.BUSCONF.getStringValue("alarm_confirm_clear_alarmnum","false");
            for (String alarmId : alarmArray) {
                MdAlarmInfo mdAlarmInfo = mdAlarmInfoDao.getAlarmInfoWithId(alarmId);
                if(!StringUtils.isEmpty(mdAlarmInfo.getDimension1_name())){
                    logDesc = logDesc + ",节点:" + mdAlarmInfo.getDimension1_name();
                }
                if(!StringUtils.isEmpty(mdAlarmInfo.getDimension2_name())){
                    logDesc = logDesc + ",主机:" + mdAlarmInfo.getDimension2_name();
                }
                logDesc = logDesc + ",告警信息:" + mdAlarmInfo.getAlarmmsg() + ",告警数量:" + mdAlarmInfo.getAlarm_num() +
                        ",告警规则:" + mdAlarmInfo.getAlarm_rule();
                mdAlarmInfoDao.confirmAlarmInfoById(username, alarmId, clearflag);
                mdAlarmInfoDao.confirmAlarmHisInfoById(username, alarmId, mdAlarmInfo.getFirst_time());
            }
        }
        //用户日志记录
        logDesc = logDesc.substring(1, logDesc.length());
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_ALARM_QUERY, "确认数据[" + logDesc + "]");
        return new WebResult(true, "确认成功!");
    }

    public List<MdAlarmHisInfo> getAlarmHisList(String alarm_id){
        return mdAlarmInfoDao.getAlarmHisList(alarm_id);
    }

    /**
     *
     * @Title: getWholeUrl
     * @Description: TODO(获取完整URL)
     * @param @param map
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public String getWholeUrl(Map<String, Object> map) {
        String url = "";
        String attr1 = "";
        String attr2 = "";
        if("undefined".equals(map.get("monitorTargetThreeType"))){
            map.put("monitorTargetThreeType",null);
        }
        if (map.get("monitorTargetThreeType") != null) {
            String monitorTargetThreeType = map.get("monitorTargetThreeType").toString();
            switch (monitorTargetThreeType) {
                case Constant.MONITORTARGET_SUMMARY:
                    url = map.get("monitorTargetThreeUrl").toString();
                    break;
                case Constant.MONITORTARGET_ALL:
                    url = map.get("monitorTargetThreeUrl").toString();
                    if (map.get("attr1") != null) {
                        attr1 = map.get("attr1").toString();
                        url = url + ConstantUtill.URL_SPLIT + "/" + attr1;
                    }
                    break;
                case Constant.MONITORTARGET_SINGLE:
                    url = map.get("monitorTargetThreeUrl").toString();
                    if (map.get("attr1") != null && map.get("attr2") != null) {
                        attr1 = map.get("attr1").toString();
                        attr2 = map.get("attr2").toString();
                        if (!attr1.equals("") && !attr2.equals("")) {
                            url = url + ConstantUtill.URL_SPLIT + "/" + attr1 + "/" + attr2;
                        }
                    }
                    break;
            }
        } else {
            if (map.get("monitorTargetOneType") != null) {
                String monitorTargetOneType = map.get("monitorTargetOneType").toString();
                switch (monitorTargetOneType) {
                    case Constant.MONITORTARGET_SUMMARY:
                        url = map.get("monitorTargetOneUrl").toString();
                        break;
                    case Constant.MONITORTARGET_ALL:
                        url = map.get("monitorTargetOneUrl").toString() + ConstantUtill.URL_SPLIT;
                        break;
                    case Constant.MONITORTARGET_SINGLE:
                        url = map.get("monitorTargetOneUrl").toString();
                        if (map.get("attr1") != null) {
                            attr1 = map.get("attr1").toString();
                            url = url + ConstantUtill.URL_SPLIT + "/" + attr1;
                        }
                        break;
                }
            } else {
                return url;
            }
        }
        return url;
    }

    public WebResult exportExcel(HttpServletResponse response,
                                 HttpServletRequest request, MdAlarmInfo mdAlarmInfo, Map<String, Object> params) {
        String wholeUrl = getWholeUrl(params);
        mdAlarmInfo.setUrl(wholeUrl);
//        String urlList = getUrlListString();
        List urlList = getUrlList();

        List<MdAlarmHisInfo> mdAlarmHisInfoList = mdAlarmInfoDao.getAlarmHisInfoList(mdAlarmInfo, urlList,null);
        String[][] datas = toArray(mdAlarmHisInfoList);
        String title = "ALARM_HIS_INFO_EXPORT";
        String currentDate = null;
        if("1".equals(mdAlarmInfo.getQuery_type())){
            currentDate = mdAlarmInfo.getStart_time();
        }else {
            currentDate = mdAlarmInfo.getEnd_time();
        }
        String[] fields = { "模块", "指标", "告警级别", "告警目标", "告警信息", "告警周期时间" };
        ExcelUtil.downloadExcelFile(title, currentDate, fields, request, response, datas);
        WebResult result = new WebResult(true, "导出成功！");
        return result;
    }

    public WebResult exportExcelWithLevel(HttpServletResponse response,
                                 HttpServletRequest request, MdAlarmInfo mdAlarmInfo, Map<String, Object> params) {
//        String urlList = getUrlListString();
        List urlList = getUrlList();
        List<MdAlarmHisInfo> mdAlarmHisInfoList = mdAlarmInfoDao.getAlarmHisInfoWithLevelIndex(mdAlarmInfo, urlList);
        String[][] datas = toArray(mdAlarmHisInfoList);
        String title = "ALARM_HIS_INFO_EXPORT";
        DateTools dateTools = new DateTools("yyyy-MM-dd");
        String currentDate = dateTools.getCurrentDate();
        String[] fields = { "模块", "指标", "告警级别", "告警目标", "告警信息", "告警周期时间" };
        ExcelUtil.downloadExcelFile(title, currentDate, fields, request, response, datas);
        WebResult result = new WebResult(true, "导出成功！");
        return result;
    }

    public String[][] toArray(List<MdAlarmHisInfo> mdAlarmHisInfoList) {
        String[][] datas = new String[mdAlarmHisInfoList.size()][6];
        for (int i = 0; i < mdAlarmHisInfoList.size(); i++) {
            MdAlarmHisInfo mdAlarmHisInfo = mdAlarmHisInfoList.get(i);
            datas[i][0] = mdAlarmHisInfo.getName();
            datas[i][1] = mdAlarmHisInfo.getMetric_name();
            datas[i][2] = mdAlarmHisInfo.getAlarm_level_name();
            datas[i][3] = mdAlarmHisInfo.getDimension_name();
            datas[i][4] = mdAlarmHisInfo.getAlarmmsg();
            datas[i][5] = mdAlarmHisInfo.getCycle_time();
        }
        return datas;
    }

    public List<MdAlarmStatisInfo> getAlarmStatisInfoList(MdAlarmInfo mdAlarmInfo){
        List<MdAlarmStatisInfo> data = null;
        if("0".equals(mdAlarmInfo.getQuery_type())){
            data = mdAlarmInfoDao.getAlarmStatisInfoListWithDay(mdAlarmInfo);
        }else if("1".equals(mdAlarmInfo.getQuery_type())){
            data = mdAlarmInfoDao.getAlarmStatisInfoListWithWeek(mdAlarmInfo);
        }else if("2".equals(mdAlarmInfo.getQuery_type())){
            data = mdAlarmInfoDao.getAlarmStatisInfoListWithMonth(mdAlarmInfo);
        }
        return data;
    }

    public WebResult exportStatisExcel(HttpServletResponse response,
                                 HttpServletRequest request, MdAlarmInfo mdAlarmInfo) {
        List<MdAlarmStatisInfo> mdAlarmStatisInfoList = getAlarmStatisInfoList(mdAlarmInfo);
        String[][] datas = toStatisArray(mdAlarmStatisInfoList);
        String title = "ALARM_STATIS_INFO_EXPORT";
        DateTools dateTools = new DateTools("yyyy-MM-dd");
        String currentDate = null;
        if("2".equals(mdAlarmInfo.getQuery_type())){
            currentDate = mdAlarmInfo.getStart_time();
        }else {
            currentDate = mdAlarmInfo.getEnd_time();
        }
        String[] fields = { "告警时间", "普通", "告警", "严重" };
        ExcelUtil.downloadExcelFile(title, currentDate, fields, request, response, datas);
        WebResult result = new WebResult(true, "导出成功！");
        return result;
    }

    public String[][] toStatisArray(List<MdAlarmStatisInfo> mdAlarmStatisInfoList) {
        String[][] datas = new String[mdAlarmStatisInfoList.size()][4];
        for (int i = 0; i < mdAlarmStatisInfoList.size(); i++) {
            MdAlarmStatisInfo mdAlarmStatisInfo = mdAlarmStatisInfoList.get(i);
            datas[i][0] = mdAlarmStatisInfo.getAlarm_time();
            datas[i][1] = mdAlarmStatisInfo.getAlarm_level_normal();
            datas[i][2] = mdAlarmStatisInfo.getAlarm_level_warn();
            datas[i][3] = mdAlarmStatisInfo.getAlarm_level_serious();
        }
        return datas;
    }

    public ChartWithGraph getAlarmInfoWithIndexGraph(MdAlarmInfo mdAlarmInfo){
//        String urlList = getUrlListString();
        List urlList = getUrlList();
        List<ChartData> data = mdAlarmInfoDao.getAlarmInfoWithIndexGraph(mdAlarmInfo, urlList);
        ChartWithGraph chartWithGraph = new ChartWithGraph();
        List<String> xlist = new ArrayList<>();
        List<Integer> ylist = new ArrayList<>();
        for(ChartData chartData : data){
            xlist.add(chartData.getMark());
            ylist.add(Integer.parseInt(chartData.getValue()));
        }
        chartWithGraph.setBarXdata(xlist);
        chartWithGraph.setBarYdata(ylist);
        return chartWithGraph;
    }

    public List<MdAlarmInfo> getAlarmListWithIndexConfirm(MdAlarmInfo mdAlarmInfo){
//        String urlList = getUrlListString();
        List urlList = getUrlList();
        List<MdAlarmInfo> alarmInfoList = mdAlarmInfoDao.getAlarmInfoListWithIndexConfirm(mdAlarmInfo, urlList);
        return alarmInfoList;
    }

    public String getUrlListString(){
        String urlList = "";

        //获取全部菜单
        List<MdMenu> mdMenuList = menuService.getAllMenu();
        //拼接菜单成字符串格式
        for(MdMenu mdMenu: mdMenuList){
            urlList = urlList +"'" + mdMenu.getUrl() + "',";
        }
        urlList = urlList.substring(0,urlList.length()-1);
        return urlList;
    }

    public List getUrlList(){
        List urlList = new ArrayList();
        //获取全部菜单
        List<MdMenu> mdMenuList = menuService.getAllMenu();
        //将url放入urlList
        for(MdMenu mdMenu: mdMenuList){
            urlList.add(mdMenu.getUrl());
        }
        return urlList;
    }
    
    
    /**
     * 
     * @param
     * @param alarmArray
     * @return
     */
    public WebResult deleteAlarmInfo(String[] alarmArray) {
        String logDesc = "";
        if (alarmArray != null && alarmArray.length != 0) {
            for (String alarmId : alarmArray) {
                MdAlarmInfo mdAlarmInfo = mdAlarmInfoDao.getAlarmListWithId(alarmId);
                if(!StringUtils.isEmpty(mdAlarmInfo.getDimension1_name())){
                    logDesc = logDesc + ",节点:" + mdAlarmInfo.getDimension1_name();
                }
                if(!StringUtils.isEmpty(mdAlarmInfo.getDimension2_name())){
                    logDesc = logDesc + ",主机:" + mdAlarmInfo.getDimension2_name();
                }
                logDesc = logDesc + ",告警信息:" + mdAlarmInfo.getAlarmmsg() + ",告警数量:" + mdAlarmInfo.getAlarm_num() +
                        ",告警规则:" + mdAlarmInfo.getAlarm_rule();
                mdAlarmInfoDao.deleteAlarmInfoById(alarmId);
            }
        }
        //用户日志记录
        logDesc = logDesc.substring(1, logDesc.length());
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_ALARM_QUERY, "删除数据[" + logDesc + "]");
        return new WebResult(true, "删除成功!");
    }

}
