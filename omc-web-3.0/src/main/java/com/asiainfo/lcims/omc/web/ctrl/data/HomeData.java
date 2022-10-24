package com.asiainfo.lcims.omc.web.ctrl.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.model.hncu.SwitchBean;
import com.asiainfo.lcims.omc.model.hncu.TopuModel;
import com.asiainfo.lcims.omc.model.serverlist.HomeAlarmBean;
import com.asiainfo.lcims.omc.model.serverlist.HostBean;
import com.asiainfo.lcims.omc.model.serverlist.ServerListModel;
import com.asiainfo.lcims.omc.model.serverlist.ServerTypeBean;
import com.asiainfo.lcims.omc.model.serverlist.ServerlistBean;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.system.HomeService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 首页业务数据
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/data/home")
public class HomeData extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeData.class);

    @Resource(name = "homeService")
    HomeService homeService;
    
    @Inject
    MenuService menuService;

    /**
     * 
     * @Title: queryRadiusProcess
     * @Description: TODO(radius进程状态)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/radius/radiusprocess")
    @ResponseBody
    public Map<String, Object> queryRadiusProcess(HttpServletRequest request, HttpSession session,
            Model model) {
        Map<String, Object> map = null;
        try {
            map = homeService.getRadiusProcess();
        } catch (Exception e) {
            LOG.error("HomeData queryRadiusProcess Exception:{}", e);
        }
        return map;
    }

    /**
     * 
     * @Title: queryRadiusAnalytic
     * @Description: TODO(radius解析量)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/radius/radiusanalytic")
    @ResponseBody
    public Map<String, Object> queryRadiusAnalytic(HttpServletRequest request, HttpSession session,
            Model model) {
        Map<String, Object> map = null;
        try {
            map = homeService.getRadiusAnalytic();
        } catch (Exception e) {
            LOG.error("HomeData queryRadiusAnalytic Exception:{}", e);
        }
        return map;
    }

    /**
     * 
     * @Title: queryHostNetConnectable
     * @Description: TODO(查询主机连通性)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/host/netconnectable")
    @ResponseBody
    public Map<String, Object> queryHostNetConnectable(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = null;
        try {
            map = homeService.getHostNetConnectable();
        } catch (Exception e) {
            LOG.error("HomeData queryHostNetConnectable Exception:{}", e);
        }
        return map;
    }

    /**
     * 
     * @Title: queryHostState
     * @Description: TODO(查询主机状态)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/host/state")
    @ResponseBody
    public Map<String, Object> queryHostState(HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> map = null;
        try {
            map = homeService.getHostState();
        } catch (Exception e) {
            LOG.error("HomeData queryHostState Exception:{}", e);
        }
        return map;
    }
    /***
     * 家宽认证响应成功率 gscm
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/host/gscm/homeAuthentication",method = RequestMethod.POST)
    @ResponseBody
    public String queryHomeAuthentication(HttpServletRequest request,
            HttpSession session, Model model) {
       String map = null;
        try {
            map = homeService.queryHomeAuthentication();
        } catch (Exception e) {
            LOG.error("HomeData queryHostState Exception:{}", e);
        }
        return map;
    }
    
    /**
     * 首页 服务器列表信息查询 jscm
     * @return
     */
    @RequestMapping(value = "/server/serverlist")
    @ResponseBody
    public List<ServerListModel> queryServerList() {
    	
    	List<ServerListModel> serverListModels=new ArrayList<ServerListModel>();
    	List<ServerlistBean> serverList=homeService.queryServerList();
    	List<ServerlistBean> serverHostList=homeService.queryServerHostList();
    	
    	HashMap<String, ServerListModel> hash=new HashMap<String, ServerListModel>();
    	if(serverList!=null && serverList.size()>0) {
    		//把查出来的数据以nodeid为key,把数据放入hash中,
	    	for (ServerlistBean serverlistBean : serverList) {
	    		ServerListModel serverListModelhash=hash.get(serverlistBean.getNodeId());
	    		if(serverListModelhash==null) {
	    			ServerListModel serverListModel=new ServerListModel();
	    			serverListModel.setNodeId(serverlistBean.getNodeId());
	    			serverListModel.setNodeName(serverlistBean.getNodeName());
	    			List<ServerTypeBean> servertypelist=new ArrayList<ServerTypeBean>();
	    			ServerTypeBean serverTypeBean=new ServerTypeBean();
	    			serverTypeBean.setServerType(serverlistBean.getServerType());
	    			serverTypeBean.setServerName(serverlistBean.getServerName());
	    			servertypelist.add(serverTypeBean);
	    			serverListModel.setServerType(servertypelist);
	    			hash.put(serverlistBean.getNodeId(), serverListModel);
	    		}else {
	    			List<ServerTypeBean> servertypelisthash=serverListModelhash.getServerType();
	    			ServerTypeBean serverTypeBean=new ServerTypeBean();
	    			serverTypeBean.setServerType(serverlistBean.getServerType());
	    			serverTypeBean.setServerName(serverlistBean.getServerName());
	    			servertypelisthash.add(serverTypeBean);
	    			serverListModelhash.setServerType(servertypelisthash);
	    			serverListModelhash.setNodeId(serverlistBean.getNodeId());
	    			serverListModelhash.setNodeName(serverlistBean.getNodeName());
	    			hash.put(serverListModelhash.getNodeId(), serverListModelhash);
	    		}
			}
	    	if(serverHostList!=null && serverHostList.size()>0) {
	    		for (ServerlistBean serverhostlistBean : serverHostList) {
	    			ServerListModel servernodelist=hash.get(serverhostlistBean.getNodeId());
	    			List<ServerTypeBean> servertypelisthost=servernodelist.getServerType();
	    			for (ServerTypeBean serverlistBean2 : servertypelisthost) {
	    				String servertypehost=serverhostlistBean.getServerType();
	    				String servertype=serverlistBean2.getServerType();
						if(servertypehost!=null && servertypehost.equals(servertype)) {
							List<HostBean> hosts=serverlistBean2.getHostList();
							if(hosts==null) {
								hosts=new  ArrayList<HostBean>();
							}
							HostBean hostBean=new HostBean();
							hostBean.setHostId(serverhostlistBean.getHostId());
							hostBean.setIp(serverhostlistBean.getAddr());
							hostBean.setName(serverhostlistBean.getHostName());
							hosts.add(hostBean);
							serverlistBean2.setHostList(hosts);
						}
					}
				}
	    	}
	    	
	    	
	    	
	    	for(String key:hash.keySet()) {
	    		serverListModels.add(hash.get(key));
	    	}
	    	
    	}else {
    		LOG.error("服务器列表查询数据为空!");
    	}
    	
    	return serverListModels;
    }
    
    /***
     * 首页 告警查询 jscm
     * @param request
     * @param session
     * @param model
     * @return
     */
    /**
     * 先写逻辑:
     * 1.查询告警信息表 MD_ALARM_INFO M WHERE M.ALARM_NUM > 0  AND M.DELETE_STATE = 0 根据指标id连接指标表,查询出指标的服务类型
     * 2.根据MD_ALARM_INFO的url分析:  DIMENSION_TYPE:维度字段
     * 		2.1 /view/class/server/module/device/node/host--/nodeid/hostid  一个节点下面的一个主机 (server会变),查出主机ip 12
     * 		2.2 /view/class/server/module/device/node 全部节点报警,给前台一个标记,全部报警  10
     * 		2.3 /view/class/server/module/device/node--/nodeid 单个节点下面的所有主机报警,给前台标记  11
     * 		2.4 /view/class/server/module/device/host 全部主机报警,给前台一个标记,全部报警 8
     * 		2.5 /view/class/server/module/device/host--/hostid  单个主机报警 9
     * 3.如果出现上面 2.1和2.5的情况,根据指标的业务类型,
     * 		3.1 如果是主机业务,就根据主机ip告警,
     * 		3.2 如果非主机业务,根据业务类型+主机ip告警
     */
    @RequestMapping(value = "/host/gscm/homeAlarmQuery")
    @ResponseBody
    public List<HomeAlarmBean> queryHomeAlarm(HttpServletRequest request,
            HttpSession session, Model model) {
    	List<HomeAlarmBean> alarmlist = null;
        try {
        	List<String> urlList=getUrlList();
        	alarmlist = homeService.queryHomeAlarm(urlList);
        } catch (Exception e) {
            LOG.error("HomeData queryHomeAlarm Exception:{}", e);
        }
        return alarmlist;
    }
    /**
     * 江苏移动 和 河南联通 拓扑图告警查询
     * @param request
     * @param session
     * @param model
     * @param alarmId
     * @return
     */
    @RequestMapping(value = "/host/gscm/homeAlarmQueryById")
    @ResponseBody
    public List<HomeAlarmBean> homeAlarmQueryById(HttpServletRequest request,
            HttpSession session, Model model,@RequestParam("alarmId") String alarmId) {
    	List<HomeAlarmBean> alarmlist = null;
        try {
        	List<String> urlList=getUrlList();
        	alarmlist = homeService.homeAlarmQueryById(alarmId,urlList);
        } catch (Exception e) {
            LOG.error("HomeData queryHostState Exception:{}", e);
        }
        return alarmlist;
    }
    /**
     *  江苏移动 和 河南联通 首页服务器数量查询
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/host/gscm/mdHostNumber")
    @ResponseBody
    public int mdHostNumber(HttpServletRequest request,
            HttpSession session, Model model) {
    	int number=0;
        List<MonHost> hosts = CommonInit.getMonHostList();
        if (hosts != null) {
            number = hosts.size();
    	}
        return number;
    }
    
    /**
     *  江苏移动 和 河南联通 首页告警级别数量查询
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/host/gscm/queryAlarmByAlarmlevel")
    @ResponseBody
    public List<HomeAlarmBean> queryAlarmByAlarmlevel(HttpServletRequest request,
            HttpSession session, Model model) {
    	List<HomeAlarmBean> alarmlist = null;
        try {
        	List<String> urlList=getUrlList();
        	alarmlist = homeService.queryAlarmByAlarmlevel(urlList);
        } catch (Exception e) {
            LOG.error("HomeData queryAlarmByAlarmlevel Exception:{}", e);
        }
        return alarmlist;
    }
    
    public List<String> getUrlList(){
    	List<String> urlList=new ArrayList<String>();
        Set<String> urlSet = new HashSet<String>();
        //获取全部菜单
        List<MdMenu> mdMenuList = menuService.getAllMenu();
        //将url放入urlList
        if(mdMenuList!=null && mdMenuList.size()>0) {
        	  for(MdMenu mdMenu: mdMenuList){
              	urlSet.add(mdMenu.getUrl());
              }
        	  for (String url : urlSet) {
        		  urlList.add(url);
			}
        }
        return urlList;
    }
    
    
    @RequestMapping(value = "/hncu/topuquery")
    @ResponseBody
    public TopuModel hncuQueryTopu() {
    	TopuModel topuModel=new TopuModel();
    	 List<MonHost> hosts = CommonInit.getMonHostList();
    	 List<MonHost> firewall=new ArrayList<>();
    	 List<SwitchBean> switchs=new ArrayList<>();
    	 List<MonHost> host254=new ArrayList<>();
    	 List<MonHost> host250=new ArrayList<>();
    	 List<MonHost> host251=new ArrayList<>();
    	 
         if (hosts != null) {
        	 Log.info("主机列表为:"+hosts.toString());
            for (MonHost monHost : hosts) {
				switch (monHost.getHosttype()) {
				case "14": //交换机 现场是:14  197数据库是:8
					if("192.169.0.254".equals(monHost.getAddr()) || "192.169.0.250".equals(monHost.getAddr())
							|| "192.169.0.251".equals(monHost.getAddr())) {
						SwitchBean switchBean=new SwitchBean();
						switchBean.setHostid(monHost.getHostid());
						switchBean.setAddr(monHost.getAddr());
						switchBean.setHostname(monHost.getHostname());
						switchs.add(switchBean);
					}
					break;
				case "13"://防火墙  现场是:13  197数据库是:9
					if("192.169.1.252".equals(monHost.getAddr()) || "192.169.1.253".equals(monHost.getAddr())) {
						firewall.add(monHost);
					}
					break;
				default: //主机
					if(monHost.getAddr().startsWith("192.169.0")) {
						String[] ips=monHost.getAddr().split("\\.");
						Integer lastadd=Integer.valueOf(ips[3]);
						if((lastadd>=1 && lastadd<=17) || lastadd==20) {
							host254.add(monHost);
						}else if(lastadd>=21 && lastadd<=25){
							host250.add(monHost);
						}else if(lastadd>=26 && lastadd<=30){
							host251.add(monHost);
						}
	        		}
					break;
				}
			}
           if(!switchs.isEmpty()) {
        	   for (SwitchBean switchhost : switchs) {
				if("192.169.0.254".equals(switchhost.getAddr())) {
					switchhost.setHost(host254);
					 Log.info("254交换机主机列表为:"+host254.toString());
				}else if("192.169.0.250".equals(switchhost.getAddr())) {
					switchhost.setHost(host250);
					 Log.info("250交换机主机列表为:"+host250.toString());
				}else if("192.169.0.251".equals(switchhost.getAddr())) {
					switchhost.setHost(host251);
					 Log.info("251交换机主机列表为:"+host251.toString());
				}
			}
           }
           topuModel.setFirewall(firewall);
           topuModel.setSwitchs(switchs);
     	}else {
     		Log.info("主机列表为空");
     	}
    	
    	return topuModel;
    }
    
    
    
    
}
