package com.asiainfo.lcims.omc.web.ctrl.view;

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
import com.asiainfo.lcims.omc.model.maintool.HostCapability;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.maintool.HostCapabilityService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;

/**
 * 主机性能查询
 * @author ZP
 *
 */
@Controller
@RequestMapping(value = "/view/class/mainttool/hostcapabilityquery")
public class HostCapabilityQueryView extends BaseController {

    @Resource(name = "hostCapabilityService")
    HostCapabilityService hostCapabilityService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 告警信息查询
     *
     * @return
     */
    @RequestMapping(value = "")
    public String moduleMonitor(HttpServletRequest request, HttpSession session, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        model.addAttribute("moduleid", "hostcapabilityquery");

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }

        return "mainttool/hostcapability";
    }

    /**
     * 查询告警信息
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<HostCapability> queryInfo(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        HostCapability hostCapability = BeanToMapUtils.toBean(HostCapability.class, params);

        List<HostCapability> hostCapabilityList = hostCapabilityService.getHostCapabilityList(hostCapability.getHost_name());
        return hostCapabilityList;
    }

}