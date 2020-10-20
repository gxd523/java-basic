package com.demo.java.basic.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

public class DownloadUtil {
    private static boolean testFlag = true;

    public static void download(DownloadBean downloadBean, String downloadUrl, int blockCount, int blockIndex, DownloadCallback downloadCallback) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            URL url = new URL(downloadUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setUseCaches(false);
            // 是否重定向
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.connect();

            switch (httpURLConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    String redirectUrl = httpURLConnection.getHeaderField("Location");
                    if (redirectUrl == null) {
                        return;
                    }
                    downloadBean.setFileName(redirectUrl.substring(redirectUrl.lastIndexOf('/') + 1));
                    download(downloadBean, redirectUrl, blockCount, blockIndex, downloadCallback);
                    break;
                case HttpURLConnection.HTTP_OK:
                    int perBlockSize = httpURLConnection.getContentLength() / blockCount;
                    inputStream = httpURLConnection.getInputStream();
                    inputStream.skip(perBlockSize * blockIndex);
                    randomAccessFile = new RandomAccessFile(downloadBean.getFileName(), "rw");
                    randomAccessFile.seek(perBlockSize * blockIndex);
                    byte[] bytes = new byte[1024];
                    int perLength;
                    int currentLength = 0;
                    while (currentLength < perBlockSize && (perLength = inputStream.read(bytes)) != -1) {
                        randomAccessFile.write(bytes, 0, perLength);
                        currentLength += perLength;
                        Thread.sleep(1);
                    }

                    if (testFlag && downloadBean.getFileName().contains("市场") && blockIndex == 1) {
                        testFlag = false;
                        throw new IOException();
                    } else {
                        System.out.println("onCompleted...." + downloadBean.getAppId() + "..." + blockIndex);
                        downloadCallback.onCompleted(downloadBean);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("onError....");
            downloadCallback.onError(downloadBean, null);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static void executeTask(ThreadPoolExecutor threadPool, DownloadBean downloadBean, int blockCount, DownloadCallback downloadCallback) {
        downloadBean.setFileName(DownloadUtil.getFileNameByDownloadUrl(downloadBean));
        for (int i = 0; i < blockCount; i++) {
            threadPool.execute(new DownloadTask(downloadBean, blockCount, i, downloadCallback));
        }
    }

    private static String getFileNameByDownloadUrl(DownloadBean downloadBean) {
        String fileName;
        String downloadUrl = getCurrentDownloadUrl(downloadBean);
        if (downloadUrl != null && downloadUrl.contains(".apk")) {
            fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
        } else {
            fileName = UUID.randomUUID() + ".apk";
        }
        return fileName;
    }

    public static String getCurrentDownloadUrl(DownloadBean downloadBean) {
        String downloadUrl = null;
        switch (downloadBean.getRetry()) {
            case 1:
                downloadUrl = downloadBean.getDownloadUrl();
                break;
            case 2:
                downloadUrl = downloadBean.getDownloadUrl2();
                break;
            case 3:
                downloadUrl = downloadBean.getDownloadUrl3();
                break;
        }
        return downloadUrl;
    }
}
