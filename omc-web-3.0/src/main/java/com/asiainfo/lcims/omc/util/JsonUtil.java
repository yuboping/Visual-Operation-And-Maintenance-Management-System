package com.asiainfo.lcims.omc.util;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.sos.SosRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);
    @SuppressWarnings("hiding")
    public static <T> T jsonToEntityClass(String jsonData,Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        try {
            if(!ToolsUtils.StringIsNull(jsonData)){
                t = mapper.readValue(jsonData,tClass);
            }
        } catch (Exception e) {
            t = null;
            LOG.info("json to entity fail", e);
        }
        return t;
    }
    
    public static void main(String[] args) {
//        String jsonData = "{\"token\":\"123455\",\"sourceType\":101,\"userName\":\"admin\"}";
        String jsonData = "{\"token\":\"123455\",\"sourceType\":101}";
        SosRequest request = jsonToEntityClass(jsonData, SosRequest.class);
        System.out.println(request.getToken());
        System.out.println(request.getSourceType());
        System.out.println(request.getUserName());
    }
}
