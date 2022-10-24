package com.asiainfo.lcims.omc.web.ctrl.view;

import com.asiainfo.lcims.omc.boot.MainServer;
import com.asiainfo.lcims.omc.model.MdParam;
import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.system.MdArea;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.service.ais.AisGroupManageService;
import com.asiainfo.lcims.omc.service.configmanage.AreaService;
import com.asiainfo.lcims.omc.service.system.ParamService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 巡检组管理类
 */
@Controller
@RequestMapping(value = "/view/class/ais/aisgroupmanage")
public class AisGroupManagerView extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(AisGroupManagerView.class);

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "aisGroupManageService")
    AisGroupManageService aisGroupManageService;

    @Resource(name = "paramService")
    ParamService paramService;

    private final String PARAM_TYPE_ICON_PATH = "21" ;

    /**
     * 巡检组管理初始化页面
     *
     * @return
     */
    @RequestMapping(value = "")
    public String aisGroupManageManage(HttpServletRequest request, Model model, HttpSession session) {
        String uri = request.getRequestURI();
        model.addAttribute("classtype", mdMenuDataListener.getParentMdMenuByUrl(uri).getName());
        if (MainServer.conf.getProvince() != null) {
            model.addAttribute("now", new Date());
            model.addAttribute("province", MainServer.conf.getProvince());
        }
        List<MdParam> paramList = paramService.getMdParamList(PARAM_TYPE_ICON_PATH);
        model.addAttribute("paramList", paramList);
        return "ais/aisgroupmanage";
    }

    /**
     * 巡检组查询方法
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public List<AisGroupModel> queryAisGroupList(HttpServletRequest request,
                                                   HttpSession session, Model model) {
        Map<String, Object> params = getParams(request);
        AisGroupModel aisGroup = BeanToMapUtils.toBean(AisGroupModel.class, params);
        String str = DbSqlUtil.replaceSpecialStr(aisGroup.getGroup_name());
        aisGroup.setGroup_name(str);
        List<AisGroupModel> aisGroupModelList = aisGroupManageService.getAisGroupList(aisGroup);
        return aisGroupModelList;
    }

    @RequestMapping(value = "/{managetype}")
    public @ResponseBody
    WebResult manageInfo(@PathVariable String managetype,
                         HttpServletRequest request) {

        LOG.info("start {} Group.", managetype);

        WebResult result = new WebResult(false, "操作类型错误！");

        try {

            Map<String, Object> params = getParams(request);
            AisGroupModel aisGroup = BeanToMapUtils.toBean(AisGroupModel.class, params);

            if (EnumUtil.MANAGETYPE_ADD.equals(managetype)) {
                result = aisGroupManageService.addInfo(aisGroup);
            } else if (EnumUtil.MANAGETYPE_DELETE.equals(managetype)) {
                String[] aisgrouplist = request.getParameterValues("aisGroupArray[]");
                result = aisGroupManageService.deleteInfo(aisgrouplist);
            } else if (EnumUtil.MANAGETYPE_MODIFY.equals(managetype)) {
                result = aisGroupManageService.modifyInfo(aisGroup);
            }

        } catch (Exception e) {

            LOG.error("Ais Schedule Group Manage View Error, method : {1} , error : {2}", managetype, e.getMessage());
        }
        return result;
    }

}
