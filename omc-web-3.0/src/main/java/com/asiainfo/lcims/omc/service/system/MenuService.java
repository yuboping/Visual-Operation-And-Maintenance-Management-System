package com.asiainfo.lcims.omc.service.system;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.apn.MdApnRecord;
import com.asiainfo.lcims.omc.model.configmanage.BdNas;
import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.po.MonHost;
import com.asiainfo.lcims.omc.persistence.system.MdMenuDAO;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ConstantUtill;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;

@Service(value = "menuService")
public class MenuService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuService.class);
    @Inject
    private MdMenuDAO mdMenuDAO;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    /**
     * 
     * @Title: getAllMenuByRoot @Description: (根据根目录获取菜单信息) @param @return
     *         参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getAllMenu() {
        List<MdMenu> mdMenuList = mdMenuDAO.getMenuList();
        return generateMenu(mdMenuList);
    }

    /**
     * 
     * @Title: getParentMdMenuById @Description: (根据ID获取菜单) @param @param
     *         name @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getMdMenuById(String Id, List<MdMenu> mdMenuList) {
        for (MdMenu mdMenu : mdMenuList) {
            if (mdMenu.getId().equals(Id)) {
                return mdMenu;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: generateMenu @Description: (生成菜单) @param @param mdMenuList
     *         参数 @return void 返回类型 @throws
     */
    public List<MdMenu> generateMenu(List<MdMenu> mdMenuList) {
        if (mdMenuList != null && mdMenuList.size() < 2) {
            return mdMenuList;
        }
        List<MdMenu> dynamicMdMenuList = setDynamicMdMenu(mdMenuList);

        // 根据角色获取权限过滤菜单
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        List<MdMenu> mdMenus = mdMenuDataListener.getMenuWithRolePermission(user.getAdmin(),
                dynamicMdMenuList, user.getRoleid());

        for (MdMenu mdMenu : mdMenus) {
            if (1 == mdMenu.getIs_show()) {
                setChildrenMdMenu(mdMenu, mdMenus);
            }
        }

        return mdMenus;
    }

    /**
     * 
     * @Title: getChildrenMdMenu @Description: (生成菜单下的子菜单) @param @param
     *         mdMenu @param @param mdMenuList 参数 @return void 返回类型 @throws
     */
    public void setChildrenMdMenu(MdMenu mdMenu, List<MdMenu> mdMenuList) {
        List<MdMenu> childrenList = new ArrayList<MdMenu>();
        for (MdMenu child : mdMenuList) {
            if (mdMenu.getId().equals(child.getParent_id()) && !"select".equals(child.getUrl())) {
                childrenList.add(child);
            }
        }
        mdMenu.setChildren(childrenList);
    }

    /**
     * 
     * @Title: setDynamicMdMenu @Description: (生成动态菜单) @param @param mdMenuList
     *         参数 @return void 返回类型 @throws
     */
    public List<MdMenu> setDynamicMdMenu(List<MdMenu> mdMenuList) {
        List<MdMenu> dynamicMdMenuList = new ArrayList<MdMenu>();
        for (MdMenu mdMenu : mdMenuList) {
            if (!"0".equals(mdMenu.getDynamic())) {
                MdMenu parentMdMenu = getMdMenuById(mdMenu.getParent_id(), mdMenuList);
                addMdMenuByDynamicType(mdMenu, parentMdMenu, "", dynamicMdMenuList, mdMenuList);
            } else {
                dynamicMdMenuList.add(mdMenu);
            }
        }
        return dynamicMdMenuList;
    }

    /**
     * 
     * @Title: addMdMenuByDynamicType @Description:
     *         (根据动态菜单标识类型添加菜单) @param @param mdMenu @param @param mdMenuList
     *         参数 @return void 返回类型 @throws
     */
    public void addMdMenuByDynamicType(MdMenu mdMenu, MdMenu parentMdMenu, String suffix,
            List<MdMenu> dynamicMdMenuList, List<MdMenu> mdMenuList) {
        MAdmin user = (MAdmin) SessionUtils.getFromSession(Constant.CURRENT_USER);
        String username = user.getAdmin();
        String roleid = user.getRoleid();
        switch (mdMenu.getDynamic()) {
        case Constant.DYNAMICTYPE_NODE:
            addChildrenNodeMdMenu(mdMenu, parentMdMenu, suffix, dynamicMdMenuList, mdMenuList,
                    username, roleid);
            break;
        case Constant.DYNAMICTYPE_AREA:
            addChildrenAreaMdMenu(mdMenu, parentMdMenu, suffix, dynamicMdMenuList, mdMenuList,
                    username, roleid);
            break;
        case Constant.DYNAMICTYPE_HOST:
            addChildrenHostMdMenu(mdMenu, parentMdMenu, suffix, dynamicMdMenuList, mdMenuList);
            break;
        case Constant.DYNAMICTYPE_BRAS:
            addChildrenBrasMdMenu(mdMenu, parentMdMenu, suffix, dynamicMdMenuList, mdMenuList);
            break;
        case Constant.DYNAMICTYPE_APN:
            addChildrenApnMdMenu(mdMenu, parentMdMenu, suffix, dynamicMdMenuList, mdMenuList);
            break;
        default:
            break;
        }
    }

    /**
     * 
     * @Title: addChildrenNodeMdMenu @Description: (添加节点动态子菜单) @param @param
     *         mdMenu @param @param childrenList 参数 @return void 返回类型 @throws
     */
    public void addChildrenNodeMdMenu(MdMenu mdMenu, MdMenu parentMdMenu, String suffix,
            List<MdMenu> dynamicMdMenuList, List<MdMenu> mdMenuList, String username,
            String roleid) {
        List<MdNode> mdNodeList = MdMenuDataListener.getMdNodeList();
        mdNodeList = mdMenuDataListener.getNodeWithRolePermission(username, mdNodeList, roleid);
        int sequence = 1;
        for (MdNode mdNode : mdNodeList) {
            String id = mdNode.getId();
            String name = mdNode.getNode_name();
            structureMdMenu(mdMenu, suffix, id, name, sequence, parentMdMenu, dynamicMdMenuList,
                    mdMenuList, name);
        }
    }

    /**
     * 
     * @Title: addChildrenAreaMdMenu @Description: (添加属地动态子菜单) @param @param
     *         mdMenu @param @param mdMenuList 参数 @return void 返回类型 @throws
     */
    public void addChildrenAreaMdMenu(MdMenu mdMenu, MdMenu parentMdMenu, String suffix,
            List<MdMenu> dynamicMdMenuList, List<MdMenu> mdMenuList, String username,
            String roleid) {
        List<MdArea> mdAreaList = MdMenuDataListener.getMdAreaList();
        mdAreaList = mdMenuDataListener.getAreaWithRolePermission(username, mdAreaList, roleid);
        int sequence = 1;
        for (MdArea mdArea : mdAreaList) {
            String id = mdArea.getAreano();
            String name = mdArea.getName();
            structureMdMenu(mdMenu, suffix, id, name, sequence, parentMdMenu, dynamicMdMenuList,
                    mdMenuList, name);
        }
    }

    /**
     * 
     * @Title: addChildrenHostMdMenu @Description: (添加主机动态子菜单) @param @param
     *         mdMenu @param @param childrenList 参数 @return void 返回类型 @throws
     */
    public void addChildrenHostMdMenu(MdMenu mdMenu, MdMenu parentMdMenu, String suffix,
            List<MdMenu> dynamicMdMenuList, List<MdMenu> mdMenuList) {
        List<MonHost> monHostList = new ArrayList<MonHost>();
        if (Constant.DYNAMICTYPE_NODE.equals(parentMdMenu.getDynamic())) {
            // 筛选节点下主机
            List<MonHost> monHostListAll = MdMenuDataListener.getMonHostList();
            for (MonHost monHost : monHostListAll) {
                if (monHost.getNodeid().equals(parentMdMenu.getName())) {
                    // 筛选业务下的主机
                    MdBusinessHost mdBusinessHost = mdMenuDataListener
                            .getMdBusinessHostByNameAndHostid(mdMenu.getName(),
                                    monHost.getHostid());
                    if (mdBusinessHost != null) {
                        monHostList.add(monHost);
                    }
                }
            }
        } else {
            // 筛选业务下的主机
            List<MdBusinessHost> mdBusinessHostList = mdMenuDataListener
                    .getMdBusinessHostListByName(mdMenu.getName());
            for (MdBusinessHost mdBusinessHost : mdBusinessHostList) {
                monHostList.add(mdMenuDataListener.getMonHostById(mdBusinessHost.getHostid()));
            }
        }
        int sequence = 1;
        for (MonHost monHost : monHostList) {
            if (monHost == null) {
                return;
            }
            String id = monHost.getHostid();
            String name = monHost.getHostname();
            String show_title = monHost.getAddr();
            structureMdMenu(mdMenu, suffix, id, name, sequence, parentMdMenu, dynamicMdMenuList,
                    mdMenuList, show_title);
        }
    }

    /**
     * 
     * @Title: addChildrenBrasMdMenu @Description: (添加Bras动态子菜单) @param @param
     *         mdMenu @param @param childrenList 参数 @return void 返回类型 @throws
     */
    public void addChildrenBrasMdMenu(MdMenu mdMenu, MdMenu parentMdMenu, String suffix,
            List<MdMenu> dynamicMdMenuList, List<MdMenu> mdMenuList) {
        List<BdNas> bdNasList = new ArrayList<BdNas>();
        if (Constant.DYNAMICTYPE_AREA.equals(parentMdMenu.getDynamic())) {
            // 筛选属地下bas
            List<BdNas> bdNasListAll = MdMenuDataListener.getBdNasList();
            for (BdNas BdNas : bdNasListAll) {
                if (BdNas.getArea_no().equals(parentMdMenu.getName())) {
                    bdNasList.add(BdNas);
                }
            }
        } else {
            bdNasList = MdMenuDataListener.getBdNasList();
        }
        int sequence = 1;
        for (BdNas bdNas : bdNasList) {
            String id = bdNas.getId();
            String name = bdNas.getNas_name();
            String show_title = bdNas.getNas_ip();
            structureMdMenu(mdMenu, suffix, id, name, sequence, parentMdMenu, dynamicMdMenuList,
                    mdMenuList, show_title);
        }
    }

    /**
     * 
     * @Title: addChildrenApnMdMenu @Description: (添加APN动态子菜单) @param @param
     *         mdMenu @param @param parentMdMenu @param @param
     *         suffix @param @param dynamicMdMenuList @param @param mdMenuList
     *         参数 @return void 返回类型 @throws
     */
    public void addChildrenApnMdMenu(MdMenu mdMenu, MdMenu parentMdMenu, String suffix,
            List<MdMenu> dynamicMdMenuList, List<MdMenu> mdMenuList) {
        List<MdApnRecord> apnRecordList = CommonInit.getMdApnRecordList();
        int sequence = 1;
        for (MdApnRecord apnRecord : apnRecordList) {
            String id = apnRecord.getApnid();
            String name = apnRecord.getApn();
            String show_title = apnRecord.getApn();
            structureMdMenu(mdMenu, suffix, id, name, sequence, parentMdMenu, dynamicMdMenuList,
                    mdMenuList, show_title);
        }
    }

    /**
     * 
     * @Title: structureMdMenu @Description: (构造动态菜单) @param @param
     *         mdMenu @param @param suffix @param @param id @param @param
     *         name @param @param sequence @param @param parent_id @param @param
     *         dynamicMdMenuList @param @param mdMenuList 参数 @return void
     *         返回类型 @throws
     */
    public void structureMdMenu(MdMenu mdMenu, String suffix, String id, String name, int sequence,
            MdMenu parentMdMenu, List<MdMenu> dynamicMdMenuList, List<MdMenu> mdMenuList,
            String show_title) {
        MdMenu menu = new MdMenu();
        String currentSuffix = suffix + "/" + id;
        String url = mdMenu.getUrl() + ConstantUtill.URL_SPLIT + currentSuffix;
        menu.setShow_name(name);
        menu.setShow_title(show_title);
        menu.setName(id);
        menu.setUrl(url);
        menu.setSequence(sequence);
        menu.setParent_id(parentMdMenu.getId());
        String uuid = IDGenerateUtil.getUuid();
        menu.setId(uuid);
        menu.setIs_menu(mdMenu.getIs_menu());
        menu.setIs_grant(mdMenu.getIs_grant());
        menu.setIs_show(mdMenu.getIs_show());
        menu.setMenu_level(mdMenu.getMenu_level());
        menu.setDynamic(mdMenu.getDynamic());
        menu.setLargeicon(mdMenu.getLargeicon());
        menu.setActive(mdMenu.getActive());
        menu.setChildren(mdMenu.getChildren());
        menu.setIcon(mdMenu.getIcon());
        dynamicMdMenuList.add(menu);
        for (MdMenu m : mdMenuList) {
            if (mdMenu.getId().equals(m.getParent_id())) {
                addMdMenuByDynamicType(m, menu, currentSuffix, dynamicMdMenuList, mdMenuList);
            }
        }
        sequence++;
    }

    /**
     * 
     * @Title: getMdMenuHostList @Description: (查询动态主机菜单) @param @return
     *         参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getMdMenuHostList() {
        List<MdMenu> mdMenuHostList = new ArrayList<MdMenu>();
        List<MdMenu> mdMenuList = mdMenuDAO.getMenuList();
        for (MdMenu mdMenu : mdMenuList) {
            if (1 == mdMenu.getIs_show()) {
                setChildrenMdMenu(mdMenu, mdMenuList);
            }
        }
        for (MdMenu mdMenu : mdMenuList) {
            if (mdMenu.getDynamic().equals(Constant.DYNAMICTYPE_HOST)) {
                getAllParentMdMenu(mdMenu, mdMenu, mdMenuList);
                mdMenuHostList.add(mdMenu);
            }
        }
        return mdMenuHostList;
    }

    /**
     * 
     * @Title: getAllParentMdMenu @Description: (获取所有父菜单) @param @param
     *         Id @param @param mdMenuList @param @return 参数 @return
     *         List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getAllParentMdMenu(MdMenu mdMenuHost, MdMenu mdMenu,
            List<MdMenu> mdMenuList) {
        if (mdMenu.getMenu_level() > 1) {
            MdMenu parentMdMenu = getMdMenuById(mdMenu.getParent_id(), mdMenuList);
            if (mdMenuHost.getBusiness_link() != null
                    && !mdMenuHost.getBusiness_link().equals("")) {
                mdMenuHost.setBusiness_link(
                        parentMdMenu.getShow_name() + "->" + mdMenuHost.getBusiness_link());
            } else {
                mdMenuHost.setBusiness_link(
                        parentMdMenu.getShow_name() + "->" + mdMenuHost.getShow_name());
            }
            getAllParentMdMenu(mdMenuHost, parentMdMenu, mdMenuList);
        }
        return mdMenuList;
    }

    /**
     * 
     * @Title: getMdMenuBusinessLink @Description: (根据URL获取链路信息) @param @param
     *         URL @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getMdMenuBusinessLink(String URL) {
        List<MdMenu> mdMenuList = mdMenuDAO.getMenuList();
        for (MdMenu mdMenu : mdMenuList) {
            if (1 == mdMenu.getIs_show()) {
                setChildrenMdMenu(mdMenu, mdMenuList);
            }
        }
        for (MdMenu mdMenu : mdMenuList) {
            if (mdMenu.getUrl().equals(URL)) {
                getAllParentMdMenu(mdMenu, mdMenu, mdMenuList);
                return mdMenu;
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getTwoLevelMdMenu @Description: (根据URL找二级菜单) @param @param
     *         URL @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getTwoLevelMdMenu(String URL) {
        try {
            List<MdMenu> mdMenuList = mdMenuDAO.getMenuList();
            for (MdMenu mdMenu : mdMenuList) {
                if (1 == mdMenu.getIs_show()) {
                    setChildrenMdMenu(mdMenu, mdMenuList);
                }
            }
            for (MdMenu mdMenu : mdMenuList) {
                if (mdMenu.getUrl().equals(URL)) {
                    MdMenu twoLevelMdMenu = recursionFindTwoLevelMdMenu(mdMenu, mdMenuList);
                    if (null != twoLevelMdMenu) {
                        return twoLevelMdMenu;
                    } else {
                        LOG.error(
                                "MenuService getTwoLevelMdMenu return twoLevelMdMenu is null , URL:{} ",
                                URL);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("MenuService getTwoLevelMdMenu Exception:{}", e);
        }
        return null;
    }

    /**
     * 
     * @Title: recursionFindTwoLevelMdMenu @Description: (递归找二级菜单) @param @param
     *         mdMenu @param @param mdMenuList @param @return 参数 @return MdMenu
     *         返回类型 @throws
     */
    public MdMenu recursionFindTwoLevelMdMenu(MdMenu mdMenu, List<MdMenu> mdMenuList) {
        if (mdMenu.getMenu_level() > 2) {
            MdMenu parentMdMenu = getMdMenuById(mdMenu.getParent_id(), mdMenuList);
            return recursionFindTwoLevelMdMenu(parentMdMenu, mdMenuList);
        } else {
            return mdMenu;
        }
    }

    /**
     * 
     * @Title: getParentMdMenuList @Description: (根据父节点ID查询菜单列表) @param @param
     *         parent_id @param @return 参数 @return List<MdMenu> 返回类型 @throws
     */
    public List<MdMenu> getParentMdMenuList(String parent_id) {
        return mdMenuDAO.getParentMdMenuList(parent_id);
    }

    /**
     * 
     * @Title: getMdMenuById @Description: (根据ID获取菜单) @param @param
     *         Id @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getMdMenuById(String Id) {
        return mdMenuDAO.getMdMenuById(Id);
    }

    /**
     * 
     * @Title: getMdMenuHostBySummaryId @Description:
     *         (根据总览找平级的动态host) @param @param Id @param @return 参数 @return
     *         MdMenu 返回类型 @throws
     */
    public MdMenu getMdMenuHostBySummaryId(String summaryId) {
        List<MdMenu> mdMenuList = mdMenuDAO.getMenuList();
        for (MdMenu mdMenu : mdMenuList) {
            if (1 == mdMenu.getIs_show()) {
                setChildrenMdMenu(mdMenu, mdMenuList);
            }
        }
        MdMenu summaryMdMenu = getMdMenuById(summaryId);
        for (MdMenu mdMenu : mdMenuList) {
            if (mdMenu.getId().equals(summaryMdMenu.getParent_id())) {
                List<MdMenu> mdMenuChildrenList = mdMenu.getChildren();
                for (MdMenu mdMenuChildren : mdMenuChildrenList) {
                    if (mdMenuChildren.getDynamic().equals(Constant.DYNAMICTYPE_HOST)) {
                        return mdMenuChildren;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * @Title: getMdMenuByUrl @Description: (根据URL获取菜单) @param @param
     *         URL @param @return 参数 @return MdMenu 返回类型 @throws
     */
    public MdMenu getMdMenuByUrl(String URL) {
        List<MdMenu> mdMenuList = mdMenuDAO.getMenuList();
        for (MdMenu mdMenu : mdMenuList) {
            if (mdMenu.getUrl().equals(URL)) {
                return mdMenu;
            }
        }
        return null;
    }
}
