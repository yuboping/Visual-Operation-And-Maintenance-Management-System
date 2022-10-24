package com.asiainfo.lcims.omc.service.configmanage;

import com.asiainfo.lcims.omc.model.WebResult;
import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.param.MdMenuDataListener;
import com.asiainfo.lcims.omc.persistence.configmanage.MdNodeDAO;
import com.asiainfo.lcims.omc.service.operateHis.OperateHisService;
import com.asiainfo.lcims.omc.util.Constant;
import com.asiainfo.lcims.omc.util.IDGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "nodeManageService")
public class NodeManageService {
    @Inject
    private MdNodeDAO mdNodeDAO;

    @Resource(name = "mdMenuDataListener")
    MdMenuDataListener mdMenuDataListener;

    @Resource(name = "alarmRuleManageService")
    AlarmRuleManageService alarmRuleManageService;

    @Autowired
    private OperateHisService operateHisService;

    /**
     * 
     * @Title: getAllMetricManage
     * @Description: TODO(获取节点信息)
     * @param @return 参数
     * @return List<MdNode> 返回类型
     * @throws
     */
    public List<MdNode> getMdNodeList(MdNode mdNode) {
        return mdNodeDAO.getMdNode(mdNode);
    }

    /**
     * 根据节点名称获取MdNode数据
     * @param nodeName
     * @return
     */
    public MdNode getMdNodeInfo(String nodeName) {
        return mdNodeDAO.getMdNodeByNodeName(nodeName);
    }

    /**
     *
     * @Title: addMdNode
     * @Description: TODO(新增节点)
     * @param @param mdNode
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult addMdNode(MdNode mdNode) {
        if (null != mdNode.getNode_name() && !("").equals(mdNode.getNode_name())) {
            String uuid = IDGenerateUtil.getUuid();
            mdNode.setId(uuid);
            int addResult = mdNodeDAO.insert(mdNode);
            if (1 == addResult) {
                // 加载菜单
                mdMenuDataListener.loadMenuList();
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_NODE_MANAGE, "新增数据[节点:"
                        + mdNode.getNode_name() + "]");
                // 添加告警规则
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("operationType", Constant.DYNAMICTYPE_NODE);
                map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
                map.put("operationId", uuid);
                alarmRuleManageService.dimensionModify(map);
                return new WebResult(true, "成功", uuid);
            } else {
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     *
     * @Title: addMdNode
     * @Description: TODO(新增节点)
     * @param @param mdNode
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult addMdNodeByRestful(MdNode mdNode) {
        if (null != mdNode.getNode_name() && !("").equals(mdNode.getNode_name())) {
            String uuid = IDGenerateUtil.getUuid();
            mdNode.setId(uuid);
            int addResult = mdNodeDAO.insert(mdNode);
            if (1 == addResult) {
                // 添加告警规则
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("operationType", Constant.DYNAMICTYPE_NODE);
                map.put("dynamicType", Constant.OPERATIONTYPE_ADD);
                map.put("operationId", uuid);
                alarmRuleManageService.dimensionModify(map);
                return new WebResult(true, "成功", uuid);
            } else {
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     *
     * @Title: addMdNode
     * @Description: TODO(修改节点)
     * @param @param mdNode
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult modifyMdNode(MdNode mdNode) {
        if (null != mdNode.getNode_name() && !("").equals(mdNode.getNode_name())) {
            MdNode hisMdNode = new MdNode();
            hisMdNode.setId(mdNode.getId());
            hisMdNode = mdNodeDAO.getMdNode(hisMdNode).get(0);
            int updateResult = mdNodeDAO.update(mdNode);
            if (1 == updateResult) {
                // 加载菜单
                mdMenuDataListener.loadMenuList();
                // 用户日志记录
                operateHisService.insertOperateHistory(Constant.OPERATE_HIS_NODE_MANAGE, "修改数据[节点:"
                        + hisMdNode.getNode_name() + "-->" + mdNode.getNode_name() + "]");
                return new WebResult(true, "成功");
            } else {
                return new WebResult(false, "失败");
            }
        } else {
            return new WebResult(false, "失败");
        }
    }

    /**
     *
     * @Title: deleteMdNode
     * @Description: TODO(批量删除节点)
     * @param @param nodeidArray
     * @param @return 参数
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public WebResult deleteMdNode(String[] nodeidArray) {
        int deleteSuccess = 0;
        List<String> delSuccessList = new ArrayList<String>();
        int deleteFail = 0;
        List<String> delFailList = new ArrayList<String>();
        String message = "";
        if (nodeidArray != null && nodeidArray.length != 0) {
            for (String id : nodeidArray) {
                MdNode mdNode = new MdNode();
                mdNode.setId(id);
                List<MdNode> mdMetricList = mdNodeDAO.getMdNode(mdNode);
                String nodeName = mdMetricList.get(0).getNode_name();
                int deleteResult = mdNodeDAO.delete(id);
                if (1 == deleteResult) {
                    // 加载菜单
                    mdMenuDataListener.loadMenuList();
                    // 删除告警规则
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("operationType", Constant.DYNAMICTYPE_NODE);
                    map.put("dynamicType", Constant.OPERATIONTYPE_DELETE);
                    map.put("operationId", id);
                    alarmRuleManageService.dimensionModify(map);
                    delSuccessList.add(nodeName);
                    deleteSuccess++;
                } else {
                    delFailList.add(nodeName);
                    deleteFail++;
                }
            }
        }
        if (deleteSuccess > 0) {
            // 用户日志记录
            operateHisService.insertOperateHistory(Constant.OPERATE_HIS_NODE_MANAGE, "删除数据[节点:"
                    + delSuccessList);
            message = message + deleteSuccess + "条节点删除成功" + delSuccessList + "。";
        }

        if (deleteFail > 0) {
            message = message + deleteFail + "条节点删除失败" + delFailList + "。删除失败的节点请先解绑对应主机。";
        }
        return new WebResult(true, message);
    }

    /**
     * 获取全部节点
     * 
     * @return
     */
    public List<MdNode> getAllNodeList() {
        return mdNodeDAO.getMdNodeList();
    }

}
