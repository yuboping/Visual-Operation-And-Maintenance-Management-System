package com.asiainfo.lcims.omc.service.gscm5G;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileDiff;
import com.asiainfo.lcims.omc.model.gscm5G.MdCollectFileMonitor;
import com.asiainfo.lcims.omc.persistence.gscm5G.CollectFileDAO;

@Service(value = "collectFileManageService")
public class CollectFileManageService {
    @Inject
    private CollectFileDAO collectFileDAO;

    public List<MdCollectFileMonitor> getMdCollectFileMonitorList(
            MdCollectFileMonitor mdCollectFileMonitor) {
        return collectFileDAO.getMdCollectFile(mdCollectFileMonitor);
    }

    public List<MdCollectFileDiff> getMdCollectFileDiffList(MdCollectFileDiff mdCollectFileDiff) {
        return collectFileDAO.getMdCollectFileDiff(mdCollectFileDiff);
    }

}
