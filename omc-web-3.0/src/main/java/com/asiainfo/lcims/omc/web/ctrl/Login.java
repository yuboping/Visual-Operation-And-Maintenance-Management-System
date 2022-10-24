package com.asiainfo.lcims.omc.web.ctrl;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.asiainfo.lcims.lcbmi.password.Password;
import com.asiainfo.lcims.lcbmi.password.PasswordException;
import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.ResultMsg;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdMenu;
import com.asiainfo.lcims.omc.model.system.MdRole;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.param.common.BusinessConf;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.param.common.SessionUtils;
import com.asiainfo.lcims.omc.persistence.system.AdminDAO;
import com.asiainfo.lcims.omc.service.configmanage.AdminManageService;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.service.system.MenuService;
import com.asiainfo.lcims.omc.service.system.ParamService;
import com.asiainfo.lcims.omc.service.system.VerifyCodeSevice;
import com.asiainfo.lcims.omc.util.AdminPwdDes;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.ToolsUtils;

/**
 * 登录及首页控制
 * 
 * @author sillywolf
 * 
 */
@Controller
@SessionAttributes({ "admin", "username" })
public class Login {

    private static final Logger log = LoggerFactory.getLogger(Login.class);

    private static final String HOME_PAGE = "index";

    private static final String HOME = "";
    @Inject
    private AdminDAO adminDAO;
    @Inject
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "operateHisService")
    OperateHisService operateHisService;

    @Resource(name = "menuService")
    MenuService menuService;

    @Resource(name = "paramService")
    ParamService paramService;

    @Resource(name = "verifyCodeSevice")
    VerifyCodeSevice verifyCodeSevice;

    @Autowired
    private AdminManageService adminManageService;

    @ModelAttribute("login")
    public MAdmin getAdmin(@RequestParam(value = "username", required = false) String username) {
        log.info("==== Invoke GetAdmin ====!");
        MAdmin admin = new MAdmin();
        try {
            if (username != null) {
                log.info("access username ==> {}, session id [{}]", username,
                        SecurityUtils.getSubject().getSession().getId());
                admin.setAdmin(username);
            }
        } catch (Exception e) {
            log.error("Login getAdmin error: {}", e);
        }
        return admin;
    }

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String home(Locale locale, Model model, HttpSession session) {
        log.info("URL is / session id:" + session.getId());
        log.info("Welcome home! The client locale is {}.", locale);
        model.addAttribute("homepage", MainServer.conf.getHomePage());
        model.addAttribute("systemname", MainServer.conf.getSystemName());

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        boolean forgetFlag = MainServer.conf.getForgetFlag();
        model.addAttribute("forgetFlag", forgetFlag);
        log.info("home view render to index..");
        return HOME_PAGE;
    }

    /**
     * 跳往忘记密码页面
     * 
     * @param locale
     * @param model
     * @return
     */
    @RequestMapping("/forgetPwd")
    public String forgetPwd(Locale locale, Model model) {
        log.info("Welcome forgetPwd! The client locale is {}.", locale);
        model.addAttribute("systemname", MainServer.conf.getSystemName());
        // 102 1 手机短信验证
        // 102 2 邮箱验证
        List<MdParam> codetype = paramService.getMdParamList("102");
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        model.addAttribute("codetype", codetype);
        List<MdParam> countdowns = paramService.getMdParamList("103");
        if (countdowns != null && countdowns.size() > 0) {
            model.addAttribute("countdown", countdowns.get(0).getCode());
        } else {
            model.addAttribute("countdown", "60");
        }

        return "forgetPwdpage";
    }

    // 校验用户名是否存在
    @RequestMapping(value = "/forgetPwd/checkAdmin", method = RequestMethod.POST)
    @ResponseBody
    public int checkAdmin(String admin) {
        // adminManageService
        int i = adminManageService.queryMAdminByAdmin(admin);
        if (i != 0) {
            i = 1;
        }
        return i;
    }

    // 校验用户名创建时手机号码/邮箱地址 发送验证码
    @RequestMapping(value = "/forgetPwd/checkType", method = RequestMethod.POST)
    @ResponseBody
    public WebResult checkType(String admin, String checkType, String checkValue,
            HttpServletRequest request) {
        WebResult webResult = verifyCodeSevice.checkType(admin, checkType, checkValue, request);
        return webResult;
    }

    // 修改用户密码
    @RequestMapping("/forgetPwd/changePossword")
    @ResponseBody
    public WebResult changePossword(@RequestParam(value = "admin", required = true) String admin,
            @RequestParam(value = "checkCode", required = true) String checkCode,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "oncePassword", required = true) String oncePassword,
            @RequestParam(value = "checkType", required = true) String checkType,
            HttpServletRequest request) {
        WebResult webResult = verifyCodeSevice.changePossword(admin, checkCode, password,
                oncePassword, checkType, request);
        return webResult;
    }

    /**
     * 登录校验，暂时未实现
     * 
     * @param vcode
     *            校验码,暂设为可空
     * @param admin
     *            页面传递的请求参数对象，包括username,password,verifycode
     * @param request
     *            页面参数转换结果，如果转换/校验失败，给出错误信息
     * @return 登录校验成功返回dashboard页面，失败则返回登录首页
     */
    @RequestMapping("/login")
    public @ResponseBody ResultMsg login(@RequestParam(value = "verifycode") String vcode,
            @ModelAttribute("login") MAdmin admin, HttpSession session, HttpServletRequest request,
            Model model) {
        /**
         * 注释校验码，安全测试，误删
         */

        // if(!checkVerify(vcode, session)) {
        // return ResultMsg.createFailedMsg("vcodeerror");
        // }

        log.info("login -> [ admin : {} ] ", admin.getAdmin());

        try {
            AdminPwdDes pwdDes = new AdminPwdDes();
            String pwd = pwdDes.strDec(admin.getPassword());
            // MD5密码加盐值
            pwd = pwd + Constant.PASSWORD_SALT;
            String encryptdata = Password.encryptPassword(pwd, 1);
            admin.setPassword(encryptdata);
            log.info("admin [{}] encrypt password succeed. ", admin.getAdmin());
        } catch (Exception e) {
            log.error("password error, reason : {}", e);
        }
        // 使用权限工具进行用户登录
        try {
            log.info("Login AuthenticationToken UsernamePasswordToken Start");
            AuthenticationToken token = null;
            token = new UsernamePasswordToken(admin.getAdmin(), admin.getPassword());
            log.info("Login AuthenticationToken UsernamePasswordToken End");
            log.info("Login SecurityUtils getSubject login Start");
            SecurityUtils.getSubject().login(token);
            log.info("Login SecurityUtils getSubject login End");
            log.info("Login Succ --> redirect to home !" + " session id ["
                    + SecurityUtils.getSubject().getSession().getId() + "]");
            log.info("Login adminDAO getAdminByAdmin Start");
            MAdmin ma = adminDAO.getAdminByAdmin(admin.getAdmin());
            log.info("Login adminDAO getAdminByAdmin End");
            if (adminDAO.getIfAdmin(ma.getAdmin()) > 0) {
                session.setAttribute(Constant.ADMIN_ROLEID_STRING, Constant.ADMIN_ROLEID);
                ma.setRoleid(Constant.ADMIN_ROLEID);
            } else {
                List<MdRole> roleList = adminDAO.getRoleByAdmin(admin.getAdmin());
                if (ToolsUtils.ListIsNull(roleList)) {
                    // 没赋值角色
                } else {
                    session.setAttribute(Constant.ADMIN_ROLEID_STRING, roleList.get(0).getRoleid());
                    ma.setRoleid(roleList.get(0).getRoleid());
                }
            }
            session.setAttribute("username", admin.getAdmin());
            model.addAttribute("username", admin.getAdmin());

            session.setAttribute("systemname", MainServer.conf.getSystemName());
            BusinessConf conf = CommonInit.BUSCONF;
            session.setAttribute("alarmsound", conf.alarmSound());
            session.setAttribute("page_history_more_flag", false);
            session.setAttribute("alarmsoundname", conf.alarmSoundName());
            session.setAttribute("alarm_sound_type", conf.alarmSoundType());
            SessionUtils.setSession(Constant.CURRENT_USER, ma);
            // session.setMaxInactiveInterval(conf.getSessionMaxInactiveInterval());
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_USER, "登录系统");

            // 加载菜单
            try {
                mdMenuDataListener.loadMenuList();
            } catch (Exception e) {
                log.error("load menu list error, reason : {}", e);
            }

            return ResultMsg.OPER_SUCC;
        } catch (ExcessiveAttemptsException e) {
            log.error("excessiveAttempts error, reason : {}", e);
            return ResultMsg.createFailedMsg("您已重复登录5次，请10分钟后再试");
        } catch (AuthenticationException e) {
            log.error("authentication error, reason : {}", e);
            return ResultMsg.createFailedMsg("用户名或密码错误");
        } catch (Exception e) {
            log.error("other exceptions error, reason : {}", e);
            return ResultMsg.createFailedMsg("用户名暂时无法登陆，请稍后再试");
        }
    }

    @RequestMapping(value = "/login/getvcode")
    @ResponseBody
    public String getvcode(HttpServletRequest request) {
        String session_vcode = String.valueOf(request.getSession().getAttribute("session_vcode"));
        request.getSession().invalidate();
        Cookie cookie = request.getCookies()[0];// 获取cookie
        cookie.setMaxAge(0);// 让cookie过期 ；
        return session_vcode;
    }

    /**
     * 用户登出
     * 
     * @param user
     * @param status
     * @return
     */
    @RequestMapping("/logout")
    public RedirectView logout(@ModelAttribute("admin") MAdmin user, SessionStatus status,
            RedirectAttributes redirectAttributes) {
        // 用户日志记录
        operateHisService.insertOperateHistory(Constant.OPERATE_HIS_USER, "退出系统");
        // 释放菜单
        mdMenuDataListener.releaseMenuList();
        status.setComplete();
        // 使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        RedirectView view = new RedirectView();
        view.setUrl(HOME);
        return view;
    }

    /**
     * 登录成功后跳转页面
     * 
     * @param request
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/loginsuccess")
    @ResponseBody
    public String deviceManage(HttpServletRequest request, Model model, HttpSession session) {
        String rediretUrl = MainServer.conf.getHomePage();
        /**
         * 判断默认跳转页面是否在权限之内，不在则跳转 菜单级别为 1 的第一个页面
         */
        List<MdMenu> mdMenuList = mdMenuDataListener.getMdMenulist();
        // 当前登录人
        boolean flag = false;
        for (MdMenu mdMenu : mdMenuList) {
            if (rediretUrl.equals(mdMenu.getUrl())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            for (MdMenu mdMenu : mdMenuList) {
                if (mdMenu.getMenu_level().intValue() == Constant.SYS_MENU_LEVEL_1) {
                    rediretUrl = mdMenu.getUrl();
                    break;
                }
            }
        }
        log.info("redirect url is : {}", rediretUrl);
        return rediretUrl;
    }

    /**
     * 修改用户原有密码
     * 
     * @param username
     *            用户名
     * @param password
     *            密码
     * @return
     */
    @RequestMapping(value = "/updateOldPassword", method = RequestMethod.GET)
    @ResponseBody
    public String updateOldPassword(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            log.info("用户名为空！");
            return "update password error!";
        }
        MAdmin mAdmin = adminDAO.getAdminByAdmin(username);
        if (mAdmin == null) {
            log.info("用户名不存在！");
            return "user is not exist!";
        }
        try {
            if (!mAdmin.getPassword().equals(Password.encryptPassword(password, 1))) {
                log.info("密码错误！");
                return "password incorrect!";
            }
            password = Password.encryptPassword(password + Constant.PASSWORD_SALT, 1);
            mAdmin.setPassword(password);
            int successCount = adminDAO.updateOldPassword(mAdmin);
            if (successCount == 0) {
                log.info("更新失败！");
                return "update password error!";
            }
        } catch (PasswordException e) {
            log.info("加密失败 -> {}", e.getMessage());
            return "update password error!";
        }
        return "update password success!";
    }
}
