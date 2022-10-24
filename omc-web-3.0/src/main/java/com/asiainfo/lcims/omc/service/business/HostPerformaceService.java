package com.asiainfo.lcims.omc.service.business;
/**
 * 采集service公用
 */

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.business.MdHostPerformance;
import com.asiainfo.lcims.omc.model.business.MdHostPerformanceALarm;
import com.asiainfo.lcims.omc.model.business.MdMetricDataSingle;
import com.asiainfo.lcims.omc.model.business.MdMetricValue;
import com.asiainfo.lcims.omc.model.system.MdRolePermissions;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.RoleManageDAO;
import com.asiainfo.lcims.omc.persistence.monitor.MetricDataDAO;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.TimeControl;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Service(value = "hostPerformaceService")
public class HostPerformaceService {
    private static final Logger LOG = LoggerFactory.getLogger(HostPerformaceService.class);
    @Inject
    private RoleManageDAO roleManageDAO;
    
    @Inject
    private MetricDataDAO metricDataDAO;
    
    @Resource(name = "menuService")
    MenuService menuService;
    /**
     * 根据角色id获取主机性能信息
     * @param roleid
     * @return
     */
	public WebResult getHostPerformance(String roleid) {
	    WebResult result = new WebResult(true, null);
	    try {
	        //查询节点
	        String nodeids = null;
	        if(!roleid.equals(Constant.ADMIN_ROLEID)){
	            //根据节点信息来查询对应主机信息
	            List<MdRolePermissions> nodeidlist = roleManageDAO.getRolePermissionByRoleid(roleid, Constant.PERMISSION_CHILDREN_NODE);
	            if(ToolsUtils.ListIsNull(nodeidlist))
	                return result;
	            nodeids = getInPermissionids(nodeidlist);
	        }
	        String menuName = CommonInit.conf.getValueOrDefault("hostperformance_menuname", "hostinfo");
	        //指标id
	        String cpu_metricid = CommonInit.getMetricIdByMetricIdentity(CommonInit.BUSCONF.getStringValue("metric_cpu"));
	        String momery_metricid = CommonInit.getMetricIdByMetricIdentity(CommonInit.BUSCONF.getStringValue("metric_memory"));
	        String process_metricid = CommonInit.getMetricIdByMetricIdentity(CommonInit.BUSCONF.getStringValue("metric_process"));
	        String connectable_metricid = CommonInit.getMetricIdByMetricIdentity(CommonInit.BUSCONF.getStringValue("metric_sys_net_connectable"));
	        //当前周期时间
	        String queryDate = TimeControl.getCycleTime("", 0, null,null);
	        //查询主机、节点、cpu、内存、连通性 指标信息
	        List<MdHostPerformance> datalist = metricDataDAO.getHostPerformance(cpu_metricid,momery_metricid,
	                connectable_metricid,queryDate,nodeids,menuName);
	        //查询主机下 cpu、内存、连通性、进程类 指标 是否存在告警
	        String metricids = "'"+cpu_metricid+"'," + "'"+momery_metricid+"'," + "'"+process_metricid+"'," + "'"+connectable_metricid+"'" ;
	        List<MdHostPerformanceALarm> alarmlist = metricDataDAO.getHostPerformanceALarm(metricids);
	        //查询主机进程信息
	        List<MdMetricDataSingle> processdatas = metricDataDAO.getHostMetricInfos(process_metricid, queryDate);
	        
	        //对cpu、内存、连通性 指标判断是否存在告警
	        hostPerformaceIsExitAlarm(datalist,alarmlist);
	        //判断进程类是否有告警
	        hostPerformaceProcessIsExitAlarm(process_metricid,datalist,processdatas,alarmlist);
	        LOG.info("homepage hostinfo data query!");
	        result.setData(datalist);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
	    return result;
	}
	
	private String getInPermissionids(List<MdRolePermissions> list){
		StringBuilder strb = new StringBuilder();
		for (MdRolePermissions mdRolePermissions : list) {
			strb.append("'"+mdRolePermissions.getPermissionid()+"',");
		}
		String insql = strb.toString();
		insql = insql.substring(0, insql.length()-1);
		return insql;
	}
	
	private void hostPerformaceIsExitAlarm(List<MdHostPerformance> datalist, List<MdHostPerformanceALarm> alarmlist){
		for (MdHostPerformance hostPerformance : datalist) {
		    String hostid = hostPerformance.getHostid();
			//对cpu进行判断
		    hostPerformance.setCpu(new MdMetricValue().setAlarmflag(false).setMvalue(hostPerformance.getCpu_value()));
		    isExitAlarm(hostid,hostPerformance.getCpu_metricid(),null,alarmlist,hostPerformance.getCpu());
			//对内存
			hostPerformance.setMemory(new MdMetricValue().setAlarmflag(false).setMvalue(hostPerformance.getMemory_value()));
			isExitAlarm(hostid,hostPerformance.getMemory_metricid(),null,alarmlist,hostPerformance.getMemory());
			//连通性
			hostPerformance.setConnectable(new MdMetricValue().setAlarmflag(false).setMvalue(hostPerformance.getConnectable_value()));
			isExitAlarm(hostid,hostPerformance.getConnectable_metricid(),null,alarmlist,hostPerformance.getConnectable());
		}
	}
	
	private boolean isExitAlarm(String hostid,String metricid,String item,List<MdHostPerformanceALarm> alarmlist, MdMetricValue metricValue){
		boolean flag = false;
		if(ToolsUtils.StringIsNull(item)){
			for (MdHostPerformanceALarm alarm : alarmlist) {
				if(hostid.equals(alarm.getHostid()) && metricid.equals(alarm.getMetric_id())){
					flag = true;
					metricValue.setAlarmflag(true);
					metricValue.setAlarm_level(alarm.getAlarm_level());
					break;
				}
			}
		}else{
			for (MdHostPerformanceALarm alarm : alarmlist) {
				if(hostid.equals(alarm.getHostid()) && metricid.equals(alarm.getMetric_id())
						&& item.equals(alarm.getAttr())){
				    metricValue.setAlarm_level(alarm.getAlarm_level());
				    metricValue.setAlarmflag(true);
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	/**
	 * 进程信息是否存在告警
	 * @param datalist
	 * @param processdatas
	 * @param alarmlistalarmlist
	 */
	private void hostPerformaceProcessIsExitAlarm(String process_metricid,List<MdHostPerformance> datalist,
	        List<MdMetricDataSingle> processdatas,List<MdHostPerformanceALarm> alarmlist){
	    // 进程信息关联到主机性能信息中
	    for (MdHostPerformance hostPerformance : datalist) {
	        if(null == hostPerformance.getProcess()){
                hostPerformance.setProcess(new MdMetricValue().setAlarmflag(false));
            }
            for (MdMetricDataSingle processinfo : processdatas) {
                if(hostPerformance.getHostid().equals(processinfo.getHostid()) && process_metricid.equals(processinfo.getMetric_id())){
                    //判断此进程是否存在告警
                    MdMetricValue value = new MdMetricValue().setItem(processinfo.getItem()).setItemname(processinfo.getItemname())
                            .setMvalue(processinfo.getMvalue()).setAlarmflag(false);
                    if( isExitAlarm(hostPerformance.getHostid(),process_metricid,processinfo.getItem(),alarmlist, value) ){
                        value.setAlarmflag(true);
                        hostPerformance.getProcess().setAlarmflag(true);
                    }
                    hostPerformance.getProcesslist().add(value);
                }
            }
        }
	}
}
