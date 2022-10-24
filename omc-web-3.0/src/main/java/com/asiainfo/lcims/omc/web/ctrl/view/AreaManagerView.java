package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.configmanage.AreaService;
import com.asiainfo.lcims.omc.util.BaseController;
import com.asiainfo.lcims.omc.util.BeanToMapUtils;
import com.asiainfo.lcims.omc.util.DbSqlUtil;
import com.asiainfo.lcims.omc.util.EnumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 属地管理类
 */
@Controller
@RequestMapping(value = "/view/class/system/areamanage")
public class AreaManagerView extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(AreaManagerView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "areaService")
    AreaService areaService;

    /**
     * 属地管理初始化页面
     *
     * @return
     */
    @RequestMapping(value = "")
    public String AreaManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        return "servermanage/area";
    }

    /**
     * 属地管理查询方法
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<MdArea> queryAreaInfoList(HttpServletRequest request,
                                                   HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        MdArea areaQueryParam = BeanToMapUtils.toBean(MdArea.class, params);
        String str = DbSqlUtil.replaceSpecialStr(areaQueryParam.getName());
        List<MdArea> areaList = areaService
                .getArea(areaQueryParam.getAreano(), str);
        return areaList;
    }

    @RequestMapping(value = "/{managetype}")
    public @ResponseBody
    WebResult manageInfo(@PathVariable String managetype,
                         HttpServletRequest request) {
        MdArea area = new MdArea();
        WebResult result = new WebResult(false, "操作类型错误！");
        if (EnumUtil.MANAGETYPE_ADD.equals(managetype)) {
            area.setName(request.getParameter("name"));
            result = areaService.addInfo(area);
        } else if (EnumUtil.MANAGETYPE_DELETE.equals(managetype)) {
            String[] areaArray = request.getParameterValues("areaArray[]");
            result = areaService.deleteInfo(areaArray);
        } else if (EnumUtil.MANAGETYPE_MODIFY.equals(managetype)) {
            area.setAreano(request.getParameter("areano"));
            area.setName(request.getParameter("name"));
            result = areaService.modifyInfo(area);
        }
        // 加载菜单
        mdMenuDataListener.loadMenuList();
        return result;
    }

}
