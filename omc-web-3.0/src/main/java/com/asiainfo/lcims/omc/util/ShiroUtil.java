package com.asiainfo.lcims.omc.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;

import com.asiainfo.lcims.omc.web.security.ShiroRealm;

public class ShiroUtil {
    //清除权限
    public static void clearAuth(){
        RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();
        ShiroRealm realm = (ShiroRealm)rsm.getRealms().iterator().next();
        realm.clearAuthz();
    }
}
