package com.demo.java.basic.download;

public class DownloadTask implements Runnable {
    private final int blockCount;
    private final int blockIndex;
    private final DownloadCallback downloadCallback;
    private final DownloadBean downloadBean;

    public DownloadTask(DownloadBean downloadBean, int blockCount, int blockIndex, DownloadCallback downloadCallback) {
        this.downloadBean = downloadBean;
        this.blockCount = blockCount;
        this.blockIndex = blockIndex;
        this.downloadCallback = downloadCallback;
    }

    @Override
    public void run() {
        String downloadUrl = DownloadUtil.getCurrentDownloadUrl(downloadBean);
        if (downloadUrl == null) {
            return;
        }
        System.out.println(Thread.currentThread().getName() + ".." + downloadBean.getAppId() + ".." + blockIndex + "..开始");
        DownloadUtil.download(downloadBean, downloadUrl, blockCount, blockIndex, downloadCallback);
    }
}