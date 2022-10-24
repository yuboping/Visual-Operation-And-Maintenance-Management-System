package com.asiainfo.lcims.omc.service.mail;

public class SimpleMail {
    private String subject;
    private String content;
    private String  partFilePath;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPartFilePath() {
        return partFilePath;
    }

    public void setPartFilePath(String partFilePath) {
        this.partFilePath = partFilePath;
    }
}
