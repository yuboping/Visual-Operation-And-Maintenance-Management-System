package com.asiainfo.lcims.omc.service.configmanage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.exception.OmcSocketException;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdHostProcess;
import com.asiainfo.lcims.omc.model.configmanage.MdProcess;
import com.asiainfo.lcims.omc.model.configmanage.ProcessOperate;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.HostMetricDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdHostProcessDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MdProcessDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.ProcessOperateDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.socket.SimpleChatClient;
import com.asiainfo.lcims.omc.socket.common.SendMsgToServer;
import com.asiainfo.lcims.omc.socket.model.ShellWeb2Server;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.page.Page;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "hostProcessManageService")
public class HostProcessManageService {

    private static final Logger LOG = LoggerFactory.getLogger(HostProcessManageService.class);

    @Inject
    private MdHostProcessDAO mdHostProcessDAO;

    @Inject
    private ProcessOperateDAO processOperateDAO;

    @Inject
    private MonHostDAO hostDAO;

    @Inject
    private MdProcessDAO mdProcessDAO;

    @Inject
    private HostMetricDAO hostMetricDAO;

    @Autowired
    private CommonInit commonInit;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    /**
     * ???????????????????????????????????????????????????
     * @param mdHostProcess
     * @return
     */
    public Page getMdHostProcessList(MdHostProcess mdHostProcess, int pageNumber) {
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        //????????????
        int totalCount = mdHostProcessDAO.getHostProcessInfoCount(mdHostProcess);
        page.setTotalCount(totalCount);
        //??????????????????
        if(totalCount>0){
            List<MdHostProcess> data = mdHostProcessDAO.getHostProcessList(mdHostProcess, page);
            page.setPageList(data);
        }

        return page;
    }

    public MdHostProcess getMdHostProcessInfo(String id){
        return mdHostProcessDAO.getHostProcessInfo(id);
    }

    public WebResult deleteInfo(String[] hostProcessArray) {
        int deleteSuccess = 0;
        int deleteFail = 0;
        String logDesc = "";
        try {
            sendMetricToClient(hostProcessArray);
            if (hostProcessArray != null && hostProcessArray.length != 0) {
                for (String id : hostProcessArray) {
                    MdHostProcess mdHostProcess = mdHostProcessDAO.getHostProcessInfo(id);
                    logDesc = logDesc + "[??????:" + mdHostProcess.getHost_ip() + ",??????:" +mdHostProcess.getProcess_name() + "],";
                    int deleteResult = mdHostProcessDAO.deleteById(id);
                    if (1 == deleteResult) {
                        deleteSuccess++;
                    } else {
                        deleteFail++;
                    }
                }
            }
        }catch (Exception e){
            LOG.info("????????????!" + e.getMessage(), e);
            return new WebResult(false, "????????????!");
        }
        String message = "???????????????" + deleteSuccess + "?????????????????????????????????" + deleteFail
                + "?????????????????????????????????????????????????????????????????????";
        //??????????????????
        logDesc = logDesc.substring(0,logDesc.length()-1);
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "????????????" + logDesc);
        return new WebResult(true, message);
    }

    /**
     * ??????????????????????????????
     * @param mdHostProcess
     * @return
     */
    public WebResult modifyHostProcess(MdHostProcess mdHostProcess) {
        LOG.info("????????????----modifyHostMetric start");
        WebResult result = new WebResult(true,"????????????");
        try {
            MdHostProcess trans = mdHostProcessDAO.getHostProcessInfo(mdHostProcess.getId());
            //??????
            mdHostProcessDAO.modifyHostProcess(mdHostProcess);

            //??????????????????
            operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "????????????[??????:" +
                    trans.getHost_ip() + ",??????:" + trans.getProcess_name() + ",????????????:" + trans.getStart_script() + "-->" +
                    mdHostProcess.getStart_script() + ",????????????:" + trans.getStop_script() + "-->" + mdHostProcess.getStop_script() +
                    ",??????:" + trans.getDescription() + "-->" + mdHostProcess.getDescription() + "]");
        }catch (Exception e){
            LOG.info(e.getMessage(), e);
            result = new WebResult(false, "???????????????");
        }
        return result;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     * @param hostProcessArray
     * @return
     */
    public WebResult excuteShellToClient(String[] hostProcessArray, String scriptType) {
        List<ShellWeb2Server> paramList = new LinkedList<>();

        //list?????????????????????id
        List<String> poList = new ArrayList<String>();
        String il = "";
        String result = null;
        String excuteShell = "";
        if (hostProcessArray != null && hostProcessArray.length != 0) {
            for (String id : hostProcessArray) {
                //??????idlist???????????????????????????
                il += "'" + id + "',";
                MdHostProcess mdHostProcess = mdHostProcessDAO.getHostProcessInfo(id);
                ProcessOperate processOperate = new ProcessOperate();
                processOperate.setId(IDGenerateUtil.getUuid());
                processOperate.setOperate_id(mdHostProcess.getId());
                processOperate.setHost_ip(mdHostProcess.getHost_ip());
                excuteShell = excuteShell + "[??????:" + mdHostProcess.getHost_ip() +
                        ",??????:" + mdHostProcess.getProcess_name() + ",??????:";
                processOperate.setProcess_name(mdHostProcess.getProcess_name());
                if(ConstantUtill.START_SCRIPT_TYPE == Integer.valueOf(scriptType)){
                    processOperate.setProcess_script(mdHostProcess.getStart_script());
                    processOperate.setScript_type(ConstantUtill.START_SCRIPT_TYPE);
                    excuteShell = excuteShell + mdHostProcess.getStart_script() + "],";
                }else{
                    processOperate.setProcess_script(mdHostProcess.getStop_script());
                    processOperate.setScript_type(ConstantUtill.STOP_SCRIPT_TYPE);
                    excuteShell = excuteShell + mdHostProcess.getStop_script() + "],";
                }

                processOperate.setOperate_state(ConstantUtill.PROCESS_START);
                processOperate.setUpdate_time(DateTools.getCurrentFormatDate());
                processOperate.setCreate_time(DateTools.getCurrentFormatDate());
                processOperateDAO.insertProcessOpereate(processOperate);

                //????????????id??????list???
                poList.add(processOperate.getId());

                //???????????????hostipList???
                ShellWeb2Server sw = new ShellWeb2Server();
                sw.setId(processOperate.getId());
                paramList.add(sw);
            }
            il = il.substring(0, il.lastIndexOf(","));
        }
        try {
            //??????????????????
            LOG.info("??????????????????????????????----start");
            result = SendMsgToServer.sendMsg(paramList);
            LOG.info("??????????????????????????????----end:");
        } catch (OmcSocketException e) {
            //???????????????????????????OmcSocketException
            LOG.info(e.getErrorMsg(), e);
            return new WebResult(ConstantUtill.BOOLEAN_FALSE, e.getErrorMsg());
        } catch (Exception e) {
            for (String id : poList) {
                ProcessOperate processOperate = new ProcessOperate();
                processOperate.setId(id);
                processOperate.setOperate_state(ConstantUtill.PROCESS_FAILURE);
                processOperate.setOperate_result(ConstantUtill.PROCESS_RESULT_FAILURE);
                processOperateDAO.modifyProcessOperate(processOperate);
            }
            LOG.info(e.getMessage(), e);
            return new WebResult(ConstantUtill.BOOLEAN_FALSE, e.getMessage());
        }

        //??????????????????
        excuteShell = excuteShell.substring(0,excuteShell.length()-1);
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "????????????:" + excuteShell);
        return new WebResult(ConstantUtill.BOOLEAN_TRUE, result, il);
    }

    public List<MdHostProcess> getMdProcessListByProcess(String operateid) {
        //??????????????????
        MdProcess mdProcess = new MdProcess();
        mdProcess.setProcess_id(operateid);
        List<MdProcess> mdProcessList = mdProcessDAO.getMdProcess(mdProcess);
        return mdHostProcessDAO.getMdProcessListByProcess(mdProcessList.get(0));
    }

    public List<MdHostProcess> getMdProcessListByHost(String operateid) {
        //??????????????????
        MonHost host = hostDAO.getHostById(operateid);
        return mdHostProcessDAO.getMdProcessListByHost(host);
    }

    public WebResult bindOperate(String datas) {
        //json???????????????
        ObjectMapper mapper = new ObjectMapper();
        List<MdHostProcess> list = null;
        String idlist = "";
        String logDesc = "";
        WebResult result = new  WebResult(true,"??????????????????");
        try {
            list = mapper.readValue(datas, new TypeReference<List<MdHostProcess>>() {});
            List<MdProcess> mdProcessList = CommonInit.getMdProcessList();
            List<MonHost> monHostList = CommonInit.getMonHostList();
            for (MdHostProcess mdHostProcess : list) {
                mdHostProcess.setId(IDGenerateUtil.getUuid());
                idlist += mdHostProcess.getId() + ",";
                for(MonHost monHost: monHostList){
                    if(mdHostProcess.getHost_id().equals(monHost.getHostid())){
                        logDesc = logDesc + "[??????:" + monHost.getAddr() + ",";
                    }
                }
                for(MdProcess mdProcess: mdProcessList){
                    if(mdHostProcess.getProcess_id().equals(mdProcess.getProcess_id())){
                        logDesc = logDesc + "??????:" + mdProcess.getProcess_name() + "],";
                    }
                }
            }

            mdHostProcessDAO.insertHostProcessList(list);
            //????????????
            idlist = idlist.substring(0,idlist.length() - 1);
            String[] hostProcessArray = idlist.split(",");
            sendMetricToClient(hostProcessArray);
            //??????????????????
            logDesc = logDesc.substring(0,logDesc.length()-1);
            operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "????????????:"+ logDesc);
        } catch (JsonParseException e) {
            LOG.info(e.getMessage(), e);
            result = new  WebResult(false,"??????????????????");
        } catch (JsonMappingException e) {
            LOG.info(e.getMessage(), e);
            result = new  WebResult(false,"??????????????????");
        } catch (IOException e) {
            LOG.info(e.getMessage(), e);
            result = new  WebResult(false,"??????????????????");
        }
        return result;
    }

    /**
     * ???????????????????????????
     * @param hostProcessArray
     * @return
     */
    public WebResult sendMetricToClient(String[] hostProcessArray) {
        String msg =  "";
        WebResult result = new  WebResult(true,"????????????");
        try {
            //??????hostMetricids ???iplist??????????????????ip??????
            List<String> ipList = mdHostProcessDAO.getHostProcessIpList(hostProcessArray);
            //??????????????????
            LOG.info(msg+"??????????????????----start");
            SimpleChatClient.sendMsg(ipList);
            LOG.info(msg+"??????????????????----end:");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            result.setOpSucc(false);
            result.setMessage("????????????");
        }
        return result;
    }
}
