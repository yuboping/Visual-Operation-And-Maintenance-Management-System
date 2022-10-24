package com.asiainfo.lcims.omc.web.ctrl.view;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.lcims.lcbmi.password.Password;
import com.asiainfo.lcims.lcbmi.password.PasswordException;
import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdRole;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.system.AdminDAO;
import com.asiainfo.lcims.omc.service.configmanage.AdminManageService;
import com.asiainfo.lcims.omc.util.AdminPwdDes;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.Constant;

/**
 * 管理员管理Controller类
 * 
 * @author zhujiansheng
 * @date 2018年7月31日 下午4:15:09
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/view/class/system/adminmanage")
public class AdminManagerView extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AdminManagerView.class);

    @Inject
    private AdminDAO adminDAO;

    @Resource(name = "mdMenuDataListener")
    private MdMenuDataListener mdMenuDataListener;

    @Autowired
    private AdminManageService adminManageService;

    @RequestMapping(value = "")
    public String adminManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        // 初始化角色
        List<MdRole> mdRoleList = adminManageService.getAdminUsedRole();
        model.addAttribute("mdRoleList", mdRoleList);

        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/adminmanage";
    }

    /**
     * 获取当前登录用户名
     * @param session
     * @return
     */
    @RequestMapping(value = "/getSession",method = RequestMethod.POST)
    @ResponseBody
    public String getSession(@ModelAttribute MAdmin mAdmin, HttpSession session) {
        String admin = mAdmin.getAdmin();
        String adminSession = (String) session.getAttribute("username");
        String roleId = (String) session.getAttribute(Constant.ADMIN_ROLEID_STRING);
        String sessionStatus = "1";

        if (roleId.equals(Constant.ADMIN_ROLEID) || roleId == Constant.ADMIN_ROLEID){
            return sessionStatus;
        }
        if (!adminSession.equals(admin)){
            sessionStatus = "0";
        }
        return sessionStatus;
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MAdmin> queryMAdminList(@ModelAttribute MAdmin mAdmin, HttpServletRequest request,
            HttpSession session, Model model) {
        String admin = mAdmin.getAdmin();
        String roleid = mAdmin.getRoleid();
        List<MAdmin> mAdminList = adminManageService.getAdmin(admin, roleid);
        return mAdminList;
    }

    @RequestMapping(value = "/queryDetail")
    @ResponseBody
    public List<MAdmin> queryDetail(@ModelAttribute MAdmin mAdmin, HttpServletRequest request,
            HttpSession session, Model model) {
        List<MAdmin> mAdminList = adminManageService.getMAdminList(mAdmin);
        return mAdminList;
    }

    @RequestMapping(value = "/rolelist")
    @ResponseBody
    public List<MdRole> rolelist(HttpServletRequest request, HttpSession session, Model model) {
        List<MdRole> roleList = adminManageService.getNotAdminRoles();
        return roleList;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public WebResult addMAdmin(@ModelAttribute MAdmin mAdmin, HttpServletRequest request,
            HttpSession session, Model model) {

        AdminPwdDes pwdDes = new AdminPwdDes();
        String pwd = pwdDes.strDec(mAdmin.getPassword());
        mAdmin.setPassword(pwd);

        return adminManageService.addMAdmin(mAdmin);
    }

    @RequestMapping(value = "/singleinfo")
    @ResponseBody
    public List<MAdmin> singleinfo(@ModelAttribute MAdmin mAdmin, HttpServletRequest request,
            HttpSession session, Model model) {
        List<MAdmin> list = adminManageService.getAdminByAdmin(mAdmin);
        return list;
    }

    @RequestMapping(value = "/modify")
    @ResponseBody
    public WebResult modifyMAdmin(@ModelAttribute MAdmin mAdmin, HttpServletRequest request,
            HttpSession session, Model model) {
        return adminManageService.modifyMAdmin(mAdmin);
    }

    @RequestMapping(value = "/chgpasswd",method = RequestMethod.POST)
    @ResponseBody
    public WebResult modifyPasswd(@ModelAttribute MAdmin mAdmin) {

        AdminPwdDes pwdDes = new AdminPwdDes();
        mAdmin.setOldPwd(pwdDes.strDec(mAdmin.getOldPwd()));
        mAdmin.setPassword(pwdDes.strDec(mAdmin.getPassword()));

        MAdmin oldAdmin = adminDAO.getAdminByAdmin(mAdmin.getAdmin());

        try {
            if (!oldAdmin.getPassword().equals(Password.encryptPassword(mAdmin.getOldPwd() + Constant.PASSWORD_SALT, 1))){
                WebResult result = new WebResult(false, "原密码错误，请重新输入！", "1");
                return result;
            }
        } catch (PasswordException e) {
            log.error("密码格式化失败！");
        }

        return adminManageService.modifyPasswd(mAdmin);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public WebResult deleteMAdmin(HttpServletRequest request, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String[] adminArray = request.getParameterValues("adminArray[]");
        return adminManageService.deleteMAdmin(adminArray, username);
    }
}
