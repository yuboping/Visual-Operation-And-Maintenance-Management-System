package com.asiainfo.lcims.util;

import com.asiainfo.lcims.omc.alarm.dao.AlarmSeqDAO;

public class AlarmSeqUtil {
    public final static int ALARM_REQ_MAX = (int) (Math.pow(2, 31) - 1);
    private static Integer alarmSeq = AlarmSeqDAO.getAlarmSeq();
    /**
     * 获取alarmSeq
     * @return
     */
    public synchronized static int getAlarmSeq(){
        if(alarmSeq==null){
            alarmSeq = AlarmSeqDAO.getAlarmSeq();
        }
        
        if(alarmSeq.intValue() == ALARM_REQ_MAX){
            alarmSeq = 1;
        }else{
            alarmSeq = alarmSeq.intValue() + 1;
        }
        return alarmSeq.intValue();
    }
    
    public synchronized static void updateAlarmSeq(){
        AlarmSeqDAO.updateAlarmSeq(alarmSeq.toString());
    }
}
