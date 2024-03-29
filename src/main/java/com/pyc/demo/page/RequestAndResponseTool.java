package com.pyc.demo.page;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.Random;

public class RequestAndResponseTool {

    private static final Logger logger = LogManager.getLogger(RequestAndResponseTool.class);

    public static Page  sendRequstAndGetResponsebak(String url) throws InterruptedException {
        long a = (long)(Math.random()*1000);
        Thread.sleep(a);
        Page page = null;
        // 1.生成 HttpClinet 对象并设置参数
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setParameter("http.protocol.cookie-policy",
                CookiePolicy.BROWSER_COMPATIBILITY);

        // 设置 HTTP 连接超时 5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
        // 2.生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        getMethod.setRequestHeader(new Header("Connection","Keep-Alive"));
//        定义了重定向是否应该自动处理。这个参数期望得到一个java.lang.Boolean类型的值。如果这个参数没有被设置，HttpClient将会自动处理重定向。
        getMethod.getParams().setParameter("http.protocol.allow-circular-redirects", true);
        // 设置 get 请求超时 5s
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        // 设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5,true));
        // 3.执行 HTTP GET 请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                logger.info("Method failed: " + getMethod.getStatusLine());
            }
            // 4.处理 HTTP 响应内容
            byte[] responseBody = getMethod.getResponseBody();// 读取为字节 数组
            Header responseHeader = getMethod.getResponseHeader("Content-Type");
            String contentType = null;
            if (responseHeader != null) {
                contentType = responseHeader.getValue();
            }
            if (StringUtils.isNotBlank(contentType)) {
                contentType = "text/html";
            }
            String result2= HttpUtil.get("https://www.baidu.com", CharsetUtil.CHARSET_UTF_8);

             // 得到当前返回类型
            page = new Page(result2.getBytes(CharsetUtil.CHARSET_UTF_8),url,contentType); //封装成为页面
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            logger.error("错误url:"+url);
            logger.error("Please check your provided http address!");
            e.printStackTrace();
           return sendRequstAndGetResponse(url);
        } catch (IOException e) {
            logger.error("网络异常错误url:"+url);
            // 发生网络异常
            e.printStackTrace();
           return  sendRequstAndGetResponse(url);
        } finally {
            // 释放连接
            getMethod.releaseConnection();
        }
        return page;
    }



    public static Page  sendRequstAndGetResponse(String url) throws InterruptedException {
        long a = (long)(Math.random()*1000);
        Thread.sleep(a);
        Page page = null;
        try {
            String result2= HttpUtil.get(url, CharsetUtil.CHARSET_UTF_8);
            // 得到当前返回类型
            page = new Page(result2.getBytes(CharsetUtil.CHARSET_UTF_8),url,"text/html"); //封装成为页面
            page.setCharset("utf-8");
        } catch (Exception e) {

            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            logger.error("错误url:"+url);
            logger.error("Please check your provided http address!");
            logger.error("网络异常错误url:"+url);
            e.printStackTrace();
            return  sendRequstAndGetResponse(url);
        }
        return page;
    }
}