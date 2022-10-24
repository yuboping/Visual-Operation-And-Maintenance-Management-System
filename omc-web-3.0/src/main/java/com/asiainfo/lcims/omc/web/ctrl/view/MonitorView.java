package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.monitor.MdChartDetail;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.monitor.PageChartService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 展示页面chartinfo
 * @author zhul
 *
 */
@Controller
public class MonitorView extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(MonitorView.class);

    @Resource(name = "pageChartService")
    PageChartService pageChartService;
    
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @RequestMapping(value = "/view/class/{classname}/module/{module}")
    public String deviceManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        List<MdMenu> mdMenuList = mdMenuDataListener.getChildrenMdMenuByUrl(uri);
        if (!ToolsUtils.ListIsNull(mdMenuList)){
            return "redirect:" + mdMenuList.get(0).getUrl();
        }else{
          //记录日志
            LOG.error("childrenMenu is null，: "+uri+" can not find childrenMenu list");
            return "redirect:/resources/unauthorized.html";
        }
    }
    
    /**
     * 总览统一页面
     * url样例: /view/class/server/device/node/summary
     * 展示图表统一数据页面
     * @return
     */
    @RequestMapping(value = "/view/class/{classname}/module/{module}/{summarytype}/summary")
    public String summaryMonitor(@PathVariable String summarytype,HttpServletRequest request, Model model){
        String uri = request.getRequestURI();
        MdMenu menu1 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 1);
        MdMenu menu2 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2);
        model.addAttribute("classtype", menu2.getName());
        String titlename1 = menu1.getShow_name();
        String titlename2 = menu2.getShow_name();
        // 查询告警信息表的所有数据
        List<MdAlarmInfo> infos = mdMenuDataListener.getAllAlarmInfo();
        /**
         * 根据uri获取当前页面的图表列表
         */
        String urlParam = "type=summary&summarytype="+summarytype;
        List<MdChartDetail> chartDetailList = CommonInit.getChartDetailsByPageUri(uri, urlParam,
                infos);
        if(ToolsUtils.ListIsNull(chartDetailList)){
            //记录日志
            LOG.error("chartDetailList is null，: "+uri+" can not find chartDetail config");
            return "redirect:/resources/unauthorized.html";
        }
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        model.addAttribute("titlename_1", titlename1);
        model.addAttribute("titlename_2", titlename2);
        model.addAttribute("titlename_3", null);
        model.addAttribute("chartDetailList", chartDetailList);
        return "monitor/radiusmon";
    }
    
    
    
    @RequestMapping(value = "/view/class/hncmradius/module/radius/host--/{id}")
    public String radiusMonitor(@PathVariable String id,
            HttpServletRequest request, Model model){
        String uri = request.getRequestURI();
        MdMenu menu1 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 1);
        MdMenu menu2 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2);
        model.addAttribute("classtype", menu2.getName());
        String titlename1 = menu1.getShow_name();
        String titlename2 = menu2.getShow_name();
        List<MdAlarmInfo> infos = mdMenuDataListener.getAllAlarmInfo();
        /**
         * 根据uri获取当前页面的图表列表
         */
        List<MdChartDetail> chartDetailList = new ArrayList<MdChartDetail>();
        model.addAttribute("host_id",id);
        model.addAttribute("chartDetailList", chartDetailList);
        model.addAttribute("titlename_1", titlename1);
        model.addAttribute("titlename_2", titlename2);
        model.addAttribute("titlename_3", null);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }

        return "hncmRadius/charts";
    }

    /**
     * 河南移动业务数据展示页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/view/class/hncmradius/module/{summarytype}/summary")
    public String radiusBusinessData(@PathVariable String summarytype,HttpServletRequest request, Model model){
        String uri = request.getRequestURI();
        MdMenu menu1 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 1);
        MdMenu menu2 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2);
        model.addAttribute("classtype", menu2.getName());
        String titlename1 = menu1.getShow_name();
        String titlename2 = menu2.getShow_name();
        /**
         * 根据uri获取当前页面的图表列表
         */
        List<MdChartDetail> chartDetailList = new ArrayList<MdChartDetail>();
        model.addAttribute("chartDetailList", chartDetailList);
        model.addAttribute("titlename_1", titlename1);
        model.addAttribute("titlename_2", titlename2);
        model.addAttribute("titlename_3", null);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "hncmRadius/businessdata";
    }


    /**
     * 节点、属地
     * url样例: 
     *     /view/class/server/device/node--/12323522
     *     /view/class/server/device/area--/12323524
     *  /view/class/server/device/host--/12323523
     * 展示图表统一数据页面
     * @return
     */
    @RequestMapping(value = "/view/class/{classname}/module/{module}/{type}--/{id}")
    public String radiusMonitor(@PathVariable String classname, @PathVariable String module,
            @PathVariable String type,@PathVariable String id,
            HttpServletRequest request, Model model){
        String uri = request.getRequestURI();
        model.addAttribute("lastClick", id);
        MdMenu menu1 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 1);
        MdMenu menu2 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2);
        model.addAttribute("classtype", menu2.getName());
        String titlename1 = menu1.getShow_name();
        String titlename2 = menu2.getShow_name();
        String titlename3 = getTitleName(type, id);
        List<MdAlarmInfo> infos = mdMenuDataListener.getAllAlarmInfo();
        /**
         * 根据uri获取当前页面的图表列表
         */
        String urlParam = "type=" + type + "&paramid="+id;
        List<MdChartDetail> chartDetailList = CommonInit.getChartDetailsByPageUri(uri, urlParam,
                infos);
        if(ToolsUtils.ListIsNull(chartDetailList)){
            //记录日志
            LOG.error("chartDetailList is null，: "+uri+" can not find chartDetail config");
            return "redirect:/resources/unauthorized.html";
        }
        model.addAttribute("chartDetailList", chartDetailList);
        model.addAttribute("titlename_1", titlename1);
        model.addAttribute("titlename_2", titlename2);
        model.addAttribute("titlename_3", titlename3);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "monitor/radiusmon";
    }
    
    /**
     * 节点/主机 ；地市/bas
     * url样例: 
     *     /view/class/server/device/node/host--/12323522/234
     *     /view/class/server/device/area/bras--/12323524/456
     * 展示图表统一数据页面
     * @return
     */
    @RequestMapping(value = "/view/class/{classname}/module/{module}/{parentType}/{childType}--/{parentId}/{childId}")
    public String radiusMonitor2(@PathVariable String classname, @PathVariable String module,
            @PathVariable String parentType,@PathVariable String childType,
            @PathVariable String parentId,@PathVariable String childId,
            HttpServletRequest request, Model model){
        String uri = request.getRequestURI();
        model.addAttribute("lastClick", parentId);
        MdMenu menu1 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 1);
        MdMenu menu2 = mdMenuDataListener.getMdMenuByLevel(
                mdMenuDataListener.getMdMenuByUrl(uri), 2);
        model.addAttribute("classtype", menu2.getName());
        String titlename1 = menu1.getShow_name();
        String titlename2 = menu2.getShow_name();
        String titlename3 = getTitleName(childType, childId);
        List<MdAlarmInfo> infos = mdMenuDataListener.getAllAlarmInfo();
        /**
         * 根据uri获取当前页面的图表列表
         */
        String urlParam = "type=" + childType+"&paramid="+childId + "&parentId="+parentId;
        List<MdChartDetail> chartDetailList = CommonInit.getChartDetailsByPageUri(uri, urlParam,
                infos);
        if(ToolsUtils.ListIsNull(chartDetailList)){
            //记录日志
            LOG.error("chartDetailList is null，: "+uri+" can not find chartDetail config");
            return "redirect:/resources/unauthorized.html";
        }
        model.addAttribute("chartDetailList", chartDetailList);
        model.addAttribute("titlename_1", titlename1);
        model.addAttribute("titlename_2", titlename2);
        model.addAttribute("titlename_3", titlename3);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "monitor/radiusmon";
    }

    private String getTitleName(String type , String id) {
        String titleName = null;
        switch (type) {
        case "host":
            MonHost host = CommonInit.getHost(id);
            if(null != host)
                titleName = host.getHostname()+" "+host.getAddr();
            break;
        case "node":
            MdNode node = CommonInit.getNodeById(id);
            if(null != node)
                titleName = node.getNode_name();
            break;
        case "area":
            MdArea area = CommonInit.getAreaByAreano(id);
            if(null != area)
                titleName = area.getName();
            break;
        case "bras":
            BdNas nas = CommonInit.getBdNasById(id);
            if(null != nas)
                titleName = nas.getNas_ip();
            break;
        default:
            break;
        }
        return titleName;
    }
    
}