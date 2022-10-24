package com.asiainfo.lcims.omc.persistence;

import com.asiainfo.lcims.lcbmi.utils.logger.LoggerFactory;
import com.asiainfo.lcims.omc.model.configmanage.ProcessOperate;
import com.asiainfo.lcims.omc.model.system.MdMenuTree;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 复杂sql
 *
 * @author yangyc
 *
 */
public class SqlRoleProvider {

    private static final Logger LOG = LoggerFactory.make();

    /**
     * 根据查询条件查询角色
     *
     * @param parameters
     * @return
     */
    public String getRoleList(Map<String, Object> parameters) {
        String name = (String) parameters.get("name");
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT * FROM MD_ROLE WHERE ROLEID <> '0' ");
        if (!StringUtils.isEmpty(name)) {
            strb.append(" AND NAME LIKE '%").append(name).append("%'");
        }
        return strb.toString();
    }

    /**
     * 获取所有菜单
     * @return
     */
    public String getMenuTreeByRole(){
        StringBuffer strb = new StringBuffer();
        strb.append("SELECT F.ID , F.SHOW_NAME AS TEXT , F.PARENT_ID AS PARENT FROM MD_MENU_TREE F WHERE F.IS_GRANT = 1 ORDER BY F.ID ASC");
        return strb.toString();
    }

}
