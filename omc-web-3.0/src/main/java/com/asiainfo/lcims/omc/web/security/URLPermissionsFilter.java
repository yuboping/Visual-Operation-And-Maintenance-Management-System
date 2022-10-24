package com.asiainfo.lcims.omc.web.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 当mappedValue=null时， 动态根据url映射权限，例如：/view/monitor/module/1，自动映射为需要权限“module/1”
 * 
 * @author luohuawuyin
 *
 */
public class URLPermissionsFilter extends PermissionsAuthorizationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(PermissionsAuthorizationFilter.class);
    Pattern[] patterns = new Pattern[] { Pattern.compile("(/class/\\w+/)"),
            Pattern.compile("(/\\d+/area)"), Pattern.compile("(/\\d+/node)"),
            Pattern.compile("(/module/\\d+/)") };

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response,
            Object mappedValue) throws IOException {
        boolean isAccess = true;
        if (mappedValue == null) {
            isAccess = super.isAccessAllowed(request, response, buildPermissions(request));
        } else {
            isAccess = super.isAccessAllowed(request, response, mappedValue);
        }
        HttpServletRequest req = (HttpServletRequest) request;
        if(isAccess){
            LOG.info(req.getRequestURI()+" is AccessAllowed");
        }else{
            LOG.info(req.getRequestURI()+" is not AccessAllowed");
        }
        
        return isAccess;
    }

    protected String[] buildPermissions(ServletRequest request) {
        List<String> perms = new ArrayList<String>();
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
//        for (Pattern pattern : patterns) {
//            Matcher matcher = pattern.matcher(path);
//            if (matcher.find()) {
//                perms.add(matcher.group(1));
//            }
//        }
        perms.add(path);

        return perms.isEmpty() ? null : (String[]) perms.toArray(new String[perms.size()]);
    }

}
