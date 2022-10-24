package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.system.MdCollCycle;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostMetricService;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.configmanage.MetricManageService;
import com.asiainfo.lcims.omc.service.system.CollCycleService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.page.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 配置管理/主机指标配置管理
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/hostmetricmanage")
public class HostMetricManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "metricManageService")
    MetricManageService metricManageService;
    
    @Resource(name = "hostService")
    HostService hostService;
    
    @Resource(name = "hostMetricService")
    HostMetricService hostMetricService;
    
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
    public String metricManage(Model model, HttpSession session,HttpServletRequest request) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        String operatetype = request.getParameter("operatetype");
        String operateid = request.getParameter("operateid");
        if(ToolsUtils.StringIsNull(operateid)){
            operateid = "";
        }
        if(ToolsUtils.StringIsNull(operatetype)){
            operatetype = "";
        }
        model.addAttribute("operatetype", operatetype);
        model.addAttribute("operateid", operateid);
        //查询所有主机
        List<MonHost> hostlist = hostService.getAllHost();
        model.addAttribute("hostlist", hostlist);
        // 查询所有指标
        MdMetric metric = new MdMetric();
        List<MdMetric> metriclist = metricManageService.getMdMetricList(metric);
        model.addAttribute("metriclist", metriclist);

        // 绑定类型参数list
        List<MdParam> sourcetypelist = paramService.getMdParamList("9");
        model.addAttribute("sourcetypelist", sourcetypelist);
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

        return "hostmetricconfig/hostmetricmanage";
    }
    
    /*
     * 初始化查询条件
     * 获取主机列表
     * 获取指标列表
     */
    @RequestMapping(value = "/getSourceList")
    @ResponseBody
    public List<Object> getSourceList(HttpServletRequest request) {
        String operatetype = request.getParameter("operatetype");
        /**
         * 1: 查询指标
         * 2：查询主机
         */
        List<Object> list = new ArrayList<Object>();
        list.add(operatetype);
        if(operatetype.equals(ConstantUtill.operatetype_host)){
            list.add(hostService.getAllHost());
        }else{
            MdMetric metric = new MdMetric();
            list.add(metricManageService.getMdMetricList(metric));
        }
        return list;
    }
    
    /**
     * 查询主机指标配置信息
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
        Page hostPage = hostMetricService.getMdMetricList(hostid, metricid, pageNumber);
        return hostPage;
    }
    
    /**
     * 查询主机指标配置信息
     * @param request
     * @return hostMetriclist
     */
    @RequestMapping(value = "/query/singleinfo")
    @ResponseBody
    public MdHostMetric querySingleinfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        MdHostMetric hostMetric = hostMetricService.querySingleinfo(id);
        return hostMetric;
    }
    
    /**
     * 查询主机指标配置信息状态
     * @param request
     * @return hostMetriclist
     */
    @RequestMapping(value = "/query/singleinfoState")
    @ResponseBody
    public WebResult singleinfoState(HttpServletRequest request) {
        String id = request.getParameter("id");
        MdHostMetric hostMetric = hostMetricService.querySingleinfo(id);
        WebResult ruslt = new WebResult(true, "成功");
        if(hostMetric==null){
            ruslt.setOpSucc(false);
            ruslt.setMessage("数据不存在");
        }else if(hostMetric.getState().intValue()==ConstantUtill.HOST_METRIC_STATE_DELETE.intValue()){
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
         * 1: 查询指标
         * 2：查询主机
         */
        List<Object> list = new ArrayList<Object>();
        //查询 脚本返回数据类型
        List<MdParam> returnTypelist = paramService.getMdParamList(ConstantUtill.SCRIPT_RETURN_TYPE);
        list.add(returnTypelist);
        //查询采集周期列表
        List<MdCollCycle> collcyclelist = collCycleService.getMdCollCycleList();
        list.add(collcyclelist);
        if(operatetype.equals(ConstantUtill.operatetype_metric)){
            //以主机为主表查询
            List<MdHostMetric> list2 = hostMetricService.getMdMetricListByMetric(operateid);
            list.add(list2);
        }else{
            //以指标为主表查询
            List<MdHostMetric> list3 = hostMetricService.getMdMetricListByHost(operateid);
            list.add(list3);
        }
        return list;
    }
    
    
    /**
     * 绑定操作
     * @param request
     * @return
     */
    @RequestMapping(value = "/getbindOperate", method = RequestMethod.POST)
    @ResponseBody
    public WebResult bindOperate(HttpServletRequest request) {
        String datas = request.getParameter("jsonData");
        WebResult result = hostMetricService.bindOperate(datas);
        return result;
    }
    
    /**
     * 解绑操作
     * @param request
     * @return
     */
    @RequestMapping(value = "/unbindHostMetric")
    @ResponseBody
    public WebResult unbindHostMetric(HttpServletRequest request) {
        String hostMetricids = request.getParameter("hostMetricids");
        WebResult result = hostMetricService.updateHostMetricState(hostMetricids,ConstantUtill.HOST_METRIC_STATE_DELETE);
        return result;
    }
    
    /**
     * 下发操作
     * @param request
     * @return
     */
    @RequestMapping(value = "/publishHostMetric")
    @ResponseBody
    public WebResult publishHostMetric(HttpServletRequest request) {
        String hostMetricids = request.getParameter("hostMetricids");
        WebResult result = hostMetricService.updateHostMetricState(hostMetricids,ConstantUtill.HOST_METRIC_STATE_UNPUBLISH);
        return result;
    }
    
    /**
     * 删除操作
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteHostMetric")
    @ResponseBody
    public WebResult deleteHostMetric(HttpServletRequest request) {
        String hostMetricids = request.getParameter("hostMetricids");
        WebResult result = hostMetricService.deleteHostMetric(hostMetricids);
        return result;
    }
    
    /**
     * 修改操作
     * @param request
     * @return
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyHostMetric(HttpServletRequest request) {
        String id = request.getParameter("id");
        String script = request.getParameter("script");
        String script_param =  request.getParameter("script_param");
        Integer cycle_id = Integer.parseInt(request.getParameter("cycle_id"));
        Integer script_return_type = Integer.parseInt(request.getParameter("script_return_type"));
        MdHostMetric hostMetric = new MdHostMetric();
        hostMetric.setId(id);
        hostMetric.setCycle_id(cycle_id);
        hostMetric.setScript(script);
        hostMetric.setScript_param(script_param);
        hostMetric.setScript_return_type(script_return_type);
        hostMetric.setState(ConstantUtill.HOST_METRIC_STATE_UNPUBLISH);
        WebResult result = hostMetricService.modifyHostMetric(hostMetric);
        return result;
    }
}
