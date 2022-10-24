package com.asiainfo.lcims.omc.web.ctrl.ems;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.lcims.omc.model.ems.EmsAddHostRequest;
import com.asiainfo.lcims.omc.model.ems.EmsAddHostResponse;
import com.asiainfo.lcims.omc.model.ems.EmsDelHostRequest;
import com.asiainfo.lcims.omc.model.ems.EmsDelHostResponse;
import com.asiainfo.lcims.omc.service.ems.EmsOmcService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * omc向ems提供新增主机、删除主机接口
 */
@Controller
@RequestMapping(value = "/omc/notify")
public class EmsOmcView extends BaseController{
    private static final Logger log = LoggerFactory.getLogger(EmsOmcView.class);
    
    @Resource(name = "emsOmcService")
    EmsOmcService emsOmcService;
    
    /**
     * 新增主机
     * @param addHostRequest
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/addHost",method = RequestMethod.POST)
    @ResponseBody
    public String emsAddHost(@RequestBody EmsAddHostRequest addHostRequest, HttpServletRequest request, HttpSession session) {
        JSONObject addHostRequestJson = JSONObject.parseObject(JSON.toJSONString(addHostRequest));
        log.info("omc receive from ems addhostinfo: " + addHostRequestJson.toJSONString());
        EmsAddHostResponse addHostResponse = emsOmcService.emsAddHost(addHostRequest);
        JSONObject addHostResponseJson = JSONObject.parseObject(JSON.toJSONString(addHostResponse));
        log.info("omc return to ems addhostinfo result: " + addHostResponseJson.toString());
        return addHostResponseJson.toString();
    }
    
    /**
     * 删除主机
     * @param delHostRequest
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/delHost",method = RequestMethod.POST)
    @ResponseBody
    public String emsDelHost(@RequestBody EmsDelHostRequest delHostRequest, HttpServletRequest request, HttpSession session) {
        JSONObject delHostRequestJson = JSONObject.parseObject(JSON.toJSONString(delHostRequest));
        log.info("omc receive from ems delhostinfo: " + delHostRequestJson.toJSONString());
        EmsDelHostResponse delHostResponse = emsOmcService.emsDelHost(delHostRequest);
        JSONObject delHostResponseJson = JSONObject.parseObject(JSON.toJSONString(delHostResponse));
        log.info("omc return to ems emsDelHost result: " + delHostResponseJson.toString());
        return delHostResponseJson.toString();
    }
    
}
