package com.asiainfo.lcims.omc.agentserver.service.business;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.agentserver.dao.RadiusBusinessDao;
import com.asiainfo.lcims.omc.agentserver.enity.RadiusReq;
import com.asiainfo.lcims.omc.agentserver.model.BaseValue;
import com.asiainfo.lcims.omc.agentserver.model.RadiusInfoWeb2Server;
import com.asiainfo.lcims.omc.agentserver.model.Result;
import com.asiainfo.lcims.omc.common.OpType;
import com.asiainfo.lcims.omc.common.ResultType;
import com.asiainfo.lcims.omc.conf.InitParam;
import com.asiainfo.lcims.omc.utils.ToolsUtils;

import io.netty.channel.Channel;

public class BusinessRadiusWeb2Server extends AbstractServerBusiness<RadiusInfoWeb2Server> {
    private static final Logger log = LoggerFactory.make();
    private static String OPERATE_SPLIT = "\\|\\|\\|";
    private static String END_WITH_SUFFIX = "\n";
    private static int SUCESS = 0;
    private static int ERROR = 1;
    private static int RUNNING = 2;
    @Override
    protected void doAction(List<RadiusInfoWeb2Server> infoList, Channel channel) {
        String msg = mkResultForWeb(ResultType.SUCCESS);
        String radiusIp = InitParam.getSystemConf().getRadiusServerIp();
        int radiusPort = InitParam.getSystemConf().getRadiusServerPort();
        Socket socket = null;
        PrintWriter write = null;
        List<RadiusReq> reqList = new ArrayList<RadiusReq>();
        RadiusBusinessDao radiusBusinessDao = new RadiusBusinessDao();
        try {
            for (RadiusInfoWeb2Server info : infoList) {
                RadiusReq req = RadiusReq.createRadiusReq(info.getInfo());
                reqList.add(req);
            }
            if(ToolsUtils.StringIsNull(radiusIp)) {
                msg = mkResultForWeb(ResultType.FAILED);
                // 更新数据中状态为 失败
                updateAllErrorState(reqList, radiusBusinessDao);
                log.error("mark:[" + mark + "].radius server ip is null.");
            } else {
                for (RadiusReq req : reqList) {
                    // info样例： 1|||874b36b1-6e6d-4d66-8aba-16ec49599ef9|||1.1.1.1;2.2.2.2;3.3.3.3;4.4.4.4\n
                    //调用Radius服务端 socket 接口
                    try{
                        socket = new Socket("127.0.0.1", 6666);
                        write = new PrintWriter(socket.getOutputStream());
                        write.println(req.getInfo()+END_WITH_SUFFIX);
                        write.flush();
                        log.info("mark:[" + mark + "].send to radius server msg["+req.getInfo()+"].");
                    } finally {
                        if (socket != null){
                            socket.close();
                        }
                    }

                }
            }
        } catch (Exception e) {
            msg = mkResultForWeb(ResultType.FAILED);
            // 更新数据中状态为 失败
            log.error("mark:[" + mark + "].radius server["+radiusIp+":"+radiusPort+"] is not connected.");
            // 更新数据中状态为 失败
            updateAllErrorState(reqList, radiusBusinessDao);
        }

        // 非采集客户端的通信通道,所以需要用传入进来的当前正在通信的通道进行通话.
        channel.writeAndFlush(msg);
        log.info("mark:[" + mark + "].has returned msg to client.");
    }
    
    private void updateAllErrorState(List<RadiusReq> reqList, RadiusBusinessDao radiusBusinessDao) {
        for (RadiusReq radiusReq : reqList) {
            radiusBusinessDao.updateStateByUuid(radiusReq.getUuid(), ERROR);
        }
    }

    private String mkResultForWeb(String resultType) {
        // 向调用方返回操作结果
        BaseValue<Result> resp = new BaseValue<Result>();
        List<Result> res_list = new LinkedList<Result>();
        Result result = new Result();
        result.setResult(resultType);
        res_list.add(result);
        resp.setInfo(res_list);
        resp.setMark(mark);
        resp.setOptype(OpType.RADIUS_OPERATE_AGENTSERVER_WEB.getType());
        String msg = JSON.toJSONString(resp) + InitParam.getDeLimiter();
        return msg;
    }
}
