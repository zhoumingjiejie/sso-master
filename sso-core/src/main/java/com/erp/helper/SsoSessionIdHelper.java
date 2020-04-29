package com.erp.helper;

import com.erp.model.SsoUser;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.store.SsoSessionIdHelper]
 * @Description 单点登录session助手，创建和解析sessionId
 * @Date 2020/3/24 14:05
 */
@SuppressWarnings("all")
public class SsoSessionIdHelper {
    /**
     * 构建客户端sessionId
     *
     * @param ssoUser 单点登录user对象
     * @return sessionId = userId_version
     */
    public static String makeSessionId(SsoUser ssoUser) {
        return ssoUser.getUserId().concat("_").concat(ssoUser.getVersion());
    }

    /**
     * 解析sessionId获取商店key（userId = storeKey)
     *
     * @param sessionId 用户session
     * @return 用户id
     */
    public static String parseStoreKey(String sessionId) {
        final String underline = "_";
        if (sessionId != null && sessionId.contains(underline)) {
            String[] sessionIdArr = sessionId.split(underline);
            if (sessionIdArr.length == 2
                    && sessionIdArr[0] != null
                    && sessionIdArr[0].trim().length() > 0) {
                return sessionIdArr[0].trim();
            }
        }
        return null;
    }

    /**
     * 解析sessionId获取version
     *
     * @param sessionId 用户session
     * @return 用户登录版本
     */
    public static String parseVersion(String sessionId) {
        final String underline = "_";
        if (sessionId != null && sessionId.contains(underline)) {
            String[] sessionIdArr = sessionId.split(underline);
            if (sessionIdArr.length == 2
                    && sessionIdArr[1] != null
                    && sessionIdArr[1].trim().length() > 0) {
                return sessionIdArr[1].trim();
            }
        }
        return null;
    }
}
