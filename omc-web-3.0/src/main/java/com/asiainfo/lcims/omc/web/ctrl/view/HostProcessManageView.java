package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdHostProcess;
import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.model.configmanage.ProcessOperate;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostProcessManageService;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.configmanage.ProcessManageService;
import com.asiainfo.lcims.omc.service.configmanage.ProcessOperateService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.asiainfo.lcims.omc.util.page.Page;

@Controller
@RequestMapping(value = "/view/class/system/hostprocessmanage")
public class HostProcessManageView extends BaseController{

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "hostProcessManageService")
    HostProcessManageService hostProcessManageService;

    @Resource(name = "hostService")
    HostService hostService;

    @Resource(name = "paramService")
    ParamService paramService;

    @Resource(name = "processManageService")
    ProcessManageService processManageService;

    @Resource(name = "processOperateService")
    ProcessOperateService processOperateService;


    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @RequestMapping(value = "")
    public String HostProcessManage(HttpServletRequest request, Model model, HttpSession session) {
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
        //??????????????????
        List<MonHost> hostlist = hostService.getAllHost();
        model.addAttribute("hostlist", hostlist);

        //????????????????????????
        List<MdProcess> processList = processManageService.getAllMdProcess();
        model.addAttribute("processlist", processList);

        //??????
        List<MdParam> sourcetypelist = paramService.getMdParamList(ConstantUtill.HOST_PROCESS_TYPE);
        model.addAttribute("sourcetypelist", sourcetypelist);

        // ??????????????????
        List<MdParam> statetypelist = paramService.getMdParamList(ConstantUtill.PROCESS_STATE);
        model.addAttribute("statetypelist", statetypelist);

        //????????????
        List<MdParam> scriptTypelist = paramService.getMdParamList(ConstantUtill.HOST_PROCESS_SCRIPT_TYPE);
        model.addAttribute("scriptTypelist", scriptTypelist);

        return "hostprocessmanage/hostprocessmanage";
    }

    /**
     * ??????????????????????????????
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryMdHostProcessList(HttpServletRequest request,
                                       HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        MdHostProcess hostProcessQueryParam = BeanToMapUtils.toBean(MdHostProcess.class, params);
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page mdHostProcessList = hostProcessManageService.getMdHostProcessList(hostProcessQueryParam, pageNumber);
        return mdHostProcessList;
    }

    /**
     * ????????????????????????????????????
     * @param request
     * @return hostMetriclist
     */
    @RequestMapping(value = "/query/singleHostProcessInfo")
    @ResponseBody
    public WebResult singleinfoState(HttpServletRequest request) {
        String id = request.getParameter("id");
        MdHostProcess mdHostProcess = hostProcessManageService.getMdHostProcessInfo(id);
        WebResult ruslt = new WebResult(true, "??????", mdHostProcess);
        if(mdHostProcess==null){
            ruslt.setOpSucc(false);
            ruslt.setMessage("???????????????");
        }
        return ruslt;
    }

    /**
     * ??????????????????????????????
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdHostProcessList(HttpServletRequest request,
                                                      HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        String[] hostProcessArray = request.getParameterValues("hostProcessArray[]");
        WebResult result = hostProcessManageService.deleteInfo(hostProcessArray);
        return result;
    }

    /**
     * ????????????
     * @param request
     * @return
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyHostMetric(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        MdHostProcess hostProcess = BeanToMapUtils.toBean(MdHostProcess.class, params);
        WebResult result = hostProcessManageService.modifyHostProcess(hostProcess);
        return result;
    }

    /**
     * ????????????
     * @param request
     * @return
     */
    @RequestMapping(value = "/execute")
    @ResponseBody
    public WebResult excuteOperate(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        String[] hostProcessArray = request.getParameterValues("hostProcessArray[]");
        String scriptType = request.getParameter("script_type");
        WebResult result = hostProcessManageService.excuteShellToClient(hostProcessArray, scriptType);
        return result;
    }

    /*
     * ?????????????????????
     * ??????????????????
     * ??????????????????
     */
    @RequestMapping(value = "/getSourceList")
    @ResponseBody
    public List<Object> getSourceList(HttpServletRequest request) {
        String operatetype = request.getParameter("operatetype");
        /**
         * 1: ????????????
         * 2???????????????
         */
        List<Object> list = new ArrayList<Object>();
        list.add(operatetype);
        if(operatetype.equals(ConstantUtill.PARAM_HOST)){
            list.add(hostService.getAllHost());
        }else{
            list.add(processManageService.getAllMdProcess());
        }
        return list;
    }

    @RequestMapping(value = "/getHostProcessConfigInfo")
    @ResponseBody
    public List<Object> getHostMetricConfigInfo(HttpServletRequest request) {
        String operatetype = request.getParameter("operatetype");
        String operateid = request.getParameter("operateid");
        /**
         * 1: ????????????
         * 2???????????????
         */
        List<Object> list = new ArrayList<Object>();
        //?????? ????????????????????????
        List<MdParam> returnTypelist = paramService.getMdParamList(ConstantUtill.HOST_PROCESS_SCRIPT_TYPE);
        list.add(returnTypelist);
        if(operatetype.equals(ConstantUtill.PARAM_HOST)){
            //????????????????????????
            List<MdHostProcess> list2 = hostProcessManageService.getMdProcessListByHost(operateid);
            list.add(list2);
        }else{
            //????????????????????????
            List<MdHostProcess> list3 = hostProcessManageService.getMdProcessListByProcess(operateid);
            list.add(list3);
        }
        return list;
    }

    /**
     * ????????????
     * @param request
     * @return
     */
    @RequestMapping(value = "/getbindOperate")
    @ResponseBody
    public WebResult bindOperate(HttpServletRequest request) {
        String datas = request.getParameter("jsonData");
        WebResult result = hostProcessManageService.bindOperate(datas);
        return result;
    }

    /**
     * ??????????????????????????????
     */
    @RequestMapping(value = "/query/processoperateinfo")
    @ResponseBody
    public ProcessOperate queryProcessOperateInfo(HttpServletRequest request,
                                                HttpSession session, Model model) {
        String id = request.getParameter("id");
        ProcessOperate processOperate = processOperateService.getProcessOperateInfo(id);
        return processOperate;
    }

}
