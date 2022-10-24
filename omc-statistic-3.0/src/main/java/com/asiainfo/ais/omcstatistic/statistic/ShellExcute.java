package com.asiainfo.ais.omcstatistic.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShellExcute {

    private static final Logger LOG = LoggerFactory.getLogger(ShellExcute.class);


    @Value("${shell.path}")
    private String shellPath;

    public void runAlarmShell(String formatDate, String interval){
        if(null!=shellPath && !"".equals(shellPath)){
            Process process;
            String command = "chmod 777 " + shellPath;
//            String[] excuteSh = new String[]{shellPath, formatDate, interval};
            String[] excuteSh = new String[]{shellPath, formatDate};
            try {
                process = Runtime.getRuntime().exec(command);
                process.waitFor();
                int exitValue = Runtime.getRuntime().exec(excuteSh).waitFor();
                if (0 != exitValue) {
                    LOG.error("call alarmShell failed. error code is :" + exitValue);
                }
            } catch (Exception e) {
                LOG.info("runAlarmShell::" + e);
            }
        }
    }

}
