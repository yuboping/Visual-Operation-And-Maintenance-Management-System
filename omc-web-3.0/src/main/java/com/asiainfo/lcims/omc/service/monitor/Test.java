package com.asiainfo.lcims.omc.service.monitor;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.lcims.omc.util.DateTools;
import com.asiainfo.lcims.omc.util.ToolsUtils;

public class Test {

    public static void main(String[] args) {
        String queryDate = "2019-02-26";
        List<String> days = new ArrayList<String>();
        DateTools DATATOOL = new DateTools("yyyy-MM-dd");
        if (ToolsUtils.StringIsNull(queryDate)) {
            queryDate = DATATOOL.getCurrentDate();
        }
        // 日期网球推n天数据
        for (int i = 0; i < 7; i++) {
            days.add(DATATOOL.getOffsetDay(-1 * i, queryDate));
        }
        StringBuilder sql_all = new StringBuilder(" ");
        for(int i=0;i<days.size();i++){
            sql_all.append("select * from "+days.get(i));
            if(i<days.size()-1){
                sql_all.append(" +++ ");
            }
        }
        System.out.println(days);
        System.out.println(sql_all.toString());
    }

}
