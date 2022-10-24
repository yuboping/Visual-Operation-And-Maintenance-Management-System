package com.asiainfo.lcims.omc.model.ems;

import java.util.ArrayList;
import java.util.List;

public class EmsAddHostResponse {
    private String uuid;
    private List<EmsAddhostResult> results = new ArrayList<EmsAddhostResult>();
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public List<EmsAddhostResult> getResults() {
        return results;
    }
    public void setResults(List<EmsAddhostResult> results) {
        this.results = results;
    }
}
