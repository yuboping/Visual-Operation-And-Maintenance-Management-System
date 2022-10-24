package com.asiainfo.lcims.omc.web.security;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RolesAuthFilter extends AuthorizationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
            Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        boolean isAccess = false; 
        if (rolesArray == null || rolesArray.length == 0) {
            isAccess = false;
        }else {
    	   for (int i = 0; i < rolesArray.length; i++) {
               if (subject.hasRole(rolesArray[i])) {
                   isAccess = true;
                   break;
               }
           }
        }
        if(!isAccess){
            LOG.info("RolesAuthFilter is not AccessAllowed");
        }
        return isAccess;
    }

}
