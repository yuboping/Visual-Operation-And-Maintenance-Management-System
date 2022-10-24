package com.asiainfo.lcims.omc.web.ctrl.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.shcm.StatisData;
import com.asiainfo.lcims.omc.model.shcm.UserOnlineAndOfflineEchartData;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.shcm.UserOnlineAndOfflineService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.ExcelUtil;

/**
 * 上海移动:用户上下线报表
 *
 */
@Controller
@RequestMapping(value = "/view/class/shcmreport/UserOnlineAndOfflineView")
public class UserOnlineAndOfflineView extends BaseController{
    private static final Logger LOG = LoggerFactory.getLogger(UserOnlineAndOfflineView.class);

    @Resource(name = "userOnlineAndOfflineService")
    UserOnlineAndOfflineService userOnlineAndOfflineService;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;
   
    @RequestMapping(value = "")
    public String moduleMonitor(HttpServletRequest request, HttpSession session, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());

         if (MainServer.conf.getProvince() != null) {
             model.addAttribute("now", new Date());
             model.addAttribute("province", MainServer.conf.getProvince());
         }
     
        return "shcm/UserOnlineAndOfflineView";
    }
    
    /**
     * 查询告警级别统计信息
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<StatisData> queryInfo(@RequestParam("queryTime") String queryTime,@RequestParam("queryType") int queryType,HttpServletRequest request) {
    	List<StatisData> data=new ArrayList<>();
    	if(queryTime==null || queryTime.equals("")) {
    		return data;
    	}
    	SimpleDateFormat format = null;
    	if(queryType==1) {
    		format = new SimpleDateFormat("yyyy-MM-dd");
    	}else {
    		format = new SimpleDateFormat("yyyy-MM");
    	}
    	SimpleDateFormat format2=new SimpleDateFormat("MM");
    	String[] dates=queryTime.split("~");
    	String startDate=dates[0].trim();
    	String endDate=dates[1].trim();
    	List<String> dateList=doDate(queryType,startDate,endDate,format,format2);
    	Set<String> dateset=new HashSet<>(dateList);
    	try {
        	data=userOnlineAndOfflineService.queryUserOnlineAndOffline(dateset,startDate,endDate,queryType);
		} catch (Exception e) {
			LOG.error("queryInfo error:",e);
		}
    	return data;
    }
    @RequestMapping(value = "/queryEchart")
    @ResponseBody
    public UserOnlineAndOfflineEchartData queryEchart(@RequestParam("queryTime") String queryTime,@RequestParam("queryType") int queryType,HttpServletRequest request) {
    	UserOnlineAndOfflineEchartData chartData=new UserOnlineAndOfflineEchartData();
    	if(queryTime==null || queryTime.equals("")) {
    		return chartData;
    	}
    	SimpleDateFormat format = null;
    	if(queryType==1) {
    		format = new SimpleDateFormat("yyyy-MM-dd");
    	}else {
    		format = new SimpleDateFormat("yyyy-MM");
    	}
    	SimpleDateFormat format2=new SimpleDateFormat("MM");
    	
    	String[] dates=queryTime.split("~");
    	String startDate=dates[0].trim();
    	String endDate=dates[1].trim();
    	//用来做x轴数据的
    	List<String> xdateList=doDate(queryType,startDate,endDate,format,format);
    	chartData.setRateData(xdateList);
    	//用来查询数据的
    	List<String> dateList=doDate(queryType,startDate,endDate,format,format2);
    	Set<String> dataset=new HashSet<>(dateList);
    	List<StatisData> data=userOnlineAndOfflineService.queryUserOnlineAndOffline(dataset,startDate,endDate,queryType);
    	Map<String,List<String>> ydata=new HashMap<>();
    	Map<String,List<StatisData>> ydataStatisData=new HashMap<>();
    	if(data!=null && data.size()>0) {
    		for (StatisData statisData : data) {
    			List<StatisData> datalist=ydataStatisData.get(statisData.getAttr1());
    			if(datalist==null) {
    				datalist=new ArrayList<>();
    				ydataStatisData.put(statisData.getAttr1(), datalist);
    			}
    			datalist.add(statisData);
			}
    	}
    	for(Map.Entry<String, List<StatisData>> entry : ydataStatisData.entrySet()){
    	    String mapKey = entry.getKey();
    	    List<StatisData> mapValue = entry.getValue();
    	    List<String> yydata=new ArrayList<>();
    	    for (int i = 0; i < xdateList.size(); i++) {
				String xdate=xdateList.get(i);
				String datay="0";
				for(StatisData StatisData : mapValue) {
					if(xdate.equals(StatisData.getAttr4())) {
						datay=StatisData.getAttr2();
						break;
					}
				}
				yydata.add(datay);
			}
    	    ydata.put(mapKey, yydata);		
			
    	}
    	chartData.setyData(ydata);
    	
    	return chartData;
    }

    @RequestMapping(value = "/export")
    @ResponseBody
    public void exportBdNas(@RequestParam("queryTime") String queryTime,@RequestParam("queryType") int queryType,HttpServletRequest request, HttpServletResponse response) {
    	List<StatisData> data=queryInfo(queryTime,queryType,request);
    	String[] fields = { "attr4&时间", "attr1&用户下线原因", "attr2&次数"};
    	ExcelUtil.downloadExcelToFile("用户上下线报表", null, fields, request, response, data);
    	
    }
    /**
     * 处理时间: 将时间区间处理成时间点
     * @param queryType 1:按日处理  其他按月处理(这里小心)
     * @param startDate 
     * @param endDate
     * @param format
     * @return
     */
    public  List<String> doDate(int queryType,String startDate,String endDate,SimpleDateFormat format,SimpleDateFormat format2){
    	List<String> date=new ArrayList<>();
        try {
        	Calendar calendar = Calendar.getInstance();
        	Date start=format.parse(startDate);
			Date end=format.parse(endDate);
        	do {
        		if(start.getTime() == end.getTime()) {
        			date.add(format2.format(start));
        			break;
    			}
        		date.add(format2.format(start));
        		calendar.setTime(start);
        		if(queryType==1) {
        			calendar.add(Calendar.DAY_OF_MONTH,1);
        		}else {
        			calendar.add(Calendar.MONTH,1);
        		}
        		start=calendar.getTime();
        	}while(true);
        	
		} catch (Exception e) {
			LOG.error("doDate error{}",e);
		}
    	return date;
    }

}
