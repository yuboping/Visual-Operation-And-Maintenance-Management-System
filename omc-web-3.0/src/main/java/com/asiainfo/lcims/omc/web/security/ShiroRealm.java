package com.asiainfo.lcims.omc.web.security;

import com.asiainfo.lcims.lcbmi.password.PasswordException;
import com.asiainfo.lcims.lcbmi.password.PwdDES3;
import com.asiainfo.lcims.omc.model.system.AdminRole;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.system.AdminDAO;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.util.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named("shiroRealm")
public class ShiroRealm extends AuthorizingRealm {

    private static final Logger LOG = LoggerFactory.getLogger(ShiroRealm.class);
    @Inject
    private AdminDAO adminDAO;
    
    @Resource(name = "menuService")
    MenuService menuService;
    
    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;
    
    
    
    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取登录时输入的用户名
        String loginName = (String) principals.fromRealm(getName()).iterator().next();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Authorized Info For User : {} ", loginName);
        }
        MAdmin admin = adminDAO.getAdminByAdmin(loginName);
        // 到数据库查是否有此对象
        if (admin != null) {
//            List<AdminRole> roles = adminDAO.getRoles(loginName);
            // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 用户的角色集合
            info.setRoles(asRoleSets(null));
            info.setStringPermissions(asPermissionsSets(admin));
            return info;
        }
        return null;
    }

    private Set<String> asPermissionsSets(MAdmin admin) {
        Set<String> set = new HashSet<String>();
        List<MdMenu> mdMenuList = mdMenuDataListener.getMdMenulist();
        for (MdMenu mdMenu : mdMenuList) {
            for (MdMenu parentMenu : mdMenuList) {
                if (Constant.MENU_IS_SHOW_OFF.equals(mdMenu.getIs_menu()) && mdMenu.getParent_id().equals(parentMenu.getId())){
                    String permissionsUrl = parentMenu.getUrl() + Constant.MENU_SPLIT_SYMBOL;
                    if ("edit".equals(mdMenu.getUrl())){
                        permissionsUrl = permissionsUrl + "modify";
                    } else {
                        permissionsUrl = permissionsUrl + mdMenu.getUrl();
                    }
                    set.add(permissionsUrl);
                    break;
                }
            }
            set.add(mdMenu.getUrl());
        }
        return set;
    }

    /**
     * 认证;
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // UsernamePasswordToken对象用来存放提交的登录信息
        if (LOG.isDebugEnabled()) {
            LOG.debug("Authen Info For User : {} ", authenticationToken.toString());
        }
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        MAdmin admin = adminDAO.getAdminByAdmin(token.getUsername());
        if (admin != null) {
            // 若存在，将此用户存放到登录认证info中
            return new SimpleAuthenticationInfo(admin.getAdmin(),
                    decryptPwd(admin.getPassword(), admin.getPasswordtype()), getName());
        }
        return null;
    }

    private Set<String> asRoleSets(List<AdminRole> roles) {
        Set<String> roleSet = new HashSet<String>();
        if (roles == null || roles.isEmpty()) {
            return roleSet;
        }
        for (AdminRole role : roles) {
            roleSet.add(role.getRoleid());
        }
        return roleSet;
    }

    private String decryptPwd(String password, int passwordtype) {
        String decryptpwd = "";
        if (passwordtype == 0 || passwordtype == 1) {
            decryptpwd = password;
        } else if (passwordtype == 2) {
            PwdDES3 pwd = new PwdDES3();
            try {
                decryptpwd = pwd.decryptPassword(password);
            } catch (PasswordException e) {
                decryptpwd = password;
                LOG.error("PasswordException:", e);
            }
        }
        return decryptpwd;
    }
    
    public void clearAuthz(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
