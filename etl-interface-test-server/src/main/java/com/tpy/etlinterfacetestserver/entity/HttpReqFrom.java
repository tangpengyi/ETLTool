package com.tpy.etlinterfacetestserver.entity;

import java.util.List;

public class HttpReqFrom {

    private String reqStyle;

    private String reqUrl;

    private boolean isScheduledTasks;

    private String startTime;

    private String endTime;

    private String postReqContext;

    private String xmlReqContext;

    private String activeName;

    private List<ReqFrom> reqParam;

    public void setReqParam(List<ReqFrom> reqParam) {
        this.reqParam = reqParam;
    }

    public HttpReqFrom() {
    }

    public HttpReqFrom(String reqStyle, String reqUrl, boolean isScheduledTasks, String startTime, String endTime, String postReqContext, String xmlReqContext, String activeName, List<ReqFrom> reqParam) {
        this.reqStyle = reqStyle;
        this.reqUrl = reqUrl;
        this.isScheduledTasks = isScheduledTasks;
        this.startTime = startTime;
        this.endTime = endTime;
        this.postReqContext = postReqContext;
        this.xmlReqContext = xmlReqContext;
        this.activeName = activeName;
        this.reqParam = reqParam;
    }


    class ReqFrom{

        String paramName;
        String paramValue;

        public ReqFrom() {
        }

        public ReqFrom(String paramName, String paramValue) {
            this.paramName = paramName;
            this.paramValue = paramValue;
        }

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public String getParamValue() {
            return paramValue;
        }

        public void setParamValue(String paramValue) {
            this.paramValue = paramValue;
        }

        @Override
        public String toString() {
            return "ReqFrom{" +
                    "paramName='" + paramName + '\'' +
                    ", paramValue='" + paramValue + '\'' +
                    '}';
        }
    }

    public String getReqStyle() {
        return reqStyle;
    }

    public void setReqStyle(String reqStyle) {
        this.reqStyle = reqStyle;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public boolean isScheduledTasks() {
        return isScheduledTasks;
    }

    public void setScheduledTasks(boolean scheduledTasks) {
        isScheduledTasks = scheduledTasks;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPostReqContext() {
        return postReqContext;
    }

    public void setPostReqContext(String postReqContext) {
        this.postReqContext = postReqContext;
    }

    public String getXmlReqContext() {
        return xmlReqContext;
    }

    public void setXmlReqContext(String xmlReqContext) {
        this.xmlReqContext = xmlReqContext;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public List<ReqFrom> getReqParam() {
        return reqParam;
    }


    @Override
    public String toString() {
        return "HttpReqFrom{" +
                "reqStyle='" + reqStyle + '\'' +
                ", reqUrl='" + reqUrl + '\'' +
                ", isScheduledTasks=" + isScheduledTasks +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", postReqContext='" + postReqContext + '\'' +
                ", xmlReqContext='" + xmlReqContext + '\'' +
                ", activeName='" + activeName + '\'' +
                ", reqParam=" + reqParam +
                '}';
    }
}
