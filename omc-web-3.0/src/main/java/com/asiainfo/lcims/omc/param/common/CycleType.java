package com.asiainfo.lcims.omc.param.common;

import java.util.concurrent.TimeUnit;

/**
 * 指标的周期
 * 
 * @author XHT
 *
 */
public enum CycleType {
    ONE_MINUTE(0, 1), FIVE_MINUTES(1, 5), TEN_MINUTES(2, 10), THIRTY_MINUTES(3, 30), ONE_HOUR(4,
            60), ONE_DAY(5, 60 * 24),;

    private int type;
    private int minutes;

    CycleType(int type, int minutes) {
        this.type = type;
        this.minutes = minutes;
    }

    public int getType() {
        return type;
    }

    public long getMillis() {
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    public int getMinutes() {
        return (int) TimeUnit.MINUTES.toMinutes(minutes);
    }
}
