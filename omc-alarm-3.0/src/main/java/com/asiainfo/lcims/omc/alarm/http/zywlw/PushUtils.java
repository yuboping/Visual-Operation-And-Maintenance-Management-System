package com.asiainfo.lcims.omc.alarm.http.zywlw;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.lcims.util.HttpJsonUtils;

public class PushUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PushUtils.class);

    private static volatile String accessToken = null;

    public static String push(String loginUrl, String userName, String password, String pushUrl,
            String requestJsonStr) {
        LOG.info("access token is : {}", accessToken);
        String result = null;
        try {
            if (StringUtils.isBlank(accessToken)) {
                result = loginPush(userName, password, requestJsonStr, loginUrl, pushUrl);
            } else {
                String pushRet = push(pushUrl, requestJsonStr);
                JSONObject pushResultJson = JSONObject.parseObject(pushRet);
                Integer pushCode = pushResultJson.getInteger("code");
                if (pushCode.intValue() == -1) {
                    result = loginPush(userName, password, requestJsonStr, loginUrl, pushUrl);
                }
            }
        } catch (Exception e) {
            LOG.error("Push deal error!", e);
        }
        return result;
    }

    private static String loginPush(String userName, String password, String requestJsonStr,
            String loginUrl, String pushUrl) {
        String result = null;

        String loginRet = login(loginUrl, userName, password);
        JSONObject loginResultJson = JSONObject.parseObject(loginRet);
        Integer loginCode = loginResultJson.getInteger("code");
        if (loginCode.intValue() == 0) {
            accessToken = loginResultJson.getString("data");
            result = push(pushUrl, requestJsonStr);
        }
        return result;
    }

    private static String push(String pushUrl, String requestJsonStr) {
        JSONObject pushJson = new JSONObject();
        pushJson.put("accessToken", accessToken);
        pushJson.put("requestJsonStr", requestJsonStr);
        String content = HttpJsonUtils.post(pushUrl, pushJson.toJSONString());
        return content;
    }

    private static String login(String loginUrl, String userName, String password) {
        JSONObject loginJson = new JSONObject();
        loginJson.put("userName", userName);
        loginJson.put("password", password);
        String content = HttpJsonUtils.post(loginUrl, loginJson.toJSONString());
        return content;
    }

}
