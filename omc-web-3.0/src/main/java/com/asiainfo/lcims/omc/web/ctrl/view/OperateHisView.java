package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.operatehistory.OperateHis;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.system.MdParamDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作历史查询
 * @author yyc
 *
 */
@Controller
@RequestMapping(value = "/view/class/mainttool/operatehis")
public class OperateHisView extends BaseController{
    private static final Logger LOG = LoggerFactory.getLogger(OperateHisView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    @Inject
    private MdParamDAO mdParamDAO;

    /**
     * 操作日志查询初始化页面
     *
     * @return
     */
    @RequestMapping(value = "")
    public String AreaManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        model.addAttribute("paramlist", mdParamDAO.getParamByType(Constant.PARAM_TYPE_OPERATE_HIS));

        return "operatehis/operateHistory";
    }

    /**
     * 操作日志查询方法
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryOperateHisList(HttpServletRequest request,
                                              HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        OperateHis operateQueryParam = BeanToMapUtils.toBean(OperateHis.class, params);

        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }

        Page operateHisList = operateHisService.getOperateHistoryList(operateQueryParam, pageNumber);
        return operateHisList;
    }

}
