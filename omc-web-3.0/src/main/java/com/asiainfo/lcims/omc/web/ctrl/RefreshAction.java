package com.asiainfo.lcims.omc.web.ctrl;

import java.text.DecimalFormat;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 刷新系统缓存数据
 * @author zhul
 *
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.util.StringUtil;

@Controller
public class RefreshAction {

    @Resource(name = "commoninit")
    CommonInit commoninit;

    @RequestMapping(value = "/sys/refresh")
    @ResponseBody
    public String refresh() {
        commoninit.init();
        return "refresh success...";
    }

    /**
     * 
     * @Title: refreshfilesystem
     * @Description: TODO(刷新主机文件系统表)
     * @param @param request
     * @param @return monthday参数可以加也可以不传，不传默认当前月日。monthday=mmdd
     *        例子(9月10日)：http://
     *        localhost:9006/sys/refreshfilesystem?monthday=0910
     *        或者(当天)：http://localhost:9006/sys/refreshfilesystem
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/sys/refreshfilesystem")
    @ResponseBody
    public String refreshfilesystem(HttpServletRequest request) {
        String monthday = request.getParameter("monthday");
        String date = "";
        if (StringUtil.isValidMonthDay(monthday)) {
            String month = monthday.substring(0, 2);
            String day = monthday.substring(2, 4);
            date = month + "_" + day;
        } else {
            Calendar now = Calendar.getInstance();
            DecimalFormat df = new DecimalFormat("00");
            String month = df.format(now.get(Calendar.MONTH) + 1L);
            String day = df.format(now.get(Calendar.DAY_OF_MONTH));
            date = month + "_" + day;
        }
        commoninit.refreshfilesystem(date);
        return "refreshfilesystem success date is " + date;
    }
}
