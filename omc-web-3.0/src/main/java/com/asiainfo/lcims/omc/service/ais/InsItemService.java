package com.asiainfo.lcims.omc.service.ais;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.ais.AisGroupModel;
import com.asiainfo.lcims.omc.model.ais.CheckCategaryModel;
import com.asiainfo.lcims.omc.model.ais.CheckItemModel;
import com.asiainfo.lcims.omc.persistence.ais.AisGroupDAO;
import com.asiainfo.lcims.omc.persistence.ais.INSItemDAO;
import com.asiainfo.lcims.omc.persistence.po.ais.INSitems;

@Service("insItemService")
public class InsItemService {

    @Inject
    private AisGroupDAO aisGroupDAO;

    @Resource(name = "aisReportService")
    private AisReportService aisReportService;

    /**
     * 获取所有的巡检定义组
     * 
     * @return
     */
    public List<CheckCategaryModel> getCategaryGroupList() {
        List<AisGroupModel> groups = aisGroupDAO.getAllAisGroup();
        List<CheckCategaryModel> categorys = new ArrayList<CheckCategaryModel>();

        if (groups != null && !groups.isEmpty()) {
            for (AisGroupModel groupModel : groups) {
                CheckCategaryModel model = new CheckCategaryModel();
                model.setGroupid(groupModel.getGroup_id());
                model.setIsdisable(groupModel.getStatus() == 0 ? true : false);
                model.setCategaryname(groupModel.getGroup_name());
                model.setCategarydesc(groupModel.getDescription());
                model.setIconclass(groupModel.getIcon());
                categorys.add(model);
            }
        }
        return categorys;
    }

//    public List<CheckGroupModel> getGroupCheckInfo(String[] groupids) {
//
//        if (groupids == null || groupids.length == 0) {
//            return null;
//        }
//        List<CheckGroupModel> result = new ArrayList<CheckGroupModel>(groupids.length);
//        for (String groupid : groupids) {
//
//            CheckGroupModel groupmodel = this.proGroupModelInfo(groupid);
//            if (groupmodel != null) {
//                result.add(groupmodel);
//            }
//        }
//
//        return result;
//    }

//    /**
//     * 组装groupmodel信息
//     * 
//     * @param groupid
//     * @return
//     */
//    public CheckGroupModel proGroupModelInfo(String groupid) {
//        CheckGroupModel groupmodel = new CheckGroupModel();
//        groupmodel.setGroupid(groupid);
//
//        // 根据groupid获取group信息
//        INSItemGroup grouppo = insItemDAO.getGroupById(Integer.valueOf(groupid));
//        // 正常状态下的group才可以巡检
//        if (grouppo != null && grouppo.getStatus() == 1) {
//            groupmodel.setGroupname(grouppo.getName());
//            groupmodel.setIcon(grouppo.getIcon());
//            groupmodel.setStatus("" + grouppo.getStatus());
//
//            // 获取groupid下所有的item信息
//            List<INSitems> itemspo = insItemDAO.getItemByGroupid(Integer.valueOf(groupid));
//            if (itemspo != null && !itemspo.isEmpty()) {
//                List<CheckItemModel> itemsmodel = new ArrayList<CheckItemModel>(itemspo.size());
//                for (INSitems itempo : itemspo) {
//                    CheckItemModel itemmodel = new CheckItemModel();
//                    itemmodel.setGroupid("" + itempo.getGroupid());
//                    itemmodel.setItemid("" + itempo.getItemid());
//                    itemmodel.setName(itempo.getName());
//                    itemmodel.setIcon(itempo.getIcon());
//
//                    itemsmodel.add(itemmodel);
//                }
//                groupmodel.setItems(itemsmodel);
//            }
//        } else {
//            return null;
//        }
//        return groupmodel;
//    }

}
