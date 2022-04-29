package com.scwl.hzzxgd.demo;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
@Slf4j
public class HttpRequestUtil {
    public static String getSuiteTokenToJson2(String ticket) {

//        JSONObject json = new JSONObject();
//        json.put("suite_id", WxCfg.WX_COMP_SUITEID);//应用id
//        json.put("suite_secret", WxCfg.WX_COMP_SECRET);//应用secret
//        json.put("suite_ticket", ticket);//企业微信后台推送的ticket
//        String returnStr = HttpRequestUtil.sendPost("https://qyapi.weixin.qq.com/cgi-bin/service/get_suite_token",json.toString());
//        return returnStr;
        String returnStr = null;
        try {
            JSONObject json = new JSONObject();
            json.put("suite_id", JSONObject.NULL);//应用id
            json.put("suite_secret",JSONObject.NULL);//应用secret
            json.put("suite_ticket", ticket);//企业微信后台推送的ticket
            returnStr = HttpRequestUtil.sendPost("https://qyapi.weixin.qq.com/cgi-bin/service/get_suite_token",json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnStr;
    }
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            log.info("\n\n=====================sendPost 请求报文：========================="+
                    "\n>>url:"+url+
                    "\n>>param:"+param+
                    "\n===================================================================\n");
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("发送 POST 请求出现异常！" + e.getMessage());
            result = new StringBuilder("{\"resCode\":\"1\",\"errCode\":\"1001\",\"resData\":\"\"}");
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        log.info("\n\n=====================sendPost 响应报文：========================="+
                "\n>>url:"+url+
                "\n>>报文体:"+result.toString()+
                "\n===================================================================\n");
        return result.toString();
    }

}
