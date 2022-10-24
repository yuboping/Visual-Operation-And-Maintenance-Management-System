package com.asiainfo.lcims.omc.persistence.configmanage;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.lcims.omc.model.configmanage.MdBusinessHost;
import com.asiainfo.lcims.omc.persistence.SqlProvider;
import com.asiainfo.lcims.omc.util.page.Page;

/**
 * 
 * @ClassName: MdBusinessHostDAO
 * @Description: TODO(业务主机关系)
 * @author yubp@asiainfo-sec.com
 * @date 2018年8月10日 下午3:59:25
 *
 */
public interface MdBusinessHostDAO {

    @SelectProvider(method = "getMdBusinessHost", type = SqlProvider.class)
    List<MdBusinessHost> getMdBusinessHost(@Param("mdBusinessHost") MdBusinessHost mdBusinessHost);

    @SelectProvider(method = "getMdBusinessHostTotalCount", type = SqlProvider.class)
    int getMdBusinessHostTotalCount(@Param("mdBusinessHost") MdBusinessHost mdBusinessHost);

    @SelectProvider(method = "getMdBusinessHostPage", type = SqlProvider.class)
    List<MdBusinessHost> getMdBusinessHostPage(
            @Param("mdBusinessHost") MdBusinessHost mdBusinessHost, @Param("page") Page page);

    @Insert("INSERT INTO MD_BUSINESS_HOST (ID,NAME,HOSTID) VALUES(#{mdBusinessHost.id},#{mdBusinessHost.name},#{mdBusinessHost.hostid})")
    int insert(@Param("mdBusinessHost") MdBusinessHost mdBusinessHost);

    @Delete("DELETE FROM MD_BUSINESS_HOST WHERE ID IN (#{id})")
    public int delete(@Param("id") String id);

    @Delete("DELETE FROM MD_BUSINESS_HOST WHERE HOSTID = #{hostid}")
    public int deleteByHostId(@Param("hostid") String hostid);

    @UpdateProvider(method = "updateMdBusinessHost", type = SqlProvider.class)
    int update(@Param("mdBusinessHost") MdBusinessHost mdBusinessHost);
    
    @Select("SELECT ID,NAME,HOSTID FROM MD_BUSINESS_HOST WHERE HOSTID  = #{hostid}")
    List<MdBusinessHost> getMdBusinessHostListByHostId(@Param("hostid") String hostid);
    
    /**
     * 批量提交Oracle
     * @param datas
     */
    @Insert("<script> insert all into MD_BUSINESS_HOST(ID,NAME,HOSTID) values " +
            "  <foreach collection='datas' item='item' separator=' into MD_BUSINESS_HOST(ID,NAME,HOSTID) values' > " +
            "  (#{item.id},#{item.name},#{item.hostid}) \n" +
            "  </foreach> ${selectSql} </script>")
    void addBattchBusinessHostForOracle(@Param("datas") List<MdBusinessHost> businessHosts, @Param("selectSql") String selectSql);
    
    /**
     * 批量提交Mysql
     * @param datas
     */
    @Insert("<script> insert into MD_BUSINESS_HOST(ID,NAME,HOSTID) values " +
            "  <foreach collection='datas' item='item' separator=',' > " +
            "  (#{item.id},#{item.name},#{item.hostid}) \n" +
            "  </foreach> </script>")
    void addBattchBusinessHostForMysql(@Param("datas") List<MdBusinessHost> businessHosts, @Param("selectSql") String selectSql);
    
    /**
     * 根据hostids批量删除 EMS模块调用
     * @param hostids
     * @return
     */
    @Delete("DELETE FROM MD_BUSINESS_HOST WHERE HOSTID IN (${hostids})")
    public int deleteByHostIds(@Param("hostids") String hostids);
}
