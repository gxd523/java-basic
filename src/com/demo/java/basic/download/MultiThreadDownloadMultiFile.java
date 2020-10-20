package com.demo.java.basic.download;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程下载多文件
 */
public class MultiThreadDownloadMultiFile {
    public static final int FILE_BLOCK_COUNT = 2;
    public static final int THREAD_COUNT = 4;

    public static void main(String[] args) {
        List<DownloadBean> downloadBeanList = new ArrayList<DownloadBean>() {{
            add(new DownloadBean("市场", "http://down.znds.com/getdownurl/?s=d2ViL2RhbmdiZWltYXJrZXRfNC4yLjZfeWouYXBr", "http://app.znds.com/down/20200928/txsp16158_6.1.0.1007_dangbei.apk", null));
            add(new DownloadBean("桌面", "http://down.znds.com/getdownurl/?s=ZG93bi8yMDIwMDcxMS9kYnptXzMuMy4xZGFuZ2JlaS5hcGs="));
            add(new DownloadBean("健身", "http://dlap2.dbkan.com/down/20200902/dbjs_2.2.7_dangbei.apk"));
            add(new DownloadBean("爱奇艺", "http://app.znds.com/down/20201015/aqy11642_10.9.3.119812_dangbei.apk"));
            add(new DownloadBean("快手", "http://app.znds.com/down/20201013/kstv_1.0.3.126_dangbei.apk"));
            add(new DownloadBean("西瓜", "http://app.znds.com/down/20200828/xgsptvb_1.9.5_dangbei.apk"));
        }};

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30), new ThreadFactory() {
            int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "thread-" + count++);
            }
        }, new ThreadPoolExecutor.AbortPolicy());

        DownloadCallback downloadCallback = new DownloadCallbackImpl(threadPool, downloadBeanList.size());

        for (DownloadBean downloadBean : downloadBeanList) {
            DownloadUtil.executeTask(threadPool, downloadBean, FILE_BLOCK_COUNT, downloadCallback);
        }
    }
}