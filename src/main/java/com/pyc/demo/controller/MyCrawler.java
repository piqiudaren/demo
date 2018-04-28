package com.pyc.demo.controller;

import com.pyc.demo.link.LinkFilter;
import com.pyc.demo.link.Links;
import com.pyc.demo.model.City;
import com.pyc.demo.model.County;
import com.pyc.demo.model.Province;
import com.pyc.demo.model.Town;
import com.pyc.demo.model.Village;
import com.pyc.demo.page.Page;
import com.pyc.demo.page.PageParserTool;
import com.pyc.demo.page.RequestAndResponseTool;
import com.pyc.demo.service.DemoService;
import com.pyc.demo.util.FileTool;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.pyc.demo.commen.Address.PROVINCE_URL;

@Controller
@RequestMapping(value = "/crawler")
public class MyCrawler {

    /**
     * 使用种子初始化 URL 队列
     *
     * @param seeds 种子 URL
     * @return
     */

    @Autowired
    @Qualifier("demoService")
    private DemoService demoService;

    List<Province> provinceList = new ArrayList<>(16);
    List<City> cityList = new ArrayList<>(16);
    List<County> countyList = new ArrayList<>(16);
    List<Town> townList = new ArrayList<>(16);
    List<Village> villageList = new ArrayList<>(16);

    Set<String> set = new HashSet<>(16);

    private void initCrawlerWithSeeds(String[] seeds) {
        for (int i = 0; i < seeds.length; i++){
            Links.addUnvisitedUrlQueue(seeds[i]);
        }
    }


    /**
     * 抓取过程
     *
     * @param seeds
     * @return
     */
    public void crawling(String[] seeds) throws InterruptedException {

        //初始化 URL 队列
        initCrawlerWithSeeds(seeds);

        //定义过滤器，提取以 http://www.baidu.com 开头的链接
        LinkFilter filter = new LinkFilter() {
            public boolean accept(String url) {
                if (url.startsWith("http://www.stats.gov.cn")) {
                    return true;
                }else {
                    return false;
                }
            }
        };

        //循环条件：待抓取的链接不空且抓取的网页不多于 1000
        while (!Links.unVisitedUrlQueueIsEmpty()  && Links.getVisitedUrlNum() <= 1000) {

            //先从待访问的序列中取出第一个；
            String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Thread.sleep(1000);
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);

            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,"a");
            if(!es.isEmpty()) {
                for (Iterator i = es.iterator(); i.hasNext(); ) {
                    Element e = (Element) i.next();
                    String href = PROVINCE_URL + e.attr("href");
                    String text = e.text();
                    if("京ICP备05034670号".equals(text)){continue;}
                    Province province = new Province();
                    province.setParentId(0L);
                    province.setUrl(href);
                    province.setName(text);
                    provinceList.add(province);
                }
                if (provinceList != null && provinceList.size() >0) {
                    //做保存
                    try {
                        provinceList = demoService.saveProvince(provinceList);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    crawlingCity();
                }
            }

            //将保存文件
            FileTool.saveToLocal(page);
        }
    }

    public void crawlingCity() throws InterruptedException {

        for (Province province : provinceList) {
            //循环条件：待抓取的链接不空且抓取的网页不多于 1000

            //先从待访问的序列中取出第一个；
            String visitUrl = province.getUrl();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Thread.sleep(1000);
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);

            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,"tr.citytr");
            if(!es.isEmpty()) {
                for (Iterator i = es.iterator(); i.hasNext(); ) {
                    Element esa = (Element) i.next();
                    Elements elements = esa.select("a");
//                        Elements elements = esa.select("a");
                    Iterator it =  elements.iterator();
                    Element e1 = (Element)it.next();
                    Element e2 = (Element)it.next();
//                    String url = province.getUrl();
//                    url = url.substring(0,url.lastIndexOf("/"));
//                    String href = url + e1.attr("href");
                    String href = PROVINCE_URL + e1.attr("href");
                    String name = e2.text();
                    String code = e1.text();
                    City city = new City();
                    city.setUrl(href);
                    city.setName(name);
                    city.setCode(code);
                    city.setParentId(province.getId());
                    cityList.add(city);
                }
                if (cityList != null && cityList.size() >0) {
                    cityList = demoService.saveCity(cityList);
                    crawlingCountry();
                    //保存市
                }
                //
            }
            //将保存文件
            FileTool.saveToLocal(page);
            cityList.clear();
        }
    }

    public void crawlingCountry() throws InterruptedException {
        for (City city : cityList) {
            //循环条件：待抓取的链接不空且抓取的网页不多于 1000

            //先从待访问的序列中取出第一个；
            String visitUrl = city.getUrl();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Thread.sleep(1000);
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);

            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,"tr.countytr");
            if(!es.isEmpty()) {
                for (Iterator i = es.iterator(); i.hasNext(); ) {
                    Element esa = (Element) i.next();
                    Elements elements = esa.select("a");

                    Iterator it =  elements.iterator();
                    Element e1 = (Element)it.next();
                    Element e2 = (Element)it.next();

                    String url = city.getUrl();
                    url = url.substring(0,url.lastIndexOf("/"))+"/";
                    String href = url + e1.attr("href");
//                    String href = PROVINCE_URL + e1.attr("href");
                    String name = e2.text();
                    String code = e1.text();
                    County county = new County();
                    county.setUrl(href);
                    county.setName(name);
                    county.setCode(code);
                    county.setParentId(city.getId());

                    countyList.add(county);
                }
                if (countyList != null && countyList.size() >0) {
                    //保存
                    countyList = demoService.saveCountry(countyList);
                    crawlingTowntr();
                }
            }
            countyList.clear();
            //将保存文件
            FileTool.saveToLocal(page);
        }
    }

    public void crawlingTowntr() throws InterruptedException {
        for (County county : countyList) {
            //循环条件：待抓取的链接不空且抓取的网页不多于 1000

            //先从待访问的序列中取出第一个；
            String visitUrl = county.getUrl();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Thread.sleep(1000);
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);

            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,"tr.towntr");
            if(!es.isEmpty()) {
                for (Iterator i = es.iterator(); i.hasNext(); ) {
                    Element esa = (Element) i.next();
                    Elements elements = esa.select("a");

                    Iterator it =  elements.iterator();
                    Element e1 = (Element)it.next();
                    Element e2 = (Element)it.next();

                    String url = county.getUrl();
                    url = url.substring(0,url.lastIndexOf("/"))+"/";;
                    String href = url + e1.attr("href");

//                    String href = PROVINCE_URL + e1.attr("href");
                    String name = e2.text();
                    String code = e1.text();
                    Town town = new Town();
                    town.setUrl(href);
                    town.setName(name);
                    town.setCode(code);
                    town.setParentId(county.getId());
                    townList.add(town);
                }
                if (townList != null && townList.size() >0) {
                    //保存
                    townList = demoService.saveTown(townList);
                    crawlingVillageList();
                }
            }
            townList.clear();
            //将保存文件
            FileTool.saveToLocal(page);
        }
    }

    public void crawlingVillageList() throws InterruptedException {
        for (Town town : townList) {
            //循环条件：待抓取的链接不空且抓取的网页不多于 1000

            //先从待访问的序列中取出第一个；
            String visitUrl = town.getUrl();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Thread.sleep(1000);
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);


            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,"tr.villagetr");
            if(!es.isEmpty()) {
                for (Iterator i = es.iterator(); i.hasNext(); ) {
                    Element esa = (Element) i.next();
                    Elements elements = esa.select("td");

                    Iterator it =  elements.iterator();
                    Element e1 = (Element)it.next();
                    Element e2 = (Element)it.next();
                    Element e3 = (Element)it.next();

                    String name = e3.text();
                    String code = e1.text();
                    Village village = new Village();
                    village.setUrl("");
                    village.setName(name);
                    village.setCode(code);
                    village.setParentId(town.getId());
                    villageList.add(village);
                }
                if (villageList != null && villageList.size() >0) {
                    //保存
                    villageList = demoService.saveVillage(villageList);
                    villageList.clear();
                }
            }
            //将保存文件
            FileTool.saveToLocal(page);
        }
    }
    //main 方法入口

    @RequestMapping("/firstTest")
    public String  myfirstTest() throws InterruptedException {
//        MyCrawler crawler = new MyCrawler();
        crawling(new String[]{"http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html"});

        return "success";
    }

    @RequestMapping("/firstTest1")
    @ResponseBody
    public String  myfirstTest1(){
//        com.pyc.demo.main.MyCrawler crawler = new com.pyc.demo.main.MyCrawler();
//        crawler.crawling(new String[]{"http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html"});
        System.out.println("asdfasfasfasfasfasdfasfdasfsd");
        return "successasdfasdfasddf";
    }


   /* public static void main (String[] args) throws InterruptedException {
        com.pyc.demo.main.MyCrawler crawler = new com.pyc.demo.main.MyCrawler();
        crawler.crawling(new String[]{"http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html"});
    }*/
}
