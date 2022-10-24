package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.util.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 配置管理/服务器管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/servermanage")
public class ServerManageView extends BaseController {

    private final static String MANAGETYPE_DELETE = "delete";
    private final static String MANAGETYPE_MODIFY = "modify";
    private final static String MANAGETYPE_ADD = "add";

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Autowired
    HostService hostService;

    /**
     * 服务器管理
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
        return "servermanage/monhost";
    }

    /*
     * 根据queryType的类型查询对应的信息并返回
     */
    @RequestMapping(value = "/query/{queryInfoType}")
    @ResponseBody
    public List<?> queryInfoList(@PathVariable String queryInfoType, HttpServletRequest request,
            HttpSession session) {
        boolean equalValue = StringUtils.equalsIgnoreCase("infolist", queryInfoType);
        if (equalValue) {
            String hostname = (String) request.getParameter("hostname");
            String addr = (String) request.getParameter("addr");
            String hosttype = (String) request.getParameter("hosttype");
            String nodeid = (String) request.getParameter("nodeid");
            List<MonHost> hostList = hostService.getHost(hostname, addr, hosttype, nodeid);
            return hostList;
        }
        equalValue = StringUtils.equalsIgnoreCase("singleinfo", queryInfoType);
        if (equalValue) {
            String hostid = request.getParameter("hostid");
            MonHost host = hostService.getHostById(hostid);
            List<MonHost> list = new LinkedList<MonHost>();
            list.add(host);
            return list;
        }
        equalValue = StringUtils.equalsIgnoreCase("hosttypelist", queryInfoType);
        if (equalValue) {
            List<MdParam> hostTypeList = hostService.getHostType();
            return hostTypeList;
        }
        equalValue = StringUtils.equalsIgnoreCase("nodelist", queryInfoType);
        if (equalValue) {
            List<MdNode> nodeList = hostService.getNodeList();
            return nodeList;
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/{managetype}")
    @ResponseBody
    public WebResult manageInfo(@PathVariable String managetype, @ModelAttribute MonHost monHost,
            HttpServletRequest request) {
        WebResult result = new WebResult(false, "操作类型错误！");
        if (MANAGETYPE_ADD.equals(managetype)) {
            result = hostService.addInfo(monHost);
        } else if (MANAGETYPE_DELETE.equals(managetype)) {
            String[] hostidArray = request.getParameterValues("hostidArray[]");
            result = hostService.deleteInfo(hostidArray);
        } else if (MANAGETYPE_MODIFY.equals(managetype)) {
            result = hostService.modifyInfo(monHost);
        }
        return result;
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public List<?> queryRepeatHost(HttpServletRequest request) {
        String addr = request.getParameter("addr");
        String hostname = request.getParameter("hostname");
        List<MonHost> monHosts = hostService.getRepeatHost(addr, hostname);
        return monHosts;
    }

    @RequestMapping(value = "/queryDetail")
    @ResponseBody
    public List<?> queryDetailHost(HttpServletRequest request) {
        String hostid = request.getParameter("hostid");
        MonHost host = hostService.getHostById(hostid);
        List<MonHost> list = new LinkedList<MonHost>();
        list.add(host);
        return list;
    }
}
