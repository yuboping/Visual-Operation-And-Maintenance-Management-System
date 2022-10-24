package com.asiainfo.lcims.omc.web.ctrl.analogdialup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.analogdialup.AnalogDialUp;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.analogdialup.AnalogDialUpService;
import com.asiainfo.lcims.omc.service.configmanage.HostService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.ConstantUtill;

/**
 * 模拟拨测管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/analogdialupmanage")
public class AnalogDialUpManageView extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(AnalogDialUpManageView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "analogDialUpService")
    AnalogDialUpService analogDialUpService;

    @Resource(name = "hostService")
    HostService hostService;

    /**
     * 模拟拨测管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String analogDialUpManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        // 查询所有主机
        List<MonHost> hostlist = hostService.getAllHost();
        List<MonHost> radiushostlist = new ArrayList<MonHost>();
        for (MonHost monHost : hostlist) {
            if (ConstantUtill.OBS_HOST_TYPE.equals(monHost.getHosttype())) {
                radiushostlist.add(monHost);
            }
        }
        model.addAttribute("radiushostlist", radiushostlist);
        List<String> minlist = new ArrayList<String>();
        for (int i = 1; i < 60; i++) {
            minlist.add(Integer.toString(i));
        }
        model.addAttribute("minlist", minlist);
        return "analogdialup/analogdialupmanage";
    }

    /**
     * 
     * @Title: queryAnalogDialUpList @Description: TODO(模拟拨测查询) @param @param
     *         analogDialUp @param @param request @param @param
     *         session @param @param model @param @return 参数 @return
     *         List<AnalogDialUp> 返回类型 @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<AnalogDialUp> queryAnalogDialUpList(@ModelAttribute AnalogDialUp analogDialUp,
            HttpServletRequest request, HttpSession session, Model model) {
        List<AnalogDialUp> analogDialUpList = analogDialUpService.getAnalogDialUpList(analogDialUp);
        return analogDialUpList;
    }

    /**
     * 
     * @Title: addAnalogDialUp @Description: TODO(模拟拨测添加) @param @param
     *         analogDialUp @param @param request @param @param
     *         session @param @param model @param @return 参数 @return WebResult
     *         返回类型 @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addAnalogDialUp(@ModelAttribute AnalogDialUp analogDialUp,
            HttpServletRequest request, HttpSession session, Model model) {
        return analogDialUpService.addAnalogDialUp(analogDialUp);
    }

    /**
     * 
     * @Title: modifyAnalogDialUp @Description: TODO(模拟拨测修改) @param @param
     *         analogDialUp @param @param request @param @param
     *         session @param @param model @param @return 参数 @return WebResult
     *         返回类型 @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyAnalogDialUp(@ModelAttribute AnalogDialUp analogDialUp,
            HttpServletRequest request, HttpSession session, Model model) {
        return analogDialUpService.modifyAnalogDialUp(analogDialUp);
    }

    /**
     * 
     * @Title: deleteAnalogDialUp @Description: TODO(模拟拨测删除) @param @param
     *         request @param @param session @param @param model @param @return
     *         参数 @return WebResult 返回类型 @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteAnalogDialUp(HttpServletRequest request, HttpSession session,
            Model model) {
        String[] analogDialUpIdArray = request.getParameterValues("analogDialUpIdArray[]");
        return analogDialUpService.deleteAnalogDialUp(analogDialUpIdArray);
    }
}
