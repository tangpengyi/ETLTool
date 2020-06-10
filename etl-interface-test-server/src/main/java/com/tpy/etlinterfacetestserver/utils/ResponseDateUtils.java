package com.tpy.etlinterfacetestserver.utils;

import com.tpy.etlinterfacetestserver.api.ResponseDate;

public class ResponseDateUtils {

    private static ResponseDate responseDate = new ResponseDate();

    public static ResponseDate getSuccessDate(Object date){
        responseDate.setCode(1);
        responseDate.setDate(date);
        responseDate.setMsg("请求成功");
        return responseDate;
    }

    public static ResponseDate getFailureDate(String msg){
        responseDate.setCode(0);
        responseDate.setDate(null);
        responseDate.setMsg(msg);
        return responseDate;
    }
}
