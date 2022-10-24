package com.asiainfo.lcims.omc.web.ctrl.autodeploy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.autodeploy.MdDeployLog;
import com.asiainfo.lcims.omc.model.autodeploy.MdHostHardwareInfoLog;
import com.asiainfo.lcims.omc.model.autodeploy.ParamBusinessHost;
import com.asiainfo.lcims.omc.model.autodeploy.ParamHostMetric;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.autodeploy.AutoDeployService;
import com.asiainfo.lcims.omc.util.AutoDeployConstant;

/**
 * @Author: YuChao
 * @Date: 2019/3/27 14:44
 */
@Controller
@RequestMapping(value = "/curl")
public class AutoDeployInterface {

    private static final Logger LOG = LoggerFactory.getLogger(AutoDeployInterface.class);

    @Autowired
    private AutoDeployService autoDeployService;

    @Autowired
    private CommonInit commonInit;

    @RequestMapping(value = "/checkipexist", method = RequestMethod.POST)
    @ResponseBody
    public String checkIpExistWithAutoDeploy(MonHost monHost) {
        LOG.info(monHost.toString());
        String response = AutoDeployConstant.CURL_ERROR_RESPONSE;
        try {
            response = autoDeployService.checkIpExistService(monHost);
        } catch (Exception ex) {
            LOG.error("check Ip exist error : {} ", ex.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/recordlog", method = RequestMethod.POST)
    @ResponseBody
    public String recordDeployLog(MdDeployLog deployLog) {
        LOG.info(deployLog.toString());
        String response = AutoDeployConstant.CURL_ERROR_RESPONSE;
        try {
            response = autoDeployService.recordDeployLog(deployLog);
        } catch (Exception ex) {
            LOG.error("record Deploy Log error : {} ", ex.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/recordhardware", method = RequestMethod.POST)
    @ResponseBody
    public String recordHostHardwareInfo(MdHostHardwareInfoLog hostHardwareInfoLog) {
        LOG.info(hostHardwareInfoLog.toString());
        String response = AutoDeployConstant.CURL_ERROR_RESPONSE;
        try {
            response = autoDeployService.recordHostHardwareInfo(hostHardwareInfoLog);
            commonInit.refreshMdHost();
        } catch (Exception ex) {
            LOG.error("record Host Hardware Info Log error : {} ", ex.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/removeHosts", method = RequestMethod.POST)
    @ResponseBody
    public String removeHosts(String ip, String batch) {

        LOG.info("removeHosts controller get param: ip = {}, batch = {}", ip, batch);

        String response = autoDeployService.removeHostsService(ip, batch);
        return response;
    }

    /**
     * 主机指标配置接口
     * 接收参数：ip、业务列表、操作系统标识、pwdDir
     * 操作系统标识定义：
        Linux系统：linux
        sunOs系统：sunOs
        HP Linux 系统: hp_linux
        IBM AIX 系统： aix
     *
     */
    @RequestMapping(value = "/updateHostMetric", method = RequestMethod.POST)
    @ResponseBody
    public String updateHostMetric(ParamHostMetric paramHostMetric) {
        LOG.info(paramHostMetric.toString());
        return autoDeployService.updateHostMetric(paramHostMetric);
    }

    /**
     * 更新业务主机配置接口
     * @param paramBusinessHost
     * @return
     */
    @RequestMapping(value = "/updateBusinessHost", method = RequestMethod.POST)
    @ResponseBody
    public String updateBusinessHost(ParamBusinessHost paramBusinessHost) {
        LOG.info(paramBusinessHost.toString());
        return autoDeployService.updateBusinessHost(paramBusinessHost);
    }

    @RequestMapping(value = "/checkSshConnect", method = RequestMethod.POST)
    @ResponseBody
    public String checkSshConnect(String pingStr) {
        LOG.info(pingStr);
        String response = AutoDeployConstant.CURL_ERROR_RESPONSE;
        try {
            response = autoDeployService.checkSshConnectService(pingStr);
        } catch (Exception ex) {
            LOG.error("check Ssh Connect error : {} ", ex.getMessage());
        }
        return response;
    }

}
