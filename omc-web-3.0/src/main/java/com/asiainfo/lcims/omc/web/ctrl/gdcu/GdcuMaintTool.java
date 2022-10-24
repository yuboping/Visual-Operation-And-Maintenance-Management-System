package com.asiainfo.lcims.omc.web.ctrl.gdcu;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.gdcu.AccessLog;
import com.asiainfo.lcims.omc.model.gdcu.BatOptRec;
import com.asiainfo.lcims.omc.model.gdcu.CheckOnlineBasUserRecord;
import com.asiainfo.lcims.omc.model.gdcu.OBSInfo;
import com.asiainfo.lcims.omc.model.gdcu.OfflineLog;
import com.asiainfo.lcims.omc.model.gdcu.OptRecord;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.CheckBasUserResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.CheckOnlineUserResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.KickLMUserResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.QueryOnlineUserResp2;
import com.asiainfo.lcims.omc.service.gdcu.GdcuMaintToolService;

/**
 * 运维工具数据查询VIEW
 * @author zhul
 *
 */
@Controller
@RequestMapping(value = { "/gdmainttool" })
public class GdcuMaintTool {
    
    private static final Logger LOG = LoggerFactory.getLogger(GdcuMaintTool.class);

    @Resource(name = "gdcuMaintToolService")
    GdcuMaintToolService gdcuMaintToolService;
    
    /**
     * 判断权限
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = { "/queryUserNode" })
    @ResponseBody
    public OBSInfo queryUserNode(HttpServletRequest request) {
        String account = request.getParameter("account");
        String opernodeid = request.getParameter("opernodeid");
        return gdcuMaintToolService.queryUserNode(account, opernodeid);
    }
    
    @RequestMapping(value = { "/queryOnlineUser" })
    @ResponseBody
    public List<QueryOnlineUserResp2> queryOnlineUser(HttpServletRequest request) {
        // 记录日志
        // oprate
        String querytype = request.getParameter("querytype");
        String queryvalue = request.getParameter("queryvalue");
        return gdcuMaintToolService.queryOnlineUser(querytype,queryvalue);
    }
    
    @RequestMapping(value = { "/kickBRASUser" })
    @ResponseBody
    public OBSInfo kickBRASUser(HttpServletRequest request) {
        // 记录日志
        // oprate
        String account = request.getParameter("account");
        String nasip = request.getParameter("nasip");
        String userip = request.getParameter("userip");
        String sessionid = request.getParameter("sessionid");
        return gdcuMaintToolService.kickBRASUser(userip,sessionid,nasip,account);
    }
    
    @RequestMapping(value = { "/kickLMUser" })
    @ResponseBody
    public OBSInfo kickLMUser(HttpServletRequest request) {
        // 记录日志
        // oprate
        String querytype = request.getParameter("querytype");
        String queryvalue = request.getParameter("queryvalue");
        return gdcuMaintToolService.kickLMUser(querytype,queryvalue);
    }
    
    @RequestMapping(value = { "/onlineBasUser" })
    @ResponseBody
    public List<CheckOnlineBasUserRecord> onlineBasUser(HttpServletRequest request) {
        String brasip = request.getParameter("brasip");
        String brasTpye = request.getParameter("brasTpye");
        // 记录日志
        // oprate
        
        CheckBasUserResp bas = gdcuMaintToolService.checkBasUser(brasip);
        //过滤brasip
        List<CheckOnlineBasUserRecord> record = gdcuMaintToolService.compareBasUser(bas,brasip,brasTpye);
        return record;
    }
    
    @RequestMapping(value = { "/onlineUserStats" })
    @ResponseBody
    public CheckOnlineUserResp onlineUserStats(HttpServletRequest request) {
        //adminLogInit.log(request, "28");
        return gdcuMaintToolService.checkOnlineUser();
    }
    
    
    @RequestMapping(value = "/checkLimits")
    @ResponseBody
    public OBSInfo checkLimits(HttpServletRequest request, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String brasip = request.getParameter("brasip");
        return gdcuMaintToolService.checkLimits(brasip, username);
    }

    @RequestMapping(value = { "/checkBasUser" })
    @ResponseBody
    public CheckBasUserResp checkBasUser(HttpServletRequest request) {
        String brasip = request.getParameter("brasip");
        return gdcuMaintToolService.checkBasUser(brasip);
    }

    @RequestMapping(value = { "/offlineKickBRASUser" })
    @ResponseBody
    public KickLMUserResp offlineKickBRASUser(HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        String brasip = request.getParameter("brasip");
        String type = request.getParameter("type");
        String remoteHost = request.getRemoteHost();
        String reason = request.getParameter("offlineReason");
        BatOptRec opt = new BatOptRec();
        opt.setNasip(brasip);
        opt.setAdmin(username);
        opt.setIpaddress(remoteHost);
        opt.setOptreason(reason);
        opt.setOpttime(new Timestamp(System.currentTimeMillis()));
        // 记录日志 todo
        return gdcuMaintToolService.offlineKickBRASUser(type, opt);
    }

    @RequestMapping(value = "/offlinelogquery/query")
    @ResponseBody
    public List<OfflineLog> queryInfo(HttpServletRequest request, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        String admin = request.getParameter("admin");
        String opttype = request.getParameter("opttype");
        String brasip = request.getParameter("brasip");
        String date = request.getParameter("date");

        List<OfflineLog> list = gdcuMaintToolService.getOfflineLog(startdate, enddate, username,
                admin, opttype, brasip, date);
        // 记录日志 todo
        return list;
    }

    @RequestMapping(value = "/batoptrecord/query")
    @ResponseBody
    public List<OptRecord> query(HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        String opttype = request.getParameter("opttype");
        LOG.info("batoptrecord opttype is : {}", opttype);
        return gdcuMaintToolService.query(opttype, username);
    }
    
    /**
     * 认证日志查询
     * @param request
     * @return
     */
    @RequestMapping(value = { "/qryAccessLog" })
    @ResponseBody
    public List<AccessLog> qryAccessLog(HttpServletRequest request) {
        //记录日志
        //adminLogInit.log(request, "30");
        String account = request.getParameter("account");
        String querydate = request.getParameter("querydate");
        return gdcuMaintToolService.qryAccessLog(account,querydate);
    }
    
    
}
