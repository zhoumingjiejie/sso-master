package com.erp.controller;

import com.erp.conf.Conf;
import com.erp.helper.SsoSessionIdHelper;
import com.erp.helper.SsoWebLoginHelper;
import com.erp.model.ReturnT;
import com.erp.model.SsoUser;
import com.erp.model.beans.UserInfo;
import com.erp.service.UserService;
import com.erp.store.SsoLoginStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.controller.WebController]
 * @Description web登录控制器(cookie 登录方式)
 * @Date 2020/3/24 11:04
 */
@Slf4j
@Controller
public class WebController {
    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 检查登录状态
        SsoUser ssoUser = SsoWebLoginHelper.loginCheck(request, response);

        if (ssoUser == null) {
            //重定向登录页面
            return "redirect:/login";
        } else {
            //已登录则跳转到index页面
            model.addAttribute("ssoUser", ssoUser);
            return "index";
        }
    }

    /**
     * 登录页面
     *
     * @param model   模型
     * @param request 请求
     * @return templates
     */
    @RequestMapping(Conf.SSO_LOGIN)
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

        log.debug("sso单点登录Controller");

        //检查登录状态
        SsoUser ssoUser = SsoWebLoginHelper.loginCheck(request, response);

        if (ssoUser != null) {

            // 成功重定向登录，默认index页面
            String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
            if (redirectUrl != null && redirectUrl.trim().length() > 0) {
                String sessionId = SsoWebLoginHelper.getSessionIdByCookie(request);
                String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
                return "redirect:" + redirectUrlFinal;
            } else {
                return "redirect:/";
            }
        }
        model.addAttribute("errorMsg", request.getParameter("errorMsg"));
        model.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return "login";
    }

    /**
     * 登陆
     *
     * @param request            请求
     * @param redirectAttributes 重定向属性
     * @param username           用户名
     * @param password           密码
     * @return 重定向页面
     */
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request,
                          HttpServletResponse response,
                          RedirectAttributes redirectAttributes,
                          String username,
                          String password,
                          String ifRemember) {

        boolean ifRem = "on".equals(ifRemember);

        //账号密码登录和数据库对比
        ReturnT<UserInfo> result = this.userService.findUser(username, password);
        if (result.getCode() != ReturnT.SUCCESS_CODE) {
            redirectAttributes.addAttribute("errorMsg", result.getMsg());
            redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
            return "redirect:/login";
        }

        // 1、构建ssoUser信息
        SsoUser ssoUser = new SsoUser();
        ssoUser.setUserId(String.valueOf(result.getData().getUserId()));
        ssoUser.setUsername(result.getData().getUserName());
        ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        ssoUser.setExpireMinute(SsoLoginStore.getRedisExpireMinute());
        ssoUser.setExpireFreshTime(System.currentTimeMillis());


        // 2、创建 session id
        String sessionId = SsoSessionIdHelper.makeSessionId(ssoUser);

        // 3、存储登录信息, store storeKey + cookie sessionId
        SsoWebLoginHelper.login(response, sessionId, ssoUser, ifRem);

        // 4、返回, redirect sessionId
        String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
        if (redirectUrl != null && redirectUrl.trim().length() > 0) {
            String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSIONID + "=" + sessionId;
            return "redirect:" + redirectUrlFinal;
        } else {
            return "redirect:/";
        }

    }

    /**
     * 注销登录
     *
     * @param request            请求
     * @param redirectAttributes 重定向属性
     * @return 跳至登录页面
     */
    @RequestMapping(Conf.SSO_LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        // logout
        SsoWebLoginHelper.logout(request, response);

        redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return "redirect:/login";
    }
}
