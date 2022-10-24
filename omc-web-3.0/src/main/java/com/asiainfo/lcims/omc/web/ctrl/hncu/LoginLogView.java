package com.asiainfo.lcims.omc.web.ctrl.hncu;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.hncu.MdLoginLog;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.hncu.LoginLogService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.page.Page;

@Controller
@RequestMapping(value = "/view/class/mainttool/loginlog")
public class LoginLogView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginLogView.class);

    @Autowired
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private HostService hostService;

    @RequestMapping(value = "")
    public String moduleMonitor(HttpServletRequest request, HttpSession session, Model model) {
        String uri = request.getRequestURI();
        String classtype = mdMenuDataListener.getParentMdMenuByUrl(uri).getName();
        model.addAttribute("classtype", classtype);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }

        List<MonHost> allHost = hostService.getAllHost();
        model.addAttribute("allHostList", allHost);

        return "hncu/loginlog";
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryLoginLogList(HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        MdLoginLog systemLog = BeanToMapUtils.toBean(MdLoginLog.class, params);
        LOG.debug("query MdLoginLog is : {}", systemLog);
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            LOG.debug("pageNumber is : {}", pageNumber);
        }
        Page page = loginLogService.getLoginLogList(systemLog, pageNumber);
        return page;
    }

    @RequestMapping(value = "/export")
    @ResponseBody
    public void exportLoginLogList(HttpServletRequest request, HttpSession session,
            HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        MdLoginLog loginLog = BeanToMapUtils.toBean(MdLoginLog.class, params);
        LOG.debug("export MdLoginLog is : {}", loginLog);
        List<MdLoginLog> systemLogList = loginLogService.exportLoginLogList(loginLog);
        String[] field = { "event_occur_host&事件发生主机名", "admin_account&管理员账号", "login_ip&登陆源IP",
                "login_time&登陆时间", "logout_time&退出时间", "online_time&在线时间(分)",
                "login_flag&是否成功登陆" };
        String fileName = "登陆日志报表";
        ExcelUtil.downloadExcelToFile(fileName, DateUtil.nowDateString(), field, request, response,
                systemLogList);
    }

}
