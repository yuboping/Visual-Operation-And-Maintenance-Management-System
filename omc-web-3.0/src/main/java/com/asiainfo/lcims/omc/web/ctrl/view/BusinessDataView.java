package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.model.radius.ChartListVo;
import com.asiainfo.lcims.omc.service.radius.BusinessDataService;
import com.asiainfo.lcims.omc.util.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/view/class/businessdata/radius")
public class BusinessDataView extends BaseController {

    @Resource(name = "businessDataService")
    BusinessDataService businessDataService;

    /**
     * 获取折线图方法
     */
    @RequestMapping(value = "/getRadiusData")
    @ResponseBody
    public List<ChartListVo> queryRadiusData() {
        return businessDataService.getLineData();
    }

}
