package com.asiainfo.lcims.omc.service.monitor;

import java.util.List;

import javax.inject.Inject;
import org.springframework.stereotype.Service;
import com.asiainfo.lcims.omc.model.monitor.MdChartDetail;
import com.asiainfo.lcims.omc.persistence.monitor.PageChartDAO;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * 页面配置图表
 * @author zhul
 *
 */
@Service(value = "pageChartService")
public class PageChartService {
    
    @Inject
    private PageChartDAO pageChartDAO;
    
    public List<MdChartDetail> getChartInfosByUrl(String uri) {
        List<MdChartDetail> list = pageChartDAO.getChartInfosByUrl(uri);
        /**
         * 则获取分割符之前的uri，重新查询
         */
        if(ToolsUtils.ListIsNull(list)){
            uri = uri.split("/"+ConstantUtill.URL_SPLIT)[0];
            list = pageChartDAO.getChartInfosByUrl(uri);
        }
        
        return list;
    }
    
}
