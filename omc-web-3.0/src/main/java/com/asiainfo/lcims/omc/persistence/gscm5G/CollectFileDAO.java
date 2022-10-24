package com.asiainfo.lcims.omc.persistence.gscm5G;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileDiff;
import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileMonitor;
import com.asiainfo.lcims.omc.persistence.gscm5G.impl.CollectFileDAOImpl;

public interface CollectFileDAO {
    @SelectProvider(method = "getMdCollectFile", type = CollectFileDAOImpl.class)
    List<MdCollectFileMonitor> getMdCollectFile(
            @Param("mdCollectFileMonitor") MdCollectFileMonitor mdCollectFileMonitor);

    @SelectProvider(method = "getMdCollectFileDiff", type = CollectFileDAOImpl.class)
    List<MdCollectFileDiff> getMdCollectFileDiff(
            @Param("mdCollectFileDiff") MdCollectFileDiff mdCollectFileDiff);

}
