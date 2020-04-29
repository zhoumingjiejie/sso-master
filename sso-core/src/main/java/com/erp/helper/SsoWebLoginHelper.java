package com.erp.helper;

import com.erp.conf.Conf;
import com.erp.model.SsoUser;
import com.erp.store.SsoLoginStore;
import com.erp.utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.helper.SsoWebLoginHelper]
 * @Description web登录助手（存储登录信息在cookie里面）
 * @Date 2020/3/24 14:18
 */
@SuppressWarnings("all")
public class SsoWebLoginHelper {
    /**
     * 客户登录
     *
     * @param response   响应
     * @param sessionId  用户身份
     * @param ifRemember true：cookie不过期，false：浏览器关闭时过期（服务器cookie）
     * @param ssoUser    用户对象（基础信息）
     */
    public static void login(HttpServletResponse response,
                             String sessionId, SsoUser ssoUser,
                             boolean ifRemember) {

        //解析sessionId获取storeKey
        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }
        //调用商店类存入redis
        SsoLoginStore.setSsoUser(storeKey, ssoUser);
        //存入cookie
        CookieUtil.set(response, Conf.SSO_SESSIONID, sessionId, ifRemember);
    }

    /**
     * 客户注销登录
     *
     * @param request  请求
     * @param response 响应
     */
    public static void logout(HttpServletRequest request,
                              HttpServletResponse response) {

        //从cookie获取sessionId
        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
        if (cookieSessionId == null) {
            return;
        }
        String storeKey = SsoSessionIdHelper.parseStoreKey(cookieSessionId);
        if (storeKey != null) {
            //移除redis登录信息
            SsoLoginStore.delSsoUser(storeKey);
        }

        //移除cookie登录信息
        CookieUtil.remove(request, response, Conf.SSO_SESSIONID);
    }


    /**
     * 登录检查
     *
     * @param request  请求
     * @param response 响应
     * @return 用户信息
     */
    public static SsoUser loginCheck(HttpServletRequest request, HttpServletResponse response) {

        String cookieSessionId = CookieUtil.getValue(request, Conf.SSO_SESSIONID);

        //检查redis的登录状态
        SsoUser ssoUser = SsoTokenLoginHelper.loginCheck(cookieSessionId);
        if (ssoUser != null) {
            return ssoUser;
        }

        // 移除旧的cookie
        SsoWebLoginHelper.removeSessionIdByCookie(request, response);

        // 设置新的cookie更新生命周期
        String paramSessionId = request.getParameter(Conf.SSO_SESSIONID);
        ssoUser = SsoTokenLoginHelper.loginCheck(paramSessionId);
        if (ssoUser != null) {
            // 浏览器关闭时过期（客户端cookie）
            CookieUtil.set(response, Conf.SSO_SESSIONID, paramSessionId, false);
            return ssoUser;
        }
        return null;
    }


    /**
     * 通过请求的cookie移除sessionId
     *
     * @param request  请求
     * @param response 响应
     */
    public static void removeSessionIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, Conf.SSO_SESSIONID);
    }

    /**
     * 通过请求获取sessionid
     *
     * @param request 请求
     * @return value（sessionId）
     */
    public static String getSessionIdByCookie(HttpServletRequest request) {
        return CookieUtil.getValue(request, Conf.SSO_SESSIONID);
    }
}
