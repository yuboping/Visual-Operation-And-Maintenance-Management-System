package com.asiainfo.lcims.omc.web.security;

import java.util.List;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;

import com.asiainfo.lcims.omc.util.ReadFile;

public class CustomDefaultFilterChainManager extends DefaultFilterChainManager {
    private PassThruAuthenticationFilter passthruAuthcFilter;
    private RolesAuthFilter rolesFilter;
    private URLPermissionsFilter permsFilter;
    public CustomDefaultFilterChainManager() {
        super();
    }

    public PassThruAuthenticationFilter getPassthruAuthcFilter() {
        return passthruAuthcFilter;
    }

    public void setPassthruAuthcFilter(PassThruAuthenticationFilter passthruAuthcFilter) {
        this.passthruAuthcFilter = passthruAuthcFilter;
    }

    public RolesAuthFilter getRolesFilter() {
        return rolesFilter;
    }

    public void setRolesFilter(RolesAuthFilter rolesFilter) {
        this.rolesFilter = rolesFilter;
    }

    public URLPermissionsFilter getPermsFilter() {
        return permsFilter;
    }

    public void setPermsFilter(URLPermissionsFilter permsFilter) {
        this.permsFilter = permsFilter;
        addFilterChain();
    }

    /**
     * 根据各省配置对应的url权限拦截
     */
    private void addFilterChain() {
        addFilter("authc", passthruAuthcFilter);
        addFilter("roles", rolesFilter);
        addFilter("perms", permsFilter);
        List<String> list = ReadFile.readListByFilename("urlFilterChain.txt");
        for (String urlfilter : list) {
            String[] values = urlfilter.split("=");
            if (values == null || values.length != 2) {
                continue;
            }
            String chainName = values[0].trim();
            String mapper = values[1].trim();
            String[] mappers = mapper.split(";");
            for (String sub : mappers) {
                String filtername = sub;
                String filterConfig = null;
                if (sub.indexOf("[") > 0) {
                    filtername = sub.substring(0, sub.indexOf("["));
                    filterConfig = sub.substring(sub.indexOf("[") + 1, sub.indexOf("]"));
                }
                addToChain(chainName, filtername, filterConfig);
            }
        }
    }

}
