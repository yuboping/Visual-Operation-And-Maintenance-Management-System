package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.model.radius.ChartListVo;
import com.asiainfo.lcims.omc.service.radius.RadiusService;
import com.asiainfo.lcims.omc.util.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/radius")
public class RadiusView extends BaseController {

    @Resource(name = "radiusService")
    RadiusService radiusService;

    /**
     * 获取折线图方法
     */
    @RequestMapping(value = "/getRadiusData")
    @ResponseBody
    public List<ChartListVo> queryRadiusData(String hostId) {
        return radiusService.getLineData(hostId);
    }
}
