package com.erp.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.utils.CookieUtil]
 * @Description cookie工具类
 * @Date 2020/3/24 14:23
 */
@SuppressWarnings("all")
public class CookieUtil {
    // 默认缓存时间,单位/秒, 2H
    private static final int COOKIE_MAX_AGE = Integer.MAX_VALUE;
    // 保存路径,根路径
    private static final String COOKIE_PATH = "/";

    /**
     * 保存
     *
     * @param response   响应
     * @param key        key
     * @param value      value
     * @param ifRemember 是否记住
     */
    public static void set(HttpServletResponse response, String key, String value, boolean ifRemember) {
        int age = ifRemember ? COOKIE_MAX_AGE : -1;
        set(response, key, value, null, COOKIE_PATH, age, true);
    }

    /**
     * 保存
     *
     * @param response   响应
     * @param key        key
     * @param value      value
     * @param domain     域名
     * @param path       uri路径
     * @param maxAge     最大期限
     * @param isHttpOnly 默认false，true不暴露给客户端脚本代码
     */
    private static void set(HttpServletResponse response, String key, String value, String domain, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    /**
     * 查询value
     *
     * @param request 响应
     * @param key     key
     * @return value
     */
    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 查询Cookie
     *
     * @param request 响应
     * @param key     key
     * @return cookie对象
     */
    private static Cookie get(HttpServletRequest request, String key) {
        Cookie[] arrCookie = request.getCookies();
        if (arrCookie != null && arrCookie.length > 0) {
            for (Cookie cookie : arrCookie) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 删除Cookie
     *
     * @param request  请求
     * @param response 响应
     * @param key      key
     */
    public static void remove(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            set(response, key, "", null, COOKIE_PATH, 0, true);
        }
    }
}
