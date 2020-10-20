package com.demo.java.basic.download;

interface DownloadCallback {
    void onCompleted(DownloadBean downloadBean);

    void onError(DownloadBean downloadBean, Throwable throwable);
}