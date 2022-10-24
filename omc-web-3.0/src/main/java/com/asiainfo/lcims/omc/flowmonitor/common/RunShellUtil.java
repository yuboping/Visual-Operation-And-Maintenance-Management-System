package com.asiainfo.lcims.omc.flowmonitor.common;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 执行shell脚本
 * 
 * @author XHT
 *
 */
public class RunShellUtil {
    private static final Logger log = LoggerFactory.make();

    /**
     * 根据command执行脚本，并返回相应的结果
     * 
     * @param command
     * @return
     */
    public static String runShell(String command) {
        Process process = null;

        String ret = null;
        try {
            StringTokenizer st = new StringTokenizer(command);
            String[] cmdarray = new String[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                cmdarray[i] = st.nextToken();
            }
            ProcessBuilder builder = new ProcessBuilder(cmdarray);
            builder.redirectErrorStream(true);
            process = builder.start();
            ret = readReturn(process);
            process.waitFor();
        } catch (Exception e) {
            log.error("脚本{" + command + "}执行失败:", e);
        } finally {
            if (process != null) {
                try {
                    process.getInputStream().close();
                } catch (IOException e) {
                    log.info("run shell exception : ", e);
                }
                try {
                    process.getOutputStream().close();
                } catch (IOException e) {
                    log.info("run shell exception : ", e);
                }
                try {
                    process.getErrorStream().close();
                } catch (IOException e) {
                    log.info("run shell exception : ", e);
                }
                process.destroy();
            }
        }
        return ret;
    }

    private static String readReturn(Process process) throws IOException {
        String ret = "";
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                ret += line;
            }
        } catch (Exception e) {
            log.info("脚本执行失败,原因：{}", e);
        } finally {
            IOUtils.closeQuietly(input);
        }
        return ret;
    }
}
