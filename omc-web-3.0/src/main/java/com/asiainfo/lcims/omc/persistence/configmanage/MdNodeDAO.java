package com.asiainfo.lcims.omc.persistence.configmanage;

import com.asiainfo.lcims.omc.model.configmanage.MdNode;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MdNodeDAO {
    @SelectProvider(method = "getMdNode", type = SqlProvider.class)
    List<MdNode> getMdNode(@Param("mdNode") MdNode mdNode);

    @Insert("INSERT INTO MD_NODE (ID,NODE_NAME,DESCRIPTION) VALUES(#{mdNode.id},#{mdNode.node_name},#{mdNode.description})")
    int insert(@Param("mdNode") MdNode mdNode);

    @Delete("DELETE FROM MD_NODE WHERE ID NOT IN (SELECT NODEID FROM MON_HOST) AND ID IN (#{id})")
    public int delete(@Param("id") String id);

    @UpdateProvider(method = "updateMdNode", type = SqlProvider.class)
    int update(@Param("mdNode") MdNode mdNode);

    @Select("SELECT ID,NODE_NAME,DESCRIPTION FROM MD_NODE")
    List<MdNode> getMdNodeList();

    @Select("SELECT ID,NODE_NAME,DESCRIPTION FROM MD_NODE WHERE NODE_NAME= #{nodeName}")
    MdNode getMdNodeByNodeName(@Param("nodeName") String nodeName);
}
