package com.asiainfo.lcims.omc.service.system;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.lcbmi.password.Password;
import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.VerifyCode;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.persistence.configmanage.MAdminDAO;
import com.asiainfo.lcims.omc.persistence.system.AdminDAO;
import com.asiainfo.lcims.omc.persistence.system.VerifyCodeDao;
import com.asiainfo.lcims.omc.service.mail.SimpleMail;
import com.asiainfo.lcims.omc.service.mail.SimpleMailSender;
import com.asiainfo.lcims.omc.util.AdminPwdDes;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Service(value = "verifyCodeSevice")
public class VerifyCodeSevice {

    private static final Logger log = LoggerFactory.getLogger(VerifyCodeSevice.class);
    @Inject
    VerifyCodeDao verifyCodeDao;
    @Autowired
    private MAdminDAO mAdminDAO;

    @Inject
    private AdminDAO adminDAO;

    @Resource(name = "paramService")
    ParamService paramService;

    // public boolean addVerifyCode(VerifyCode verifyCode) {
    // boolean b = false;
    // try {
    // verifyCodeDao.addVerifyCode(verifyCode);
    // b = true;
    // } catch (Exception e) {
    // // TODO: handle exception
    // b = false;
    // log.info("verifyCode add error:" + e.getMessage());
    // }
    // return b;
    // }

    // public VerifyCode queryVerifyCodeByAdminAndIsApply(String admin, int
    // isApply) {
    // return verifyCodeDao.queryVerifyCodeByAdminAndIsApply(admin, isApply);
    // }

    public boolean updateVerifyCodeById(String id) {
        boolean b = false;
        try {
            verifyCodeDao.updateVerifyCodeById(id);
            b = true;
        } catch (Exception e) {
            // TODO: handle exception
            b = false;
            log.info("verifyCode update error:" + e.getMessage(), e);
        }
        return b;
    }

    /**
     * 发送验证码
     * 
     * @param username
     * @param sendType
     * @param sendTarget
     * @param request
     * @return
     */
    public WebResult checkType(String username, String sendType, String sendTarget,
            HttpServletRequest request) {
        WebResult webResult = new WebResult(true, "");
        // 校验用户信息、手机号、邮箱是否正确
        boolean checkUserInof = chechUserInfo(username, Integer.valueOf(sendType), sendTarget);
        if (checkUserInof || "3".equals(sendType)) {
            // 校验验证码有效期
            WebResult codeActive = verifyCodeisActive(username, sendTarget, request);
            if (codeActive.isOpSucc()) {
                // 密码在有效期内
                webResult.setOpSucc(false);
                webResult.setMessage(codeActive.getMessage());
                return webResult;
            }
            webResult = sendVerifyCode(username, sendType, sendTarget, request);
        } else {
            String mag = "";
            if ("1".equals(sendType)) {
                mag = "1_手机号码与创建时不匹配";
            } else if ("2".equals(sendType)) {
                mag = "2_邮箱地址与创建时不匹配";
            } else {
                mag = "3_请选择验证方式";
            }
            webResult.setMessage(mag);
        }
        if (Constant.getForgetPwdCodeMap().containsKey(username)) {
            log.info(username + ":" + sendTarget + " verifycode====:"
                    + Constant.getForgetPwdCodeMap().get(username).getVerifyCode() + " "
                    + Constant.getForgetPwdCodeMap().get(username).getSendDate());
            // System.out.println(username+":"+sendTarget+"
            // verifycode====:"+Constant.FORGET_PWD_CODE_MAP.get(username).getVerifyCode()+"
            // "+Constant.FORGET_PWD_CODE_MAP.get(username).getSendDate());
        }
        return webResult;
    }

    private boolean chechUserInfo(String username, int checkType, String checkValue) {
        boolean rt = false;
        int i = mAdminDAO.queryCheckType(username, checkType, checkValue);
        if (i > 0)
            rt = true;
        return rt;
    }

    /**
     * 校验验证码是否在有效期内,在有效期内：true，否则 false username 用户名 sendTarget 手机号/邮箱
     * 
     * @return
     */
    private WebResult verifyCodeisActive(String username, String sendTarget,
            HttpServletRequest request) {
        WebResult result = new WebResult(false, "");

        if (Constant.getForgetPwdCodeMap().containsKey(username)) {
            VerifyCode verifyCode = Constant.getForgetPwdCodeMap().get(username);
            /*
             * 当前发送目标是否和 已发送目标保持一致 1、保持一致，进行校验时间判断 2、不一致，重新发送（第一次短信发送、第二次邮箱发送）
             */
            if (ToolsUtils.StringIsNull(sendTarget)
                    || sendTarget.equals(verifyCode.getSendTarget())) {
                // 验证 验证码，校验时间
                long timefalg = ratioTime(verifyCode.getSendDate());
                if (timefalg > 0) { // 在有效期内
                    result.setOpSucc(true);
                    result.setMessage(
                            "5_" + timefalg / 1000 + "_验证码已发送,请" + timefalg / 1000 + "秒之后再试");
                } else {
                    Constant.getForgetPwdCodeMap().remove(username);
                }
            } else {
                Constant.getForgetPwdCodeMap().remove(username);
            }
        }
        return result;
    }

    public long ratioTime(Date sendDate) {
        // 查询验证码配置的过期时间
        List<MdParam> countdowns = paramService.getMdParamList("103");
        int verifyCodetime = 60;
        if (countdowns != null && countdowns.size() > 0) {
            verifyCodetime = Integer.valueOf(countdowns.get(0).getCode());
        }
        long time = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Calendar before = (Calendar) calendar.clone();
        // 这个方法很灵活，在哪个字段上操作，增减多少，都很方便
        before.add(Calendar.SECOND, -verifyCodetime);
        // 用DateFormat对象将Calendar对象格式化
        String nowTime = format.format(before.getTime());
        try {
            Date dt1 = format.parse(nowTime);
            // time小于或者等于0说明过期了,大于0说明没有过期
            time = sendDate.getTime() - dt1.getTime();
            // System.out.println((dt1.getTime()-dt2.getTime())/1000);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            log.info("time ratio error:" + e.getMessage());
        }
        return time;
    }

    private WebResult sendVerifyCode(String username, String sendType, String sendTtarget,
            HttpServletRequest request) {
        WebResult result = new WebResult(false, "");
        // 生成验证码
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        log.info("create verifyCode：" + verifyCode);
        switch (sendType) {
        case "1":
            // 短信发送
            sendSMS(username, sendTtarget, verifyCode, result);
            break;
        case "2":
            // 邮件发送
            sendEmail(username, sendTtarget, verifyCode, result);
            break;
        case "3":
            // 手机令牌
            sendToken(username, sendTtarget, verifyCode, result);
            break;
        default:
            result.setOpSucc(false);
            result.setMessage("3_请检查发送配置");
            break;
        }
        // 测试用
        // result.setOpSucc(true);

        if (result.isOpSucc()) {
            VerifyCode vc = new VerifyCode();
            vc.setVerifyCode(verifyCode);
            vc.setSendDate(new Date());
            vc.setSendTarget(sendTtarget);
            Constant.getForgetPwdCodeMap().put(username, vc);
            log.info("verifyCode send success：" + verifyCode);
        } else {
            Constant.getForgetPwdCodeMap().remove(username);
            log.info("verifyCode send fail：" + verifyCode);
        }
        return result;
    }

    // 短信发送
    private void sendSMS(String username, String sendTtarget, String verifyCode, WebResult result) {
        switch (MainServer.conf.getProvince()) {
        case ProviceUtill.PROVINCE_JSCM:
            sendSMSJSCM(username, sendTtarget, verifyCode, result);
            break;
        case ProviceUtill.PROVINCE_GSCM5G:
            sendSMSGSCM5G(username, sendTtarget, verifyCode, result);
            break;
        default:
            result.setOpSucc(false);
            result.setMessage("3_请配置省份短信发送接口信息");
            break;
        }
    }

    // 手机令牌
    private void sendToken(String username, String sendTtarget, String verifyCode,
            WebResult result) {
        switch (MainServer.conf.getProvince()) {
        case ProviceUtill.PROVINCE_GSCM5G:
            sendTokenGSCM5G(username, sendTtarget, verifyCode, result);
            break;
        default:
            result.setOpSucc(false);
            result.setMessage("3_请配置省份短信发送接口信息");
            break;
        }
    }

    private void sendSMSGSCM5G(String username, String sendTtarget, String verifyCode,
            WebResult result) {
        result.setOpSucc(true);
        result.setMessage("发送成功请注意查收");
    }

    private void sendTokenGSCM5G(String username, String sendTtarget, String verifyCode,
            WebResult result) {
        result.setOpSucc(true);
        result.setMessage("发送成功请注意查收");
    }

    private void sendSMSJSCM(String username, String sendTtarget, String verifyCode,
            WebResult result) {
        Socket clientSocket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        String line = null;
        byte[] res = null;
        try {
            List<MdParam> codetype = paramService.getMdParamList("105");
            if (codetype.isEmpty()) {
                result.setOpSucc(false);
                result.setMessage("3_请配置短信发送接口信息");
                return;
            }
            HashMap<String, String> ma = new HashMap<String, String>();
            for (MdParam mdParam : codetype) {
                ma.put(mdParam.getCode(), mdParam.getDescription());
            }
            clientSocket = new Socket(ma.get("phone_ip"), Integer.valueOf(ma.get("phone_port")));
            out = new DataOutputStream(clientSocket.getOutputStream());
            // line = sendTtarget +
            // "|||尊敬的"+username+"管理员您好！您的验证码为"+verifyCode+"，有效期5分钟。【OMC验证码】";
            line = sendTtarget + "|||Dear " + username
                    + " administrator, Your verification code is " + verifyCode
                    + ", valid for 5 minutes. [OMC verification code]";
            log.info("send to socketServer[" + ma.get("phone_ip") + ":" + ma.get("phone_port")
                    + "] data:{" + line + "}");
            line = line + "\n";
            String lineGbk = new String(line.getBytes("UTF-8"), "UTF-8");
            byte[] cbuf = lineGbk.getBytes();
            out.write(cbuf);
            out.flush();
            in = new DataInputStream(clientSocket.getInputStream());
            // 接收返回结果,等待处理.
            byte[] b = new byte[8192];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int count = 0;
            do {
                count = in.read(b);
                bos.write(b, 0, count);
            } while (in.available() > 0);
            res = bos.toByteArray();
            if (res != null && res.length > 0) {
                String returnValues = (new String(res, "UTF-8")).trim();
                log.info("receive socket return info:" + returnValues);
                String[] rt = returnValues.split("\\|\\|\\|");
                if ("0".equals(rt[0])) {
                    result.setOpSucc(true);
                    result.setMessage("发送成功请注意查收");
                } else {
                    result.setOpSucc(false);
                    result.setMessage("4_验证码发送失败,请联系管理员");
                }
            } else {
                log.info("receive socket return info is null");
            }

        } catch (Exception e) {
            result.setOpSucc(false);
            result.setMessage("4_验证码发送失败,请联系管理员");
            if (clientSocket != null)
                log.error("clientSocket conncted is " + clientSocket.isConnected() + ".");
            log.error(username + " send phone Messaging unusual :" + e.getMessage(), e);
            if (out != null)
                IOUtils.closeQuietly(out);
            if (in != null)
                IOUtils.closeQuietly(in);
            if (clientSocket != null)
                IOUtils.closeQuietly(clientSocket);
        } finally {
            if (clientSocket != null)
                log.info("clientSocket conncted is " + clientSocket.isConnected() + ".");
            if (out != null)
                IOUtils.closeQuietly(out);
            if (in != null)
                IOUtils.closeQuietly(in);
            if (clientSocket != null)
                IOUtils.closeQuietly(clientSocket);
        }
    }

    /**
     * 邮箱发送
     * 
     * @param admin
     * @param checkValue
     * @param verifyCode
     * @return
     */
    private int sendEmail(String username, String sendTtarget, String verifyCode,
            WebResult result) {
        // 获取配置的发送邮箱信息
        List<MdParam> emailList = paramService.getMdParamList("104");
        if (emailList.isEmpty()) {
            result.setOpSucc(false);
            result.setMessage("3_请配置发送邮箱信息");
            return 1;
        }
        Map<String, String> pama = new HashMap<String, String>();
        for (MdParam mdParam : emailList) {
            pama.put(mdParam.getCode(), mdParam.getDescription());
        }
        SimpleMail mail = new SimpleMail();
        // 收件人邮箱地址
        try {
            log.info("send Email");
            // 标题
            mail.setSubject("forget password verify");
            // 内容
            mail.setContent("[亚信安全] 验证码:" + verifyCode + ". 您正在使用修改密码功能,该验证码仅用于身份验证,请勿泄露给他人使用.");
            String[] recipients = new String[1];
            recipients[0] = sendTtarget;
            // 发送邮箱类型,账号,授权码
            SimpleMailSender sender = new SimpleMailSender(pama.get("smtpType"),
                    pama.get("smtpUsername"), pama.get("smtpPassword"));
            sender.send(recipients, mail);
            result.setOpSucc(true);
            result.setMessage("发送成功请注意查收");
            return 0;
        } catch (Exception e) {
            log.info("发送邮件失败:" + e.getMessage(), e);
            // e.printStackTrace();
            result.setOpSucc(false);
            result.setMessage("4_验证码发送失败,请联系管理员");
            return 4;
        }
    }

    public WebResult changePossword(String admin, String checkCode, String password,
            String oncePassword, String checkType, HttpServletRequest request) {
        WebResult webResult = new WebResult(false, "修改失败");
        if (!password.equals(oncePassword)) {
            webResult.setMessage("密码不一致");
            return webResult;
        }
        // 校验 验证码有效期
        WebResult codeActiveRT = verifyCodeisActive(admin, null, request);
        if (codeActiveRT.isOpSucc() || checkType.equals("3")) {
            if (MainServer.conf.getProvince().equals(ProviceUtill.PROVINCE_GSCM5G)
                    || checkType.equals("3")) {
                // 修改密码
                updatePassword(admin, password, webResult);
                Constant.getForgetPwdCodeMap().remove(admin);
            } else {
                String verifyCode = Constant.getForgetPwdCodeMap().get(admin).getVerifyCode();
                if (checkCode.equals(verifyCode)) {
                    // 修改密码
                    updatePassword(admin, password, webResult);
                    Constant.getForgetPwdCodeMap().remove(admin);
                } else {
                    webResult.setMessage("验证码错误,请重新输入.");
                }
            }
        } else {
            webResult.setMessage("验证码已过期,请重新获取.");
        }
        return webResult;
    }

    private void updatePassword(String admin, String password, WebResult webResult) {
        AdminPwdDes pwdDes = new AdminPwdDes();
        String pwd = pwdDes.strDec(password);
        try {
            pwd = Password.encryptPassword(pwd + Constant.PASSWORD_SALT, 1);
            MAdmin mAdmin = new MAdmin();
            mAdmin.setAdmin(admin);
            mAdmin.setPassword(pwd);
            int successCount = adminDAO.updateOldPassword(mAdmin);
            if (successCount > 0) {
                webResult.setOpSucc(true);
                webResult.setMessage("密码修改成功");
            } else {
                webResult.setMessage("密码修改失败,请联系管理员");
            }
        } catch (Exception e) {
            webResult.setMessage("密码修改失败,请联系管理员");
            log.info("update password error:" + e.getMessage(), e);
        }
    }
}
