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
     * 根据查询条件查询主机进程关联关系表
     * @param mdHostProcess
     * @return
     */
    public Page getMdHostProcessList(MdHostProcess mdHostProcess, int pageNumber) {
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        //查询总数
        int totalCount = mdHostProcessDAO.getHostProcessInfoCount(mdHostProcess);
        page.setTotalCount(totalCount);
        //查询分页数据
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
                    logDesc = logDesc + "[主机:" + mdHostProcess.getHost_ip() + ",进程:" +mdHostProcess.getProcess_name() + "],";
                    int deleteResult = mdHostProcessDAO.deleteById(id);
                    if (1 == deleteResult) {
                        deleteSuccess++;
                    } else {
                        deleteFail++;
                    }
                }
            }
        }catch (Exception e){
            LOG.info("删除失败!" + e.getMessage(), e);
            return new WebResult(false, "删除失败!");
        }
        String message = "删除成功：" + deleteSuccess + "条指标类型，删除失败：" + deleteFail
                + "条指标类型。如果指标删除失败请先解绑对应指标。";
        //用户日志记录
        logDesc = logDesc.substring(0,logDesc.length()-1);
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "删除数据" + logDesc);
        return new WebResult(true, message);
    }

    /**
     * 修改主机进程关联信息
     * @param mdHostProcess
     * @return
     */
    public WebResult modifyHostProcess(MdHostProcess mdHostProcess) {
        LOG.info("修改操作----modifyHostMetric start");
        WebResult result = new WebResult(true,"操作成功");
        try {
            MdHostProcess trans = mdHostProcessDAO.getHostProcessInfo(mdHostProcess.getId());
            //修改
            mdHostProcessDAO.modifyHostProcess(mdHostProcess);

            //用户日志记录
            operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "修改数据[主机:" +
                    trans.getHost_ip() + ",进程:" + trans.getProcess_name() + ",启动脚本:" + trans.getStart_script() + "-->" +
                    mdHostProcess.getStart_script() + ",停止脚本:" + trans.getStop_script() + "-->" + mdHostProcess.getStop_script() +
                    ",描述:" + trans.getDescription() + "-->" + mdHostProcess.getDescription() + "]");
        }catch (Exception e){
            LOG.info(e.getMessage(), e);
            result = new WebResult(false, "修改失败！");
        }
        return result;
    }

    /**
     * 执行脚本，记录操作信息，若失败则更新脚本状态为失败
     * @param hostProcessArray
     * @return
     */
    public WebResult excuteShellToClient(String[] hostProcessArray, String scriptType) {
        List<ShellWeb2Server> paramList = new LinkedList<>();

        //list存放执行操作表id
        List<String> poList = new ArrayList<String>();
        String il = "";
        String result = null;
        String excuteShell = "";
        if (hostProcessArray != null && hostProcessArray.length != 0) {
            for (String id : hostProcessArray) {
                //拼接idlist，用于查询最新状态
                il += "'" + id + "',";
                MdHostProcess mdHostProcess = mdHostProcessDAO.getHostProcessInfo(id);
                ProcessOperate processOperate = new ProcessOperate();
                processOperate.setId(IDGenerateUtil.getUuid());
                processOperate.setOperate_id(mdHostProcess.getId());
                processOperate.setHost_ip(mdHostProcess.getHost_ip());
                excuteShell = excuteShell + "[主机:" + mdHostProcess.getHost_ip() +
                        ",进程:" + mdHostProcess.getProcess_name() + ",脚本:";
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

                //将操作表id房里list中
                poList.add(processOperate.getId());

                //将数据放入hostipList中
                ShellWeb2Server sw = new ShellWeb2Server();
                sw.setId(processOperate.getId());
                paramList.add(sw);
            }
            il = il.substring(0, il.lastIndexOf(","));
        }
        try {
            //调用下发接口
            LOG.info("执行脚本调用下发接口----start");
            result = SendMsgToServer.sendMsg(paramList);
            LOG.info("执行脚本调用下发接口----end:");
        } catch (OmcSocketException e) {
            //自定义运行时异常类OmcSocketException
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

        //用户日志记录
        excuteShell = excuteShell.substring(0,excuteShell.length()-1);
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "执行脚本:" + excuteShell);
        return new WebResult(ConstantUtill.BOOLEAN_TRUE, result, il);
    }

    public List<MdHostProcess> getMdProcessListByProcess(String operateid) {
        //获取进程信息
        MdProcess mdProcess = new MdProcess();
        mdProcess.setProcess_id(operateid);
        List<MdProcess> mdProcessList = mdProcessDAO.getMdProcess(mdProcess);
        return mdHostProcessDAO.getMdProcessListByProcess(mdProcessList.get(0));
    }

    public List<MdHostProcess> getMdProcessListByHost(String operateid) {
        //获取主机信息
        MonHost host = hostDAO.getHostById(operateid);
        return mdHostProcessDAO.getMdProcessListByHost(host);
    }

    public WebResult bindOperate(String datas) {
        //json转换实体类
        ObjectMapper mapper = new ObjectMapper();
        List<MdHostProcess> list = null;
        String idlist = "";
        String logDesc = "";
        WebResult result = new  WebResult(true,"绑定操作成功");
        try {
            list = mapper.readValue(datas, new TypeReference<List<MdHostProcess>>() {});
            List<MdProcess> mdProcessList = CommonInit.getMdProcessList();
            List<MonHost> monHostList = CommonInit.getMonHostList();
            for (MdHostProcess mdHostProcess : list) {
                mdHostProcess.setId(IDGenerateUtil.getUuid());
                idlist += mdHostProcess.getId() + ",";
                for(MonHost monHost: monHostList){
                    if(mdHostProcess.getHost_id().equals(monHost.getHostid())){
                        logDesc = logDesc + "[主机:" + monHost.getAddr() + ",";
                    }
                }
                for(MdProcess mdProcess: mdProcessList){
                    if(mdHostProcess.getProcess_id().equals(mdProcess.getProcess_id())){
                        logDesc = logDesc + "进程:" + mdProcess.getProcess_name() + "],";
                    }
                }
            }

            mdHostProcessDAO.insertHostProcessList(list);
            //下发指标
            idlist = idlist.substring(0,idlist.length() - 1);
            String[] hostProcessArray = idlist.split(",");
            sendMetricToClient(hostProcessArray);
            //用户日志记录
            logDesc = logDesc.substring(0,logDesc.length()-1);
            operateHisService.insertOperateHistory( Constant.OPERATE_HIS_HOST_PROCESS, "绑定数据:"+ logDesc);
        } catch (JsonParseException e) {
            LOG.info(e.getMessage(), e);
            result = new  WebResult(false,"绑定操作失败");
        } catch (JsonMappingException e) {
            LOG.info(e.getMessage(), e);
            result = new  WebResult(false,"绑定操作失败");
        } catch (IOException e) {
            LOG.info(e.getMessage(), e);
            result = new  WebResult(false,"绑定操作失败");
        }
        return result;
    }

    /**
     * 下发指标信息到主机
     * @param hostProcessArray
     * @return
     */
    public WebResult sendMetricToClient(String[] hostProcessArray) {
        String msg =  "";
        WebResult result = new  WebResult(true,"操作成功");
        try {
            //查询hostMetricids 的iplist信息，做过滤ip操作
            List<String> ipList = mdHostProcessDAO.getHostProcessIpList(hostProcessArray);
            //调用下发接口
            LOG.info(msg+"调用下发接口----start");
            SimpleChatClient.sendMsg(ipList);
            LOG.info(msg+"调用下发接口----end:");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            result.setOpSucc(false);
            result.setMessage("操作失败");
        }
        return result;
    }
}
