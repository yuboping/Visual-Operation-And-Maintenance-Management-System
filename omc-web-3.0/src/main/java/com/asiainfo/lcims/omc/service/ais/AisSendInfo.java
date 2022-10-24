package com.asiainfo.lcims.omc.service.ais;

import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.excelReport.ExcelReport;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.ais.INSSchedule;
import com.asiainfo.lcims.omc.service.mail.SimpleMail;
import com.asiainfo.lcims.omc.service.mail.SimpleMailSender;

public class AisSendInfo {
    private static final Logger LOG = LoggerFactory.make();

    /**
     * 根据配置选择发邮件或者发短信通知
     * 
     * @param schedule
     * @param report
     * @param fileName
     */
    public static void sendEmailSms(INSSchedule schedule, ExcelReport report, String filePath,
            String fileName) {
        if (CommonInit.BUSCONF.getAISEmailNotice()) {// 发邮件
            try {
                SimpleMail mail = new SimpleMail();
                mail.setSubject(fileName);
                mail.setPartFilePath(filePath);
                mail.setContent("巡检报告请查看附件.");
                sendEmail(schedule.getEmails(), mail);
            } catch (Exception ee) {
                LOG.error("发送邮件失败", ee);
            }
        }
    }

    /**
     * 发送邮件
     * 
     * @param report
     * @param path
     * @param fileName
     */
    private static void sendEmail(String emails, SimpleMail mail) {
        if (StringUtils.isEmpty(emails)) {
            return;
        }
        String[] tmp = emails.split(";");
        try {
            LOG.info("send Email");
            SimpleMailSender sender = new SimpleMailSender(CommonInit.BUSCONF.getAISSmtpHost(),
                    CommonInit.BUSCONF.getAISSmtpUsername(),
                    CommonInit.BUSCONF.getAISSmtpPassword());
            sender.send(tmp, mail);
        } catch (Exception e) {
            LOG.info("发送邮件失败", e);
//            e.printStackTrace();
        }
    }
}
