package com.erp.conf;

import com.erp.model.ReturnT;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.conf.Conf]
 * @Description 单点登录常量
 * @Date 2020/3/24 12:17
 */
public class Conf {
    /**
     * sso sessionid, between browser and sso-server (web + token client)
     */
    public static final String SSO_SESSIONID = "sso_sessionid";

    /**
     * redirect url (web client)
     */
    public static final String REDIRECT_URL = "redirect_url";

    /**
     * sso user, request attribute (web client)
     */
    public static final String SSO_USER = "sso_user";


    /**
     * sso server address (web + token client)
     */
    public static final String SSO_SERVER = "sso_server";

    /**
     * login url, server relative path (web client)
     */
    public static final String SSO_LOGIN = "/login";
    /**
     * logout url, server relative path (web client)
     */
    public static final String SSO_LOGOUT = "/logout";


    /**
     * logout path, client relatice path
     */
    public static final String SSO_LOGOUT_PATH = "SSO_LOGOUT_PATH";

    /**
     * excluded paths, client relatice path, include path can be set by "filter-mapping"
     */
    public static final String SSO_EXCLUDED_PATHS = "SSO_EXCLUDED_PATHS";


    /**
     * login fail result
     */
    public static final ReturnT SSO_LOGIN_FAIL_RESULT = new ReturnT(501, "sso not login.");

}
