package com.tpy.etlinterfacetestserver.controller;

import com.tpy.etlinterfacetestserver.api.ResponseDate;
import com.tpy.etlinterfacetestserver.entity.User;
import com.tpy.etlinterfacetestserver.utils.ResponseDateUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class LoginController {

    private static Log log = LogFactory.getLog(LoginController.class);

    @PostMapping("/login")
    @ResponseBody
    public ResponseDate login(@RequestBody User user,int page,int rows){

        log.info(user.toString()+"=="+page+"=="+rows);

        String username = user.getUsername();
        String userPwd = user.getUserPwd();

        if("admin".equals(username) && "admin1".equals(userPwd)){
            return ResponseDateUtils.getSuccessDate("登录成功");
        }

        return ResponseDateUtils.getFailureDate("账号密码错误");
    }

}
