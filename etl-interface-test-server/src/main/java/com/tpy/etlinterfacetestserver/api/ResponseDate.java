package com.tpy.etlinterfacetestserver.api;

public class ResponseDate {

    private String msg;

    private int code;

    private Object date;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ResponseDate{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", date=" + date +
                '}';
    }
}
