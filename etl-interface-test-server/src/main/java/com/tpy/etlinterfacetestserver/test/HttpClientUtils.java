package com.tpy.etlinterfacetestserver.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tpy.etlinterfacetestserver.utils.httpClient.HttpClientFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 *
 * @ClassName: HttpsUtils
 * @Description: TODO(https post忽略证书请求)
 */
public class HttpClientUtils {
    private final static Log logger = LogFactory.getLog(HttpClientUtils.class);

    public static String postSOAP(String url, String soapContent) {

        HttpClient httpclient = null;
        HttpPost httpPost = null;
        BufferedReader reader = null;
        int i = 0;

        while (i < 4) {
            try {
                httpclient = HttpClientFactory.getInstance().getHttpClient();
                httpPost = new HttpPost(url);
                StringEntity myEntity = new StringEntity(soapContent, "UTF-8");
                httpPost.addHeader("Content-Type", "text/xml; charset=UTF-8");
                httpPost.setEntity(myEntity);
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    reader = new BufferedReader(new InputStreamReader(resEntity
                            .getContent(), "UTF-8"));
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        sb.append("\r\n");
                    }
                    return sb.toString();
                }

            } catch (Exception e) {
                i++;
                if (i == 4) {
                    logger.error("not connect:" + url + "\n" + e.getMessage());
                }
            } finally {
                if (httpPost != null) {
                    httpPost.abort();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (httpclient != null) {
                    httpclient.getConnectionManager().shutdown();
                }
            }
        }
        return "none";
    }

    public static void main(String[] args) {
        String url = "http://localhost:9999/ward/entry";
        String soap = "<xml>\r\n"
                + "<body>\r\n"
                + "传递过来的内容\r\n"
                + "</body>\r\n"
                + "</xml>";

        String xmlStr="<?xml version='1.0' encoding='UTF-8' standalone='no'?><user><loginSource>iws</loginSource><userAccount>admin</userAccount><userPasswd>admin1</userPasswd></user>";

        System.out.println(postSOAP(url, xmlStr));

    }

}
