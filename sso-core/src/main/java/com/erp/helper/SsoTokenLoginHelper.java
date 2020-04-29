package com.erp.helper;

import com.erp.conf.Conf;
import com.erp.model.SsoUser;
import com.erp.store.SsoLoginStore;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.helper.SsoTokenLoginHelper]
 * @Description Token登录助手（存储信息在Redis里面）
 * @Date 2020/3/24 14:47
 */
@SuppressWarnings("all")
public class SsoTokenLoginHelper {
    /**
     * 登录
     *
     * @param sessionId sessionId
     * @param ssoUser   用户对象
     */
    public static void login(String sessionId, SsoUser ssoUser) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
        }

        SsoLoginStore.setSsoUser(storeKey, ssoUser);
    }

    /**
     * 注销登录
     *
     * @param sessionId sessionId
     */
    public static void logout(String sessionId) {
        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return;
        }
        SsoLoginStore.delSsoUser(storeKey);
    }

    /**
     * 注销登录
     *
     * @param request 请求
     */
    public static void logout(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        logout(headerSessionId);
    }


    /**
     * 登录状态检查
     *
     * @param sessionId sessionId
     * @return 用户对象
     */
    public static SsoUser loginCheck(String sessionId) {

        String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
        if (storeKey == null) {
            return null;
        }
        SsoUser ssoUser = SsoLoginStore.getSsoUser(storeKey);
        if (ssoUser != null) {
            String version = SsoSessionIdHelper.parseVersion(sessionId);
            if (ssoUser.getVersion().equals(version)) {

                // （过期时间已经过了一半后,自动刷新;）
                if ((System.currentTimeMillis() - ssoUser.getExpireFreshTime()) > ssoUser.getExpireMinute() / 2) {
                    ssoUser.setExpireFreshTime(System.currentTimeMillis());
                    SsoLoginStore.setSsoUser(storeKey, ssoUser);
                }
                return ssoUser;
            }
        }
        return null;
    }


    /**
     * 登录状态检查
     *
     * @param request 请求
     * @return 用户对象信息
     */
    public static SsoUser loginCheck(HttpServletRequest request) {
        String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
        return loginCheck(headerSessionId);
    }
}
