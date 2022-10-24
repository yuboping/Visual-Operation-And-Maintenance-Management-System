package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.NodeManageService;
import com.asiainfo.lcims.omc.util.BaseController;

/**
 * 配置管理/节点管理
 * 
 * @author yuboping
 *
 */
@Controller
@RequestMapping(value = "/view/class/system/nodemanage")
public class NodeManageView extends BaseController {

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "nodeManageService")
    NodeManageService nodeManageService;

    /**
     * 节点管理
     * 
     * @return
     */
    @RequestMapping(value = "")
    public String nodeManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/nodemanage";
    }

    /**
     * 
     * @Title: queryMdNodeList
     * @Description: TODO(查询节点信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdNode> queryMdNodeList(@ModelAttribute MdNode mdNode, HttpServletRequest request,
            HttpSession session, Model model) {
        List<MdNode> mdNodeList = nodeManageService.getMdNodeList(mdNode);
        return mdNodeList;
    }

    /**
     * 
     * @Title: addMdNode
     * @Description: TODO(新增节点信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult addMdNode(@ModelAttribute MdNode mdNode, HttpServletRequest request,
            HttpSession session, Model model) {
        return nodeManageService.addMdNode(mdNode);
    }

    /**
     * 
     * @Title: modifyMdNode
     * @Description: TODO(修改节点信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMdNode(@ModelAttribute MdNode mdNode, HttpServletRequest request,
            HttpSession session, Model model) {
        return nodeManageService.modifyMdNode(mdNode);
    }

    /**
     * 
     * @Title: deleteMdNode
     * @Description: TODO(删除节点信息)
     * @param @param request
     * @param @param session
     * @param @param model
     * @param @return 参数
     * @return List<MdMetric> 返回类型
     * @throws
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMdNode(HttpServletRequest request, HttpSession session, Model model) {
        String[] nodeidArray = request.getParameterValues("nodeidArray[]");
        return nodeManageService.deleteMdNode(nodeidArray);
    }

}
