package com.asiainfo.lcims.omc.web.ctrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 上海移动OMC监控统计指标数据收集平台对外接口(测试)
 * 
 * @author yubp
 *
 */
@Controller
public class TestAction extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(TestAction.class);

    @RequestMapping(value = "/api/v1/graph/history")
    @ResponseBody
    public String search(HttpServletRequest request, @RequestBody Object object) {
        try {
            Map<String, String> headerMap = getHeadersInfo(request);
            LOG.info("headerMap : {}", headerMap);
            String apitoken = headerMap.get("Apitoken");
            String contentType = headerMap.get("Content-Type");
            if (!"{\"name\":\"mobile-shh\",\"sig\":\"shh-cmc-token-in-server-side\"}"
                    .equals(apitoken)) {
                return "Apitoken error";
            }
            if (!"application/json".equals(contentType)) {
                return "Content-Type error";
            }
            JSONObject jsonObj = new JSONObject(object.toString());
            LOG.info("jsonObj : {}", object);
            String consol_fun = jsonObj.get("consol_fun").toString();
            if (!"AVERAGE".equals(consol_fun) && !"MAX".equals(consol_fun)
                    && !"MIN".equals(consol_fun)) {
                return "consol_fun error";
            }
            int step = Integer.parseInt(jsonObj.get("step").toString());
            String start_time = jsonObj.get("start_time").toString();
            int start_time_int = Integer.parseInt(start_time);
            int end_time = Integer.parseInt(jsonObj.get("end_time").toString());
            JSONArray hostnamesJSONArray = JSONArray
                    .parseArray(jsonObj.get("hostnames").toString());
            JSONArray countersJSONArray = JSONArray.parseArray(jsonObj.get("counters").toString());
            JSONArray rspJsonArray = new JSONArray();
            for (Object counters : countersJSONArray) {
                for (Object hostnames : hostnamesJSONArray) {
                    Map<String, Object> rspMap = new HashMap<String, Object>();
                    rspMap.put("endpoint", hostnames);
                    rspMap.put("counter", counters);
                    rspMap.put("dstype", "GAUGE");
                    rspMap.put("step", step);
                    ArrayList<Map<String, Object>> valuesList = new ArrayList<Map<String, Object>>();
                    if (start_time_int == 0 && end_time == 0) {
                        Map<String, Object> valuesMap = new HashMap<String, Object>();
                        valuesMap.put("timestamp",
                                Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
                        valuesMap.put("value", Double.valueOf(String
                                .format("%.5f", (1 + Math.random() * (100 - 1 + 1))).toString()));
                        valuesList.add(valuesMap);
                    } else {
                        for (int i = 0; i < 2; i++) {
                            int timestamp = (int) (Math.random() * (end_time - start_time_int)
                                    + start_time_int);
                            Map<String, Object> valuesMap = new HashMap<String, Object>();
                            valuesMap.put("timestamp", timestamp);
                            valuesMap.put("value",
                                    Double.valueOf(String
                                            .format("%.5f", (1 + Math.random() * (100 - 1 + 1)))
                                            .toString()));
                            valuesList.add(valuesMap);
                        }
                    }
                    rspMap.put("Values", valuesList);
                    rspJsonArray.add(rspMap);
                }
            }
            String rsp = rspJsonArray.toJSONString();
            LOG.info("resp : {}", rsp);
            return rsp;
        } catch (NumberFormatException e) {
            return e.toString();
        } catch (JSONException e) {
            return e.toString();
        }
    }
}
