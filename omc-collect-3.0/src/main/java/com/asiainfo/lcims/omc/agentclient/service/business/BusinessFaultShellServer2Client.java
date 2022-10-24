package com.asiainfo.lcims.omc.agentclient.service.business;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.FaultShellResult2Server;
import com.asiainfo.lcims.omc.agentserver.model.FaultShellServer2Client;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.common.OperateState;
import com.asiainfo.lcims.omc.common.ResultType;
import com.asiainfo.lcims.omc.utils.RunShellUtil;

/**
 * 接收到agentServer执行脚本的命令后,执行对应的脚本.
 * 
 */
public class BusinessFaultShellServer2Client extends AbstractClientBusiness<FaultShellServer2Client> {
    private static final Logger log = LoggerFactory.make();
    
    @Override
    protected void doAction(List<FaultShellServer2Client> infoList) {
        //infoList 过滤重复脚本
        List<String> shellList = filterScript(infoList);
        //执行过滤后的脚本
        List<FaultShellResult2Server> shellExecResultList = this.execShell(shellList);
        //根据过滤后的脚本返回信息，组装成过滤前的数据返回
        this.sendInfo2Server(this.mkMsg2Server(infoList, shellExecResultList));
    }
    
    private List<String> filterScript(List<FaultShellServer2Client> infoList){
        List<String> shellList = new ArrayList<String>();
        for (FaultShellServer2Client info : infoList) {
            if(!shellList.contains(info.getShell())){
                shellList.add(info.getShell());
            }
        }
        return shellList;
    }
    
    // 构造返回结果
    private String mkMsg2Server(List<FaultShellServer2Client> infoList, 
            List<FaultShellResult2Server> shellExecResultList) {
        List<FaultShellResult2Server> resultList = new ArrayList<FaultShellResult2Server>();
        
        for (FaultShellServer2Client o : infoList) {
            resultList.add(makeResultToServerInfo(o, shellExecResultList));
        }
        BaseValue<FaultShellResult2Server> baseValue = new BaseValue<FaultShellResult2Server>();
        baseValue.setInfo(resultList);
        baseValue.setMark(mark);
        baseValue.setOptype(OpType.SHELL_FAULT_AGENTCLIENT_SERVER.getType());
        // 返回操作结果给服务端
        String jsonInfo = JSON.toJSONString(baseValue);
        log.info("send shell exe result to server: "+jsonInfo);
        return jsonInfo;
    }
    
    private FaultShellResult2Server makeResultToServerInfo(FaultShellServer2Client o, 
            List<FaultShellResult2Server> shellExecResultList){
        FaultShellResult2Server result2Server = new FaultShellResult2Server();
        result2Server.setId(o.getId());
        result2Server.setAlarmFaultId(o.getAlarmFaultId());
        result2Server.setShell(o.getShell());
        //填默认值
        result2Server.setFaultState(OperateState.FAILED);
        for (FaultShellResult2Server faultShellResult2Server : shellExecResultList) {
            if(o.getShell().equals(faultShellResult2Server.getShell())){
                result2Server.setFaultState(faultShellResult2Server.getFaultState());
                result2Server.setShellResult(faultShellResult2Server.getShellResult());
                break;
            }
        }
        return result2Server;
    }
    
    // 执行脚本
    private List<FaultShellResult2Server> execShell(List<String> shellList) {
        String shell_exec_result = null;
        int resultState = OperateState.SUCCESS;
        List<FaultShellResult2Server> shellResultList = new LinkedList<FaultShellResult2Server>();
        for (String shell : shellList) {
            shell_exec_result = RunShellUtil.runShell(shell);
            if (shell_exec_result == null || shell_exec_result.isEmpty()) {
                shell_exec_result = ResultType.SUCCESS;
            }else{
                // 预留空间： 根据返回值来判断脚本执行情况
                if(shell_exec_result.equals(ResultType.FAILED))
                    resultState = OperateState.FAILED;
            }
            log.info("start exe shell: "+shell + ", result="+shell_exec_result);
            FaultShellResult2Server shellResult2Server = new FaultShellResult2Server();
            shellResult2Server.setShell(shell);
            shellResult2Server.setFaultState(resultState);
            shellResult2Server.setShellResult(shell_exec_result);
            shellResultList.add(shellResult2Server);
        }
        return shellResultList;
    }

}
