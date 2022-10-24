package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.model.system.MdMenuTree;
import com.asiainfo.lcims.omc.model.system.MdRole;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.service.configmanage.AreaService;
import com.asiainfo.lcims.omc.service.configmanage.NodeManageService;
import com.asiainfo.lcims.omc.service.configmanage.RoleManageService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色管理类
 */
@Controller
@RequestMapping(value = "/view/class/system/rolemanage")
public class RoleManageView extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(RoleManageView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "rolemanageService")
    RoleManageService rolemanageService;

    @Resource(name = "areaService")
    AreaService areaService;

    @Resource(name = "nodeManageService")
    NodeManageService nodeManageService;

    @Resource(name = "paramService")
    ParamService paramService;

    /**
     * 角色管理初始化页面
     *
     * @return
     */
    @RequestMapping(value = "")
    public String RoleManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "rolemanage/rolemanage";
    }

    /**
     * 角色列表查询
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdRole> queryMdMetricTypeList(HttpServletRequest request,
                                              HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        MdRole roleQueryParam = BeanToMapUtils.toBean(MdRole.class, params);
        String str = DbSqlUtil.replaceSpecialStr(roleQueryParam.getName());
        List<MdRole> roleList = rolemanageService.getRoleList(str);
        return roleList;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult roleDelete( HttpServletRequest request){
        String[] roleArray = request.getParameterValues("roleArray[]");
        WebResult result = rolemanageService.deleteInfo(roleArray);
        return result;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public WebResult roleAdd( HttpServletRequest request){
        Map<String, Object> params = getParams(request);
        MdRole mdRole = BeanToMapUtils.toBean(MdRole.class, params);
        String areanolist = request.getParameter("areanolist");
        String nodelist = request.getParameter("nodelist");
        String[] menulist = request.getParameterValues("menulist[]");
        String[] menulist2 = request.getParameterValues("menulist2[]");
        WebResult result = rolemanageService.addRoleInfo(mdRole, areanolist, nodelist, menulist, menulist2);
        return result;
    }

    /**
     * 根据queryType的类型查询对应的信息并返回
     */
    @RequestMapping(value = "/query/{queryInfoType}")
    @ResponseBody
    public List<?> queryInfoList(@PathVariable String queryInfoType, HttpServletRequest request, HttpSession session) {
        String roleid = String.valueOf(session.getAttribute(Constant.ADMIN_ROLEID_STRING));
        BusinessConf conf = new BusinessConf();
        if (queryInfoType == null || queryInfoType.isEmpty()) {
            return Collections.emptyList();
        }
        if ("areaList".equalsIgnoreCase(queryInfoType)) {
            List<MdArea> areaList = areaService.getAllAreaList();
            if(!Constant.ADMIN_ROLEID.equals(roleid)){
                areaList = rolemanageService.queryAreaRolePermissionWithRoleid(areaList, roleid);
            }
            return areaList;
        }
        if ("nodeList".equalsIgnoreCase(queryInfoType)) {
            List<MdNode> nodeList = nodeManageService.getAllNodeList();
            if(!Constant.ADMIN_ROLEID.equals(roleid)) {
                nodeList = rolemanageService.queryNodeRolePermissionWithRoleid(nodeList, roleid);
            }
            return nodeList;
        }
        if ("paramList".equalsIgnoreCase(queryInfoType)) {
            //状态
            List<MdParam> paramList = paramService.getMdParamList(ConstantUtill.ROLE_PARAM_LIST);
            return paramList;
        }
        if ("menuList".equalsIgnoreCase(queryInfoType)) {
            //状态

            List<MdMenuTree> menuList = rolemanageService.getMdMenuTreeByRole();
            if(!Constant.ADMIN_ROLEID.equals(roleid)) {
                menuList = rolemanageService.queryMenuRolePermissionWithRoleid(menuList, roleid);
            }
            return menuList;
        }
        return Collections.emptyList();
    }

    /**
     * 查询待修改角色信息，结果放入HashMap中
     * @param request
     * @return
     */
    @RequestMapping(value = "/querymodifyinfo")
    @ResponseBody
    public WebResult queryModifyInfo(HttpServletRequest request, HttpSession session){
        String nowroleid = String.valueOf(session.getAttribute("roleid"));
        String roleid = request.getParameter("roleid");
        String querytype = request.getParameter("querytype");
        WebResult result = rolemanageService.queryModifyInfoWithId(roleid, querytype, nowroleid);
        return result;
    }

    /**
     * 查询待修改角色信息，结果放入HashMap中
     * @param request
     * @return
     */
    @RequestMapping(value = "/querydetailinfo")
    @ResponseBody
    public WebResult queryDetailInfo(HttpServletRequest request, HttpSession session){
        String roleid = request.getParameter("roleid");
        WebResult result = rolemanageService.queryDetailInfoWithId(roleid);
        return result;
    }

    /**
     * 修改角色信息与角色权限
     * @param request
     * @return
     */
    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult roleModify( HttpServletRequest request){
        Map<String, Object> params = getParams(request);
        MdRole mdRole = BeanToMapUtils.toBean(MdRole.class, params);
        String areanolist = request.getParameter("areanolist");
        String nodelist = request.getParameter("nodelist");
        String[] menulist = request.getParameterValues("menulist[]");
        String[] menulist2 = request.getParameterValues("menulist2[]");
        WebResult result = rolemanageService.ModifyRoleInfo(mdRole, areanolist, nodelist, menulist, menulist2);
        return result;
    }
}
