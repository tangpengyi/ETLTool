package com.tpy.etlinterfacetestserver.test;

import com.tpy.etlinterfacetestserver.utils.UtilTools;
import com.tpy.etlinterfacetestserver.utils.httpClient.HttpClientUtil;
import com.tpy.etlinterfacetestserver.utils.Commons;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        sendPostByXML();
    }

    public static void sendGetByForm(){
        String s = HttpClientUtil.sendGetByForm("http://localhost:8001/ws/userService/user/1/张三", new HashMap<>());
        System.out.println(s);
        s = HttpClientUtil.sendGetByForm("http://localhost:8001/ws/userService/user", new HashMap<>());
        System.out.println(s);
    }

    public static void sendPostByForm(){
//        String url = "http://www.webxml.com.cn/WebServices/TranslatorWebService.asmx";
        String url = "http://localhost:8001/ws/userService/user";
        Map<String,String> map = new HashMap<>();
        map.put("user","{\"id\":\"1\",\"username\":\"张三\",\"city\":\"张三\"}");
        String s = HttpClientUtil.sendPostByForm(url, map, 1);
        System.out.println(s);
    }

    public static void sendPostByJson() throws UnsupportedEncodingException {
        String url = "http://localhost:8001/ws/userService/user";
//        String jsonStr = "{\"张三\"}";
        String jsonStr = "{\"张三\"}";
//        String s = HttpClientUtil.sendPostByJson(url, jsonStr, 1);
        String s = HttpClientUtil.sendPostByJson(url,jsonStr,1);

        String encoding = getEncoding(s);
        System.out.println("编码格式："+encoding);

        String str2 = new String(s.getBytes("ISO-8859-1"), "utf-8");
        System.out.println(str2);
    }

    public static void sendPostByXML() throws UnsupportedEncodingException {
        String url = "http://www.webxml.com.cn/WebServices/TranslatorWebService.asmx";
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");
        sb.append("<soap:Body>\n");
        sb.append("<getEnCnTwoWayTranslator xmlns=\"http://WebXml.com.cn/\">\n");
        sb.append("<Word>");
        sb.append("hello");
        sb.append("</Word>\n");
        sb.append("</getEnCnTwoWayTranslator>\n");
        sb.append("</soap:Body>\n");
        sb.append("</soap:Envelope>\n");
        String xmlStr = sb.toString();
        List<Map<String, Object>> maps = UtilTools.parseXML(xmlStr);
        System.out.println(maps.toString());
        String s = HttpClientUtil.sendPostByXml(url, xmlStr, 1);
        String encoding = getEncoding(s);
        System.out.println("编码格式："+encoding);


        String str2 = new String(s.getBytes(getEncoding(s)), "utf-8");
        System.out.println(s);
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

}
