package com.tpy.etlinterfacetestserver.task;

import com.tpy.etlinterfacetestserver.config.SchedulerConfig;
import com.tpy.etlinterfacetestserver.utils.Commons;
import com.tpy.etlinterfacetestserver.utils.UtilTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class HttpPostJob implements Job {

    private static Log log = LogFactory.getLog(HttpPostJob.class);
    private static Map<String, Object> reqFrom = null;
    private static String startTimeStr = "";
    private static String endTimeStr = "";
    private static SchedulerConfig schedulerConfig;

    public static void setReqFrom(Map<String, Object> form,SchedulerConfig config){
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
                String s = Commons.reqPost(reqFrom);
                UtilTools.writeFile(date[0] + "httpPostLog.txt",reqFrom.get("reqUrl")+"--"+format);
                UtilTools.writeFile(date[0] + "httpPostLog.txt","参数:"+reqFrom.get("postReqContext"));
                UtilTools.writeFile(date[0] + "httpPostLog.txt","返回结果:"+s);
                UtilTools.writeFile(date[0] + "httpPostLog.txt"," ");
                log.info("Http Post 结果打印到文件===" + s);
            } else if (nowTime.getTime() > endTime.getTime()) {
                schedulerConfig.deleteJob("httpPostJob","httpJobGroup");
                log.info("结束 Http Post 任务");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SchedulerException e){
            e.printStackTrace();
        }

    }
}
