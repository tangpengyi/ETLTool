package com.tpy.etlinterfacetestserver.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tpy.etlinterfacetestserver.api.ResponseDate;
import com.tpy.etlinterfacetestserver.config.SchedulerConfig;
import com.tpy.etlinterfacetestserver.task.HttpGetJob;
import com.tpy.etlinterfacetestserver.task.SocketReqJob;
import com.tpy.etlinterfacetestserver.utils.Commons;
import com.tpy.etlinterfacetestserver.utils.webSocketUtils.WebSocketUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/test")
public class ReqTestController {

    private static Log log = LogFactory.getLog(ReqTestController.class);

    @Autowired
    private SchedulerConfig schedulerConfig;

    @PostMapping("/getReqTest")
    @ResponseBody
    public ResponseDate getReqTest(@RequestBody Map<String, Object> reqFrom) throws SchedulerException {
        Map<String, Object> reqFrom1 = (Map<String, Object>) reqFrom.get("reqFrom");
        return Commons.reqHttp(reqFrom1,schedulerConfig);
    }

    @PostMapping("/reqSoap")
    @ResponseBody
    public ResponseDate reqSoap(@RequestBody Map<String, Object> reqFrom) throws UnsupportedEncodingException {
        Map<String, Object> paramMap = (Map<String, Object>) reqFrom.get("reqFrom");
        return Commons.reqSoap(paramMap,schedulerConfig);
    }

    @PostMapping("/socketConnection")
    @ResponseBody
    public ResponseDate reqSocket(@RequestBody Map<String, Object> reqFrom) throws ParseException {
        Map<String, Object> formMap = (Map<String, Object>) reqFrom.get("reqFrom");
        return WebSocketUtils.connection((String) formMap.get("reqUrl"));
    }

    @GetMapping("/disconnectSocket")
    @ResponseBody
    public ResponseDate disconnectSocket() throws ParseException {
        return WebSocketUtils.disconnect();
    }

    @PostMapping("/socketSendMsg")
    @ResponseBody
    public ResponseDate socketSendMsg(@RequestBody Map<String, Object> reqFrom) throws ParseException, SchedulerException {

        Map<String, Object> formMap = (Map<String, Object>) reqFrom.get("reqFrom");
        Boolean isScheduledTasks = (Boolean) formMap.get("isScheduledTasks");
        ResponseDate responseDate = WebSocketUtils.sendMsg((String) formMap.get("sendMsg"));
        if(isScheduledTasks == true){
            SocketReqJob.setFormMap(formMap,schedulerConfig);
            schedulerConfig.startSocketJob();
        }
        return responseDate;
    }

}
