package com.asiainfo.lcims.omc.web.ctrl.sos;

import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.asiainfo.lcims.lcbmi.password.Password;
import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.sos.SosBase;
import com.asiainfo.lcims.omc.model.sos.SosCheckTokenResponse;
import com.asiainfo.lcims.omc.model.sos.SosLoginResult;
import com.asiainfo.lcims.omc.model.sos.SosRequest;
import com.asiainfo.lcims.omc.model.sos.SosRoleBean;
import com.asiainfo.lcims.omc.model.sos.SosRoleResult;
import com.asiainfo.lcims.omc.model.sos.UrlRequestConfig;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.configmanage.MAdminRoleDAO;
import com.asiainfo.lcims.omc.persistence.system.AdminDAO;
import com.asiainfo.lcims.omc.protocol.http.HttpClient;
import com.asiainfo.lcims.omc.service.configmanage.RoleManageService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.JsonUtil;
import com.asiainfo.lcims.omc.util.ProviceUtill;
import com.asiainfo.lcims.omc.util.SosConstant;
import com.asiainfo.lcims.omc.util.ToolsUtils;

@Controller
@RequestMapping(value = "/sos")
public class SosController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(SosController.class);

    private static final String IP = "#{ip}";

    @Resource(name = "rolemanageService")
    RoleManageService rolemanageService;

    @Inject
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "paramService")
    ParamService paramService;

    @Inject
    private AdminDAO adminDAO;

    @Inject
    private MAdminRoleDAO mAdminRoleDAO;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "1122";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam String token, @RequestParam int sourceType,
            HttpSession session, HttpServletRequest request) {
        try {
            log.info("sos login start.");
            String requestIp = request.getParameter("ip");
            log.info("sos login sourceType : {} . token : {} . ip : {} .", sourceType, token,
                    requestIp);
            SosRequest sosRequest = new SosRequest();
            sosRequest.setSourceType(sourceType);
            sosRequest.setToken(token);
            SosLoginResult sosLoginResult = null;
            switch (sosRequest.getSourceType()) {
            case SosConstant.SOURCETYPE_3A:
                // 3A 校验token
                sosLoginResult = loginCheck(sosRequest, session, requestIp);
                break;
            default:
                sosLoginResult = unknowSourceType(sosRequest);
                break;
            }
            log.info("sos login sosLoginResult MesCode:{}", sosLoginResult.getMesCode());
            if ("200".equals(sosLoginResult.getMesCode())) {
                log.info("sos login success SuccessUrl {} .", sosLoginResult.getSuccessUrl());
                return "forward:" + sosLoginResult.getSuccessUrl() + "?";
            }
        } catch (Exception e) {
            log.error("SosController login error:", e);
        }
        log.info("sos login end.");
        return "redirect:/login";
    }

    private SosLoginResult loginCheck(SosRequest sosRequest, HttpSession session,
            String requestIp) {
        log.info("sos loginCheck start.");
        SosLoginResult sosLoginResult = new SosLoginResult();
        // 校验token
        if (ToolsUtils.StringIsNull(sosRequest.getToken())) {
            sosLoginResult = unknowSourceType(sosRequest);
            return sosLoginResult;
        }
        UrlRequestConfig urlRequestConfig = getRequestConfig();
        if (null == urlRequestConfig) {
            sosLoginResult.setMesCode("206");
            sosLoginResult.setMessage("操作失败");
            return sosLoginResult;
        }
        // 去接口校验token
        SosCheckTokenResponse rokenResponse = checkTokenByInterface(sosRequest, urlRequestConfig,
                requestIp);
        // 校验失败
        if (null == rokenResponse || !"200".equals(rokenResponse.getMesCode())) {
            sosLoginResult.setMesCode("206");
            sosLoginResult.setMessage("操作失败");
        } else {
            // 校验成功
            // 根据用户名查询用户是否存在:存在,直接登录,不存在先插入数据在登录
            // MD5密码加盐值

            try {
                String pwd = "!qaz@WSX1234";
                pwd = pwd + Constant.PASSWORD_SALT;
                String encryptdata = Password.encryptPassword(pwd, 1);
                MAdmin ma = adminDAO.getAdminByAdmin(rokenResponse.getUserName());
                if (ma == null) {
                    MAdmin iadmin = new MAdmin();
                    iadmin.setAdmin(rokenResponse.getUserName());
                    iadmin.setPassword(encryptdata);
                    iadmin.setPasswordtype(0);
                    iadmin.setStatus(0);
                    adminDAO.insertAdmin(iadmin);
                }
                SecurityUtils.getSubject().login(new UsernamePasswordToken(
                        rokenResponse.getUserName(), ma == null ? encryptdata : ma.getPassword()));
                log.info("Login Succ --> redirect to home !" + " session id ["
                        + SecurityUtils.getSubject().getSession().getId() + "]");
            } catch (Exception e) {
                log.error("sos error:", e);
                sosLoginResult.setMesCode("206");
                sosLoginResult.setMessage("操作失败");
                return sosLoginResult;
            }
            sosLoginResult = loginSuccess(sosLoginResult, rokenResponse, session);
        }
        log.info("sos loginCheck end.");
        return sosLoginResult;
    }

    private SosCheckTokenResponse checkTokenByInterface(SosRequest sosRequest,
            UrlRequestConfig urlRequestConfig, String requestIp) {
        SosCheckTokenResponse response = null;
        if (urlRequestConfig.getType().equals("http")) {
            response = sendCheckByHttp(sosRequest, urlRequestConfig, requestIp);
        } else if (urlRequestConfig.getType().equals("https")) {
            // 待实现
        }
        return response;
    }

    private SosLoginResult loginSuccess(SosLoginResult sosLoginResult,
            SosCheckTokenResponse response, HttpSession session) {
        try {
            MAdmin ma = new MAdmin();
            ma.setAdmin(response.getUserName());// AAA系统传过来的用户名
            ma.setRoleid(response.getRoleId());// AAA系统传过来的角色id
            log.info("loginSuccess userName : {} , roleId : {} .", ma.getAdmin(), ma.getRoleid());

            // 自动赋权角色用户绑定
            mAdminRoleDAO.deleteByAdmin(ma.getAdmin());
            mAdminRoleDAO.insert(ma);

            log.info("loginSuccess mAdminRoleDAO success");

            session.setAttribute(Constant.ADMIN_ROLEID_STRING, ma.getRoleid());
            session.setAttribute("username", ma.getAdmin());
            session.setAttribute("systemname", MainServer.conf.getSystemName());
            BusinessConf conf = CommonInit.BUSCONF;
            session.setAttribute("alarmsound", conf.alarmSound());
            session.setAttribute("page_history_more_flag", false);
            session.setAttribute("alarmsoundname", conf.alarmSoundName());
            session.setAttribute("alarm_sound_type", conf.alarmSoundType());
            SessionUtils.setSession(Constant.CURRENT_USER, ma);

            log.info("loginSuccess setSession success");
            // 加载菜单
            mdMenuDataListener.loadMenuList();
            String rediretUrl = MainServer.conf.getHomePage();
            log.info("loginSuccess loadMenuList success");
            /**
             * 判断默认跳转页面是否在权限之内，不在则跳转 菜单级别为 1 的第一个页面
             */
            List<MdMenu> mdMenuList = mdMenuDataListener.getMdMenulist();
            // 当前登录人页面
            for (MdMenu mdMenu : mdMenuList) {
                if (mdMenu.getMenu_level().intValue() == Constant.SYS_MENU_LEVEL_1) {
                    rediretUrl = mdMenu.getUrl();
                    break;
                }
            }
            log.info("loginSuccess successUrl rediretUrl  : {} .", rediretUrl);
            sosLoginResult.setSuccessUrl(rediretUrl);// 验证成功跳转url
            sosLoginResult.setMesCode("200");
            sosLoginResult.setMessage("登录成功");
        } catch (Exception e) {
            log.error("load menu list error, reason : {}", e);
            sosLoginResult.setMesCode("206");
            sosLoginResult.setMessage("操作失败");
        }
        return sosLoginResult;
    }

    private SosCheckTokenResponse sendCheckByHttp(SosRequest sosRequest,
            UrlRequestConfig urlRequestConfig, String requestIp) {
        log.info("sos sendCheckByHttp start.");
        SosCheckTokenResponse response = null;
        if (urlRequestConfig.getRequestType().equals("get")) {
            log.info("sos is get requset not writer");
        } else if (urlRequestConfig.getRequestType().equals("post")) {
            String param = "{\"token\":\"" + sosRequest.getToken() + "\"}";
            String url = urlRequestConfig.getUrl();
            String province = MainServer.conf.getProvince();
            if (StringUtils.equals(province, ProviceUtill.PROVINCE_CQCM)
                    || StringUtils.equals(province, ProviceUtill.PROVINCE_CQCMTEST)) {
                url = url.replace(IP, requestIp);
            }
            log.info("send to url:" + url + ";data:" + param);
            String jsonResult = HttpClient.sendPostForJson(url, param);
            log.info("receive from url:" + url + ";data:" + jsonResult);
            response = JsonUtil.jsonToEntityClass(jsonResult, SosCheckTokenResponse.class);
        }
        log.info("sos sendCheckByHttp end.");
        return response;
    }

    private UrlRequestConfig getRequestConfig() {
        UrlRequestConfig config = null;
        List<MdParam> codetype = paramService.getMdParamList(SosConstant.CHECK_TOKEN_URL_CONFIG);
        if (!ToolsUtils.ListIsNull(codetype)) {
            config = new UrlRequestConfig();
            for (MdParam param : codetype) {
                if (param.getCode().equals("type")) {
                    config.setType(param.getDescription());
                } else if (param.getCode().equals("request_type")) {
                    config.setRequestType(param.getDescription());
                } else if (param.getCode().equals("url")) {
                    config.setUrl(param.getDescription());
                }
            }
        }
        if (config == null) {
            log.error(
                    "login check token url is not Incomplete configuration: config is null");
        }
        return config;
    }

    private SosLoginResult unknowSourceType(SosRequest sosRequest) {
        SosLoginResult sosLoginResult = new SosLoginResult();
        log.info("this sourceType：" + sosRequest.getSourceType() + " is illegal !!");
        sosLoginResult.setMesCode("206");
        sosLoginResult.setMessage("操作失败: Illegal operation");
        return sosLoginResult;
    }

    @RequestMapping(value = "/queryAllRole", method = RequestMethod.POST)
    @ResponseBody
    public SosRoleResult queryAllRole(@RequestBody SosRequest sosRequest) {
        SosRoleResult sosRoleResult = new SosRoleResult();
        if (101 != sosRequest.getSourceType()) {
            log.info("this token：" + sosRequest.getToken() + "is not AAA system!!");
            sosRoleResult.setMesCode("206");
            sosRoleResult.setMessage("操作失败");
            return sosRoleResult;
        }
        List<SosRoleBean> roleList = rolemanageService.getSosAllRoleList();
        if (roleList == null) {
            sosRoleResult.setMesCode("206");
            sosRoleResult.setMessage("操作失败");
        } else {
            sosRoleResult.setMesCode("200");
            sosRoleResult.setMessage("操作成功");
            sosRoleResult.setRolelist(roleList);
        }
        return sosRoleResult;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public SosBase logout(@RequestBody SosRequest sosRequest, SessionStatus status,
            HttpSession session) {
        SosBase sosBase = new SosBase();
        if (101 != sosRequest.getSourceType()) {
            log.info("this token：" + sosRequest.getToken() + "is not AAA system!!");
            sosBase.setMesCode("206");
            sosBase.setMessage("操作失败");
            return sosBase;
        }
        try {
            // 释放菜单
            mdMenuDataListener.releaseMenuList();
            status.setComplete();
            Enumeration<String> en = session.getAttributeNames();
            while (en.hasMoreElements()) {
                System.out.println(en.nextElement().toString());
                session.removeAttribute(en.nextElement().toString());
            }
            sosBase.setMesCode("200");
            sosBase.setMessage("操作成功");
        } catch (Exception e) {
            log.error("this token：" + sosRequest.getToken()
                    + " from AAA system logout operation fail：" + e.getMessage());
            sosBase.setMesCode("206");
            sosBase.setMessage("操作失败");
        }
        return sosBase;
    }
}
