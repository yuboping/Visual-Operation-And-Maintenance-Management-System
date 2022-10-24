package com.asiainfo.lcims.omc.service.gdcu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.gdcu.AccessLog;
import com.asiainfo.lcims.omc.model.gdcu.BaseData;
import com.asiainfo.lcims.omc.model.gdcu.BatOptRec;
import com.asiainfo.lcims.omc.model.gdcu.BmsArea;
import com.asiainfo.lcims.omc.model.gdcu.CheckOnlineBasUserRecord;
import com.asiainfo.lcims.omc.model.gdcu.OBSInfo;
import com.asiainfo.lcims.omc.model.gdcu.OfflineLog;
import com.asiainfo.lcims.omc.model.gdcu.OptRecord;
import com.asiainfo.lcims.omc.model.gdcu.obs.req.CheckBasUserReq;
import com.asiainfo.lcims.omc.model.gdcu.obs.req.CheckOnlineUserReq;
import com.asiainfo.lcims.omc.model.gdcu.obs.req.KickBRASUserReq;
import com.asiainfo.lcims.omc.model.gdcu.obs.req.KickLMUserReq;
import com.asiainfo.lcims.omc.model.gdcu.obs.req.QueryOnlineUserReq;
import com.asiainfo.lcims.omc.model.gdcu.obs.req.QueryUserNodeReq;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.CheckBasUserResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.CheckOnlineUserResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.KickBRASUserResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.KickLMUserResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.QueryOnlineUserResp2;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.QueryUserNodeResp;
import com.asiainfo.lcims.omc.model.gdcu.obs.resp.ResponseFactory;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdRolePermissions;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.param.monitor.MonitorInit;
import com.asiainfo.lcims.omc.persistence.configmanage.RoleManageDAO;
import com.asiainfo.lcims.omc.persistence.maintool.BatOptRecDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.MD5Util;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * gdcu 运维工具接口
 * @author zhul
 *
 */
@Service(value = "gdcuMaintToolService")
public class GdcuMaintToolService {
    private static final Logger log = LoggerFactory.getLogger(GdcuMaintToolService.class);
    
    BusinessConf buss = new BusinessConf();

    @Inject
    private RoleManageDAO roleManageDAO;
    
    @Autowired
    private BatOptRecDAO batOptRecDAO;
    
    /**
     * 调用obs接口查询用户信息（属地）
     * @param account
     * @param opernodeid
     * @param username
     * @return
     */
    public OBSInfo queryUserNode(String account,String opernodeid) {
        QueryUserNodeResp resp = null;
        QueryUserNodeReq req = new QueryUserNodeReq();
        BusinessConf conf = CommonInit.BUSCONF;
        req.setAccount(account);
        req.setOpernodeid(opernodeid);
        ObsInterface obs = null;
        OBSInfo result = null;
        try {
            obs = new ObsInterface(conf.queryUserNodeIp(), conf.queryUserNodePort());
            log.info("QueryUserNode_send:{"+req.encode()+"}");
            obs.sendMsg2Server(req.encode());
            String ret = obs.getMsgFromServer();
            log.info("QueryUserNode_result:{"+ret+"}");
            resp = (QueryUserNodeResp) ResponseFactory.decode(ret, "QueryUserNodeResp");
            result= compareObs(resp);
        } catch (Exception e) {
            log.error("调用obs接口用户信息（属地）查询接口失败", e);
            resp = new QueryUserNodeResp(req.getSerialno(), "-1", "调用obs接口用户信息（属地）查询接口失败");
        } finally {
            close(obs);
        }
        return result;
    }
    
    /**
     * 权限过滤
     * @param resp
     * @param username
     * @return
     */
    private OBSInfo compareObs(QueryUserNodeResp resp) {
        // 获取数据
        List<BmsArea> bmsArealist = MonitorInit.getBmsAreaList();
        OBSInfo info = new OBSInfo();
        if (!ToolsUtils.ListIsNull(bmsArealist) && resp.getReturncode().equals("0")) {
            // 获取当前登录用户
            MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
            // 获取当前登录用户地市权限
            List<MdRolePermissions> arealist = roleManageDAO.getRolePermissionByRoleid(user.getRoleid(),
                    Constant.PERMISSION_CHILDREN_AREA);
            for (BmsArea area : bmsArealist) {
                if (area.getBmsid().equals(resp.getNode())) {
                    String areano = CommonInit.getAreaNoByAreaname(area.getAreaName());
                    if ("其他".equals(area.getAreaName())) {
                        // 超级管理员直接返回
                        if (user.getRoleid().equals(CommonInit.BUSCONF.getRoleAdmin())) {
                            info.setMessage("操作成功！");
                            info.setReturncode("0");
                            return info;
                        }
                    } else if (areano != null && !areano.isEmpty()) {
                        // 判断 areano 是否属于当前登录用户的权限，是返回成功，否返回失败
                        if (havePermission(areano, arealist)) {
                            info.setMessage("操作成功！");
                            info.setReturncode("0");
                            return info;
                        }
                    }
                }
            }
        }
        info.setMessage("无操作权限！");
        info.setReturncode("1");
        return info;
    }
    
    private boolean havePermission(String areano, List<MdRolePermissions> arealist){
        if(ToolsUtils.ListIsNull(arealist)){
            log.info("arealist is null");
        }
        //增加超级管理员判断
        // 获取当前登录用户
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        if (user.getRoleid().equals(CommonInit.BUSCONF.getRoleAdmin())) {
            return true;
        }
        
        for (MdRolePermissions mdRolePermissions : arealist) {
            if(mdRolePermissions.getPermissionid().equals(areano)){
                return true;
            }
        }
        return false;
    }
    
    private void close(ObsInterface obs) {
        if (obs != null) {
            obs.close();
        }
    }
    
    /**
     * 调用obs接口查询在线用户
     * @param querytype
     * @param queryvalue
     * @return
     */
    public List<QueryOnlineUserResp2> queryOnlineUser(String querytype ,String queryvalue) {
        QueryOnlineUserReq req = new QueryOnlineUserReq();
        req.setQuerytype(querytype);
        req.setQueryvalue(queryvalue);
        BusinessConf conf = CommonInit.BUSCONF;
        ObsInterface obs = null;
        List<BaseData> resps = null;
        List<QueryOnlineUserResp2> resplist = new ArrayList<QueryOnlineUserResp2>();
        try {
            obs = new ObsInterface(conf.queryOnlineUserIp(), conf.checkBasUserPort());
            log.info("QueryOnlineUser_send:{"+req.encode()+"}");
            obs.sendMsg2Server(req.encode());
            String ret = obs.getMsgFromServer();
            log.info("QueryOnlineUser_result:{"+ret+"}");
            //ret = "{{155538467747681} {0} {OK} {HZZJFTTH1155159056@16900.gd} {2019/04/16 07:29:33} {120.80.184.16} {58.255.21.51} {ec:26:ca:ff:7d:e1} {D06FA60651DC5D5CB513DE} {2019/04/16 09:25:49} {03:48:26} {UPD} {Null,Null,Null,1770.1894}}}";
            resps = ResponseFactory.decodeList(ret, "QueryOnlineUserResp2");
            for (BaseData baseData : resps) {
                resplist.add((QueryOnlineUserResp2) baseData);
            }
        } catch (Exception e) {
            log.error("调用obs接口查询在线用户失败", e);
        } finally {
            close(obs);
        }
        return resplist;
    }
    
    
    /**
     * 调用obs接口踢用户下线
     * @param dmuserip
     * @param dmsession
     * @param dmbras
     * @param dmuser
     * @return
     */
    public OBSInfo kickBRASUser(String dmuserip ,String dmsession,String dmbras ,String dmuser) {
        KickBRASUserResp resp = null;
        KickBRASUserReq req = new KickBRASUserReq();
        BusinessConf conf = CommonInit.BUSCONF;
        req.setDmbras(dmbras);
        req.setDmsession(dmsession);
        req.setDmuserip(dmuserip);
        req.setDmuser(dmuser);
        ObsInterface obs = null;
        OBSInfo info = new OBSInfo();
        try {
            obs = new ObsInterface(conf.kickBRASUserIp(), conf.kickBRASUserPort());
            log.info("KickBRASUser_send:{"+req.encode()+"}");
            obs.sendMsg2Server(req.encode());
            String ret = obs.getMsgFromServer();
            log.info("KickBRASUser_result:{"+ret+"}");
            resp = (KickBRASUserResp) ResponseFactory.decode(ret, "KickBRASUserResp");
            info.setReturncode(resp.getReturncode());
            if("0".equals(resp.getReturncode()) ){
                info.setMessage("踢用户下线成功！");
            }else if("1".equals(resp.getReturncode())){
                info.setMessage("操作不成功！该用户不在线");
            }else {
                info.setMessage("踢用户下线失败！");
            }
        } catch (Exception e) {
            log.error("调用obs接口踢用户下线失败", e);
            resp = new KickBRASUserResp(req.getSerialno(), "-1", "调用obs接口踢用户下线失败");
        } finally {
            close(obs);
        }
        return info;
    }
    
    /**
     * 调用obs接口删除用户在线信息失败
     * @param kicktype
     * @param kickname
     * @return
     */
    public OBSInfo kickLMUser(String kicktype ,String kickname) {
        KickLMUserResp resp = null;
        KickLMUserReq req = new KickLMUserReq();
        BusinessConf conf = CommonInit.BUSCONF;
        req.setKickname(kickname);
        req.setKicktype(kicktype);
        ObsInterface obs = null;
        OBSInfo info = new OBSInfo();
        try {
            obs = new ObsInterface(conf.kickLMUserIp(), conf.kickLMUserPort());
            log.info("KickLMUser_send:{"+req.encode()+"}");
            obs.sendMsg2Server(req.encode());
            String ret = obs.getMsgFromServer();
            log.info("KickLMUser_result:{"+ret+"}");
            resp = (KickLMUserResp) ResponseFactory.decode(ret, "KickLMUserResp");
            info.setReturncode(resp.getReturncode());
            if("0".equals(resp.getReturncode()) ){
                info.setMessage("删除用户在线信息成功！");
            }else if("1".equals(resp.getReturncode())){
                info.setMessage("操作不成功！该用户不在线");
            }else {
                info.setMessage("删除用户在线信息失败！");
            }
        } catch (Exception e) {
            log.error("调用obs接口删除用户在线信息失败", e);
            resp = new KickLMUserResp(req.getSerialno(), "-1", "调用obs接口删除用户在线信息失败");
        } finally {
            close(obs);
        }
        return info;
    }
    
    /**
     * 调用obs运维接口查询basip用户在线数
     * @param brasip
     * @return
     */
    public CheckBasUserResp checkBasUser(String brasip) {
        CheckBasUserResp resp = null;
        CheckBasUserReq req = new CheckBasUserReq();
        BusinessConf conf = CommonInit.BUSCONF;
        ObsInterface obs = null;
        try {
            obs = new ObsInterface(conf.checkBasUserIp(), conf.checkBasUserPort());
            log.info("CheckBasUser_send:{"+req.encode()+"}");
            obs.sendMsg2Server(req.encode());
            String ret = obs.getMsgFromServer();
            log.info("CheckBasUser_result:{"+ret+"}");
            resp = (CheckBasUserResp) ResponseFactory.decode(ret, "CheckBasUserResp");
            resp.setTotal(findTotal(resp, brasip));
        } catch (Exception e) {
            log.error("调用obs运维接口查询basip用户在线数失败", e);
            resp = new CheckBasUserResp(req.getSerialno(), "-1", "调用obs接口查询basip用户在线数失败");
        } finally {
            close(obs);
        }
        return resp;
    }
    
    /**
     * 设置查询brasip的数量
     * @param resp
     * @param brasip
     * @return
     */
    private Integer findTotal(CheckBasUserResp resp, String brasip) {
        Map<String, Integer> map = resp.getNuminfo();
        if (map == null) {
            return 0;
        }
        for (Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().indexOf(brasip) >= 0) {
                return entry.getValue();
            }
        }
        return 0;
    }
    
    /**
     * 过滤brasip信息
     * @param record 
     * @param resp
     * @param brasip
     * @param brasTpye
     * @return
     */
    public List<CheckOnlineBasUserRecord> compareBasUser(CheckBasUserResp resp, String brasip,String brasTpye) {
        List<CheckOnlineBasUserRecord> record = new ArrayList<CheckOnlineBasUserRecord>();
        // 获取当前登录用户
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        // 获取当前登录用户地市权限
        List<MdRolePermissions> arealist = roleManageDAO.getRolePermissionByRoleid(user.getRoleid(),
                Constant.PERMISSION_CHILDREN_AREA);
        //获取naslist
        List<BdNas> naslist = CommonInit.getBdNasList();
        Map<String, Integer> map = resp.getNuminfo();
        if (map != null && !map.isEmpty()) {
            
            for (Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                String brasIp = key.substring(key.indexOf("<")+1, key.indexOf(","));
                String businessName = key.substring(key.indexOf(',')+2, key.indexOf('>'));
                Integer onlineNum = entry.getValue();

                CheckOnlineBasUserRecord info = new CheckOnlineBasUserRecord();
                info.setBusinessName(businessName);
                info.setOnlineNum(onlineNum);
                info.setBrasIp(brasIp);
                //
                for(BdNas nas : naslist){
                    if(nas.getNas_ip().equals(brasIp)){
                        String areano = nas.getArea_no();
                        info.setAreaname(nas.getAreaname());
                        info.setBrasTpye(nas.getModelname());
                        //判断areanno 是否在权限内
                        if(havePermission(areano, arealist)){
                            //过滤brasip
                            if(brasip != null && !brasip.equals("")){
                                if(brasip.indexOf(brasIp) >= 0){
                                    record.add(info);
                                    if(brasTpye != null && !brasTpye.isEmpty()){
                                        if(!brasTpye.equals(businessName)){
                                            record.remove(info);
                                        }
                                    }
                                }
                            }else {
                                record.add(info);
                                if(brasTpye != null && !brasTpye.isEmpty()){
                                    if(!brasTpye.equals(businessName)){
                                        record.remove(info);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return record;
    }
    
    // 查询用户是否有操作的权限
    public OBSInfo checkLimits(String brasip, String username) {
        OBSInfo info = new OBSInfo();
        // 获取当前登录用户
        MAdmin ma = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        // 获取当前登录用户地市权限
        List<MdRolePermissions> arealist = roleManageDAO.getRolePermissionByRoleid(ma.getRoleid(),
                Constant.PERMISSION_CHILDREN_AREA);
        String areano = batOptRecDAO.getArea(brasip);
        if (areano == null||areano.equals("")) {
            if (ma.getRoleid()!=null && ma.getRoleid().equals(CommonInit.BUSCONF.getRoleAdmin())) {
                info.setMessage("操作成功！");
                info.setReturncode("0");
                return info;
            }
        } else if (areano != null && !areano.isEmpty()) {
            if (havePermission(areano, arealist)) {
                info.setMessage("操作成功！");
                info.setReturncode("0");
                return info;
            }
        }
        info.setMessage("操作失败，没有权限！");
        info.setReturncode("1");
        return info;
    }
    
    /**
     * 调用obs接口查询用户在线总数
     * @return
     */
    public CheckOnlineUserResp checkOnlineUser() {
        CheckOnlineUserResp resp = null;
        CheckOnlineUserReq req = new CheckOnlineUserReq();
        BusinessConf conf = CommonInit.BUSCONF;
        ObsInterface obs = null;
        try {
            obs = new ObsInterface(conf.checkOnlineUserIp(), conf.checkOnlineUserPort());
            log.info("CheckOnlineUser_send:{"+req.encode()+"}");
            obs.sendMsg2Server(req.encode());
            String ret = obs.getMsgFromServer();
            log.info("CheckOnlineUser_result:{"+ret+"}");
            resp = (CheckOnlineUserResp) ResponseFactory.decode(ret, "CheckOnlineUserResp");
        } catch (Exception e) {
            log.error("调用obs接口查询用户在线总数失败", e);
            resp = new CheckOnlineUserResp(req.getSerialno(), "-1", "调用obs接口查询用户在线总数失败");
        } finally {
            close(obs);
        }
        return resp;
    }
    
    
    /**
     * 批量删除用户信息
     * 
     * @param type
     * @param opt
     * @return
     */
    public KickLMUserResp offlineKickBRASUser(String type, BatOptRec opt) {
        KickLMUserResp resp = null;
        KickLMUserReq req = new KickLMUserReq();
        req.setKicktype(type);
        req.setKickname(opt.getNasip());
        BusinessConf conf = CommonInit.BUSCONF;
        ObsInterface obs = null;
        try {
            obs = new ObsInterface(conf.kickLMUserIp(), conf.kickLMUserPort());
            log.info("KickLMUser_send:{" + req.encode() + "}");
            obs.sendMsg2Server(req.encode());
            String ret = obs.getMsgFromServer();
            log.info("KickLMUser_result:{" + ret + "}");
            resp = (KickLMUserResp) ResponseFactory.decode(ret, "KickLMUserResp");
        } catch (Exception e) {
            log.error("调用obs接口删除用户在线信息失败", e);
            resp = new KickLMUserResp(req.getSerialno(), "-1", "调用obs接口删除用户在线信息失败");
        } finally {
            close(obs);
        }
        insertOpt(opt, resp, req);
        return resp;
    }

    private void insertOpt(BatOptRec opt, KickLMUserResp resp, KickLMUserReq req) {
        opt.setSerno(req.getSerialno());
        opt.setOpttype(req.getCommand());
        opt.setReturncode(resp.getReturncode());
        opt.setResultfile(resp.getErrordescription());
        batOptRecDAO.insert(opt);
    }

    public List<OfflineLog> getBatOptLog(String startdate, String enddate, String admin,
            String opttype, String brasip, String date) {
        return batOptRecDAO.getBatOptLog(startdate, enddate, admin, opttype, brasip, date);
    }

    public List<OptRecord> query(String opttype, String username) {
        List<OptRecord> result = batOptRecDAO.getByOpt(opttype);
        return compareBraip(result, username);
    }

    private List<OptRecord> compareBraip(List<OptRecord> ret, String username) {
        // 获取当前登录用户
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        // 获取当前登录用户地市权限
        List<MdRolePermissions> arealist = roleManageDAO.getRolePermissionByRoleid(user.getRoleid(),
                Constant.PERMISSION_CHILDREN_AREA);
        // 获取naslist
        List<BdNas> naslist = CommonInit.getBdNasList();
        List<OptRecord> record = new ArrayList<OptRecord>();
        for (OptRecord opt : ret) {
            String roleId = user.getRoleid();
            String roleAdmin = CommonInit.BUSCONF.getRoleAdmin();
            if (StringUtils.equals(roleId, roleAdmin)) {
                record.add(opt);
            } else {
                for (BdNas nas : naslist) {
                    String nasIp = nas.getNas_ip();
                    String optNasIp = opt.getNasip();
                    if (StringUtils.indexOf(nasIp, optNasIp) >= 0) {
                        String areano = nas.getArea_no();
                        if (havePermission(areano, arealist)) {
                            record.add(opt);
                        }
                    }
                }
            }
        }
        log.debug("OptRecord record list : {}", record);
        return record;
    }
    
    /**
     * 认证日志查询
     * @param account
     * @param querydate
     * @return
     */
    public List<AccessLog> qryAccessLog(String account, String querydate) {
        /**
         * account ： 组成查询条件：md5加密后转大写+account
         */
        log.info("receve param: [account : {}, querydate : {}]", account, querydate);
        String query_accunt = MD5Util.md5(account).toUpperCase() + account;
        HBaseUtils.getInstance();
        log.info("hbase init successfully");
        HbaseParm request = new HbaseParm();
        request.setAccount(query_accunt);
        request.setQuerydate(querydate);
        List<AccessLog> access = HBaseUtils.getAccessLogGdRows(request);
        return access;
    }
    
    public List<String> callShell(String shellString, String account) {
        List<String> datas = new ArrayList<String>();
        try {
            Process process = Runtime.getRuntime().exec(shellString);
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine())!= null){
                log.info("callShell:"+line);
                if(line.contains("value="+account)){
                    datas.add(line);
                }
            }
            process.waitFor();
            is.close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            log.error("call [{}] failed, reason : {}", shellString, e);
        }
        return datas;
    }
    
    
    public List<OfflineLog> getOfflineLog(String startdate, String enddate, String username,
            String admin, String opttype, String brasip, String date) {
        List<OfflineLog> list = new ArrayList<OfflineLog>();
        // 获取当前登录用户
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        String roleAdmin = CommonInit.BUSCONF.getRoleAdmin();
        if (StringUtils.equals(roleAdmin, user.getRoleid())) {
            list = batOptRecDAO.getBatOptLog(startdate, enddate, admin, opttype, brasip, date);
        } else {
            list = batOptRecDAO.getBatOptLog(startdate, enddate, username, opttype, brasip, date);
        }
        return list;
    }

}
