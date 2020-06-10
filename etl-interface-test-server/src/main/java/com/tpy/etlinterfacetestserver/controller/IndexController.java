package com.tpy.etlinterfacetestserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("index")
    public String index(){
        return "index";
    }

    @GetMapping("test")
    @ResponseBody
    public String test(){
        return "hello";
    }
}
