package com.asiainfo.lcims.omc.param.monitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.gdcu.BmsArea;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.ReadFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "monitorinit")
public class MonitorInit {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorInit.class);
    
    private static final List<BmsArea> BMS_AREA_LIST = new ArrayList<BmsArea>();

    public void init() {
        String province = MainServer.conf.getProvince();
        if (StringUtils.equals(province, ProviceUtill.PROVINCE_GDCU)) {
            loadBMSAreaList();
        }
    }
    
    public static List<BmsArea> getBmsAreaList() {
        return BMS_AREA_LIST;
    }

    private void loadBMSAreaList() {
        try {
            String json = ReadFile.readByFilename("monitor/bms_area.json");
            ObjectMapper mapper = new ObjectMapper();
            List<BmsArea> bmsAreas = mapper.readValue(json, new TypeReference<List<BmsArea>>() {
            });
            BMS_AREA_LIST.clear();
            BMS_AREA_LIST.addAll(bmsAreas);
        } catch (Exception e) {
            LOG.error("bms_area.json配置文件读取失败", e);
            BMS_AREA_LIST.clear();
        }
    }
}
