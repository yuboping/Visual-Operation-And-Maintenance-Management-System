package com.asiainfo.lcims.omc.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.alarm.MdAlarmInfo;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.model.system.MdMenuTree;
import com.asiainfo.lcims.omc.model.system.MdRolePermissions;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.alarm.MdAlarmInfoDao;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.service.configmanage.RoleManageService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.ShiroUtil;

/**
 * 初始化菜单加载数据到内存
 * 
 * @author yuboping
 * 
 */
@Service(value = "mdMenuDataListener")
public class MdMenuDataListener {

    @Inject
    MenuService menuService;

    @Inject
    RoleManageService rolemanageService;

    @Autowired
    private MdAlarmInfoDao mdAlarmInfoDao;

    @Autowired
    private CommonInit commonInit;

    /**
     * 创建用户菜单列表Map
     */
    private static Map<String, List<MdMenu>> userMdMenulistMap = new HashMap<String, List<MdMenu>>();

    /**
     * 创建告警所需菜单列表
     */
    private static List<MdMenu> mdMenuWithAlarmList = new ArrayList<>();

    public static Map<String, List<MdMenu>> getUserMdMenulistMap() {
        return userMdMenulistMap;
    }

    public static List<MdNode> getMdNodeList() {
        List<MdNode> nodeList = CommonInit.getNodeList();
        return nodeList;
    }

    public static List<MdArea> getMdAreaList() {
        List<MdArea> mdAreaList = CommonInit.getMdAreaList();
        return mdAreaList;
    }

    public static List<MonHost> getMonHostList() {
        List<MonHost> monHostList = CommonInit.getMonHostList();
        return monHostList;
    }

    public static List<BdNas> getBdNasList() {
        List<BdNas> bdNasList = CommonInit.getBdNasList();
        return bdNasList;
    }

    public static List<MdMenu> getMdMenuWithAlarmList() {
        return mdMenuWithAlarmList;
    }

    public static void setUserMdMenulistMap(Map<String, List<MdMenu>> userMdMenulistMap) {
        MdMenuDataListener.userMdMenulistMap = userMdMenulistMap;
    }

    public static void setMdMenuWithAlarmList(List<MdMenu> mdMenuWithAlarmList) {
        MdMenuDataListener.mdMenuWithAlarmList = mdMenuWithAlarmList;
    }

    /**
     * 
     * @Title: getUsername @Description: (获取用户名) @param @return 参数 @return
     *         String 返回类型 @throws
     */
    public String getUsername() {
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        return user.getAdmin();
    }

    /**
     * 
     * @Title: loadMenuList @Description: (加载菜单数据) @return void 返回类型 @throws
     */
    public void loadMenuList() {
        // 增加清除权限
        ShiroUtil.clearAuth();
        // mdNodeList = mdNodeDAO.getMdNode(new MdNode());
        // mdAreaList = areaDAO.getAll();
        // monHostList = monHostDAO.getAllHost();
        // bdNasList = bdNasDAO.getBdNas(new BdNas());
        // mdBusinessHostList = mdBusinessHostDAO.getMdBusinessHost(new
        // MdBusinessHost());
        commonInit.init();
        userMdMenulistMap.put(getUsername(), menuService.getAllMenu());
    }
    
    // 修改MdMenu中alarmcount字段的值，使页面能够显示
    public List<MdMenu> getAlarmCount(List<MdMenu> mdMenuList) {
        for (MdMenu mdMenu : mdMenuList) {
            Integer alarmNum = 0;
            List<MdMenu> childMenuList = mdMenu.getChildren();
            if (childMenuList != null) {
                for (MdMenu childMenus : childMenuList) {
                    String url = childMenus.getUrl();
                    Integer calcNum = calcNumByUrl(url);
                    alarmNum = alarmNum + calcNum;
                    List<MdMenu> childMenu = childMenus.getChildren();
                    if (childMenu != null) {
                        for (MdMenu menu : childMenu) {
                            String childurl = menu.getUrl();
                            Integer childNum = calcNumByUrl(childurl);
                            alarmNum = alarmNum + childNum;
                        }
                    }
                }
            }
            mdMenu.setAlarmcount(alarmNum);
        }
        return mdMenuList;
    }

    public Integer calcNumByUrl(String url) {
        Integer alarmNum = 0;
        List<MdAlarmInfo> alarmInfos = mdAlarmInfoDao.getAlarmNum(url);
        if (alarmInfos != null) {
            for (MdAlarmInfo alarmInfo : alarmInfos) {
                alarmNum = alarmNum + alarmInfo.getAlarm_num();
            }
        }
        return alarmNum;
    }

    // 查询告警信息表里所有数据
    public List<MdAlarmInfo> getAllAlarmInfo() {
        List<MdAlarmInfo> alarmInfos = mdAlarmInfoDao.getAlarmInfos();
        return alarmInfos;
    }

    /**
     * 
     * @Title: getMonHostById @Description: (根据ID查找节点) @param @param
     *         Id @param @return 参数 @return MonHost 返回类型 @throws
     */
    public MdNode getMdNodeById(String Id) {
        List<MdNode> nodeList = CommonInit.getNodeList();
        for (MdNode mdNode : nodeList) {
            if (mdNode.getId().equals(Id)) {
                return mdNode;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getMonHostById @Description: (根据ID查找属地) @param @param
     *         Id @param @return 参数 @return MonHost 返回类型 @throws
     */
    public MdArea getMdAreaById(String Id) {
        List<MdArea> mdAreaList = CommonInit.getMdAreaList();
        for (MdArea mdArea : mdAreaList) {
            if (mdArea.getAreano().equals(Id)) {
                return mdArea;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getMonHostById @Description: (根据ID查找主机) @param @param
     *         Id @param @return 参数 @return MonHost 返回类型 @throws
     */
    public MonHost getMonHostById(String Id) {
        List<MonHost> monHostList = CommonInit.getMonHostList();
        for (MonHost monHost : monHostList) {
            if (monHost.getHostid().equals(Id)) {
                return monHost;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getMonHostById @Description: (根据ID查找Nas) @param @param
     *         Id @param @return 参数 @return MonHost 返回类型 @throws
     */
    public BdNas getBdNasById(String Id) {
        List<BdNas> bdNasList = CommonInit.getBdNasList();
        for (BdNas bdNas : bdNasList) {
            if (bdNas.getId().equals(Id)) {
                return bdNas;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getMonHostById @Description:
     *         (根据Name查找MdBusinessHost列表) @param @param Id @param @return
     *         参数 @return MonHost 返回类型 @throws
     */
    public List<MdBusinessHost> getMdBusinessHostListByName(String name) {
        List<MdBusinessHost> mdBusinessHostByNameList = new ArrayList<MdBusinessHost>();
        for (MdBusinessHost mdBusinessHost : CommonInit.getMdbusinesshostlist()) {
            if (mdBusinessHost.getName().equals(name)) {
                mdBusinessHostByNameList.add(mdBusinessHost);
            }
        }
        return mdBusinessHostByNameList;
    }

    /**
     * 
     * @Title: getMdBusinessHostByNameAndHostid @Description:
     *         (根据name和hostid查找MdBusinessHost) @param @param name @param @param
     *         hostid @param @return 参数 @return MdBusinessHost 返回类型 @throws
     */
    public MdBusinessHost getMdBusinessHostByNameAndHostid(String name, String hostid) {
        for (MdBusinessHost mdBusinessHost : getMdBusinessHostListByName(name)) {
            if (mdBusinessHost.getHostid().equals(hostid)) {
                return mdBusinessHost;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: releaseMenuList @Description: (释放菜单数据) @param @return void
     *         返回类型 @throws
     */
    public void releaseMenuList() {
        userMdMenulistMap.put(getUsername(), null);
    }

    /**
     * 
     * @Title: getMdMenulist @Description: (获取内存的菜单数据) @param @return 参数 @return
     *         List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getMdMenulist() {
        return userMdMenulistMap.get(getUsername());
    }

    /**
     * 
     * @Title: getTopMenuList @Description: TODO(获取首页菜单) @param @return
     *         参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getTopMenuList() {
        MdMenu mdMenu = getMdMenuById(Constant.SYS_MENU_ID);
        return mdMenu.getChildren();
    }

    /**
     * 
     * @Title: getMdMenuById @Description: (根据ID查找菜单) @param @param
     *         Id @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getMdMenuById(String Id) {
        for (MdMenu mdMenu : getMdMenulist()) {
            if (mdMenu.getId().equals(Id)) {
                return mdMenu;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getChildrenMdMenuById @Description: (根据ID获取子菜单列表) @param @param
     *         name @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getChildrenMdMenuById(String Id) {
        return getMdMenuById(Id).getChildren();
    }

    /**
     * 
     * @Title: getParentMdMenuById @Description: (根据ID获取父菜单) @param @param
     *         name @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getParentMdMenuById(String Id) {
        return getMdMenuById(getMdMenuById(Id).getParent_id());
    }

    /**
     * 
     * @Title: getBrotherMdMenuById @Description: (根据ID获取兄弟菜单) @param @param
     *         name @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getBrotherMdMenuById(String Id) {
        return getChildrenMdMenuById(getParentMdMenuById(Id).getId());
    }

    /**
     * 
     * @Title: getMdMenuByName @Description: (根据name获取菜单) @param @param
     *         name @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getMdMenuByName(String name) {
        for (MdMenu mdMenu : getMdMenulist()) {
            if (mdMenu.getName().equals(name)) {
                return mdMenu;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getChildrenMdMenuByName @Description: (根据name获取子菜单) @param @param
     *         name @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getChildrenMdMenuByName(String name) {
        return getMdMenuByName(name).getChildren();
    }

    /**
     * 
     * @Title: getParentMdMenuByname @Description: (根据name获取父菜单) @param @param
     *         name @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getParentMdMenuByName(String name) {
        return getMdMenuById(getMdMenuByName(name).getParent_id());
    }

    /**
     * 
     * @Title: getBrotherMdMenuByName @Description: (根据name获取兄弟菜单) @param @param
     *         name @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getBrotherMdMenuByName(String name) {
        return getChildrenMdMenuByName(getParentMdMenuByName(name).getName());
    }

    /**
     * 
     * @Title: getMdMenuByUrl @Description: (根据URL获取菜单) @param @param
     *         Url @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public MdMenu getMdMenuByUrl(String url) {
        for (MdMenu mdMenu : getMdMenulist()) {
            if (mdMenu.getUrl().equals(url)) {
                return mdMenu;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getMdMenuByUrl @Description: (根据URL获取子菜单) @param @param
     *         Url @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getChildrenMdMenuByUrl(String url) {
        return getMdMenuByUrl(url).getChildren();
    }

    /**
     * 
     * @Title: getParentMdMenuByUrl @Description: (根据URL获取父菜单) @param @param
     *         url @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getParentMdMenuByUrl(String url) {
        return getMdMenuById(getMdMenuByUrl(url).getParent_id());
    }

    /**
     * 
     * @Title: getBrotherMdMenuByUrl @Description: (根据URL获取兄弟菜单) @param @param
     *         url @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getBrotherMdMenuByUrl(String url) {
        return getChildrenMdMenuByUrl(getParentMdMenuByUrl(url).getUrl());
    }

    /**
     * 
     * @Title: setMdMenuActive @Description: (根据URL设置菜单的active) @param @param
     *         name @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> setMdMenuActive(String url) {
        setMdMenuActiveFalse();
        List<MdMenu> mdMenulist = getMdMenulist();
        for (MdMenu mdMenu : mdMenulist) {
            if (mdMenu.getUrl().equals(url)) {
                setMdMenuActiveByLevel(mdMenu);
                break;
            }
        }
        return mdMenulist;
    }

    /**
     * 
     * @Title: setChildrenMdMenuFirstOrderActive @Description:
     *         (设置子菜单中第一顺序的菜单置为选中) @param @param mdMenu 参数 @return void
     *         返回类型 @throws
     */
    public void setChildrenMdMenuFirstOrderActive(MdMenu mdMenu) {
        List<MdMenu> mdMenuList = mdMenu.getChildren();
        if (null != mdMenuList && 0 != mdMenuList.size()) {
            mdMenuList.get(0).setActive(true);
        }
    }

    /**
     * 
     * @Title: setMdMenuActiveFalse @Description: (重置active) @param @return void
     *         返回类型 @throws
     */
    public void setMdMenuActiveFalse() {
        for (MdMenu mdMenu : getMdMenulist()) {
            mdMenu.setActive(false);
        }
    }

    /**
     * 
     * @Title: setMdMenuActiveByLevel @Description:
     *         (根据Level设置菜单的active) @param @param mdMenu 参数 @return void
     *         返回类型 @throws
     */
    public void setMdMenuActiveByLevel(MdMenu mdMenu) {
        mdMenu.setActive(true);
        if (mdMenu.getMenu_level() > 1) {
            MdMenu parentMdMenu = getMdMenuById(mdMenu.getParent_id());
            setMdMenuActiveByLevel(parentMdMenu);
        }
    }

    /**
     * 
     * @Title: getMdMenuByLevel @Description: (根据Level获取菜单) @param @param mdMenu
     *         参数 @return void 返回类型 @throws
     */
    public MdMenu getMdMenuByLevel(MdMenu mdMenu, Integer level) {
        if (level >= mdMenu.getMenu_level()) {
            return mdMenu;
        } else {
            MdMenu parentMdMenu = getParentMdMenuById(mdMenu.getId());
            return getMdMenuByLevel(parentMdMenu, level);
        }
    }

    /**
     * 添加角色权限控制
     * 
     * @param username
     * @param mdMenuList
     * @return
     */
    public List<MdMenu> getMenuWithRolePermission(String username, List<MdMenu> mdMenuList,
            String roleid) {
        List<MdMenuTree> mdMenuTrees = CommonInit.getMdMenuTreeList();
        List<MonHost> monHostList = CommonInit.getMonHostList();
        if (roleid == Constant.ADMIN_ROLEID || Constant.ADMIN_ROLEID.equals(roleid)) {
            mdMenuWithAlarmList = mdMenuList;
            return mdMenuList;
        }
        List<MdRolePermissions> menuPermissionsList = rolemanageService
                .queryMenuByUsername(username);
        List<MdRolePermissions> areaPermissionsList = rolemanageService
                .queryPermissionTypeByUsername(username, Constant.PERMISSION_AREA);
        List<MdRolePermissions> nodePermissionsList = rolemanageService
                .queryPermissionTypeByUsername(username, Constant.PERMISSION_NODE);
        List<MdMenu> mdCorList = new ArrayList<MdMenu>();
        for (MdMenu mdMenu : mdMenuList) {
            if (Constant.PERMISSION_CHILDREN_MENU.equals(mdMenu.getDynamic())) {
                for (MdRolePermissions mdRolePermissions : menuPermissionsList) {
                    if (mdRolePermissions != null
                            && mdRolePermissions.getPermissionid().equals(mdMenu.getId())) {
                        mdCorList.add(mdMenu);
                    }
                }
            } else if (Constant.PERMISSION_CHILDREN_AREA.equals(mdMenu.getDynamic())) {
                for (MdRolePermissions mdRolePermissions : areaPermissionsList) {
                    if (mdRolePermissions != null
                            && mdRolePermissions.getPermissionid().equals(mdMenu.getName())) {
                        mdCorList.add(mdMenu);
                    }
                }
            } else if (Constant.PERMISSION_CHILDREN_NODE.equals(mdMenu.getDynamic())) {
                for (MdRolePermissions mdRolePermissions : nodePermissionsList) {
                    if (mdRolePermissions != null
                            && mdRolePermissions.getPermissionid().equals(mdMenu.getName())) {
                        mdCorList.add(mdMenu);
                    }
                }
            } else if (Constant.PERMISSION_CHILDREN_HOST.equals(mdMenu.getDynamic())) {
                String url = mdMenu.getUrl().substring(0,
                        (mdMenu.getUrl().indexOf(ConstantUtill.URL_SPLIT)));
                Boolean ifmenu = false;
                Boolean ifnode = false;
                for (MdRolePermissions mdRolePermissions : menuPermissionsList) {
                    String id = null;
                    for (MdMenuTree mdMenuTree : mdMenuTrees) {
                        if (mdMenu.getUrl() != null && mdMenuTree.getUrl().equals(url)) {
                            id = mdMenuTree.getId();
                            break;
                        }
                    }
                    if (id != null && id.equals(mdRolePermissions.getPermissionid())) {
                        ifmenu = true;
                        break;
                    }
                }
                for (MdRolePermissions mdNodePermissions : nodePermissionsList) {
                    String nodeid = null;
                    for (MonHost monHost : monHostList) {
                        if (monHost.getHostid() != null
                                && monHost.getHostid().equals(mdMenu.getName())) {
                            nodeid = monHost.getNodeid();
                            break;
                        }
                    }
                    if (nodeid != null && nodeid.equals(mdNodePermissions.getPermissionid())) {
                        ifnode = true;
                        break;
                    }
                }
                if (ifmenu && ifnode) {
                    mdCorList.add(mdMenu);
                }
            } else if (Constant.PERMISSION_CHILDREN_BRAS.equals(mdMenu.getDynamic())) {
                mdCorList.add(mdMenu);
            } else if (Constant.PERMISSION_CHILDREN_APN.equals(mdMenu.getDynamic())) {
                mdCorList.add(mdMenu);
            }
        }
        mdMenuWithAlarmList = mdCorList;
        return mdCorList;
    }

    /**
     * 添加角色权限控制
     * 
     * @param username
     * @param mdAreaList
     * @return
     */
    public List<MdArea> getAreaWithRolePermission(String username, List<MdArea> mdAreaList,
            String roleid) {
        if (roleid == Constant.ADMIN_ROLEID || Constant.ADMIN_ROLEID.equals(roleid)) {
            return mdAreaList;
        }
        List<MdRolePermissions> mdRolePermissionsList = rolemanageService
                .queryPermissionTypeByUsername(username, Constant.PERMISSION_AREA);
        List<MdArea> mdCorList = new ArrayList<MdArea>();
        for (MdArea mdArea : mdAreaList) {
            for (MdRolePermissions mdRolePermissions : mdRolePermissionsList) {
                if (mdRolePermissions != null) {
                    if (mdRolePermissions.getPermissionid().equals(mdArea.getAreano())) {
                        mdCorList.add(mdArea);
                    }
                }
            }
        }
        return mdCorList;
    }

    /**
     * 添加角色权限控制
     * 
     * @param username
     * @param mdNodeList
     * @return
     */
    public List<MdNode> getNodeWithRolePermission(String username, List<MdNode> mdNodeList,
            String roleid) {
        if (roleid == Constant.ADMIN_ROLEID || Constant.ADMIN_ROLEID.equals(roleid)) {
            return mdNodeList;
        }
        List<MdRolePermissions> mdRolePermissionsList = rolemanageService
                .queryPermissionTypeByUsername(username, Constant.PERMISSION_NODE);
        List<MdNode> mdCorList = new ArrayList<MdNode>();
        for (MdNode mdNode : mdNodeList) {
            for (MdRolePermissions mdRolePermissions : mdRolePermissionsList) {
                if (mdRolePermissions != null) {
                    if (mdRolePermissions.getPermissionid().equals(mdNode.getId())) {
                        mdCorList.add(mdNode);
                    }
                }
            }
        }
        return mdCorList;
    }

    public List<MdMenu> getLeftAlarm(List<MdMenu> mdMenus) {
        for (MdMenu leftMenu : mdMenus) {
            Integer alarmNum = 0;
            String leftUrl = leftMenu.getUrl();
            Integer calcNum = calcNumByUrl(leftUrl);
            alarmNum = alarmNum + calcNum;
            List<MdMenu> childLeftMenu = leftMenu.getChildren();
            if (childLeftMenu != null) {
                for (MdMenu childMenu : childLeftMenu) {
                    Integer childLeftNum = 0;
                    String childUrl = childMenu.getUrl();
                    Integer childNum = calcNumByUrl(childUrl);
                    alarmNum = alarmNum + childNum;
                    childLeftNum = childLeftNum + childNum;
                    childMenu.setAlarmcount(childLeftNum);
                }
            }
            leftMenu.setAlarmcount(alarmNum);
        }
        return mdMenus;
    }

    /**
     * 
     * @Title: getMenuTreeName @Description: TODO(获取菜单名称) @param @param
     *         url @param @return 参数 @return String 返回类型 @throws
     */
    public MdMenu getMenuTreeName(String url) {
        MdMenu menuTree = new MdMenu();
        List<MdMenu> mdMenulist = getMdMenulist();
        for (MdMenu mdMenu : mdMenulist) {
            if (mdMenu.getUrl().equals(url)) {
                menuTree = mdMenu;
            }
        }
        return menuTree;
    }

    /**
     * 
     * @Title: moveMdMenuChildren @Description: TODO(去除菜单中的子菜单) @param @param
     *         mdMenulist @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> moveMdMenuChildren(List<MdMenu> mdMenulist) {
        List<MdMenu> moveMdMenuChildrenlist = new ArrayList<MdMenu>();
        for (MdMenu mdMenu : mdMenulist) {
            MdMenu moveMdMenu = new MdMenu();
            moveMdMenu.setActive(mdMenu.getActive());
            moveMdMenu.setShow_name(mdMenu.getShow_name());
            moveMdMenu.setUrl(mdMenu.getUrl());
            moveMdMenuChildrenlist.add(moveMdMenu);
        }
        return moveMdMenuChildrenlist;
    }
}
