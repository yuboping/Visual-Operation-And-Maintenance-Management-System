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
import com.asiainfo.lcims.omc.model.hncu.MdSystemLog;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.service.hncu.SystemLogService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.page.Page;

@Controller
@RequestMapping(value = "/view/class/mainttool/systemlog")
public class SystemLogView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogView.class);

    @Autowired
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private SystemLogService systemLogService;

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

        return "hncu/systemlog";
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public Page querySystemLogList(HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        MdSystemLog systemLog = BeanToMapUtils.toBean(MdSystemLog.class, params);
        LOG.debug("query MdSystemLog is : {}", systemLog);
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            LOG.debug("pageNumber is : {}", pageNumber);
        }
        Page page = systemLogService.getSystemLogList(systemLog, pageNumber);
        return page;
    }
    
    @RequestMapping(value = "/export")
    @ResponseBody
    public void exportSystemLogList(HttpServletRequest request, HttpSession session,
            HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        MdSystemLog systemLog = BeanToMapUtils.toBean(MdSystemLog.class, params);
        LOG.debug("export MdSystemLog is : {}", systemLog);
        List<MdSystemLog> systemLogList = systemLogService.exportSystemLogList(systemLog);
        String[] field = { "event_occur_host&事件发生主机名", "system_time&时间", "host_name&主机名",
                "process_name&进程名", "error_level&错误级别", "msg_data&消息格式" };
        String fileName = "系统日志报表";
        ExcelUtil.downloadExcelToFile(fileName, DateUtil.nowDateString(), field, request, response,
                systemLogList);
    }
}
