package com.asiainfo.lcims.omc.agentclient.service.business;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.ShellResult2Server;
import com.asiainfo.lcims.omc.agentserver.model.ShellServer2Client;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.common.ResultType;
import com.asiainfo.lcims.omc.utils.RunShellUtil;

/**
 * 接收到agentServer执行脚本的命令后,执行对应的脚本.
 * <p>
 * 如果脚本有返回结果则将返回结果记录返回,如果脚本没有返回结果,则根据是否正常执行脚本返回成功、失败标识.
 * 
 * @author XHT
 *
 */
public class BusinessShellServer2Client extends AbstractClientBusiness<ShellServer2Client> {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessShellServer2Client.class);

    @Override
    protected void doAction(List<ShellServer2Client> infoList) {
        List<ShellResult2Server> shellExecResultList = this.execShell(infoList);
        this.sendInfo2Server(this.mkMsg2Server(shellExecResultList));
    }

    // 构造返回结果
    private String mkMsg2Server(List<ShellResult2Server> shellExecResultList) {
        BaseValue<ShellResult2Server> baseValue = new BaseValue<ShellResult2Server>();
        baseValue.setInfo(shellExecResultList);
        baseValue.setMark(mark);
        baseValue.setOptype(OpType.SHELL_AGENTCLIENT_SERVER.getType());
        // 返回操作结果给服务端
        String jsonInfo = JSON.toJSONString(baseValue);
        return jsonInfo;
    }

    // 执行脚本
    private List<ShellResult2Server> execShell(List<ShellServer2Client> infoList) {
        String shell_exec_result = null;
        List<ShellResult2Server> shellResultList = new LinkedList<ShellResult2Server>();

        for (ShellServer2Client shell : infoList) {
            String shellScript = shell.getShell();
            LOG.info("shell script is : {}", shellScript);
            shell_exec_result = RunShellUtil.exec(shellScript);
            LOG.info("shell script result is : {}", shell_exec_result);
            if (shell_exec_result == null || shell_exec_result.isEmpty()) {
                shell_exec_result = ResultType.SUCCESS;
            }
            ShellResult2Server shellResult2Server = new ShellResult2Server();
            shellResult2Server.setId(shell.getId());
            if (StringUtils.length(shell_exec_result) >= 512) {
                shell_exec_result = StringUtils.substring(shell_exec_result, 0, 256);
            }
            shellResult2Server.setShellResult(shell_exec_result);
            shellResultList.add(shellResult2Server);
        }
        return shellResultList;
    }

}
