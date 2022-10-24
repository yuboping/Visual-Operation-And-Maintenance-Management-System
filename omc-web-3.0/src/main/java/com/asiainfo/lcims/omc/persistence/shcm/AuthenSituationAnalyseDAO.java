package com.asiainfo.lcims.omc.persistence.shcm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.asiainfo.lcims.omc.model.shcm.AuthenSituationChartData;
import com.asiainfo.lcims.omc.persistence.shcm.impl.AuthenSituationAnalyseDAOImpl;

public interface AuthenSituationAnalyseDAO {

    @SelectProvider(method = "getAuthenSituationListWithDay", type = AuthenSituationAnalyseDAOImpl.class)
    List<AuthenSituationChartData> getAuthenSituationListWithDay(
            @Param("paramsMap") Map<String, Object> params);

}
