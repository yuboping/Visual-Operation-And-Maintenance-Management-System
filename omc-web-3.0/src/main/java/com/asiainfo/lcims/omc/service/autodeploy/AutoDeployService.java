package com.asiainfo.lcims.omc.service.autodeploy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.autodeploy.AnsibleLog;
import com.asiainfo.lcims.omc.model.autodeploy.MdDeployLog;
import com.asiainfo.lcims.omc.model.autodeploy.MdFindBusiness;
import com.asiainfo.lcims.omc.model.autodeploy.MdHostHardwareInfoLog;
import com.asiainfo.lcims.omc.model.autodeploy.ParamBusinessHost;
import com.asiainfo.lcims.omc.model.autodeploy.ParamHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.configmanage.MdHostMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdMetric;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdMenuTree;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.autodeploy.AutoDeployDao;
import com.asiainfo.lcims.omc.persistence.configmanage.HostMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdBusinessHostDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdHostProcessDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.AlarmRuleManageService;
import com.asiainfo.lcims.omc.service.configmanage.BusinessHostManageService;
import com.asiainfo.lcims.omc.service.configmanage.NodeManageService;
import com.asiainfo.lcims.omc.socket.SimpleChatClient;
import com.asiainfo.lcims.omc.util.AutoDeployConstant;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.ExcelUtil;
import com.asiainfo.lcims.omc.util.FileUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.RegexUtil;
import com.asiainfo.lcims.omc.util.StringUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * @Author: YuChao
 * @Date: 2019/3/27 15:10
 */
@Component
public class AutoDeployService {

    private static Map<String, CopyOnWriteArrayList<AnsibleLog>> ansibleLogListMap = new HashMap<String, CopyOnWriteArrayList<AnsibleLog>>();
    private static Boolean ansibleStartUpState = false;// ansible????????????
    private static final Logger LOG = LoggerFactory.getLogger(AutoDeployService.class);
    private static String ansibleHostsFileFullPath = CommonInit.BUSCONF
            .getAnsibleHostsFileFullPath();
    private static String ansibleMdFindBusinessFileFullPath = CommonInit.BUSCONF
            .getAnsibleMdFindBusinessFileFullPath();
    private static String ansibleHostsDeleteFileFullPath = CommonInit.BUSCONF
            .getAnsibleHostsDeleteFileFullPath();
    private static String ansibleStartSh = CommonInit.BUSCONF.getAnsibleStartSh();
    private static String ansibleDeleteStartSh = CommonInit.BUSCONF.getAnsibleDeleteStartSh();
    private static String ansibleCheckStartSh = CommonInit.BUSCONF.getAnsibleCheckStartSh();

    @Autowired
    private MonHostDAO monHostDAO;

    @Autowired
    private MdHostProcessDAO mdHostProcessDAO;

    @Inject
    private MdBusinessHostDAO mdBusinessHostDAO;

    @Resource(name = "mdMenuDataListener")
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private CommonInit commonInit;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Autowired
    private AutoDeployDao deployDao;

    @Resource(name = "nodeManageService")
    NodeManageService nodeManageService;

    @Inject
    private HostMetricDAO hostMetricDAO;

    @Inject
    private AutoDeployDao autoDeployDao;

    @Resource(name = "businessHostManageService")
    BusinessHostManageService businessHostManageService;

    public String checkIpExistService(MonHost monHost) {

        MonHost hostinfo = deployDao.getMonHostInfoWithIp(monHost);
        // ??????????????????????????????
        monHost.setDeploy_status(AutoDeployConstant.DEPLOY_STATUS_RUNNING);

        MdNode mdNode = nodeManageService.getMdNodeInfo(monHost.getNodeidname());
        if (null == mdNode) {
            saveNodes(monHost);
            mdNode = nodeManageService.getMdNodeInfo(monHost.getNodeidname());
        }
        monHost.setNodeid(mdNode.getId());

        Optional optional = Optional.ofNullable(hostinfo);
        if (optional.isPresent()) {

            Object host = optional.get();
            MonHost opHost = (MonHost) host;
            if (opHost.getDeploy_status() != AutoDeployConstant.DEPLOY_STATUS_RUNNING) {

                deployDao.updateMonHostInfo(monHost);
            } else {

                LOG.info("check ip exist failed: deploy is running");
                return AutoDeployConstant.CURL_ERROR_RESPONSE;
            }

        } else {

            monHost.setHostid(IDGenerateUtil.getUuid());
            deployDao.addMonHostInfo(monHost);
        }

        return AutoDeployConstant.CURL_SUCCESS_RESPONSE;
    }

    public String checkSshConnectService(String pingStr) {

        List<String> pingErrList = RegexUtil.getReplaceWithRegex(pingStr);

        for (String errStr : pingErrList) {
            MdDeployLog deployLog = new MdDeployLog();
            deployLog.setDeploy_status(-1);
            deployLog.setDeploy_des(AutoDeployConstant.ERROR_SSH_CONNECTION);
            deployLog.setIp(errStr);
            recordDeployLog(deployLog);
        }

        return AutoDeployConstant.CURL_SUCCESS_RESPONSE;
    }

    /**
     * ??????????????????
     * 
     * @param deployLog
     * @return
     */
    public String recordDeployLog(MdDeployLog deployLog) {

        String response = AutoDeployConstant.CURL_ERROR_RESPONSE;

        MonHost monHost = deployLogToMonHost(deployLog);

        deployDao.updateMonHostInfo(monHost);

        // ????????????
        deployLog.setId(IDGenerateUtil.getUuid());
        // ??????????????????
        String description = deployLog.getDeploy_des();
        deployLog.setDeploy_des(getDeployDescription(description));

        int successCount = deployDao.addDeployLogInfo(deployLog);
        if (successCount > 0) {

            response = AutoDeployConstant.CURL_SUCCESS_RESPONSE;
        }
        return response;
    }

    /**
     * ????????????????????????
     * 
     * @param hostHardwareInfoLog
     * @return
     */
    public String recordHostHardwareInfo(MdHostHardwareInfoLog hostHardwareInfoLog) {

        String response = AutoDeployConstant.CURL_ERROR_RESPONSE;

        MonHost monHost = hostHardwareToMonHost(hostHardwareInfoLog);

        int monSuccessCount = deployDao.updateMonHostInfo(monHost);
        if (monSuccessCount < 1) {

            return response;
        }

        // ????????????
        hostHardwareInfoLog.setId(IDGenerateUtil.getUuid());

        int successCount = deployDao.addHostHardwareInfoLogInfo(hostHardwareInfoLog);
        if (successCount > 0) {

            response = AutoDeployConstant.CURL_SUCCESS_RESPONSE;
        }
        return response;
    }

    /**
     * ?????????????????????????????????
     *
     * @param ip
     * @return
     */
    public String removeHostsService(String ip, String batch) {

        String response = AutoDeployConstant.CURL_ERROR_RESPONSE;

        try {

            // ????????????
            MonHost monHost = new MonHost();
            monHost.setAddr(ip);
            // ??????HostInfo
            MonHost hostinfo = deployDao.getMonHostInfoWithIp(monHost);
            String hostid = hostinfo.getHostid();
            String hostname = hostinfo.getHostname();
            deployDao.deleteMdHostMetricByHostId(hostid);
            int deleteResult = monHostDAO.deleteByHostId(hostid);
            if (deleteResult > 0) {
                int deleteHostProcess = mdHostProcessDAO.deleteByHostId(hostid);
                int deleteBusinessHost = mdBusinessHostDAO.deleteByHostId(hostid);
                // ????????????
                // mdMenuDataListener.loadMenuList();
                // ???????????????????????????????????????
                commonInit.refreshMdHost();
                // ??????????????????
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("operationType", Constant.DYNAMICTYPE_HOST);
                map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                map.put("operationId", hostid);
                alarmRuleManageService.dimensionModify(map);

                MdDeployLog deployLog = new MdDeployLog();
                // ????????????
                deployLog.setId(IDGenerateUtil.getUuid());
                deployLog.setIp(ip);
                // ??????????????????
                String description = AutoDeployConstant.SUCCESS_REMOVE_DEPLOY;
                deployLog.setDeploy_status(AutoDeployConstant.DEPLOY_STATUS_SUCCESS);
                deployLog.setDeploy_des(getDeployDescription(description));
                deployLog.setDeploy_batch(batch);

                deployDao.addDeployLogInfo(deployLog);

                response = AutoDeployConstant.CURL_SUCCESS_RESPONSE;
            }
        } catch (Exception ex) {

            LOG.error("remove Hosts Info Log error : {} ", ex.getMessage(), ex);
            // ex.printStackTrace();
        }

        return response;
    }

    private MonHost deployLogToMonHost(MdDeployLog deployLog) {

        MonHost monHost = new MonHost();
        monHost.setAddr(deployLog.getIp());
        monHost.setDeploy_status(deployLog.getDeploy_status());
        monHost.setDeploy_des(getDeployDescription(deployLog.getDeploy_des()));

        return monHost;
    }

    private MonHost hostHardwareToMonHost(MdHostHardwareInfoLog hostHardwareInfoLog) {

        MonHost monHost = new MonHost();
        monHost.setDeploy_status(AutoDeployConstant.DEPLOY_STATUS_RUNNING);
        monHost.setOs(hostHardwareInfoLog.getOs());
        monHost.setCpu(hostHardwareInfoLog.getCpu());
        monHost.setMemory(hostHardwareInfoLog.getMemory());
        monHost.setDiskspace(hostHardwareInfoLog.getDiskspace());
        monHost.setAddr(hostHardwareInfoLog.getIp());

        return monHost;
    }

    /**
     * ????????????????????????
     */
    private String getDeployDescription(String descType) {

        String description = "????????????";

        switch (descType) {
        case AutoDeployConstant.ERROR_CHECK_DATABASE:
            description = "?????????????????????";
            break;
        case AutoDeployConstant.ERROR_SSH_CONNECTION:
            description = "ssh?????????????????????";
            break;
        case AutoDeployConstant.ERROR_HARDWARE_RECORD:
            description = "????????????????????????";
            break;
        case AutoDeployConstant.ERROR_START_COLLECT_CLIENT:
            description = "???????????????????????????";
            break;
        case AutoDeployConstant.ERROR_CONFIG_HOST_METRIC:
            description = "????????????????????????";
            break;
        case AutoDeployConstant.ERROR_UPDATE_HOST_METRIC:
            description = "??????????????????????????????";
            break;
        case AutoDeployConstant.SUCCESS_AUTO_DEPLOY:
            description = "??????????????????";
            break;
        case AutoDeployConstant.SUCCESS_REMOVE_DEPLOY:
            description = "????????????????????????";
            break;
        }

        return description;
    }

    /**
     * ?????????????????????????????????
     *
     * @param paramHostMetric
     * @return
     */
    public String updateHostMetric(ParamHostMetric paramHostMetric) {
        String result = AutoDeployConstant.CURL_SUCCESS_RESPONSE;
        try {
            if (ToolsUtils.StringIsNull(paramHostMetric.getIp())) {
                LOG.info("updateHostMetric: ip is null!");
                return AutoDeployConstant.CURL_ERROR_RESPONSE;
            }
            if (ToolsUtils.StringIsNull(paramHostMetric.getPwdDir())) {
                LOG.info("updateHostMetric: pwdDir is null!");
                return AutoDeployConstant.CURL_ERROR_RESPONSE;
            }
            String businesslist = paramHostMetric.getBusinesslist();
            if (ToolsUtils.StringIsNull(businesslist)
                    || AutoDeployConstant.BUSINESS_LIST_NULL.equals(businesslist)) {
                businesslist = AutoDeployConstant.BUSINESS_HOST;
            } else {
                businesslist = businesslist + AutoDeployConstant.AUTO_DEPLOY_SPLIT_STR
                        + AutoDeployConstant.BUSINESS_HOST;
            }
            MonHost host = CommonInit.getHostByIp(paramHostMetric.getIp());
            if (null == host) {
                LOG.info("updateHostMetric: ip[" + paramHostMetric.getIp()
                        + "] query MonHost is null!");
                return AutoDeployConstant.CURL_ERROR_RESPONSE;
            }

            // ?????? ip ???????????????????????????
            deployDao.deleteHostMetricByHostId(host.getHostid());
            // add new ??????????????????????????????
            // ???????????????????????????????????? MD_BUSINESS_REL
            List<MdMetric> metriclist = deployDao.queryMetricList(businesslist);
            if (ToolsUtils.ListIsNull(metriclist)) {
                LOG.info("updateHostMetric: ip[" + paramHostMetric.getIp()
                        + "] query metriclist is null!");
                return AutoDeployConstant.CURL_ERROR_RESPONSE;
            }
            LOG.info("updateHostMetric: delete ip[" + paramHostMetric.getIp()
                    + "] host_metric infos.");
            // ??????????????????????????????????????????
            List<MdHostMetric> list = new ArrayList<MdHostMetric>();
            paramHostMetric.setBusinesslist(businesslist);
            for (MdMetric mdMetric : metriclist) {
                String scriptParam = null;
                if (ConstantUtill.PROCESSMETRICTYPE.equals(mdMetric.getMetric_type())) {
                    /**
                     * ???????????? ?????????????????? ????????????????????? MD_FIND_BUSINESS ???????????? ???????????????????????????
                     */
                    scriptParam = getProcessParam(businesslist);
                }
                replaceMetricScript(mdMetric, paramHostMetric, scriptParam);
                list.add(makeHostMetric(host, mdMetric));
            }
            // ?????????????????????????????? list
            for (MdHostMetric mdHostMetric : list) {
                mdHostMetric.setId(IDGenerateUtil.getUuid());
                host = CommonInit.getHost(mdHostMetric.getHost_id());
                hostMetricDAO.insertHostMetric(mdHostMetric);
            }
            LOG.info(
                    "updateHostMetric: add ip[" + paramHostMetric.getIp() + "] host_metric infos.");
            return AutoDeployConstant.CURL_SUCCESS_RESPONSE;
        } catch (Exception e) {
            LOG.error("updateHostMetric: " + e.getMessage(), e);
            // e.printStackTrace();
            result = AutoDeployConstant.CURL_ERROR_RESPONSE;
        }
        return result;
    }

    private void replaceMetricScript(MdMetric mdMetric, ParamHostMetric paramHostMetric,
            String scriptParam) {
        if (!ToolsUtils.StringIsNull(scriptParam))
            mdMetric.setScript_param(scriptParam);
        // ??????????????????????????????????????????xxx.sh --> xxx_osType.sh
        String script = mdMetric.getScript();
        if (ToolsUtils.StringIsNull(script))
            return;
        int a = script.lastIndexOf("/");
        if (a < 0)
            return;
        String script_name = script.substring(a + 1, script.length());
        switch (paramHostMetric.getOsType()) {
        case AutoDeployConstant.OS_TYPE_LINUX:
            script_name = script_name.replace(".sh",
                    "_" + AutoDeployConstant.OS_TYPE_LINUX + ".sh");
            break;
        case AutoDeployConstant.OS_TYPE_SUNOS:
            script_name = script_name.replace(".sh",
                    "_" + AutoDeployConstant.OS_TYPE_SUNOS + ".sh");
            break;
        case AutoDeployConstant.OS_TYPE_HP_LINUX:
            script_name = script_name.replace(".sh",
                    "_" + AutoDeployConstant.OS_TYPE_HP_LINUX + ".sh");
            break;
        case AutoDeployConstant.OS_TYPE_AIX:
            script_name = script_name.replace(".sh", "_" + AutoDeployConstant.OS_TYPE_AIX + ".sh");
            break;
        default:
            break;
        }
        if (paramHostMetric.getPwdDir().endsWith("/")) {
            script = paramHostMetric.getPwdDir() + "ai-omc/shell/" + script_name;
        } else {
            script = paramHostMetric.getPwdDir() + "/ai-omc/shell/" + script_name;
        }
        mdMetric.setScript(script);
    }

    private String getProcessParam(String businesslist) {
        List<MdFindBusiness> findBusinesslist = deployDao.getFindBusinessList(businesslist);
        StringBuilder strb = new StringBuilder("");
        for (MdFindBusiness mdFindBusiness : findBusinesslist) {
            strb.append(mdFindBusiness.getProcessParam() + "#");
        }
        String returnStr = strb.toString();
        if (ToolsUtils.StringIsNull(returnStr))
            return null;
        returnStr = returnStr.substring(0, returnStr.length() - 1);
        return returnStr;
    }

    /**
     * HOST_ID???METRIC_ID???CYCLE_ID???SCRIPT???SCRIPT_PARAM???SCRIPT_RETURN_TYPE
     *
     * @param host
     * @param mdMetric
     * @return
     */
    private MdHostMetric makeHostMetric(MonHost host, MdMetric mdMetric) {
        MdHostMetric hostMetric = new MdHostMetric();
        hostMetric.setHost_id(host.getHostid());
        hostMetric.setMetric_id(mdMetric.getId());
        hostMetric.setCycle_id(mdMetric.getCycle_id());
        hostMetric.setScript(mdMetric.getScript());
        hostMetric.setScript_param(mdMetric.getScript_param());
        hostMetric.setScript_return_type(mdMetric.getScript_return_type());
        return hostMetric;
    }

    public WebResult uploadMould(CommonsMultipartFile file) {
        LOG.info("uploadMould start");
        List<ArrayList<String>> readResult = null;// ????????????
        try {
            // ????????????????????????
            if (file.isEmpty()) {
                return new WebResult(false, "??????????????????");
            }
            // ??????????????????
            long size = file.getSize();
            String name = file.getOriginalFilename();
            if (StringUtils.isBlank(name) || size == 0) {
                return new WebResult(false, "??????????????????");
            }
            // ??????????????????
            String postfix = ExcelUtil.getPostfix(name);

            // ??????????????????
            if (StringUtils.equals("xlsx", postfix)) {
                readResult = ExcelUtil.readXlsx(file);
            } else if (StringUtils.equals("xls", postfix)) {
                readResult = ExcelUtil.readXls(file);
            } else {
                return new WebResult(false, "??????????????????");
            }
            if (readResult == null || readResult.size() <= 1) {
                return new WebResult(false, "??????????????????");
            }
            List<MonHost> monHostList = new ArrayList<MonHost>();
            int i = 0;
            for (ArrayList<String> arr : readResult) {
                if (arr == null || arr.size() != 6) {
                    return new WebResult(false, "??????????????????");
                }
                if (i == 0) {
                    i++;
                    continue;
                }
                WebResult autoDeployWebResult = autoDeployVerification(i, arr);
                if (!autoDeployWebResult.isOpSucc()) {
                    return autoDeployWebResult;
                } else {
                    for (MonHost host : monHostList) {
                        if (host.getAddr().equals(arr.get(0).toString())) {
                            return new WebResult(true, "???" + i + "?????????????????????????????????????????????IP");
                        }
                    }
                    MonHost monHost = new MonHost();
                    monHost.setAddr(arr.get(0).toString());
                    monHost.setHostname(arr.get(1).toString());
                    monHost.setNodeidname(arr.get(2).toString());
                    monHost.setSsh_user(arr.get(3).toString());
                    monHost.setSsh_password(arr.get(4).toString());
                    monHost.setSsh_port(Integer.valueOf(arr.get(5).toString()));
                    monHostList.add(monHost);
                }
                i++;
            }
            return generateHostFile(monHostList);
        } catch (Exception e) {
            LOG.error("AutoDeployService.uploadMould--->", e);
        }
        LOG.info("uploadMould end");
        return new WebResult(true, "??????");
    }

    public WebResult addAutoDeploy(Map<String, Object> map) {
        List<MonHost> monHostList = new ArrayList<MonHost>();
        int addinfojsonlength = Integer.parseInt(map.get("addinfojsonlength").toString());
        for (int i = 0; i < addinfojsonlength; i++) {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(map.get("addinfojson[" + i + "][0]").toString());
            arr.add(map.get("addinfojson[" + i + "][1]").toString());
            arr.add(map.get("addinfojson[" + i + "][2]").toString());
            arr.add(map.get("addinfojson[" + i + "][3]").toString());
            arr.add(map.get("addinfojson[" + i + "][4]").toString());
            arr.add(map.get("addinfojson[" + i + "][5]").toString());
            WebResult autoDeployWebResult = autoDeployVerification(i, arr);
            if (!autoDeployWebResult.isOpSucc()) {
                return autoDeployWebResult;
            } else {
                MonHost monHost = new MonHost();
                monHost.setAddr(arr.get(0).toString());
                monHost.setHostname(arr.get(1).toString());
                monHost.setNodeidname(arr.get(2).toString());
                monHost.setSsh_user(arr.get(3).toString());
                monHost.setSsh_password(arr.get(4).toString());
                monHost.setSsh_port(Integer.valueOf(arr.get(5).toString()));
                monHostList.add(monHost);
            }
        }
        try {
            return generateHostFile(monHostList);
        } catch (Exception e) {
            LOG.error("AutoDeployService.addAutoDeploy--->", e);
        }
        return new WebResult(true, "??????");
    }

    /**
     * 
     * @Title: autoDeployVerification @Description:
     *         TODO(???????????????????????????????????????) @param @param arr @param @return ?????? @return
     *         WebResult ???????????? @throws
     */
    public WebResult autoDeployVerification(int i, ArrayList<String> arr) {
        if (null == arr.get(0).toString() || arr.get(0).toString().isEmpty()) {
            return new WebResult(false, "???" + i + "????????????????????????IP??????");
        }
        if (!StringUtil.isIP(arr.get(0).toString())) {
            return new WebResult(false, "???" + i + "????????????????????????IP?????????");
        }
        if (null == arr.get(1).toString() || arr.get(1).toString().isEmpty()) {
            return new WebResult(false, "???" + i + "????????????????????????????????????");
        }
        if (arr.get(1).toString().length() > 100) {
            return new WebResult(false, "???" + i + "????????????????????????????????????");
        }
        if (null == arr.get(2).toString() || arr.get(2).toString().isEmpty()) {
            return new WebResult(false, "???" + i + "????????????????????????????????????");
        }
        if (arr.get(2).toString().length() > 50) {
            return new WebResult(false, "???" + i + "????????????????????????????????????");
        }
        if (null == arr.get(3).toString() || arr.get(3).toString().isEmpty()) {
            return new WebResult(false, "???" + i + "??????????????????SSH???????????????");
        }
        if (arr.get(3).toString().length() > 35) {
            return new WebResult(false, "???" + i + "??????????????????SSH???????????????");
        }
        if (null == arr.get(4).toString() || arr.get(4).toString().isEmpty()) {
            return new WebResult(false, "???" + i + "??????????????????SSH????????????");
        }
        if (arr.get(4).toString().length() > 40) {
            return new WebResult(false, "???" + i + "??????????????????SSH????????????");
        }
        if (null == arr.get(5).toString() || arr.get(5).toString().isEmpty()) {
            return new WebResult(false, "???" + i + "??????????????????SSH????????????");
        }
        if (!StringUtil.isPort(arr.get(5).toString())) {
            return new WebResult(false, "???" + i + "??????????????????SSH???????????????");
        }
        return new WebResult(true, "??????");
    }

    /**
     * 
     * @Title: generateHostFile
     * @Description: TODO(??????ansible???Hosts??????)
     * @param @param
     *            monHostList
     * @param @return
     * @param @throws
     *            IOException ??????
     * @return WebResult ????????????
     * @throws @eg.
     *             10.1.198.82 ansible_ssh_user=zhangying9
     *             ansible_ssh_pass=zhangying9 ansible_ssh_port=22
     */
    public WebResult generateHostFile(List<MonHost> monHostList) throws IOException {
        LOG.info("generateHostFile start");
        if (!ansibleStartUpState) {
            ansibleStartUpState = true;
            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);
            for (MonHost monHost : monHostList) {
                bw.write(monHost.getAddr());
                bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                bw.write("ansible_ssh_user=");
                bw.write(monHost.getSsh_user());
                bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                bw.write("ansible_ssh_pass=");
                bw.write(monHost.getSsh_password());
                bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                bw.write("ansible_ssh_pass_transform=");
                bw.write(passwordTransform(monHost.getSsh_password()));
                bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                bw.write("ansible_ssh_port=");
                bw.write(monHost.getSsh_port().toString());
                bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                bw.write("hostname=");
                bw.write(monHost.getHostname());
                bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                bw.write("nodeidname=");
                bw.write(monHost.getNodeidname());
                bw.newLine();
            }
            bw.flush();
            String content = sw.getBuffer().toString();
            bw.close();
            sw.close();
            LOG.info("generateHostFile content:" + content);
            String result = FileUtil.writeFile(ansibleHostsFileFullPath, content, "UTF-8");
            if ("0".equals(result)) {
                return generateMdFindBusinessFile();
            } else {
                return new WebResult(false, "ansible???????????????????????????");
            }
        } else {
            return new WebResult(false, "?????????????????????????????????????????????????????????");
        }
    }

    /**
     *
     * @Title: generateMdFindBusinessFile @Description:
     *         TODO(??????????????????????????????) @param @return @param @throws IOException
     *         ?????? @return WebResult ???????????? @throws
     */
    public WebResult generateMdFindBusinessFile() throws IOException {
        LOG.info("generateMdFindBusinessFile start");
        List<MdFindBusiness> mdFindBusinessList = autoDeployDao
                .getFindBusinessListExcludeHost(AutoDeployConstant.BUSINESS_HOST);
        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);
        for (MdFindBusiness mdFindBusiness : mdFindBusinessList) {
            bw.write(mdFindBusiness.getBusiness_tag());
            bw.write(Constant.ANSIBLE_MDFINDBUSINESS_FILE_SEPARATOR);
            bw.write(mdFindBusiness.getProcess_key());
            bw.newLine();
        }
        bw.flush();
        String content = sw.getBuffer().toString();
        bw.close();
        sw.close();
        LOG.info("generateMdFindBusinessFile content:" + content);
        String result = FileUtil.writeFile(ansibleMdFindBusinessFileFullPath, content, "UTF-8");
        if ("0".equals(result)) {
            return new WebResult(true, "??????");
        } else {
            return new WebResult(false, "?????????????????????????????????????????????");
        }
    }

    /**
     *
     * @Title: startUpAnsible @Description: TODO(??????Ansible) @param @return
     *         ?????? @return WebResult ???????????? @throws
     */
    public WebResult startUpAnsible() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                runShellInToMemory(ansibleStartSh);
                // ????????????
                mdMenuDataListener.loadMenuList();
                // ??????????????????
                ansibleStartUpState = false;
            }
        });
        t.start();
        return new WebResult(true, "??????");
    }

    /**
     * 
     * @param paramBusinessHost
     * @return
     */
    public String updateBusinessHost(ParamBusinessHost paramBusinessHost) {
        if (ToolsUtils.StringIsNull(paramBusinessHost.getIp())) {
            LOG.info("updateBusinessHost: ip is null!");
            return AutoDeployConstant.CURL_ERROR_RESPONSE;
        }

        String businesslist = paramBusinessHost.getBusinesslist();
        if (ToolsUtils.StringIsNull(businesslist)
                || AutoDeployConstant.BUSINESS_LIST_NULL.equals(businesslist)) {
            businesslist = AutoDeployConstant.BUSINESS_HOST;
        } else {
            businesslist = businesslist + AutoDeployConstant.AUTO_DEPLOY_SPLIT_STR
                    + AutoDeployConstant.BUSINESS_HOST;
        }
        paramBusinessHost.setBusinesslist(businesslist);
        MonHost host = CommonInit.getHostByIp(paramBusinessHost.getIp());
        if (null == host) {
            LOG.info("updateBusinessHost: ip[" + paramBusinessHost.getIp()
                    + "] query MonHost is null!");
            return AutoDeployConstant.CURL_ERROR_RESPONSE;
        }
        String result = AutoDeployConstant.CURL_SUCCESS_RESPONSE;
        try {
            // ????????????MD_BUSINESS_HOST ????????????
            deleteBusinessHost(host.getHostid());
            // ???????????????????????????????????? MD_BUSINESS_REL
            List<MdMenuTree> menuList = deployDao.queryMenuList(businesslist);
            if (ToolsUtils.ListIsNull(menuList)) {
                LOG.info("updateBusinessHost: ip[" + paramBusinessHost.getIp()
                        + "] query menuList is null!");
                return AutoDeployConstant.CURL_SUCCESS_RESPONSE;
            }

            for (MdMenuTree menu : menuList) {
                MdBusinessHost mdBusinessHost = makeBusinessHost(host, menu);
                businessHostManageService.addMdBusinessHost(mdBusinessHost, false);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            LOG.error("updateBusinessHost: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param hostid
     */
    private void deleteBusinessHost(String hostid) {
        // ??????hostid??????id???????????????????????????
        List<MdBusinessHost> list = businessHostManageService.getMdBusinessHostListByHostId(hostid);
        if (ToolsUtils.ListIsNull(list))
            return;
        // list ????????????
        String[] businessHostidArray = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            businessHostidArray[i] = list.get(i).getId();
        }
        businessHostManageService.deleteMdBusinessHost(businessHostidArray, false);
    }

    private MdBusinessHost makeBusinessHost(MonHost host, MdMenuTree menu) {
        MdBusinessHost mdBusinessHost = new MdBusinessHost();
        mdBusinessHost.setHostid(host.getHostid());
        mdBusinessHost.setName(menu.getName());
        mdBusinessHost.setId(IDGenerateUtil.getUuid());
        return mdBusinessHost;
    }

    private void saveNodes(MonHost monHost) {
        MdNode mdNode = new MdNode();
        mdNode.setNode_name(monHost.getNodeidname());

        nodeManageService.addMdNodeByRestful(mdNode);
    }

    /**
     * 
     * @Title: getAnsibleLog @Description: TODO(??????????????????AnsibleLog) @param @param
     *         monHostList @param @return ?????? @return WebResult ???????????? @throws
     */
    public List<AnsibleLog> getAnsibleLog() {
        List<AnsibleLog> temAnsibleLogList = new ArrayList<AnsibleLog>();
        CopyOnWriteArrayList<AnsibleLog> ansibleLogList = ansibleLogListMap.get(getUsername());
        if (null != ansibleLogList) {
            for (AnsibleLog ansibleLog : ansibleLogList) {
                LOG.info("??????????????????????????????:" + ansibleLog.getRowNo());
                LOG.info("??????????????????????????????:" + ansibleLog.getLogLine());
                LOG.info("????????????????????????????????????:" + ansibleLog.getIsRead());
                if (!ansibleLog.getIsRead()) {
                    temAnsibleLogList.add(ansibleLog);
                    ansibleLog.setIsRead(true);
                    LOG.info("????????????????????????????????????:" + ansibleLog.getRowNo());
                    LOG.info("????????????????????????????????????:" + ansibleLog.getLogLine());
                    LOG.info("??????????????????????????????????????????:" + ansibleLog.getIsRead());
                }
            }
        }
        return temAnsibleLogList;
    }

    /**
     * ??????command????????????????????????????????????
     * 
     * @param command
     * @return
     */
    public void runShellInToMemory(String command) {
        Process process = null;
        try {
            StringTokenizer st = new StringTokenizer(command);
            String[] cmdarray = new String[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                cmdarray[i] = st.nextToken();
            }
            ProcessBuilder builder = new ProcessBuilder(cmdarray);
            builder.redirectErrorStream(true);
            process = builder.start();
            setMemory(process);
            process.waitFor();
        } catch (Exception e) {
            LOG.error("??????{" + command + "}????????????:", e);
        } finally {
            if (process != null) {
                try {
                    process.getInputStream().close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    LOG.error("error:", e);
                }
                try {
                    process.getOutputStream().close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    LOG.error("error:", e);
                }
                try {
                    process.getErrorStream().close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    LOG.error("error:", e);
                }
                process.destroy();
            }
        }
    }

    /**
     * 
     * @Title: setMemory @Description: TODO(????????????) @param @param
     *         process @param @throws IOException ?????? @return void ???????????? @throws
     */
    private void setMemory(Process process) throws IOException {
        BufferedReader input = null;
        try {
            CopyOnWriteArrayList<AnsibleLog> ansibleLogList = new CopyOnWriteArrayList<AnsibleLog>();
            ansibleLogListMap.put(getUsername(), ansibleLogList);
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            // ????????????
            ansibleLogList.clear();
            int rowNo = 0;
            AnsibleLog ansibleLogStart = new AnsibleLog();
            ansibleLogStart.setRowNo(rowNo);
            ansibleLogStart.setIsRead(false);
            String startTime = DateUtil.nowDateString();
            ansibleLogStart.setLogTime(startTime);
            ansibleLogStart.setLogLine(AutoDeployConstant.AUTO_DEPLOY_START);
            ansibleLogList.add(ansibleLogStart);
            rowNo++;
            while ((line = input.readLine()) != null) {
                if (!"".equals(line)) {
                    AnsibleLog ansibleLog = new AnsibleLog();
                    LOG.info("??????????????????????????????:" + rowNo);
                    LOG.info("??????????????????????????????:" + line);
                    ansibleLog.setRowNo(rowNo);
                    ansibleLog.setIsRead(false);
                    ansibleLog.setLogTime(DateUtil.nowDateString());
                    ansibleLog.setLogLine(line);
                    ansibleLogList.add(ansibleLog);
                    rowNo++;
                }
            }
            AnsibleLog ansibleLogEnd = new AnsibleLog();
            ansibleLogEnd.setRowNo(rowNo);
            ansibleLogEnd.setIsRead(false);
            String endTime = DateUtil.nowDateString();
            ansibleLogEnd.setLogTime(endTime);
            String timeDifference = DateUtil.calculateTimeDifferenceByDuration(
                    DateUtil.parseDate(startTime, "yyyy-MM-dd HH:mm:ss"),
                    DateUtil.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"));
            ansibleLogEnd.setLogLine(
                    AutoDeployConstant.AUTO_DEPLOY_END + ",??????????????????????????????" + timeDifference + "??????");
            ansibleLogList.add(ansibleLogEnd);
        } catch (Exception e) {
            LOG.info("??????????????????????????????{}", e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * 
     * @Title: getUsername @Description: (???????????????) @param @return ?????? @return
     *         String ???????????? @throws
     */
    public String getUsername() {
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        return user.getAdmin();
    }

    /**
     * 
     * @Title: ansibleLogRefresh @Description: TODO(????????????) @param @return
     *         ?????? @return WebResult ???????????? @throws
     */
    public WebResult ansibleLogRefresh() {
        CopyOnWriteArrayList<AnsibleLog> ansibleLogList = ansibleLogListMap.get(getUsername());
        if (null != ansibleLogList) {
            for (AnsibleLog ansibleLog : ansibleLogList) {
                ansibleLog.setIsRead(false);
            }
        }
        return new WebResult(true, "??????");
    }

    /**
     * 
     * @Title: deleteMonHost @Description: TODO(????????????????????????) @param @param
     *         hostidArray @param @return ?????? @return WebResult ???????????? @throws
     */
    public WebResult deleteMonHost(String[] hostidArray) {
        try {
            if (!ansibleStartUpState) {
                ansibleStartUpState = true;
                String deploy_batch = IDGenerateUtil.getUuid();
                StringWriter sw = new StringWriter();
                BufferedWriter bw = new BufferedWriter(sw);
                List<MonHost> monHostList = new ArrayList<MonHost>();
                if (hostidArray != null && hostidArray.length > 0) {
                    for (String hostid : hostidArray) {
                        MonHost monHost = monHostDAO.getHostById(hostid);
                        monHostList.add(monHost);
                        bw.write(monHost.getAddr());
                        bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                        bw.write("ansible_ssh_user=");
                        bw.write(monHost.getSsh_user());
                        bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                        bw.write("ansible_ssh_pass=");
                        bw.write(monHost.getSsh_password());
                        bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                        bw.write("ansible_ssh_pass_transform=");
                        bw.write(passwordTransform(monHost.getSsh_password()));
                        bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                        bw.write("ansible_ssh_port=");
                        bw.write(monHost.getSsh_port().toString());
                        bw.write(Constant.ANSIBLE_HOSTS_FILE_SPACE);
                        bw.write("deploy_batch=");
                        bw.write(deploy_batch);
                        bw.newLine();
                    }
                }
                bw.flush();
                String content = sw.getBuffer().toString();
                bw.close();
                sw.close();
                String result = FileUtil.writeFile(ansibleHostsDeleteFileFullPath, content,
                        "UTF-8");
                if ("0".equals(result)) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int rowNo = 0;
                            String startTime = "";
                            Map<String, Object> retCheckMap = runAnsibleCheckShell(
                                    ansibleCheckStartSh);
                            startTime = retCheckMap.get("startTime").toString();
                            Map<String, Object> retDeleteMap = runAnsibleDeleteShell(
                                    ansibleDeleteStartSh, retCheckMap);
                            rowNo = Integer.valueOf(retDeleteMap.get("rowNo").toString());
                            @SuppressWarnings("unchecked")
                            CopyOnWriteArrayList<AnsibleLog> ansibleLogList = (CopyOnWriteArrayList<AnsibleLog>) retDeleteMap
                                    .get("ansibleLogList");
                            List<String> pingErrList = RegexUtil
                                    .getReplaceWithRegex(retCheckMap.get("ret").toString());
                            // ?????????????????????restful??????: ???????????????????????????
                            SimpleChatClient.sendMsg(pingErrList);
                            // ?????????????????????restful??????: ???????????????????????????
                            for (String ip : pingErrList) {
                                removeHostsService(ip, deploy_batch);
                            }
                            List<String> delSuccessList = new LinkedList<String>();
                            List<String> delFailList = new LinkedList<String>();
                            String message = "";
                            List<MdDeployLog> mdDeployLogList = new ArrayList<MdDeployLog>();
                            // ??????????????????????????????????????????,???????????????????????????????????????MdDeployLog?????????
                            int overTime = 0;
                            while (true) {
                                mdDeployLogList = autoDeployDao
                                        .getFindMdDeployLogByDeployBatch(deploy_batch);
                                Boolean isAllIpExist = true;
                                for (MonHost monHost : monHostList) {
                                    Boolean monHostIpExist = false;
                                    for (MdDeployLog mdDeployLog : mdDeployLogList) {
                                        if (monHost.getAddr().equals(mdDeployLog.getIp())) {
                                            monHostIpExist = true;
                                            break;
                                        }
                                    }
                                    if (!monHostIpExist) {
                                        isAllIpExist = false;
                                        break;
                                    }
                                }
                                if (isAllIpExist) {
                                    break;
                                } else {
                                    LOG.info("MdDeployLog??????????????????????????????,????????????" + (overTime * 3)
                                            + "???,???????????????????????????:" + deploy_batch);
                                    try {
                                        Thread.sleep(3000);// ????????????3???
                                    } catch (Exception e) {
                                        LOG.error("????????????????????????????????????", e);
                                    }
                                    overTime++;
                                    // 5????????????????????????
                                    if (overTime > 100) {
                                        String overTimeLogLine = "?????????????????????5???????????????????????????????????????"
                                                + deploy_batch + ",?????????????????????????????????";
                                        AnsibleLog ansibleFailLog = new AnsibleLog();
                                        LOG.info("??????????????????????????????:" + rowNo);
                                        LOG.info("??????????????????????????????:" + overTimeLogLine);
                                        ansibleFailLog.setRowNo(rowNo);
                                        ansibleFailLog.setIsRead(false);
                                        ansibleFailLog.setLogTime(DateUtil.nowDateString());
                                        ansibleFailLog.setLogLine(overTimeLogLine);
                                        ansibleLogList.add(ansibleFailLog);
                                        rowNo++;
                                        break;
                                    }
                                }
                            }
                            for (MdDeployLog mdDeployLog : mdDeployLogList) {
                                if (1 == mdDeployLog.getDeploy_status()) {
                                    delSuccessList.add(mdDeployLog.getIp());
                                } else {
                                    String logLine = mdDeployLog.getIp() + "??????????????????????????????"
                                            + mdDeployLog.getDeploy_des();
                                    AnsibleLog ansibleFailLog = new AnsibleLog();
                                    LOG.info("??????????????????????????????:" + rowNo);
                                    LOG.info("??????????????????????????????:" + logLine);
                                    ansibleFailLog.setRowNo(rowNo);
                                    ansibleFailLog.setIsRead(false);
                                    ansibleFailLog.setLogTime(DateUtil.nowDateString());
                                    ansibleFailLog.setLogLine(logLine);
                                    ansibleLogList.add(ansibleFailLog);
                                    rowNo++;
                                    delFailList.add(mdDeployLog.getIp());
                                }
                            }
                            if (!delSuccessList.isEmpty()) {
                                message = message + delSuccessList.size() + "??????????????????????????????IP???"
                                        + delSuccessList + "???";
                            }
                            if (!delFailList.isEmpty()) {
                                message = message + delFailList.size() + "??????????????????????????????IP???"
                                        + delFailList + "???";
                            }
                            if (!"".equals(message)) {
                                AnsibleLog ansibleStatisticsLog = new AnsibleLog();
                                LOG.info("??????????????????????????????:" + rowNo);
                                LOG.info("??????????????????????????????:" + message);
                                ansibleStatisticsLog.setRowNo(rowNo);
                                ansibleStatisticsLog.setIsRead(false);
                                ansibleStatisticsLog.setLogTime(DateUtil.nowDateString());
                                ansibleStatisticsLog.setLogLine(message);
                                ansibleLogList.add(ansibleStatisticsLog);
                                rowNo++;
                            }
                            AnsibleLog ansibleLogEnd = new AnsibleLog();
                            ansibleLogEnd.setRowNo(rowNo);
                            ansibleLogEnd.setIsRead(false);
                            String endTime = DateUtil.nowDateString();
                            ansibleLogEnd.setLogTime(endTime);
                            String timeDifference = DateUtil.calculateTimeDifferenceByDuration(
                                    DateUtil.parseDate(startTime, "yyyy-MM-dd HH:mm:ss"),
                                    DateUtil.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"));
                            ansibleLogEnd.setLogLine(AutoDeployConstant.AUTO_DEPLOY_END
                                    + ",??????????????????????????????" + timeDifference + "??????");
                            ansibleLogList.add(ansibleLogEnd);
                            // ????????????
                            mdMenuDataListener.loadMenuList();
                            // ??????????????????
                            ansibleStartUpState = false;
                        }
                    });
                    t.start();
                    return new WebResult(true, deploy_batch);
                } else {
                    return new WebResult(false, "ansible???????????????????????????????????????");
                }
            } else {
                return new WebResult(false, "?????????????????????????????????????????????????????????");
            }
        } catch (IOException e) {
            LOG.error("????????????????????????????????????", e);
            return new WebResult(false, "????????????????????????????????????");
        }
    }

    /**
     * 
     * @Title: runAnsibleCheckShell @Description:
     *         TODO(??????AnsibleCheck??????) @param @param command @param @return
     *         ?????? @return String ???????????? @throws
     */
    public Map<String, Object> runAnsibleCheckShell(String command) {
        Process process = null;
        Map<String, Object> retMap = null;
        try {
            StringTokenizer st = new StringTokenizer(command);
            String[] cmdarray = new String[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                cmdarray[i] = st.nextToken();
            }
            ProcessBuilder builder = new ProcessBuilder(cmdarray);
            builder.redirectErrorStream(true);
            process = builder.start();
            retMap = readAnsibleCheckReturn(process);
            process.waitFor();
        } catch (Exception e) {
            LOG.error("??????{" + command + "}????????????:", e);
        } finally {
            if (process != null) {
                try {
                    process.getInputStream().close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    LOG.error("error:", e);
                }
                try {
                    process.getOutputStream().close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    LOG.error("error:", e);
                }
                try {
                    process.getErrorStream().close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    LOG.error("error:", e);
                }
                process.destroy();
            }
        }
        return retMap;
    }

    /**
     * 
     * @Title: readAnsibleCheckReturn @Description:
     *         TODO(AnsibleCheck????????????????????????) @param @param
     *         process @param @return @param @throws IOException ?????? @return
     *         Map<String,Object> ???????????? @throws
     */
    private Map<String, Object> readAnsibleCheckReturn(Process process) throws IOException {
        Map<String, Object> retMap = new HashMap<String, Object>();
        String ret = "";
        BufferedReader input = null;
        try {
            CopyOnWriteArrayList<AnsibleLog> ansibleLogList = new CopyOnWriteArrayList<AnsibleLog>();
            ansibleLogListMap.put(getUsername(), ansibleLogList);
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            // ????????????
            ansibleLogList.clear();
            int rowNo = 0;
            AnsibleLog ansibleLogStart = new AnsibleLog();
            ansibleLogStart.setRowNo(rowNo);
            ansibleLogStart.setIsRead(false);
            String startTime = DateUtil.nowDateString();
            ansibleLogStart.setLogTime(startTime);
            ansibleLogStart.setLogLine(AutoDeployConstant.AUTO_DEPLOY_START);
            ansibleLogList.add(ansibleLogStart);
            rowNo++;
            while ((line = input.readLine()) != null) {
                if (!"".equals(line)) {
                    AnsibleLog ansibleLog = new AnsibleLog();
                    LOG.info("??????????????????????????????:" + rowNo);
                    LOG.info("??????????????????????????????:" + line);
                    ansibleLog.setRowNo(rowNo);
                    ansibleLog.setIsRead(false);
                    ansibleLog.setLogTime(DateUtil.nowDateString());
                    ansibleLog.setLogLine(line);
                    ansibleLogList.add(ansibleLog);
                    rowNo++;
                }
                ret += line;
            }
            retMap.put("ret", ret);
            retMap.put("rowNo", rowNo);
            retMap.put("startTime", startTime);
            retMap.put("ansibleLogList", ansibleLogList);
        } catch (Exception e) {
            LOG.error("??????????????????????????????{}", e);
        } finally {
            IOUtils.closeQuietly(input);
        }
        return retMap;
    }

    /**
     * 
     * @Title: runAnsibleDeleteShell @Description:
     *         TODO(??????AnsibleDelete??????) @param @param command @param @param
     *         map @param @return ?????? @return Map<String,Object> ???????????? @throws
     */
    public Map<String, Object> runAnsibleDeleteShell(String command, Map<String, Object> map) {
        Process process = null;
        Map<String, Object> retMap = null;
        try {
            StringTokenizer st = new StringTokenizer(command);
            String[] cmdarray = new String[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                cmdarray[i] = st.nextToken();
            }
            ProcessBuilder builder = new ProcessBuilder(cmdarray);
            builder.redirectErrorStream(true);
            process = builder.start();
            retMap = readAnsibleDeleteReturn(process, map);
            process.waitFor();
        } catch (Exception e) {
            LOG.error("??????{" + command + "}????????????:", e);
        } finally {
            if (process != null) {
                IOUtils.closeQuietly(process.getInputStream());
                IOUtils.closeQuietly(process.getOutputStream());
                IOUtils.closeQuietly(process.getErrorStream());
                process.destroy();
            }
        }
        return retMap;
    }

    /**
     * 
     * @Title: readAnsibleDeleteReturn @Description:
     *         TODO(AnsibleDelete????????????????????????) @param @param process @param @param
     *         map @param @return @param @throws IOException ?????? @return
     *         Map<String,Object> ???????????? @throws
     */
    private Map<String, Object> readAnsibleDeleteReturn(Process process, Map<String, Object> map)
            throws IOException {
        Map<String, Object> retMap = new HashMap<String, Object>();
        BufferedReader input = null;
        try {
            int rowNo = Integer.valueOf(map.get("rowNo").toString());
            @SuppressWarnings("unchecked")
            CopyOnWriteArrayList<AnsibleLog> ansibleLogList = (CopyOnWriteArrayList<AnsibleLog>) map
                    .get("ansibleLogList");
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                if (!"".equals(line)) {
                    AnsibleLog ansibleLog = new AnsibleLog();
                    LOG.info("??????????????????????????????:" + rowNo);
                    LOG.info("??????????????????????????????:" + line);
                    ansibleLog.setRowNo(rowNo);
                    ansibleLog.setIsRead(false);
                    ansibleLog.setLogTime(DateUtil.nowDateString());
                    ansibleLog.setLogLine(line);
                    ansibleLogList.add(ansibleLog);
                    rowNo++;
                }
            }
            retMap.put("rowNo", rowNo);
            retMap.put("ansibleLogList", ansibleLogList);
        } catch (Exception e) {
            LOG.error("??????????????????,?????????{}", e);
        } finally {
            IOUtils.closeQuietly(input);
        }
        return retMap;
    }

    /**
     * 
     * @Title: passwordTransform @Description:
     *         ??????get????????????????????????????????????????????????\,??????,%,#,&,=????????? ??????????????????URL?????????????????? ????????????????????? +
     *         URL ???+??????????????? %2B ?????? URL?????????????????????+??????????????? %20 / ???????????????????????? %2F ?
     *         ???????????????URL????????? %3F % ?????????????????? %25 # ???????????? %23 & URL ????????????????????????????????? %26 =
     *         URL ????????????????????? %3D @param @param password @param @return ?????? @return
     *         String ???????????? @throws
     */
    private String passwordTransform(String password) {
        try {
            password = password.replaceAll("\\%", "%25");
            password = password.replaceAll("\\+", "%2B");
            password = password.replaceAll("\\ ", "%20");
            password = password.replaceAll("\\/", "%2F");
            password = password.replaceAll("\\?", "%3F");
            password = password.replaceAll("\\#", "%23");
            password = password.replaceAll("\\&", "%26");
            password = password.replaceAll("\\=", "%3D");
            password = password.replaceAll("\\!", "%21");
        } catch (Exception e) {
            LOG.error("passwordTransform error:{}", e);
        }
        return password;
    }
}
