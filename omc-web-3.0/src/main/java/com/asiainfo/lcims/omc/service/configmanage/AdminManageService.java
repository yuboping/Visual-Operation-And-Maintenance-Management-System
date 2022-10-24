package com.asiainfo.lcims.omc.service.configmanage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.KeyValueModel;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.system.MAdmin;
import com.asiainfo.lcims.omc.model.system.MdRole;
import com.asiainfo.lcims.omc.param.common.CommonInit;
import com.asiainfo.lcims.omc.persistence.configmanage.MAdminDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.MAdminRoleDAO;
import com.asiainfo.lcims.omc.persistence.configmanage.RoleManageDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;

/**
 * 管理员管理Service类
 * 
 * @author zhujiansheng
 * @date 2018年7月31日 下午4:18:02
 * @version V1.0
 */
@Service
public class AdminManageService {

    @Autowired
    private MAdminDAO mAdminDAO;

    @Autowired
    private MAdminRoleDAO mAdminRoleDAO;

    @Autowired
    private RoleManageDAO roleManageDAO;

    @Autowired
    private OperateHisService operateHisService;

    @Autowired
    private CommonInit commonInit;

    public List<MAdmin> getAdmin(String admin, String roleid) {
        List<MAdmin> list = mAdminDAO.getAdmin(admin, roleid);
        return list;
    }

    public List<MAdmin> getMAdminList(MAdmin mAdmin) {
        List<MAdmin> mAdminList = mAdminDAO.getMAdminList(mAdmin);
        return mAdminList;
    }

    // 初始化角色
    public List<MdRole> getAdminUsedRole() {
        List<MdRole> list = roleManageDAO.getAllRoleList();
        return list;
    }

    // 新增时可选角色是除了超级管理员的所有角色
    public List<MdRole> getNotAdminRoles() {
        List<MdRole> notAdminRoles = mAdminDAO.getNotAdminRoles();
        return notAdminRoles;
    }

    public WebResult addMAdmin(MAdmin mAdmin) {
        WebResult result = new WebResult(false, "新增失败");
        mAdmin.setStatus(0);
        int addResult = mAdminDAO.insert(mAdmin);
        int insertResult = mAdminRoleDAO.insert(mAdmin);
        if (addResult == 0 || insertResult == 0) {
            result = new WebResult(false, "新增失败");
        } else {
            // 刷新管理员缓存
            commonInit.loadMAdminInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ADMIN_MANAGE,
                    "新增数据[管理员:" + mAdmin.getAdmin() + "]");
            result = new WebResult(true, "新增成功");
        }
        return result;
    }

    public List<MAdmin> getAdminByAdmin(MAdmin mAdmin) {
        String admin = mAdmin.getAdmin();
        List<MAdmin> list = mAdminDAO.getMAdminList(mAdmin);
        mkAdminListInfoForcheck(list, admin);
        return list;
    }

    // 修改管理员信息时显示
    private void mkAdminListInfoForcheck(List<MAdmin> list, String mAdmin) {
        List<MdRole> allRoleList = new LinkedList<MdRole>();
        if ("admin".equals(mAdmin)) {
            MdRole mdRole = roleManageDAO.getRolebyRoleid("0");
            allRoleList.add(mdRole);
        } else {
            allRoleList = mAdminDAO.getNotAdminRoles();
        }
        for (MAdmin admin : list) {
            // 角色
            List<KeyValueModel> roleListForCheck = new ArrayList<KeyValueModel>();
            for (MdRole role : allRoleList) {
                KeyValueModel temp = new KeyValueModel();
                temp.setKey(role.getRoleid());
                temp.setValue(role.getName());
                if (role.getRoleid().equals(admin.getRoleid())) {
                    temp.setCheckflag(true);
                }
                roleListForCheck.add(temp);
            }
            admin.setRolelistforcheck(roleListForCheck);
        }
    }

    public WebResult modifyMAdmin(MAdmin mAdmin) {
        WebResult result = new WebResult(false, "修改失败");

        List<MAdmin> mAdminList = CommonInit.getMAdminList();
        String adminName = "";
        for (MAdmin admin : mAdminList) {
            if (admin.getAdmin().equals(mAdmin.getAdmin())) {
                adminName = admin.getAdmin();
            }
        }

        int updateResult = mAdminDAO.update(mAdmin);
        String admin = mAdmin.getAdmin();
        if (!"admin".equals(admin)) {
            mAdminRoleDAO.deleteByAdmin(admin);
            mAdminRoleDAO.insert(mAdmin);
        }
        if (updateResult == 1) {
            result = new WebResult(true, "修改成功");
            // 刷新管理员缓存
            commonInit.loadMAdminInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ADMIN_MANAGE,
                    "修改数据[管理员:" + adminName + "-->" + mAdmin.getAdmin() + "]");
        } else {
            result = new WebResult(false, "修改失败");
        }
        return result;
    }

    // 修改密码
    public WebResult modifyPasswd(MAdmin mAdmin) {
        WebResult result = new WebResult(false, "修改密码失败");
        int modifyResult = mAdminDAO.modifyPasswd(mAdmin);
        if (modifyResult == 1) {
            result = new WebResult(true, "修改密码成功");
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ADMIN_MANAGE,
                    mAdmin.getAdmin() + "修改密码成功！");
        } else {
            result = new WebResult(false, "修改密码失败");
        }
        return result;
    }

    public WebResult deleteMAdmin(String[] mAdmins, String username) {
        WebResult result = new WebResult(false, "删除失败");
        List<String> delSuccessList = new LinkedList<String>();
        List<String> delFailList = new LinkedList<String>();
        String message = "";
        if (mAdmins != null && mAdmins.length > 0) {
            for (String mAdmin : mAdmins) {
                int delResult = mAdminDAO.delete(mAdmin);
                mAdminRoleDAO.deleteByAdmin(mAdmin);
                if (delResult == 1) {
                    delSuccessList.add(mAdmin);
                } else {
                    delFailList.add(mAdmin);
                }
            }
        }
        if (!delSuccessList.isEmpty()) {
            message = message + delSuccessList.size() + "条管理员删除成功" + delSuccessList + "。";
            // 刷新管理员缓存
            commonInit.loadMAdminInfo();
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_ADMIN_MANAGE,
                    "删除数据[管理员:" + delSuccessList);
        }
        if (!delFailList.isEmpty()) {
            message = message + delFailList.size() + "条管理员删除失败" + delFailList + "，超级管理员不可删除！";
        }
        result = new WebResult(true, message);
        return result;
    }
    
    
    
    public int queryMAdminByAdmin(String admin) {
    	return mAdminDAO.queryMAdminByAdmin(admin);
    }
    
    public int queryCheckType(String admin,int checkType,String checkValue) {
    	return mAdminDAO.queryCheckType(admin,checkType,checkValue);
    }
}
