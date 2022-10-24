package com.asiainfo.lcims.omc.model.configmanage;

/**
 * 
 * @author zhujiansheng
 * @date 2019年9月2日 下午5:23:43
 * @version V1.0
 */
public class MdSoundAlarm {

    private String id;

    private String sound_name;

    private String sound_duration;

    private String sound_music;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSound_name() {
        return sound_name;
    }

    public void setSound_name(String sound_name) {
        this.sound_name = sound_name;
    }

    public String getSound_duration() {
        return sound_duration;
    }

    public void setSound_duration(String sound_duration) {
        this.sound_duration = sound_duration;
    }

    public String getSound_music() {
        return sound_music;
    }

    public void setSound_music(String sound_music) {
        this.sound_music = sound_music;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
