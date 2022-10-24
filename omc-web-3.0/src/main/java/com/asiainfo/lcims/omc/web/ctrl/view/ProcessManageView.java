package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.ProcessManageService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 配置管理/进程管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/processmanage")
public class ProcessManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "processManageService")
    ProcessManageService processManageService;

    /**
     * 进程管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String processManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/processmanage";
    }

    /**
     * 
     * @Title: queryMdProcessList
     * @Description: TODO(查询进程信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryMdProcessList(@ModelAttribute MdProcess mdProcess, HttpServletRequest request,
            HttpSession session, Model model) {
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        Page mdProcessPage = processManageService.getMdProcessPage(mdProcess, page);
        return mdProcessPage;
    }

    /**
     * 
     * @Title: addMdProcess
     * @Description: TODO(新增进程信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdProcess(@ModelAttribute MdProcess mdProcess, HttpServletRequest request,
            HttpSession session, Model model) {
        return processManageService.addMdProcess(mdProcess);
    }

    /**
     * 
     * @Title: modifyMdProcess
     * @Description: TODO(修改进程信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdProcess(@ModelAttribute MdProcess mdProcess,
            HttpServletRequest request, HttpSession session, Model model) {
        return processManageService.modifyMdProcess(mdProcess);
    }

    /**
     * 
     * @Title: deleteMdProcess
     * @Description: TODO(删除进程信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdProcess(HttpServletRequest request, HttpSession session, Model model) {
        String[] processidArray = request.getParameterValues("processidArray[]");
        return processManageService.deleteMdProcess(processidArray);
    }

}
