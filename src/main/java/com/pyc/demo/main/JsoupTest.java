package com.pyc.demo.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 14  * 全国省市县镇村数据爬取
 * 15  * @author liushaofeng
 * 16  * @date 2015-10-11 上午12:19:39
 * 17  * @version 1.0.0
 * 18
 */
public class JsoupTest {
    private static Map<Integer, String> cssMap = new HashMap<Integer, String>();
    private static BufferedWriter bufferedWriter = null;

    static {
        cssMap.put(1, "provincetr");// 省
        cssMap.put(2, "citytr");// 市
        cssMap.put(3, "countytr");// 县
        cssMap.put(4, "towntr");// 镇
        cssMap.put(5, "villagetr");// 村
    }

    public static void main(String[] args) throws IOException {
        int level = 1;

        initFile();

        // 获取全国各个省级信息
        Document connect = connect("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/index.html");
        Elements rowProvince = connect.select("tr." + cssMap.get(level));
        for (Element provinceElement : rowProvince)// 遍历每一行的省份城市
        {
            Elements select = provinceElement.select("a");
            for (Element province : select)// 每一个省份(四川省)
            {
                parseNextLevel(province, level + 1);
            }
        }

        closeStream();
    }

    private static void initFile() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File("d:\\CityInfo.txt"), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeStream() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bufferedWriter = null;
        }
    }

    private static void parseNextLevel(Element parentElement, int level) throws IOException {
        try {
            Thread.sleep(500);//睡眠一下，否则可能出现各种错误状态码
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Document doc = connect(parentElement.attr("abs:href"));
        if (doc != null) {
            Elements newsHeadlines = doc.select("tr." + cssMap.get(level));//
            // 获取表格的一行数据
            for (Element element : newsHeadlines) {
                printInfo(element, level + 1);
                Elements select = element.select("a");// 在递归调用的时候，这里是判断是否是村一级的数据，村一级的数据没有a标签
                if (select.size() != 0) {
                    parseNextLevel(select.last(), level + 1);
                }
            }
        }
    }

    /**
     * 写一行数据到数据文件中去
     *
     * @param element 爬取到的数据元素
     * @param level   城市级别
     **/
    private static void printInfo(Element element, int level) {
        try {
            bufferedWriter.write(element.select("td").last().text() + "{" + level + "}["
                    + element.select("td").first().text() + "]");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Document connect(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("The input url('" + url + "') is invalid!");
        }
        try {
            return Jsoup.connect(url).timeout(100 * 1000).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}