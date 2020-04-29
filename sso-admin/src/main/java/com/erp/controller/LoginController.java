package com.erp.controller;

import com.erp.conf.Conf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.controller.loginController]
 * @Description 登录控制器
 * @Date 2020/3/25 10:35
 */
@Slf4j
@Controller
public class LoginController {

    @RequestMapping(Conf.SSO_LOGIN)
    @ResponseBody
    public void login() {
        log.debug("登录请求监听：{}", new Date());
    }
}
