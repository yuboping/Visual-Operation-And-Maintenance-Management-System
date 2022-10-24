package com.asiainfo.lcims.omc.persistence;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.util.EnumUtil;

public class SqlShcmUserOnlineOffLineProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SqlShcmUserOnlineOffLineProvider.class);
    
    public static final BusinessConf BUSCONF = new BusinessConf();

    @SuppressWarnings("unchecked")
	public String getQueryUserOnlineAndOffline(Map<String, Object> parameters) {
    	Set<String> dateSet=(Set<String>) parameters.get("dateSet");
    	String startDate=(String) parameters.get("startDate");
    	String endDate=(String) parameters.get("endDate");
    	int queryType=(int) parameters.get("queryType");
        StringBuffer strb = new StringBuffer();
        int n=0;
        if(queryType == 1) {
        	strb.append("SELECT a.ATTR4,case when p.DESCRIPTION is null then '其他原因' else p.DESCRIPTION end as ATTR1,a.ATTR2 FROM ( ");
        }else {
        	strb.append("SELECT a.ATTR4,case when p.DESCRIPTION is null then '其他原因' else p.DESCRIPTION end as ATTR1,sum(a.ATTR2) as ATTR2 FROM ( ");
        }
        
        for (String string : dateSet) {
			if(n!=0) {
				strb.append(" UNION ALL ");
			}
			if(queryType == 1) {
				 if(EnumUtil.DB_ORACLE.equals(BUSCONF.getDbName())){
					 strb.append(" SELECT case when ATTR1>18 then '0' else ATTR1 end as  ATTR1,ATTR2,substr(ATTR4, 1, 10) as ATTR4 FROM ");
				}else{
					strb.append(" SELECT case when ATTR1>18 then '0' else ATTR1 end as  ATTR1,ATTR2,substring(ATTR4, 1, 10) as ATTR4 FROM ");
				}
			}else {
				if(EnumUtil.DB_ORACLE.equals(BUSCONF.getDbName())){
					 strb.append(" SELECT case when ATTR1>18 then '0' else ATTR1 end as  ATTR1,ATTR2,substr(ATTR4, 1, 7) as ATTR4 FROM ");
				}else{
					strb.append(" SELECT case when ATTR1>18 then '0' else ATTR1 end as  ATTR1,ATTR2,substring(ATTR4, 1, 7) as ATTR4 FROM ");
				}
			}
			
			strb.append(" STATIS_DATA_MONTH_"+string+" ")
				.append(" WHERE METRIC_ID='a8a871bc38b14963bc396c88cefd6e26' ");
			if(queryType == 1) {
				 if(EnumUtil.DB_ORACLE.equals(BUSCONF.getDbName())){
					 strb.append(" And  substr(ATTR4, 1, 10) >= '" +startDate+ "'" )
						.append(" And  substr(ATTR4, 1, 10) <= '" +endDate+ "'" );
				}else{
					strb.append(" And  substring(ATTR4, 1, 10) >= '" +startDate+ "'" )
					.append(" And  substring(ATTR4, 1, 10) <= '" +endDate+ "'" );
				}
				
				
			}
			n++;
		}
        if(EnumUtil.DB_ORACLE.equals(BUSCONF.getDbName())){
        	 if(queryType == 1) {
             	strb.append(") a left join MD_PARAM p on a.ATTR1=p.CODE and p.TYPE='108' ORDER BY a.ATTR4");
             }else {
             	strb.append(") a  left join MD_PARAM p on a.ATTR1=p.CODE and p.TYPE='108' group by a.ATTR4,p.DESCRIPTION ORDER BY a.ATTR4");
             }
		}else{
			 if(queryType == 1) {
	        	strb.append(") a left join MD_PARAM p on a.ATTR1=p.`CODE` and p.TYPE='108' ORDER BY a.ATTR4");
	         }else {
	        	strb.append(") a  left join MD_PARAM p on a.ATTR1=p.`CODE` and p.TYPE='108' group by a.ATTR4,p.DESCRIPTION ORDER BY a.ATTR4");
	         }
		}
        LOG.info("getQueryUserOnlineAndOffline sql = {}", strb.toString());
        return strb.toString();
    }

}
