package com.asiainfo.lcims.omc.analogdialup;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.password.PwdDES3;
import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.analogdialup.AnalogDialUp;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.analogdialup.AnalogDialUpDAO;
import com.asiainfo.lcims.omc.socket.SimpleChatClient;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DateUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.UUIDBits;;

public class AnalogDialUpJob implements Job {

    private static final Logger logger = LoggerFactory.make();

    private static final String COMMAND_NAME = "SimuDialUp";

    private static final String BLANK_SPACE = " ";

    // private static final String SOCKETCLIENT_CHARSET = "UTF-8";

    public static final String DELIMITERSTR = "\n";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String nowTime = DateUtil.nowDateString();
        AnalogDialUpDAO analogDialUpDAO = (AnalogDialUpDAO) context.getMergedJobDataMap()
                .get("analogDialUpDAO");
        AnalogDialUp analogDialUpResult = new AnalogDialUp();
        BusinessConf conf = CommonInit.BUSCONF;
        String serialno = UUIDBits.getUUIDBitsNoSpecial(6);
        AnalogDialUp analogDialUp = (AnalogDialUp) context.getMergedJobDataMap()
                .get("analogDialUp");
        String analogDialUpId = analogDialUp.getId();
        try {
            analogDialUp.setCommand_name(COMMAND_NAME);
            analogDialUp.setSerialno(serialno);
            logger.info("------start AnalogDialUpJob excute nowTime : " + nowTime
                    + ";analogDialUpId : " + analogDialUpId + ";serialno : " + serialno + "------");
            String host = analogDialUp.getHost_ip();
            List<String> sendMsgList = new ArrayList<String>();
            sendMsgList.add(analogDialUp.getCommand_name());
            sendMsgList.add(addBrackets(serialno));
            if (analogDialUp.getUsername() != null && analogDialUp.getUsername().length() != 0) {
                analogDialUpResult.setUsername(analogDialUp.getUsername());
                sendMsgList.add(addBrackets(analogDialUp.getUsername()));
            } else {
                analogDialUpResult.setUsername(conf.getAnalogDialUpUsername());
                sendMsgList.add(addBrackets(conf.getAnalogDialUpUsername()));
            }
            if (analogDialUp.getPassword() != null && analogDialUp.getPassword().length() != 0) {
                analogDialUpResult.setPassword(analogDialUp.getPassword());
                // 密码解密去盐
                PwdDES3 pwd = new PwdDES3();
                String decryptPassword = pwd.decryptPassword(analogDialUp.getPassword());
                String password = decryptPassword.substring(0,
                        decryptPassword.length() - Constant.PASSWORD_SALT.length());
                sendMsgList.add(addBrackets(password));
            } else {
                // 密码加盐加密
                PwdDES3 pwd = new PwdDES3();
                String password = conf.getAnalogDialUpPassword() + Constant.PASSWORD_SALT;
                String encryptPassword = pwd.encryptPassword(password);
                analogDialUpResult.setPassword(encryptPassword);
                sendMsgList.add(addBrackets(conf.getAnalogDialUpPassword()));
            }
            int port;
            if (analogDialUp.getNas_port() != null && analogDialUp.getNas_port().length() != 0) {
                port = Integer.parseInt(analogDialUp.getNas_port());
                analogDialUpResult.setNas_port(analogDialUp.getNas_port());
                sendMsgList.add(addBrackets(analogDialUp.getNas_port()));
            } else {
                port = Integer.parseInt(conf.getAnalogDialUpNasPort());
                analogDialUpResult.setNas_port(conf.getAnalogDialUpNasPort());
                sendMsgList.add(addBrackets(conf.getAnalogDialUpNasPort()));
            }
            if (analogDialUp.getCall_from_id() != null
                    && analogDialUp.getCall_from_id().length() != 0) {
                analogDialUpResult.setCall_from_id(analogDialUp.getCall_from_id());
                sendMsgList.add(addBrackets(analogDialUp.getCall_from_id()));
            } else {
                analogDialUpResult.setCall_from_id(conf.getAnalogDialUpCallFromId());
                sendMsgList.add(addBrackets(conf.getAnalogDialUpCallFromId()));
            }
            if (analogDialUp.getCall_to_id() != null
                    && analogDialUp.getCall_to_id().length() != 0) {
                analogDialUpResult.setCall_to_id(analogDialUp.getCall_to_id());
                sendMsgList.add(addBrackets(analogDialUp.getCall_to_id()));
            } else {
                analogDialUpResult.setCall_to_id(conf.getAnalogDialUpCallToId());
                sendMsgList.add(addBrackets(conf.getAnalogDialUpCallToId()));
            }
            if (analogDialUp.getExt() != null && analogDialUp.getExt().length() != 0) {
                analogDialUpResult.setExt(analogDialUp.getExt());
                sendMsgList.add(addBrackets(analogDialUp.getExt()));
            } else {
                analogDialUpResult.setExt(conf.getAnalogDialUpExt());
                sendMsgList.add(addBrackets(conf.getAnalogDialUpExt()));
            }
            // 例子 SimuDialUp 1 test1 aaabbb 8888 12345678 163
            // SimuDialUp {hIc7Ik} {0931test_001} {123456} {37788} {263} {263}
            // {}
            String sendMsg = String.join(BLANK_SPACE, sendMsgList);
            // SocketClient client = new SocketClient(host, port);
            SimpleChatClient simpleChatClient = new SimpleChatClient(host, port);
            // 本地测试服务器
            // SocketClient client = new SocketClient("127.0.0.1", 8888);
            // SimpleChatClient simpleChatClient = new
            // SimpleChatClient("10.1.198.83", 37788);
            logger.info("AnalogDialUpJob excute nowTime : " + nowTime + ";analogDialUpId : "
                    + analogDialUpId + ";serialno : " + serialno + ";host : " + host + ";port : "
                    + port + ";sendMsg : " + sendMsg + ".");
            long startTime = System.nanoTime();
            // 调用模拟拨测接口
            // String resultMsg = client.sendMsg(sendMsg, SOCKETCLIENT_CHARSET,
            // DELIMITERSTR);
            String resultMsg = simpleChatClient.sendMsg(sendMsg, DELIMITERSTR);
            logger.info("AnalogDialUpJob excute nowTime : " + nowTime + ";analogDialUpId : "
                    + analogDialUpId + ";serialno : " + serialno + ";host : " + host + ";port : "
                    + port + ";sendMsg : " + sendMsg + ";resultMsg : " + resultMsg + ".");
            long endTime = System.nanoTime();
            long executeTime = endTime - startTime;
            double seconds = (double) executeTime / 1000000000.0;
            seconds = (double) Math.round(seconds * 10000) / 10000;
            String callTime = String.valueOf(seconds);
            // 测试例子
            // resultMsg = "{" + serialno + "} {ERROR} {err}";
            if (null != resultMsg) {
                String result_serialno = "";
                String result_returncode = "";
                String result_errordesc = "";
                List<String> resultMsgList = GetResultSplit(resultMsg);
                if (resultMsgList.size() > 0 && resultMsgList.get(0).length() > 2) {
                    result_serialno = resultMsgList.get(0);
                }
                if (resultMsgList.size() > 1 && resultMsgList.get(1).length() > 2) {
                    result_returncode = resultMsgList.get(1);
                }
                if (resultMsgList.size() > 2 && resultMsgList.get(2).length() > 2) {
                    result_errordesc = resultMsgList.get(2);
                }
                logger.info("AnalogDialUpJob excute nowTime : " + nowTime + ";analogDialUpId : "
                        + analogDialUpId + ";serialno : " + serialno + ";host : " + host
                        + ";port : " + port + ";resultMsg : " + resultMsg + ";result_serialno : "
                        + result_serialno + ";result_returncode : " + result_returncode
                        + ";result_errordesc : " + result_errordesc + ";callTime : " + callTime
                        + ".");
                analogDialUpResult.setId(IDGenerateUtil.getUuid());
                analogDialUpResult.setHost_id(analogDialUp.getHost_id());
                analogDialUpResult.setHost_ip(analogDialUp.getHost_ip());
                analogDialUpResult.setAnalog_dial_up_id(analogDialUp.getId());
                analogDialUpResult.setCron_erp(analogDialUp.getCron_erp());
                analogDialUpResult.setDial_up_time(nowTime);
                analogDialUpResult.setCommand_name(analogDialUp.getCommand_name());
                analogDialUpResult.setSerialno(analogDialUp.getSerialno());
                analogDialUpResult.setReturncode(result_returncode);
                analogDialUpResult.setErrordesc(result_errordesc);
                analogDialUpResult.setCalltime(callTime);
                analogDialUpResult.setMetric_id(conf.getAnalogDialUpMetricId());
                int addResult = analogDialUpDAO.insertResult(analogDialUpResult);
                if (1 == addResult) {
                    analogDialUpDAO.insertMetricDataMulti(analogDialUpResult);
                }
            } else {
                logger.error("AnalogDialUpJob excute error nowTime : " + nowTime
                        + ";analogDialUpId : " + analogDialUpId + ";serialno : " + serialno
                        + ";host : " + host + ";port : " + port + ";callTime : " + callTime
                        + ";resultMsg is null .");
            }
            logger.info("------end AnalogDialUpJob excute nowTime : " + nowTime
                    + ";analogDialUpId : " + analogDialUpId + ";serialno : " + serialno + "------");
        } catch (Exception e) {
            logger.error("AnalogDialUpJob excute error nowTime : " + nowTime + ";analogDialUpId : "
                    + analogDialUpId + ";serialno : " + serialno + ";errorMessage:"
                    + e.getMessage());
        }
    }

    private String addBrackets(String str) {
        return "{" + str + "}";
    }

    /**
     * 解析obs返回字符串,去除最外层大括号，如{1}{a}{b}{{1|2|3}{1|2|3}},返回列表为 1、a、b、{1|2|3}{1|2|3}
     * 
     * @param input
     *            obs返回字符串
     * @return
     */
    private List<String> GetResultSplit(String input) {
        List<String> ret = new ArrayList<String>();
        char a[] = input.toCharArray();
        StringBuffer temp = null;
        boolean bIsOne = false;
        int braceDepth = 0;
        for (int n = 0; n < a.length; n++) {
            if (!bIsOne && a[n] == ' ') {
                if (temp != null) {
                    ret.add(temp.toString());
                }
                temp = null;
                continue;
            } else {
                if (a[n] == '{') {
                    if (bIsOne) {
                        if (temp == null) {
                            temp = new StringBuffer();
                        }
                        temp.append("{");
                    }
                    bIsOne = true;
                    braceDepth++;
                } else if (a[n] == '}') {
                    braceDepth--;
                    if (braceDepth == 0) {
                        if (temp == null) {
                            ret.add("");
                        } else {
                            ret.add(temp.toString());
                        }
                        temp = null;
                        bIsOne = false;
                    } else {
                        if (temp == null) {
                            temp = new StringBuffer();
                        }
                        temp.append(a[n]);
                    }
                } else {
                    if (temp == null) {
                        temp = new StringBuffer();
                    }
                    temp.append(a[n]);
                }
            }
        }
        if (temp != null) {
            ret.add(temp.toString());
        }
        return ret;
    }
}
