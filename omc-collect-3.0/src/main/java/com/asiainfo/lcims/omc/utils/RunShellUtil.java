package com.asiainfo.lcims.omc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.common.ResultType;

public class RunShellUtil {
    private static final Logger log = LoggerFactory.make();
    private static String SPLIT_DELIM_CHAR = "\\ \\\t\\\n\\\r\\\f";
    private static String NOT_SPLIT_CHAR_FLAG = "\\'";

    // 根据command执行脚本，并返回相应的结果
    public static String runShell(String command) {
        Process process = null;
        String ret = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(mkCommand(command));
            builder.redirectErrorStream(true);
            process = builder.start();
            ret = readReturn(process);
            process.waitFor();
        } catch (Exception e) {
            log.error("脚本{" + command + "}执行失败:", e);
            return ResultType.FAILED;
        } finally {
            closeProcess(process);
        }
        return ret;
    }

    private static List<String> mkCommand(String command) {
        List<String> cmds = new ArrayList<String>();
        if (command == null) {
            return cmds;
        }
        char[] charOfCmd = command.toCharArray();
        boolean split_num_flag = true;
        StringBuilder strb = new StringBuilder();
        for (char c : charOfCmd) {
            if (SPLIT_DELIM_CHAR.indexOf(String.valueOf(c)) >= 0 && split_num_flag) {
                String tmp = strb.toString();
                if (tmp.trim().isEmpty()) {
                    continue;
                }
                cmds.add(tmp);
                strb.setLength(0);
            } else if (NOT_SPLIT_CHAR_FLAG.indexOf(String.valueOf(c)) >= 0) {
                if (split_num_flag) {
                    split_num_flag = false;
                } else {
                    split_num_flag = true;
                }
                strb.append(c);
            } else {
                strb.append(c);
            }
        }
        String tmp = strb.toString();
        if (!tmp.trim().isEmpty()) {
            cmds.add(tmp);
        }
        return cmds;
    }

    private static void closeProcess(Process process) {
        if (process != null) {
            if (process.getInputStream() != null) {
                try {
                    process.getInputStream().close();
                } catch (IOException e) {
                    log.error("process.getInputStream() close error:", e);
                }
            }
            if (process.getOutputStream() != null) {
                try {
                    process.getOutputStream().close();
                } catch (IOException e) {
                    log.error("process.getOutputStream() close error:", e);
                }
            }
            if (process.getErrorStream() != null) {
                try {
                    process.getErrorStream().close();
                } catch (IOException e) {
                    log.error("process.getErrorStream() close error:", e);
                }
            }
            process.destroy();
        }
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
            log.error("脚本执行失败", e);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return ret;
    }

    public static String exec(String command) {
        String result = "";
        try {
            Process ps = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\t");
            }
            result = sb.toString();
            ps.waitFor();
        } catch (Exception e) {
            log.error("脚本{" + command + "}执行失败:", e);
            return e.getMessage();
        }
        return result;
    }

}
