package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.autodeploy.AnsibleLog;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.autodeploy.AutoDeployService;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;

/**
 * 运维工具/自动部署
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/mainttool/autodeploy")
public class AutoDeployView extends BaseController {
    private static final Logger LOG = LoggerFactory.make();

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Autowired
    AutoDeployService autoDeployService;

    @Autowired
    HostService hostService;

    /**
     * 自动部署
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String serverManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        // 初始化主机类型
        List<MdParam> hostTypeList = hostService.getHostType();
        model.addAttribute("hostTypeList", hostTypeList);

        // 初始化节点
        List<MdNode> nodeList = hostService.getNodeList();
        model.addAttribute("nodeList", nodeList);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "mainttool/autodeploy";
    }

    /**
     * 
     * @Title: downloadMould
     * @Description: TODO(下载自动部署模板)
     * @param @param request
     * @param @param response
     * @param @param model 参数
     * @return void 返回类型
     * @throws
     */
    @RequestMapping(value = "/downloadMould")
    public void downloadMould(HttpServletRequest request, HttpServletResponse response, Model model) {
        exportFile(request, response, Constant.AUTO_DEPLOY_MOULD_PATH,
                Constant.AUTO_DEPLOY_MOULD_NAME);
    }

    /**
     * 
     * @Title: uploadMould
     * @Description: TODO(上传自动部署文件)
     * @param @param request
     * @param @param response
     * @param @param model 参数
     * @return void 返回类型
     * @throws
     */
    @RequestMapping(value = "/uploadMould", method = RequestMethod.POST)
    @ResponseBody
    public WebResult uploadMould(
            @RequestParam(value = "file", required = false) CommonsMultipartFile file) {
        return autoDeployService.uploadMould(file);
    }

    /**
     * 
     * @Title: startUpAnsible
     * @Description: TODO(启动自动部署)
     * @param @param request
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    @RequestMapping(value = "/startUpAnsible")
    @ResponseBody
    public WebResult startUpAnsible(HttpServletRequest request) {
        return autoDeployService.startUpAnsible();
    }

    /**
     * 
     * @Title: addAutoDeploy
     * @Description: TODO(新增自动部署)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addAutoDeploy(HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> map = getParams(request);
        WebResult webResult = null;
        try {
            webResult = autoDeployService.addAutoDeploy(map);
        } catch (Exception e) {
            LOG.error("AutoDeployView addAutoDeploy Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: getAnsibleLog
     * @Description: TODO(获取主动部署日志)
     * @param @param request
     * @param @param session
     * @param @return 参数
     * @return List<AnsibleLog> 返回类型
     * @throws
     */
    @RequestMapping(value = "/getAnsibleLog")
    @ResponseBody
    public List<AnsibleLog> getAnsibleLog(HttpServletRequest request, HttpSession session) {
        List<AnsibleLog> ansibleLogList = null;
        try {
            ansibleLogList = autoDeployService.getAnsibleLog();
        } catch (Exception e) {
            LOG.error("AutoDeployView addAutoDeploy Exception:{}", e);
        }
        return ansibleLogList;
    }

    /**
     * 
     * @Title: ansibleLogRefresh
     * @Description: TODO(刷新自动部署日志)
     * @param @param request
     * @param @param session
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    @RequestMapping(value = "/ansibleLogRefresh")
    @ResponseBody
    public WebResult ansibleLogRefresh(HttpServletRequest request, HttpSession session) {
        WebResult webResult = null;
        try {
            webResult = autoDeployService.ansibleLogRefresh();
        } catch (Exception e) {
            LOG.error("AutoDeployView ansibleLogRefresh Exception:{}", e);
        }
        return webResult;
    }

    /**
     * 
     * @Title: delete
     * @Description: TODO(删除自动部署主机)
     * @param @param request
     * @param @return 参数
     * @return WebResult 返回类型
     * @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult delete(HttpServletRequest request) {
        String[] hostidArray = request.getParameterValues("hostidArray[]");
        return autoDeployService.deleteMonHost(hostidArray);
    }
}
