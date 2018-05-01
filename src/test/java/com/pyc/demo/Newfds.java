package com.pyc.demo;

import java.io.File;
import java.io.IOException;

public class Newfds {

    public static void main(String[] args) throws IOException {
     /*   String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/11/01/110112.html";

       String url1 = url.substring(0,url.lastIndexOf("/"));
        System.out.println(url1);*/

     String path = ".."+File.separator+"forder";
     File file = new File(path);
     if(!file.exists()){
         file.mkdirs();
     }

     File file1 = new File(path+File.separator+"1.txt");
     if(!file1.exists()){
         file1.createNewFile();
     }
    }
}
