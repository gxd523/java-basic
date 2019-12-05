package com.demo.java.basic;

import com.demo.java.basic.io.IoUtil;

public class Main {
    public static void main(String[] args) {
        IoUtil.printToFile("/Users/guoxiaodong/Desktop/a.txt", true);
        IoUtil.copyFile("/Users/guoxiaodong/Movies/Parasite.2019.mp4", "/Users/guoxiaodong/Desktop/a.mp4");
    }
}
