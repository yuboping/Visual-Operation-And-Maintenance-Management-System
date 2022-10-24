package com.asiainfo.lcims.omc.web.ctrl.gscm5G;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileDiff;
import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileMonitor;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.gscm5G.CollectFileManageService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;

/**
 * 配置管理/采集文件监控管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/collectfilemanage")
public class CollectFileManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "collectFileManageService")
    CollectFileManageService collectFileManageService;

    @Autowired
    HostService hostService;

    /**
     * 采集文件监管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String metricManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        List<MonHost> hostList = hostService.getHost("", "", "15", "");
        model.addAttribute("hostList", hostList);
        return "gscm5G/collectfilemanage";
    }

    /**
     * 
     * @Title: queryMdMetricList @Description: TODO(查询采集文件监信息) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<MdMetric> 返回类型 @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdCollectFileMonitor> queryMdCollectFileMonitorList(
            @ModelAttribute MdCollectFileMonitor mdCollectFileMonitor, HttpServletRequest request,
            HttpSession session, Model model) {
        List<MdCollectFileMonitor> mdCollectFileMonitorList = collectFileManageService
                .getMdCollectFileMonitorList(mdCollectFileMonitor);
        return mdCollectFileMonitorList;
    }

    @RequestMapping(value = "/query/contrast")
    @ResponseBody
    public List<MdCollectFileDiff> queryMdCollectFileDiffList(
            @ModelAttribute MdCollectFileDiff mdCollectFileDiff, HttpServletRequest request,
            HttpSession session, Model model) {
        List<MdCollectFileDiff> mdCollectFileDiffList = collectFileManageService
                .getMdCollectFileDiffList(mdCollectFileDiff);
        return mdCollectFileDiffList;
    }

    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response,
            Model model) {
        exportFileTXT(request, response, Constant.COLLECT_FILE_PATH,
                Constant.COLLECT_FILE_NAME);
    }

}
