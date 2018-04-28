package com.pyc.demo;

public class Newfds {

    public static void main(String[] args) {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/11/01/110112.html";

       String url1 = url.substring(0,url.lastIndexOf("/"));
        System.out.println(url1);
    }
}
