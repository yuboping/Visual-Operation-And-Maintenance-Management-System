package com.asiainfo.lcims.omc.report.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.alarm.model.MdCollCycleTime;
import com.asiainfo.lcims.omc.alarm.model.MdMetric;
import com.asiainfo.lcims.omc.alarm.param.InitParam;
import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.omc.report.dao.ReportMetricDataDAO;
import com.asiainfo.lcims.omc.report.model.FileTarget;
import com.asiainfo.lcims.omc.report.model.ReportChartData;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.FileOperate;

public class GscmUploadService extends AbstractUploadService{

    private static final Logger LOG = LoggerFactory.make();
    
    private Map<String,List<ReportChartData>> reportData = new HashMap<String, List<ReportChartData>>();
    
    private String lastWeeks;
    @Override
    public FileTarget makeUploadTarget(UploadConf conf, List<MdCollCycleTime> cycleTimelist) {
      //查询指标值信息
        String metricIdentitys = conf.getUploadMetricIdentitys();
        String[] metricIdentityArr = metricIdentitys.split(",");
        String stime = DateTools.DEFAULT.getCurrentDate();
        Calendar cal = Calendar.getInstance();
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);
        int weekdays = 0;
        int lastWeeksday = 0;
        int lastmonth = 0;
        int lastyear = 0;
        String fileName = null;
        List<ReportChartData> datas = null;
        // 判断当前日期是否是每个月倒数第三天
        weekdays = DateTools.DEFAULT.getLastButTwoDay(nowMonth, year);
        LOG.info("today is {}, last but two day is {}", nowDay, weekdays);
        if (nowMonth == 1) {
            lastmonth = 12;
            lastyear = year - 1;
        } else {
            lastmonth = nowMonth - 1;
            lastyear = year;
        }
        lastWeeksday = DateTools.DEFAULT.getLastButTwoDay(lastmonth, lastyear);
        if (nowDay != weekdays) {
            return null;
        }
        // 不是凌晨2点不执行此代码
        if (!"02".equals(DateTools.DEFAULT.getCurrentDate("HH"))) {
            return null;
        }
        stime = stime + String.valueOf(nowDay);
        if (lastmonth < 10) {
            lastWeeks = String.valueOf(lastyear) + "0" + String.valueOf(lastmonth)
                    + String.valueOf(lastWeeksday);
        } else {
            lastWeeks = String.valueOf(lastyear) + String.valueOf(lastmonth)
                    + String.valueOf(lastWeeksday);
        }
        for (String metric_identity : metricIdentityArr) {
        	if("radius_num".equals(metric_identity) || "radius_percent".equals(metric_identity)) {
        		if(fileName ==null) {
        			fileName = "GS_NRTM_NKFmonth_JS_"+stime.substring(0, 6)+"_P01_END"+".csv";
        		}
        		MdMetric metric = InitParam.getMetricByIdentity(metric_identity);
        		if(metric!=null) {
        			datas= ReportMetricDataDAO.getSpecialData(metric.getId(), stime);
        		}else {
        			LOG.info(metric_identity + " metric is not find !!");
        		}
        		reportData.put(metric_identity, datas);
        	}     	
        } 
        FileTarget target = createUploadTarget(stime, conf,fileName);
        return target;
    }
    
    //GS_NRTM_NKFmonth_JS_201907_P01_END.csv
    private FileTarget createUploadTarget(String stime, UploadConf conf,String fileName){
        FileTarget target = new FileTarget();
        
        String fileNamePath=conf.getUploadProtocol().getLocalDir()+"/"+fileName;
        String head=null;
        if(fileName.startsWith("GS_NRTM_NKFmonth_JS_")) {
        	head="province_id|starttimemonth|HBMobileBillUserNbr|HBChainNetIncreBillUserNbr|HBActiveUserNbr"
    				+ "|HBUnder20MUserNbr|HB20M49MUserNbr|HB50M99MUserNbr|HB100M499MUserNbr|HB500M999MUserNbr|"
    				+ "HBOver1000MUserNbr|HBUnder20MUserRatio|HB20M49MUserRatio|HB50M99MUserRatio|"
    				+ "HB100M499MUserRatio|HB500M999MUserRatio|HBOver1000MUserRatio|HBThirdPartyUserNbrMUserRatio";
        }
        File file = FileOperate.createLocalFile(fileNamePath);
        writeFile(file,stime,head,fileName);
        target.setLocalFile(file);
        target.setUpload_dir(conf.getUploadProtocol().getUploadDir());
        return target;
    }
    
    private void writeFile(File file,String stime,String head,String fileName) {
    	
    	FileWriter fw = null;
    	StringBuffer context=new StringBuffer();
    	List<ReportChartData> data=null;
    	Map<String, String> dataMap=new HashMap<String, String>();
    	try {
    		fw = new FileWriter(file, true); // true表示追加
    		if(head!=null) {
    			fw.write(head);
        		fw.write("\r\n");//换行
    		}
    		if(!reportData.isEmpty()) {
    			for(String key : reportData.keySet()){ //把指定单个指标的数据处理好放map中
    				data=reportData.get(key);
    				//根据key组装数据
    				String text=compmentData(key,data); 
    				dataMap.put(key, text); //key：指标标识 value：单个指标数据处理结果
    			}
    			context =handleData(fileName,dataMap,stime); //处理指标和指标之间的数据关系
    		}
    		if(context!=null) {
    			fw.write(context.toString());
    		}
    		fw.flush();
		} catch (Exception e) {
			LOG.info("gscm writer file error:"+e.getMessage());
		}finally {
			if(fw!=null) {
				try {
					fw.close();
				} catch (IOException e) {
                    LOG.info("gscm IOException error:" + e.getMessage());
				}
			}
		}
    	
    }
    
   private String compmentData(String key,List<ReportChartData> data) {
	   StringBuffer context=new StringBuffer();
	   switch (key) {
		case "radius_num":
			context=getData(data); //不通用，根据需求写
			break;
	    case "radius_percent":
	    	context=getData(data);
		    break;
			
		default:
			break;
		}
	   return context.toString();
   }
   public StringBuffer handleData(String fileName,Map<String, String> dataMap,String stime) {
	   StringBuffer context=new StringBuffer();
	   List<ReportChartData> datas1=new ArrayList<ReportChartData>();
	   List<ReportChartData> datas2=new ArrayList<ReportChartData>();
	   List<ReportChartData> datas3=new ArrayList<ReportChartData>();
	   if(fileName.equals("GS_NRTM_NKFmonth_JS_"+stime.substring(0, 6)+"_P01_END"+".csv")){
		   context.append("931|");//固定不变
		   context.append(stime.substring(0, 6)+"|"); //当前月份
		   //获取当月注册数据
		   MdMetric metric1 = InitParam.getMetricByIdentity("radius_registNum");
		   String nowNum="0";
		   String lastNum="0";
		   if(metric1!=null) {
			   datas1 = ReportMetricDataDAO.getSpecialData(metric1.getId(), stime);
			   nowNum=datas1.isEmpty()? "0":datas1.get(0).getValue();
		   }else {
			   LOG.info("now month radius_registNum metric data is not find!");
		   }
		   //获取上月注册数据
   		   if(metric1!=null) {
   			   datas2= ReportMetricDataDAO.getSpecialData(metric1.getId(), lastWeeks);
   			   lastNum=datas2.isEmpty()? "0":datas1.get(0).getValue();
   		    }else {
   		    	LOG.info("last month radius_registNum metric data is not find!");
   		    }
		   context.append(nowNum); //家宽注册用户数
		   context.append("|");
		   context.append(String.valueOf(Integer.valueOf(nowNum)-Integer.valueOf(lastNum))); //家宽注册用户数环比上月增长量
		   context.append("|");
		   MdMetric metric2 = InitParam.getMetricByIdentity("radius_activeNum");
		   if(metric2!=null) {
			   datas3 = ReportMetricDataDAO.getSpecialData(metric2.getId(), stime);
		   }else {
			   LOG.info("radius_activeNum metric data is not find!");
		   }
		   context.append(datas3.isEmpty()? "0":datas3.get(0).getValue()); //家宽活跃用户数
		   context.append("|");
		   context.append(dataMap.get("radius_num")); //个区间宽带用户数
		   context.append("|");
		   context.append(dataMap.get("radius_percent")); //个区间宽带用户数占比
		   context.append("|");
		   context.append("0"); //该字段不涉及，填0
	   }
	   
	   return context;
	   
   }
    
   private StringBuffer getData(List<ReportChartData> data) {
	   StringBuffer context=new StringBuffer();
	   Map<String, String> dataMap=new HashMap<String, String>();
	   if(data!=null) {
		   for (ReportChartData reportChartData : data) {
			   if(reportChartData!=null) {
				   dataMap.put(reportChartData.getItem(), reportChartData.getValue());
			   }else {
				   LOG.info("ReportChartData is null!");
			   }
		   }
	   }
	   context.append(dataMap.get("20"));
	   context.append("|");
	   context.append(dataMap.get("20-49"));
	   context.append("|");
	   context.append(dataMap.get("50-99"));
	   context.append("|");
	   context.append(dataMap.get("100-499"));
	   context.append("|");
	   context.append(dataMap.get("500-999"));
	   context.append("|");
	   context.append(dataMap.get("1000"));
	   return context;
   }


}
