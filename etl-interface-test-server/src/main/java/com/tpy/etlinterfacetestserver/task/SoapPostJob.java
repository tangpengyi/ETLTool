package com.tpy.etlinterfacetestserver.task;

import com.tpy.etlinterfacetestserver.config.SchedulerConfig;
import com.tpy.etlinterfacetestserver.utils.UtilTools;
import com.tpy.etlinterfacetestserver.utils.httpClient.HttpClientUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.tpy.etlinterfacetestserver.utils.Commons.getEncoding;

public class SoapPostJob implements Job {

    private static SchedulerConfig schedulerConfig;
    private static Log log = LogFactory.getLog(HttpGetJob.class);
    private static Map<String, Object> reqFrom = null;
    private static String startTimeStr = "";
    private static String endTimeStr = "";


    public static void setReqFrom(Map<String, Object> form,SchedulerConfig config) {
        reqFrom = form;
        schedulerConfig = config;
        startTimeStr = (String) reqFrom.get("startTime")+":00";
        endTimeStr = (String) reqFrom.get("endTime")+":00";
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(new Date());

        log.info("开始时间："+startTimeStr+"===现在时间:"+format+"====停止时间："+endTimeStr);

        String[] date = format.split(" ");

        try {
            Date nowTime = df.parse(format);
            Date startTime = df.parse(startTimeStr);
            Date endTime = df.parse(endTimeStr);

            if (nowTime.getTime() >= startTime.getTime() && nowTime.getTime() <= endTime.getTime()) {

                String url = (String) reqFrom.get("reqUrl");
                String s = null;
                if ("second".equals((String) reqFrom.get("activeName"))) {
                    String jsonReqContext = HttpClientUtil.sendPostByJson(url, (String) reqFrom.get("jsonReqContext"), 1);
                    s = new String(jsonReqContext.getBytes(getEncoding(jsonReqContext)), "utf-8");
                } else if ("three".equals((String) reqFrom.get("activeName"))) {
                    s = HttpClientUtil.sendPostByXml(url, (String) reqFrom.get("xmlReqContext"), 1);
                }

                UtilTools.writeFile(date[0] + "soapPostLog.txt",reqFrom.get("reqUrl")+"--"+format);
                UtilTools.writeFile(date[0] + "soapPostLog.txt","参数："+reqFrom.get("xmlReqContext"));
                UtilTools.writeFile(date[0] + "soapPostLog.txt","返回结果："+s);
                UtilTools.writeFile(date[0] + "soapPostLog.txt"," ");

                log.info("Soap Post 结果打印到文件===" + s);
            } else if (nowTime.getTime() > endTime.getTime()) {
                schedulerConfig.deleteJob("soapPostJob","soapJobGroup");
                log.info("结束 Soap Post 任务");
            }



        } catch (ParseException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SchedulerException e){
            e.printStackTrace();
        }
    }
}
