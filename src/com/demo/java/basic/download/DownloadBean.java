package com.demo.java.basic.download;

public class DownloadBean {
    private String appId;
    private String downloadUrl;
    private String downloadUrl2;
    private String downloadUrl3;
    private String md5;

    private int retry = 1;
    private String fileName;

    public DownloadBean(String appId, String downloadUrl) {
        this(appId, downloadUrl, null, null);
    }

    public DownloadBean(String appId, String downloadUrl, String downloadUrl2, String downloadUrl3) {
        this.appId = appId;
        this.downloadUrl = downloadUrl;
        this.downloadUrl2 = downloadUrl2;
        this.downloadUrl3 = downloadUrl3;
    }

    public String getAppId() {
        return appId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDownloadUrl2() {
        return downloadUrl2;
    }

    public String getDownloadUrl3() {
        return downloadUrl3;
    }

    public int getRetry() {
        return retry;
    }

    public void incrementRetry() {
        this.retry += 1;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = appId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}