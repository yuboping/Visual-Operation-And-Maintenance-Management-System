package com.asiainfo.lcims.omc.service.configmanage;

import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.sos.SosRoleBean;
import com.asiainfo.lcims.omc.model.system.*;
import com.asiainfo.lcims.omc.persistence.configmanage.RoleManageDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import com.asiainfo.lcims.omc.util.ToolsUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(value = "rolemanageService")
public class RoleManageService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleManageService.class);

    @Inject
    private RoleManageDAO roleManageDAO;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    public List<MdRole> getRoleList(String name) {
        List<MdRole> list = roleManageDAO.getRoleList(name);
        return list;
    }

    public List<MdRole> getMdRoleList() {
        List<MdRole> list = roleManageDAO.getAllRoleList();
        return list;
    }
    
    public List<SosRoleBean> getSosAllRoleList() {
        List<SosRoleBean> list = roleManageDAO.getSosAllRoleList();
        return list;
    }

    public List<MdMenuTree> getMdMenuTreeByRole() {
        List<MdMenuTree> list = roleManageDAO.getMenuTreeByRole();
        List<MdMenuTree> mdMenuTrees = getMdMenuTreeByRoleWithProd(list);
        return mdMenuTrees;
    }

    public List<MdMenuTree> getMdMenuTreeByRoleWithProd(List<MdMenuTree> list) {
        List<MdMenuTree> mdMenuTrees = new ArrayList<>();
        for(MdMenuTree mdMenuTree : list){
            if(StringUtils.isEmpty(mdMenuTree.getParent()) || "null".equals(mdMenuTree.getParent())){
                Map<String,Boolean> state = mdMenuTree.getState();
                state.put("undetermined",true);
                mdMenuTree.setStateMap(state);
                mdMenuTree.setParent("#");
            }
            mdMenuTrees.add(mdMenuTree);
        }
        return mdMenuTrees;
    }

    public WebResult deleteInfo(String[] roleArray) {
        Map<String, Object> map = new HashMap<String, Object>();
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();

        if (roleArray != null && roleArray.length != 0) {
            for (String roleid : roleArray) {
                WebResult result = deleteValid(roleid);
                MdRole mdRole = roleManageDAO.getRolebyRoleid(roleid);
                if (result.operFail()) {
                    delFailList.add(mdRole.getName());
                    deleteFail++;
                    continue;
                }
                int deleteResult = roleManageDAO.deleteById(roleid);
                roleManageDAO.deleteRolePermissionsById(roleid);
                if (1 == deleteResult) {
                    delSuccessList.add(mdRole.getName());
                    deleteSuccess++;
                } else {
                    delFailList.add(mdRole.getName());
                    deleteFail++;
                }
            }
        }
        String message = "";
        if (deleteSuccess > 0) {
            message = message + deleteSuccess + "???????????????????????????" + delSuccessList + "???";
        }
        if (deleteFail > 0) {
            message = message + deleteFail + "???????????????????????????" + delFailList + "???????????????????????????????????????????????????????????????????????????";
        }
        if(ToolsUtils.StringIsNull(message)){
            message = "???????????????";
        }
        //??????????????????
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_ROLE_MANAGE, "????????????:" + delSuccessList);
        return new WebResult(true, message);
    }

    private WebResult deleteValid(String roleid) {
        if (Constant.ADMIN_ROLEID.equals(roleid)) {
            return new WebResult(false, "???????????????????????????!");
        }
        int count = roleManageDAO.queryIfExistInAdminRole(roleid);
        if (count > 0) {
            return new WebResult(false, "??????????????????????????????????????????????????????!");
        }
        return new WebResult(true, "");
    }

    public WebResult addRoleInfo(MdRole mdRole, String areanolist, String nodelist, String[] menulist, String[] menulist2) {
        String[] alArray = areanolist.split(",");
        String[] nlArray = nodelist.split(",");

        int rolecount = roleManageDAO.queryIfExistInRoleNameWithAdd( mdRole);
        if(rolecount > 0){
            LOG.info("?????????????????? : "+ mdRole.getName());
            return new WebResult(false, "???????????????????????????", "repeat");
        }
        mdRole.setRoleid(IDGenerateUtil.getUuid());
        roleManageDAO.addMdRoleInfo(mdRole);
        addRolePermissionWithType(alArray, mdRole.getRoleid(), Constant.PERMISSION_AREA);
        addRolePermissionWithType(nlArray, mdRole.getRoleid(), Constant.PERMISSION_NODE);
        addMenuRolePermissionWithType(menulist, mdRole.getRoleid(), Constant.PERMISSION_MENU, Constant.MENU_TYPE_ALL);
        addMenuRolePermissionWithType(menulist2, mdRole.getRoleid(), Constant.PERMISSION_MENU, Constant.MENU_TYPE_HALF);

        //??????????????????
        operateHisService.insertOperateHistory( Constant.OPERATE_HIS_ROLE_MANAGE, "????????????[??????:" + mdRole.getName() + "]");

        return new WebResult(true, "?????????????????????");
    }

    /**
     * ?????????????????????????????????
     * @param typeArray
     * @param roleid
     * @param type
     */
    private void addRolePermissionWithType(String[] typeArray, String roleid, String type){
            if (typeArray != null && typeArray.length != 0) {
            for (String ta : typeArray) {
                if(StringUtils.isEmpty(ta)){
                    break;
                }
                MdRolePermissions mdRolePermissions = new MdRolePermissions();
                mdRolePermissions.setRoleid(roleid);
                mdRolePermissions.setType(type);
                mdRolePermissions.setPermissionid(ta);
                int succount = roleManageDAO.addMdRolePermissionInfo(mdRolePermissions);
            }
        }
    }

    /**
     * ?????????????????????????????????
     * @param typeArray
     * @param roleid
     * @param type
     */
    private void addMenuRolePermissionWithType(String[] typeArray, String roleid, String type, String menutype){
        if (typeArray != null && typeArray.length != 0) {
            for (String ta : typeArray) {
                if(StringUtils.isEmpty(ta)){
                    break;
                }
                MdRolePermissions mdRolePermissions = new MdRolePermissions();
                mdRolePermissions.setRoleid(roleid);
                mdRolePermissions.setType(type);
                mdRolePermissions.setPermissionid(ta);
                mdRolePermissions.setMenu_type(menutype);
                roleManageDAO.addMenuMdRolePermissionInfo(mdRolePermissions);
            }
        }
    }

    /**
     * ??????roleid????????????????????????
     * @param roleid
     * @return
     */
    public WebResult queryModifyInfoWithId(String roleid, String querytype, String nowroleid){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        WebResult result;
        if(Constant.ROLE_PERMISSION_MODIFY.equals(querytype) || querytype == Constant.ROLE_PERMISSION_MODIFY){
            if (Constant.ADMIN_ROLEID.equals(roleid)) {
                return new WebResult(false, "???????????????????????????!");
            }
        }
        try {
            MdRole mdRole = roleManageDAO.getRolebyRoleid(roleid);
            resultMap.put("roleinfo", mdRole);
            List<MdParam> paramlist = roleManageDAO.getModifyInfoWithParam(roleid, Constant.PERMISSION_AREA, Constant.PERMISSION_NODE);
            List<MdMenuTree> mdMenuTrees = roleManageDAO.getModifyInfoWithMenu(roleid, Constant.PERMISSION_MENU);
            List<MdMenuTree> menulist = getMdMenuTreeByRoleWithProd(mdMenuTrees);

            if(!Constant.ADMIN_ROLEID.equals(nowroleid)) {
                menulist = queryMenuRolePermissionWithRoleid(menulist, nowroleid);
            }
            List<MdArea> arealist = roleManageDAO.getModifyInfoWithArea(roleid, Constant.PERMISSION_AREA);
            if(!Constant.ADMIN_ROLEID.equals(nowroleid)) {
                arealist = queryAreaRolePermissionWithRoleid(arealist, nowroleid);
            }
            List<MdNode> nodelist = roleManageDAO.getModifyInfoWithNode(roleid, Constant.PERMISSION_NODE);
            if(!Constant.ADMIN_ROLEID.equals(nowroleid)) {
                nodelist = queryNodeRolePermissionWithRoleid(nodelist, nowroleid);
            }
            resultMap.put("arealist", arealist);
            resultMap.put("nodelist", nodelist);
            resultMap.put("menulist", menulist);
            resultMap.put("paramlist", paramlist);
            result = new WebResult(true, "????????????????????????", resultMap);
        }catch (Exception e){
            LOG.info("???????????????????????? : "+ e.getMessage(), e);
            result = new WebResult(false, "????????????????????????");

        }
        return result;
    }

    /**
     * ??????roleid????????????????????????
     * @param roleid
     * @return
     */
    public WebResult queryDetailInfoWithId(String roleid){
        Map<String, Object> resultMap = new HashMap<>();
        WebResult result;
        try {
            MdRole mdRole = roleManageDAO.getRolebyRoleid(roleid);
            resultMap.put("roleinfo", mdRole);
            List<MdParam> paramlist = roleManageDAO.getModifyInfoWithParam(roleid, Constant.PERMISSION_AREA, Constant.PERMISSION_NODE);
            List<MdMenuTree> mdMenuTrees = roleManageDAO.getDetailInfoWithMenu(roleid, Constant.PERMISSION_MENU);
            List<MdMenuTree> menulist = getMdMenuTreeByRoleWithProd(mdMenuTrees);
            for(MdMenuTree mdMenuTree : menulist){
                Map<String, Boolean> statemap = mdMenuTree.getState();
                statemap.put("disabled",true);
                mdMenuTree.setStateMap(statemap);
            }
            List<MdArea> arealist = roleManageDAO.getDetailInfoWithArea(roleid, Constant.PERMISSION_AREA);
            List<MdNode> nodelist = roleManageDAO.getDetailInfoWithNode(roleid, Constant.PERMISSION_NODE);
            resultMap.put("arealist", arealist);
            resultMap.put("nodelist", nodelist);
            resultMap.put("menulist", menulist);
            resultMap.put("paramlist", paramlist);
            result = new WebResult(true, "????????????????????????", resultMap);
        }catch (Exception e){
            LOG.info("???????????????????????? : "+ e.getMessage(), e);
            result = new WebResult(false, "????????????????????????");

        }
        return result;
    }

    /**
     * ?????????????????????????????????
     * @param mdRole
     * @param areanolist
     * @param nodelist
     * @param menulist
     * @return
     */
    public WebResult ModifyRoleInfo(MdRole mdRole, String areanolist, String nodelist, String[] menulist, String[] menulist2) {
        WebResult result;
        String[] alArray = areanolist.split(",");
        String[] nlArray = nodelist.split(",");
        try {
            int rolecount = roleManageDAO.queryIfExistInRoleName(mdRole);
//            MdRole roleWithName = roleManageDAO.getRolebyRoleid(mdRole.getRoleid());
            if(rolecount > 0){
                LOG.info("?????????????????? : "+ mdRole.getName());
                return new WebResult(false, "???????????????????????????", "repeat");
            }
            roleManageDAO.updateRoleInfo(mdRole);
            roleManageDAO.deleteRolePermissionsById(mdRole.getRoleid());
            addRolePermissionWithType(alArray, mdRole.getRoleid(), Constant.PERMISSION_AREA);
            addRolePermissionWithType(nlArray, mdRole.getRoleid(), Constant.PERMISSION_NODE);
            addMenuRolePermissionWithType(menulist, mdRole.getRoleid(), Constant.PERMISSION_MENU, Constant.MENU_TYPE_ALL);
            addMenuRolePermissionWithType(menulist2, mdRole.getRoleid(), Constant.PERMISSION_MENU, Constant.MENU_TYPE_HALF);
            result = new WebResult(true, "?????????????????????");
            //??????????????????
            operateHisService.insertOperateHistory( Constant.OPERATE_HIS_ROLE_MANAGE, "????????????[??????:" + mdRole.getName() + "]");
        }catch (Exception e){
            LOG.info("?????????????????? : "+ e.getMessage(), e);
            result = new WebResult(false, "??????????????????");
        }
        return result;
    }

    /**
     * ???????????????????????????
     * @param username
     * @return
     */
    public List<MdRolePermissions> queryMenuByUsername(String username){
        return roleManageDAO.queryMenuByUsername(username);
    }

    /**
     * ???????????????????????????
     * @param username
     * @return
     */
    public List<MdRolePermissions> queryPermissionTypeByUsername(String username, String type){
        return roleManageDAO.queryPermissionTypeByUsername(username, type);
    }

    /**
     * ???????????????????????????
     * @return
     */
    public List<MdMenuTree> queryMenuRolePermissionWithRoleid(List<MdMenuTree> permissionList, String roleid){
        List<MdRolePermissions> permissions = roleManageDAO.getMenuRolePermissionByRoleid(roleid,Constant.PERMISSION_MENU);
        List<MdMenuTree> lists = new ArrayList<>();
        for(MdMenuTree mdMenuTree : permissionList){
            Boolean flag = false;
            for(MdRolePermissions mdRolePermissions : permissions){
                String id = mdMenuTree.getId();
                if(mdRolePermissions.getPermissionid().equals(id)){
                    lists.add(mdMenuTree);
                    flag = true;
                }
            }
            if(flag){
                continue;
            }else {
                Map<String, Boolean> statemap = mdMenuTree.getState();
                statemap.put("disabled", true);
                mdMenuTree.setStateMap(statemap);
                lists.add(mdMenuTree);
            }
        }
        return lists;
    }

    /**
     * ???????????????????????????
     * @return
     */
    public List<MdArea> queryAreaRolePermissionWithRoleid(List<MdArea> permissionList, String roleid){
        List<MdRolePermissions> permissions = roleManageDAO.getMenuRolePermissionByRoleid(roleid,Constant.PERMISSION_AREA);
        List<MdArea> lists = new ArrayList<>();
        for(MdArea mdArea : permissionList){
            Boolean flag = false;
            for(MdRolePermissions mdRolePermissions : permissions){
                String id = mdArea.getAreano();
                if(mdRolePermissions.getPermissionid().equals(id)){
                    mdArea.setDisabled(Constant.CHECKBOX_ABLED);
                    lists.add(mdArea);
                    flag = true;
                }
            }
            if(flag){
                continue;
            }else {
                mdArea.setDisabled(Constant.CHECKBOX_DISABLED);
                lists.add(mdArea);
            }
        }
        return lists;
    }

    /**
     * ???????????????????????????
     * @return
     */
    public List<MdNode> queryNodeRolePermissionWithRoleid(List<MdNode> permissionList, String roleid){
        List<MdRolePermissions> permissions = roleManageDAO.getMenuRolePermissionByRoleid(roleid,Constant.PERMISSION_NODE);
        List<MdNode> lists = new ArrayList<>();
        for(MdNode mdNode : permissionList){
            Boolean flag = false;
            for(MdRolePermissions mdRolePermissions : permissions){
                String id = mdNode.getId();
                if(mdRolePermissions.getPermissionid().equals(id)){
                    mdNode.setDisabled(Constant.CHECKBOX_ABLED);
                    lists.add(mdNode);
                    flag = true;
                }
            }
            if(flag){
                continue;
            }else {
                mdNode.setDisabled(Constant.CHECKBOX_DISABLED);
                lists.add(mdNode);
            }
        }
        return lists;
    }
}
