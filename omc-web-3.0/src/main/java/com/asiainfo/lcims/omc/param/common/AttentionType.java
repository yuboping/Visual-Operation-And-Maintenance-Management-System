package com.asiainfo.lcims.omc.param.common;

public enum AttentionType {
    INDETERMINATE(0, "INDETERMINATE","unmonitor", 1), 
    WARNING(1, "WARNING警告告警", "normal",2), 
    MINOR(2, "MINOR次要告警", "warn",3), 
    MAJOR(3, "MAJOR重大告警","error", 4), 
    CRITICAL(4, "CRITICAL严重告警","disable", 5),
    ;
    private int level;
    private String name;
    private String style;
    private int priority;

    private AttentionType(int level, String name, String style, int priority) {
        this.level = level;
        this.name=name;
        this.style = style;
        this.priority = priority;
    }

    public int getLevel() {
        return level;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    public static AttentionType getByLevel(int level) {
        for (AttentionType band : AttentionType.values()) {
            if (band.level == level) {
                return band;
            }
        }
        return null;
    }

}
