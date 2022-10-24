package com.asiainfo.lcims.omc.web.ctrl.data;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.service.business.HostPerformaceService;

@Controller
public class SicuHomeData {
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;
    
    @Resource(name = "hostPerformaceService")
    HostPerformaceService hostPerformaceService;
    
    @RequestMapping(value = "/data/home/getHostPerformance")
    @ResponseBody
    public WebResult getHostPerformance(HttpServletRequest request, Model model, HttpSession session){
        WebResult result = null;
        //判断当前用户角色的节点权限
        String roleid = (String) SessionUtils.getSession().getAttribute("roleid");
        result = hostPerformaceService.getHostPerformance(roleid);
        return result;
    }
}
