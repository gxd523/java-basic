package com.demo.java.basic.download;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class DownloadCallbackImpl implements DownloadCallback {
    private final Map<String, Integer> map = new ConcurrentHashMap<>();
    private final ThreadPoolExecutor threadPool;
    private final int apkCount;
    private int complete;
    long startTime = System.currentTimeMillis();

    public DownloadCallbackImpl(ThreadPoolExecutor threadPool, int apkCount) {
        this.threadPool = threadPool;
        this.apkCount = apkCount;
    }

    @Override
    public void onCompleted(DownloadBean downloadBean) {
        String appId = downloadBean.getAppId();
        Integer count = map.get(appId);
        if (count == null) {
            map.put(appId, 1);
        } else if (count + 1 == MultiThreadDownloadMultiFile.FILE_BLOCK_COUNT) {
            // TODO 检查md5
            if (appId == "3") {

            }
            System.out.println("开始安装..." + appId);
            complete++;
            if (complete == apkCount) {
                threadPool.shutdown();
                System.out.println("关闭线程池..." + (System.currentTimeMillis() - startTime));
//                        while (!threadPool.isTerminated()) {
//                            System.out.println("还没关...");
//                        }
            }
        } else {
            map.put(appId, count + 1);
        }
    }

    @Override
    public void onError(DownloadBean downloadBean, Throwable throwable) {
        String appId = downloadBean.getAppId();
        map.remove(appId);
        boolean result = new File(downloadBean.getFileName()).delete();
        System.out.println("删除结果..." + result);
        if (downloadBean.getRetry() < 3) {
            downloadBean.incrementRetry();
            DownloadUtil.executeTask(threadPool, downloadBean, MultiThreadDownloadMultiFile.FILE_BLOCK_COUNT, this);
        }
    }
}