package com.asiainfo.lcims.omc.report.model;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传详情
 * 
 */
public class FileTarget {
    File localFile;// 必填
    private String upload_dir;
    private String src_name;
    private String dest_name;
    private String md5;
    private Integer record_num;
    private long file_size;
    private Timestamp create_start;
    private Timestamp create_complete;
    private Timestamp upload_start;
    private Timestamp upload_complete;
    private List<FileTarget> fileTargetList = new ArrayList<>();

    public List<FileTarget> getFileTargetList() {
        return fileTargetList;
    }

    public void setFileTargetList(List<FileTarget> fileTargetList) {
        this.fileTargetList = fileTargetList;
    }

    public File getLocalFile() {
        return localFile;
    }
    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }


    public String getUpload_dir() {
        return upload_dir;
    }

    public void setUpload_dir(String upload_dir) {
        this.upload_dir = upload_dir;
    }

    public String getSrc_name() {
        return src_name;
    }

    public void setSrc_name(String src_name) {
        this.src_name = src_name;
    }

    public String getDest_name() {
        return dest_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getRecord_num() {
        return record_num;
    }

    public void setRecord_num(Integer record_num) {
        this.record_num = record_num;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }


    public Timestamp getCreate_start() {
        return create_start;
    }

    public void setCreate_start(Timestamp create_start) {
        this.create_start = create_start;
    }

    public Timestamp getCreate_complete() {
        return create_complete;
    }

    public void setCreate_complete(Timestamp create_complete) {
        this.create_complete = create_complete;
    }

    public Timestamp getUpload_start() {
        return upload_start;
    }

    public void setUpload_start(Timestamp upload_start) {
        this.upload_start = upload_start;
    }

    public Timestamp getUpload_complete() {
        return upload_complete;
    }

    public void setUpload_complete(Timestamp upload_complete) {
        this.upload_complete = upload_complete;
    }
}
