package com.asiainfo.lcims.omc.web.ctrl.mainttool;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.maintool.MainttoolService;
import com.asiainfo.lcims.omc.util.BaseController;

@Controller
@RequestMapping(value = "/mainttool")
public class Mainttool extends BaseController {
    
    @Resource(name = "mainttoolService")
    MainttoolService mainttoolService;
    
    /**
     * 查询radius主机
     * @param request
     * @return
     */
    @RequestMapping(value = { "/queryRadiusHostInfo" })
    @ResponseBody
    public List<MonHost> queryRadiusHostInfo(HttpServletRequest request) {
        String nodeid = request.getParameter("nodeid");
        return mainttoolService.queryRadiusHostInfo(nodeid);
    }
    
    /**
     * 直通、恢复操作
     * @param request
     * @return
     */
    @RequestMapping(value = { "/operateRadiusBusiness" })
    @ResponseBody
    public WebResult operateRadiusBusiness(HttpServletRequest request) {
        String ipdatas = request.getParameter("ipdatas");
        String operateTypeStr = request.getParameter("operateType");
//        int operateType = Integer.parseInt(operateTypeStr);
        WebResult result = mainttoolService.operateRadiusBusiness(operateTypeStr, ipdatas);
        return result;
    }
    
    
    /**
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = { "/queryOperateRadiusBusinessResult" })
    @ResponseBody
    public WebResult queryOperateRadiusBusinessResult(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");
        WebResult result = mainttoolService.queryOperateRadiusBusinessResult(uuid);
        return result;
    }
    
}
