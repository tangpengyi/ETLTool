package com.tpy.etlinterfacetestserver.task;

import com.tpy.etlinterfacetestserver.api.ResponseDate;
import com.tpy.etlinterfacetestserver.config.SchedulerConfig;
import com.tpy.etlinterfacetestserver.utils.UtilTools;
import com.tpy.etlinterfacetestserver.utils.webSocketUtils.WebSocketUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.tpy.etlinterfacetestserver.utils.Commons.getEncoding;

public class SocketReqJob implements Job {

    private static SchedulerConfig schedulerConfig;
    private static Log log = LogFactory.getLog(SocketReqJob.class);
    private static Map<String, Object> formMap;
    private static String startTimeStr = "";
    private static String endTimeStr = "";

    public static void setFormMap(Map<String, Object> map,SchedulerConfig config){
        formMap = map;
        schedulerConfig = config;
        startTimeStr = (String) map.get("startTime")+":00";
        endTimeStr = (String) map.get("endTime")+":00";
        log.info(map.toString());
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("socket 测试");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = df.format(new Date());

        log.info("开始时间："+startTimeStr+"===现在时间:"+format+"====停止时间："+endTimeStr);

        String[] date = format.split(" ");

        try {
            Date nowTime = df.parse(format);
            Date startTime = df.parse(startTimeStr);
            Date endTime = df.parse(endTimeStr);

            if (nowTime.getTime() >= startTime.getTime() && nowTime.getTime() <= endTime.getTime()) {

                String url = (String) formMap.get("reqUrl");
                ResponseDate responseDate = WebSocketUtils.sendMsg((String) formMap.get("sendMsg"));

                UtilTools.writeFile(date[0] + "socketLog.txt",formMap.get("reqUrl")+"--"+format);
                UtilTools.writeFile(date[0] + "socketLog.txt","参数："+formMap.get("sendMsg"));
                UtilTools.writeFile(date[0] + "socketLog.txt","返回结果："+responseDate.toString());
                UtilTools.writeFile(date[0] + "socketLog.txt"," ");

                log.info("Socket Requset 结果打印到文件===" + responseDate.toString());
            } else if (nowTime.getTime() > endTime.getTime()) {
                schedulerConfig.deleteJob("socketReqJob","socketJobGroup");
                log.info("结束 Socket Requset 任务");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SchedulerException e){
            e.printStackTrace();
        }

    }
}
