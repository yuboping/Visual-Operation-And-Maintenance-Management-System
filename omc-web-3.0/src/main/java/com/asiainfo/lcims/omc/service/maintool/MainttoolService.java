package com.asiainfo.lcims.omc.service.maintool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.maintool.MdMaintOperateLog;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.configmanage.MonHostDAO;
import com.asiainfo.lcims.omc.persistence.maintool.MainttoolDAO;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "mainttoolService")
public class MainttoolService {
    private static final Logger LOG = LoggerFactory.getLogger(MainttoolService.class);
    @Autowired
    private MainttoolDAO mainttoolDAO;
    
    @Autowired
    private MonHostDAO monHostDAO;
    
    @Autowired
    private MdParamDAO paramDAO;
    
    public static final String RADIUS_OPERATE_PARAM_TYPE = "36";
    
    
    public List<MonHost> queryRadiusHostInfo(String nodeid) {
        // 获取radius主机类型  2
        int hostType = CommonInit.BUSCONF.getIntValue("radius_host_type", 2);
        List<MonHost> list = monHostDAO.getHostByNodeHostType(nodeid, hostType);
        return list;
    }
    
    /**
     * 直通、恢复操作
     * @param operateType
     * @param ipdatas
     * @return
     */
    public WebResult operateRadiusBusiness(String operateType, String ipdatas) {
        WebResult result = new WebResult(true, "操作成功");
        try {
            List<MdParam> params = paramDAO.getParamByType(RADIUS_OPERATE_PARAM_TYPE);
            MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
            String username = user.getAdmin();
            LOG.info("用户"+username+"执行"+getOperateRadiusName(operateType, params)+"操作：ip "+ipdatas);
            String uuid = UUID.randomUUID().toString();
            result.setData(uuid);
            ObjectMapper mapper = new ObjectMapper();
            List<String> list = mapper.readValue(ipdatas, new TypeReference<List<String>>() {});
            List<MdMaintOperateLog> datas = new ArrayList<MdMaintOperateLog>();
            Timestamp creatTime = new Timestamp(new Date().getTime());
            StringBuffer ips = new StringBuffer("");
            for (String ip : list) {
                // 组装 MAINT_OPERATE_LOG
                datas.add(new MdMaintOperateLog().setId(IDGenerateUtil.getUuid()).setHost_ip(ip)
                        .setUuid(uuid).setOperate_state(2).setOperate_user(username)
                        .setCreate_time(creatTime).setOperate_type(Integer.parseInt(operateType)));
                ips.append(ip).append(";");
            }
            if(!ToolsUtils.ListIsNull(datas)) {
                // 批量入库操作 MAINT_OPERATE_LOG
                String selectSql = DbSqlUtil.getSqlQuery();
                if(DbSqlUtil.isOracle()) {
                    mainttoolDAO.addBattchOperateLogForOracle(datas, selectSql);
                } else {
                    mainttoolDAO.addBattchOperateLogForMysql(datas, selectSql);
                }
                String ipsStr = ips.toString();
                ipsStr = ipsStr.substring(0, ipsStr.length()-1);
                //调用socket接口
                //数据样例： 1|||874b36b1-6e6d-4d66-8aba-16ec49599ef9|||1.1.1.1;2.2.2.2;3.3.3.3;4.4.4.4\n
                StringBuffer msg = new StringBuffer("");
                msg.append(operateType).append(Constant.RADIUS_OPERATE_SPLIT_STR)
                    .append(uuid).append(Constant.RADIUS_OPERATE_SPLIT_STR)
                    .append(ipsStr).append(Constant.RADIUS_OPERATE_END);
                //线程调用下发接口
                LOG.info("修改调用3A接口----uuid{"+uuid+"} start");
                new Thread(new RadiusOperateRunable(mainttoolDAO, uuid, msg.toString())).start();
//              List<String> infos = new ArrayList<String>();
//              infos.add(msg.toString());
//              SimpleChatClient.sendMsg(infos,null);
              LOG.info("修改调用3A接口----uuid{"+uuid+"} end");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            result.setOpSucc(false);
            result.setMessage("操作失败");
        }
        return result;
    }
    
    private String getOperateRadiusName(String operateType,List<MdParam> params) {
        String name = "";
        for (MdParam param : params) {
            if(param.getCode().equals(operateType)) {
                name = param.getDescription();
                break;
            }
        }
        return name;
    }
    
    /**
     * 根据uuid查询执行结果
     * 0 执行成功、1执行失败  2 执行中
     * @param uuid
     * @return
     */
    public WebResult queryOperateRadiusBusinessResult(String uuid) {
        WebResult result = new WebResult(true, "查询成功");
        try {
            List<MdMaintOperateLog> data = mainttoolDAO.queryOperateRadiusBusinessResult(uuid);
            result.setData(data);
        } catch (Exception e) {
            result.setOpSucc(false);
            result.setMessage("查询失败");
            result.setData(null);
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
    
}
