package com.erp.controller;

import com.erp.conf.Conf;
import com.erp.helper.SsoSessionIdHelper;
import com.erp.helper.SsoTokenLoginHelper;
import com.erp.model.ReturnT;
import com.erp.model.SsoUser;
import com.erp.model.beans.UserInfo;
import com.erp.service.UserService;
import com.erp.store.SsoLoginStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.controller.AppController]
 * @Description app登录控制器（ Token 单点登录方式）
 * @Date 2020/3/24 11:06
 */
@Slf4j
@Controller
@RequestMapping("app")
public class AppController {
    private final UserService userService;

    public AppController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Login
     *
     * @param username 用户名
     * @param password 密码
     * @return sessionId
     */
    @RequestMapping(Conf.SSO_LOGIN)
    @ResponseBody
    public ReturnT<String> login(String username, String password) {

        // valid login
        ReturnT<UserInfo> result = this.userService.findUser(username, password);
        if (result.getCode() != ReturnT.SUCCESS_CODE) {
            return new ReturnT<>(result.getCode(), result.getMsg());
        }

        // 1、make xxl-sso user
        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserId(String.valueOf(result.getData().getUserId()));
        ssoUser.setUsername(result.getData().getUserName());
        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        ssoUser.setExpireMinute(SsoLoginStore.getRedisExpireMinute());
        ssoUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、生成 sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);

        // 3、login, store storeKey
        SsoTokenLoginHelper.login(sessionId, ssoUser);

        // 4、return sessionId
        return new ReturnT<>(sessionId);
    }


    /**
     * 注销登录
     *
     * @param sessionId sessionId
     * @return 注销结果
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ReturnT<String> logout(String sessionId) {
        // logout, remove storeKey
        SsoTokenLoginHelper.logout(sessionId);
        return ReturnT.SUCCESS;
    }

    /**
     * 登录状态检查
     *
     * @param sessionId sessionId
     * @return 返回登录用户信息
     */
    @RequestMapping("/loginCheck")
    @ResponseBody
    public ReturnT<SsoUser> loginCheck(String sessionId) {

        // logout
        SsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "sso not login.");
        }
        return new ReturnT<>(xxlUser);
    }
}
