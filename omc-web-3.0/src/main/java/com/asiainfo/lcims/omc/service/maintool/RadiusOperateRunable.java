package com.asiainfo.lcims.omc.service.maintool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.maintool.RadiusResp;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.maintool.MainttoolDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ToolsUtils;

public class RadiusOperateRunable implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(RadiusOperateRunable.class);
    
    private MainttoolDAO mainttoolDAO;
    private String uuid;
    private String sendMsg;
    
    public RadiusOperateRunable(MainttoolDAO mainttoolDAO, String uuid, String sendMsg) {
        this.mainttoolDAO = mainttoolDAO;
        this.uuid = uuid;
        this.sendMsg = sendMsg;
    }
    
    @Override
    public void run() {
        String serverIp = CommonInit.BUSCONF.getStringValue("radius_server_ip");
        int serverPort = CommonInit.BUSCONF.getIntValue("radius_server_port", 1);
        try{
            if(ToolsUtils.StringIsNull(serverIp)) {
              // 更新数据中状态为 失败
                updateAllErrorState();
                LOG.error("uuid:[" + uuid + "].radius server ip is not config.");
            } else {
                long start = System.currentTimeMillis();
                LOG.info("uuid:[" + uuid + "].connect to Radius server...");
                RadiusSocketClient socketClient = new RadiusSocketClient(serverIp, serverPort);
                LOG.info("uuid:[" + uuid + "].connected to Radius server success");
                LOG.info("send to AAA:"+sendMsg);
                socketClient.sendMsg2Server(sendMsg);
                String data ="",processData = "";
                String returnValues = null;
                int a,len;
                StringBuilder sb = new StringBuilder();
                long stop = 0;
                long runStart = System.currentTimeMillis();
                //20s内无数据返回，端口服务端连接，状态更新为失败，待完成
                while (stop <= 20) {
                    returnValues = socketClient.getMsgFromServer();
                    if(!ToolsUtils.StringIsNull(sb.toString())) {
                        returnValues = returnValues.replaceAll(sb.toString(), "");
                    }
                    if (returnValues != null && !"".equals(returnValues)) {
                        runStart = System.currentTimeMillis();
                        data = data + returnValues;
                        len = data.length();
                        a = data.lastIndexOf(Constant.RADIUS_OPERATE_END);
                        if(a>0) {
                            processData = data.substring(0, a);
                            sb.append(data.substring(0, a+1).replace("|", "\\|"));
                            if(len>(a+1)) {
                                data = data.substring(a+1, len);
                            } else {
                                data = "";
                            }
                            LOG.info("uuid:[" + uuid + "].data received ["+processData+"].");
                            processReceiveData(processData);
                        } else {
                            processData = data;
                        }
                        
                        if (processData.contains(Constant.END_SOCKET) || data.contains(Constant.END_SOCKET) ) {
                            LOG.info("uuid:[" + uuid + "].data received ["+processData+"].");
                            processReceiveData(processData);
                            LOG.info("uuid:[" + uuid + "].data received success.");
                            break;
                        }
                    } else {
                        stop = (System.currentTimeMillis() - runStart)/1000;
                    }
                }
                
                if(stop > 20) {
                    //20s内无数据，状态更新为失败
                    updateAllErrorState();
                }
                socketClient.close();
                long time = System.currentTimeMillis() - start;
                LOG.info("uuid:[" + uuid + "].use time is: "+time/1000.0 + " second");
            }
            
        } catch (Exception e) {
            updateAllErrorState();
            LOG.error("Exception: ", e);
        } 
        
    }
    
    private void updateAllErrorState() {
        mainttoolDAO.updateRadiusOperateStateByUuid(uuid, 1);
    }
    
    private void processReceiveData(String data) {
        if(!ToolsUtils.StringIsNull(data)) {
            data = data.trim();
            String [] arr = data.split(Constant.RADIUS_OPERATE_END);
            for (String info : arr) {
                LOG.info("omc process data:["+info+"]");
                if(!info.equals(Constant.END_SOCKET)) {
                    RadiusResp resp = RadiusResp.createRadiusResp(info);
                    mainttoolDAO.updateRadiusOperateStateByUuidAndHostip(resp.getUuid(), resp.getHost_ip(), resp.getOperate_state());
                }
            }
        }
    }
}
