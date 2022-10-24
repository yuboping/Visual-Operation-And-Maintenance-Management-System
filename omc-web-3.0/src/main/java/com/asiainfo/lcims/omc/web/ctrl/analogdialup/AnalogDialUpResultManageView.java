package com.asiainfo.lcims.omc.web.ctrl.analogdialup;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.analogdialup.AnalogDialUp;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.analogdialup.AnalogDialUpService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 模拟拨测结果管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/analogdialupresultmanage")
public class AnalogDialUpResultManageView extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(AnalogDialUpResultManageView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "analogDialUpService")
    AnalogDialUpService analogDialUpService;

    /**
     * 模拟拨测管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String analogDialUpResultManage(HttpServletRequest request, Model model,
            HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        List<Map<String, Object>> mapList = analogDialUpService.getReturncodeName();
        model.addAttribute("mapList", mapList);
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "analogdialup/analogdialupresultmanage";
    }

    /**
     * 
     * @Title: queryAnalogDialUpResultList @Description:
     *         TODO(模拟拨测查询) @param @param analogDialUp @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return List<AnalogDialUp> 返回类型 @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Page queryAnalogDialUpResultList(@ModelAttribute AnalogDialUp analogDialUp,
            HttpServletRequest request, HttpSession session, Model model) {
        int pageNumber = 1;
        if (null != request.getParameter("pageNumber")) {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        }
        Page page = new Page(0);
        page.setPageNumber(pageNumber);
        Page analogDialUpPage = analogDialUpService.getAnalogDialUpResultPage(analogDialUp, page);
        return analogDialUpPage;
    }

}
