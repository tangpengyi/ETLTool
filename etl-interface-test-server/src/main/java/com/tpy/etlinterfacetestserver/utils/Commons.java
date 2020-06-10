package com.tpy.etlinterfacetestserver.utils;

import com.tpy.etlinterfacetestserver.api.ResponseDate;
import com.tpy.etlinterfacetestserver.config.SchedulerConfig;
import com.tpy.etlinterfacetestserver.task.HttpGetJob;
import com.tpy.etlinterfacetestserver.task.HttpPostJob;
import com.tpy.etlinterfacetestserver.task.SoapGetJob;
import com.tpy.etlinterfacetestserver.task.SoapPostJob;
import com.tpy.etlinterfacetestserver.utils.httpClient.HttpClientUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Commons {

    private static Log log = LogFactory.getLog(Commons.class);

    public static ResponseDate reqHttp(Map<String, Object> reqFrom1,SchedulerConfig schedulerConfig) throws SchedulerException {

        String reqStyle = (String) reqFrom1.get("reqStyle");

        if ("".equals(reqFrom1.get("reqUrl"))) {
            return ResponseDateUtils.getFailureDate("请求地址未填");
        }

        Boolean isScheduledTasks = (Boolean) reqFrom1.get("isScheduledTasks");

        if ("GET".equals(reqStyle)) {
            //定时
            if (isScheduledTasks) {
                HttpGetJob.setReqFrom(reqFrom1,schedulerConfig);
                schedulerConfig.startHttpJob("httpGet");
            }
            return ResponseDateUtils.getSuccessDate(reqGet(reqFrom1));
        } else if ("POST".equals(reqStyle)) {
            if (isScheduledTasks) {
                HttpPostJob.setReqFrom(reqFrom1,schedulerConfig);
                schedulerConfig.startHttpJob("httpPost");
            }
            return ResponseDateUtils.getSuccessDate(reqPost(reqFrom1));
        }

        return ResponseDateUtils.getFailureDate("请求类型不合");
    }

    public static String reqPost(Map<String, Object> reqFrom) {

        String paramStyle = (String) reqFrom.get("activeName");
        if ("first".equals(paramStyle)) {
            Map<String, String> paramMap = new HashMap<>();
            List reqParam = (ArrayList<HashMap<String, Object>>) reqFrom.get("reqParam");
            for (int i = 0; i < reqParam.size(); i++) {
                Map map = (HashMap<String, Object>) reqParam.get(i);
                paramMap.put((String) map.get("paramName"), (String) map.get("paramValue"));
            }
            return HttpClientUtil.sendPostByForm((String) reqFrom.get("reqUrl"), paramMap, 1);
        } else if ("second".equals(paramStyle)) {
            return HttpClientUtil.doPostJson((String) reqFrom.get("reqUrl"), (String) reqFrom.get("postReqContext"));
        } else if ("three".equals(paramStyle)) {
            return HttpClientUtil.sendPostByXml((String) reqFrom.get("reqUrl"), (String) reqFrom.get("xmlReqContext"), 1);
        }
        return null;
    }

    public static String reqGet(Map<String, Object> reqFrom) {

        Map<String, String> paramMap = new HashMap<>();
        String paramStyle = (String) reqFrom.get("activeName");

        //form 键值对传参
        if ("first".equals(paramStyle)) {
            return HttpClientUtil.sendGetByForm((String) reqFrom.get("reqUrl"), getHttpGetParamsByForm(reqFrom));
        }
        //json对象传参
        else if ("second".equals(paramStyle)) {
            String json = (String) reqFrom.get("postReqContext");
            paramMap = UtilTools.strToMap(json, paramMap);
            return HttpClientUtil.sendGetByForm((String) reqFrom.get("reqUrl"), paramMap);
        }
        //xml文件传参
        else if ("three".equals(paramStyle)) {
        }
        return null;
    }

    public static Map<String,String> getHttpGetParamsByForm(Map<String, Object> reqFrom){
        Map<String, String> paramMap = new HashMap<>();
        List reqParam = (ArrayList<HashMap<String, Object>>) reqFrom.get("reqParam");
        for (int i = 0; i < reqParam.size(); i++) {
            Map map = (HashMap<String, Object>) reqParam.get(i);
            paramMap.put((String) map.get("paramName"), (String) map.get("paramValue"));
        }
        return paramMap;
    }

    public static ResponseDate reqSoap(Map<String, Object> reqFrom,SchedulerConfig schedulerConfig) throws UnsupportedEncodingException {

        String activeName = (String) reqFrom.get("activeName");
        Boolean isParam = (Boolean) reqFrom.get("isParam");
        ResponseDate responseDate = ResponseDateUtils.getSuccessDate(null);
        String url = (String) reqFrom.get("reqUrl");
        Boolean isScheduledTasks = (Boolean) reqFrom.get("isScheduledTasks");

        if ("GET".equals((String) reqFrom.get("reqStyle"))) {

            String s = null;
            if ("first".equals(activeName) && isParam == false) {
                s = HttpClientUtil.sendGetByForm(url, new HashMap<>());
            } else {
                List<HashMap<Object, Object>> reqParam = (List<HashMap<Object, Object>>) reqFrom.get("reqParam");
                for (int i = 0; i < reqParam.size(); i++) {
                    HashMap<Object, Object> objectObjectHashMap = reqParam.get(i);
                    String paramValue = (String) objectObjectHashMap.get("paramValue");
                    url = url + "/" + paramValue;
                }
                reqFrom.put("reqUrl",url);
                s = HttpClientUtil.sendGetByForm(url, new HashMap<>());
            }

            //是否定时
            if (isScheduledTasks) {
                SoapGetJob.setReqFrom(reqFrom,schedulerConfig);
                try {
                    schedulerConfig.startSoapJob("soapGet");
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }

            if("".equals(s)){
                responseDate.setDate("url请求失败");
            }else{
                responseDate.setDate(s);
            }
            return responseDate;
        } else if ("POST".equals((String) reqFrom.get("reqStyle"))) {

            if ("second".equals(activeName)) {
                String jsonReqContext = HttpClientUtil.sendPostByJson(url, (String) reqFrom.get("jsonReqContext"), 1);
                responseDate.setDate(new String(jsonReqContext.getBytes(getEncoding(jsonReqContext)), "utf-8"));
            } else if ("three".equals(activeName)) {
                String xmlReqContext = HttpClientUtil.sendPostByXml(url, (String) reqFrom.get("xmlReqContext"), 1);
                responseDate.setDate(xmlReqContext);
            }

            //是否定时
            if (isScheduledTasks) {
                SoapPostJob.setReqFrom(reqFrom,schedulerConfig);
                try {
                    schedulerConfig.startSoapJob("soapPost");
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseDate;
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
