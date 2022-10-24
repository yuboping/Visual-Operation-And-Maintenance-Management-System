package com.asiainfo.lcims.omc.web.ctrl.gscm5G;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.configmanage.MetricManageService;
import com.asiainfo.lcims.omc.service.gscm5G.CollectMetricService;
import com.asiainfo.lcims.omc.service.system.CollCycleService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 配置管理/采集机指标配置管理
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/collectmetricmanage")
public class CollectMetricManageView extends BaseController {

    private static final String HOSTTYPE = "15";

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "metricManageService")
    MetricManageService metricManageService;

    @Resource(name = "hostService")
    HostService hostService;

    @Resource(name = "collectMetricService")
    CollectMetricService collectMetricService;

    @Resource(name = "paramService")
    ParamService paramService;

    @Resource(name = "collCycleService")
    CollCycleService collCycleService;

    /**
     * 主机指标配置管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String metricManage(Model model, HttpSession session, HttpServletRequest request) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        String operatetype = request.getParameter("operatetype");
        String operateid = request.getParameter("operateid");
        if (ToolsUtils.StringIsNull(operateid)) {
            operateid = "";
        }
        if (ToolsUtils.StringIsNull(operatetype)) {
            operatetype = "";
        }
        model.addAttribute("operatetype", operatetype);
        model.addAttribute("operateid", operateid);
        // 查询采集机主机
        List<MonHost> hostlist = hostService.getHost("", "", HOSTTYPE, "");
        model.addAttribute("hostlist", hostlist);
        // 查询所有指标
        List<MdMetric> metriclist = new ArrayList<MdMetric>();
        MdMetric firewallMetric = new MdMetric();
        firewallMetric.setId(ConstantUtill.FIREWALL);
        firewallMetric.setMetric_name(ConstantUtill.FIREWALL_NAME);
        metriclist.add(firewallMetric);
        MdMetric thirdpartyMetric = new MdMetric();
        thirdpartyMetric.setId(ConstantUtill.THIRDPARTY);
        thirdpartyMetric.setMetric_name(ConstantUtill.THIRDPARTY_NAME);
        metriclist.add(thirdpartyMetric);
        MdMetric protocolMetric = new MdMetric();
        protocolMetric.setId(ConstantUtill.PROTOCOL);
        protocolMetric.setMetric_name(ConstantUtill.PROTOCOL_NAME);
        metriclist.add(protocolMetric);
        model.addAttribute("metriclist", metriclist);

        // 状态
        List<MdParam> statelist = paramService.getMdParamList(ConstantUtill.HOST_METRIC_STATE);
        model.addAttribute("statelist", statelist);
        // 脚本返回类型
        List<MdParam> scriptReturnTypelist = paramService
                .getMdParamList(ConstantUtill.SCRIPT_RETURN_TYPE);
        model.addAttribute("scriptReturnTypelist", scriptReturnTypelist);

        // 查询采集周期列表
        List<MdCollCycle> collcyclelist = collCycleService.getMdCollCycleList();
        model.addAttribute("collcyclelist", collcyclelist);

        return "gscm5G/collectmetricmanage";
    }

    /*
     * 初始化查询条件 获取主机列表 获取指标列表
     */
    @RequestMapping(value = "/getSourceList")
    @ResponseBody
    public List<Object> getSourceList(HttpServletRequest request) {
        String operatetype = request.getParameter("operatetype");
        /**
         * 1: 查询指标 2：查询主机
         */
        List<Object> list = new ArrayList<Object>();
        list.add(operatetype);
        if (operatetype.equals(ConstantUtill.operatetype_host)) {
            list.add(hostService.getAllHost());
        } else {
            MdMetric metric = new MdMetric();
            list.add(metricManageService.getMdMetricList(metric));
        }
        return list;
    }

    /**
     * 查询主机指标配置信息
     * 
     * @param request
     * @return hostMetriclist
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryInfo(HttpServletRequest request) {
        String hostid = request.getParameter("hostid");
        String metricid = request.getParameter("metricid");
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page hostPage = collectMetricService.getMdMetricList(hostid, metricid, pageNumber);
        return hostPage;
    }

    /**
     * 查询主机指标配置信息
     * 
     * @param request
     * @return hostMetriclist
     */
    @RequestMapping(value = "/query/singleinfo")
    @ResponseBody
    public MdHostMetric querySingleinfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        String metricid = request.getParameter("metricid");
        MdHostMetric hostMetric = collectMetricService.querySingleinfo(id, metricid);
        return hostMetric;
    }

    /**
     * 查询主机指标配置信息状态
     * 
     * @param request
     * @return hostMetriclist
     */
    @RequestMapping(value = "/query/singleinfoState")
    @ResponseBody
    public WebResult singleinfoState(HttpServletRequest request) {
        String id = request.getParameter("id");
        String metricid = request.getParameter("metricid");
        MdHostMetric hostMetric = collectMetricService.querySingleinfo(id, metricid);
        WebResult ruslt = new WebResult(true, "成功");
        if (hostMetric == null) {
            ruslt.setOpSucc(false);
            ruslt.setMessage("数据不存在");
        } else if (hostMetric.getState().intValue() == ConstantUtill.HOST_METRIC_STATE_DELETE
                .intValue()) {
            ruslt.setOpSucc(false);
            ruslt.setMessage("不能修改待删除状态数据");
        }
        return ruslt;
    }

    @RequestMapping(value = "/getHostMetricConfigInfo")
    @ResponseBody
    public List<Object> getHostMetricConfigInfo(HttpServletRequest request) {
        String operatetype = request.getParameter("operatetype");
        String operateid = request.getParameter("operateid");
        /**
         * 1: 查询指标 2：查询主机
         */
        List<Object> list = new ArrayList<Object>();
        // 查询 脚本返回数据类型
        List<MdParam> returnTypelist = paramService
                .getMdParamList(ConstantUtill.SCRIPT_RETURN_TYPE);
        list.add(returnTypelist);
        // 查询采集周期列表
        List<MdCollCycle> collcyclelist = collCycleService.getMdCollCycleList();
        list.add(collcyclelist);

        // 以指标为主表查询
        List<MdHostMetric> list3 = collectMetricService.getMdMetricListByHost(operateid,
                operatetype);
        list.add(list3);

        return list;
    }

    /**
     * 绑定操作
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/getbindOperate", method = RequestMethod.POST)
    @ResponseBody
    public WebResult bindOperate(HttpServletRequest request) {
        String datas = request.getParameter("jsonData");
        String operatetype = request.getParameter("operatetype");
        WebResult result = collectMetricService.bindOperate(datas, operatetype);
        return result;
    }

    /**
     * 解绑操作
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/unbindHostMetric")
    @ResponseBody
    public WebResult unbindHostMetric(HttpServletRequest request) {
        String hostMetricids = request.getParameter("hostMetricids");
        String metricid = request.getParameter("metricid");
        WebResult result = collectMetricService.updateHostMetricState(hostMetricids,
                ConstantUtill.HOST_METRIC_STATE_DELETE, metricid);
        return result;
    }

    /**
     * 下发操作
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/publishHostMetric")
    @ResponseBody
    public WebResult publishHostMetric(HttpServletRequest request) {
        String hostMetricids = request.getParameter("hostMetricids");
        String metricid = request.getParameter("metricid");
        WebResult result = collectMetricService.updateHostMetricState(hostMetricids,
                ConstantUtill.HOST_METRIC_STATE_UNPUBLISH, metricid);
        return result;
    }

    /**
     * 删除操作
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteHostMetric")
    @ResponseBody
    public WebResult deleteHostMetric(HttpServletRequest request) {
        String hostMetricids = request.getParameter("hostMetricids");
        String metricid = request.getParameter("metricid");
        WebResult result = collectMetricService.deleteHostMetric(hostMetricids, metricid);
        return result;
    }

    /**
     * 修改操作
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyHostMetric(HttpServletRequest request) {
        String id = request.getParameter("id");
        String script = request.getParameter("script");
        String script_param = request.getParameter("script_param");
        Integer cycle_id = Integer.parseInt(request.getParameter("cycle_id"));
        Integer script_return_type = Integer.parseInt(request.getParameter("script_return_type"));
        String metricid = request.getParameter("metricid");
        MdHostMetric hostMetric = new MdHostMetric();
        hostMetric.setId(id);
        hostMetric.setCycle_id(cycle_id);
        hostMetric.setScript(script);
        hostMetric.setScript_param(script_param);
        hostMetric.setScript_return_type(script_return_type);
        hostMetric.setState(ConstantUtill.HOST_METRIC_STATE_UNPUBLISH);
        WebResult result = collectMetricService.modifyHostMetric(hostMetric, metricid);
        return result;
    }
}
