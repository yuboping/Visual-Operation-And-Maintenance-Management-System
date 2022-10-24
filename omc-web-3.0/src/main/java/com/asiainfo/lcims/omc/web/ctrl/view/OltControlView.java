package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.oltcontrol.OltControlVo;
import com.asiainfo.lcims.omc.model.oltcontrol.OltLineChartVo;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.oltcontrol.OltControlService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/view/class/mainttool/oltcontrol")
public class OltControlView extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(com.asiainfo.lcims.omc.web.ctrl.view.OltControlView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "oltControlService")
    OltControlService oltControlService;

    /**
     * 操作日志查询初始化页面
     *
     * @return
     */
    @RequestMapping(value = "")
    public String OltManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        model.addAttribute("paramlist", oltControlService.getOltSelectData());

        return "oltcontrol/oltControl";
    }

    /**
     * 操作日志查询方法
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryOltList(HttpServletRequest request,
                                    HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        OltControlVo oltControlVoParam = BeanToMapUtils.toBean(OltControlVo.class, params);

        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }

        Page oltControlList = oltControlService.getOltControlAlarmTableList(oltControlVoParam, pageNumber);
        return oltControlList;
    }

    /**
     * 获取折线图方法
     */
    @RequestMapping(value = "/query/getOltLineData")
    @ResponseBody
    public List<OltLineChartVo> queryOltLineData(HttpServletRequest request,
                                    HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        OltControlVo oltControlVoParam = BeanToMapUtils.toBean(OltControlVo.class, params);
        List<OltLineChartVo> oltLineChartVoList = oltControlService.getLineData(oltControlVoParam);
        return oltLineChartVoList;
    }

}
