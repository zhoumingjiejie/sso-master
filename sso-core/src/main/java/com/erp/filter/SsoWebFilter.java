package com.erp.filter;

import com.erp.config.SpringContextHolder;
import com.erp.conf.Conf;
import com.erp.config.WebProperties;
import com.erp.helper.SsoWebLoginHelper;
import com.erp.model.SsoUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.filter.SsoWebFilter]
 * @Description web登录, 监听请求是否放行
 * @Date 2020/3/25 0:04
 */
@Slf4j
@SuppressWarnings("all")
public class SsoWebFilter extends HttpServlet implements Filter {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 获取 url
        String servletPath = req.getServletPath();
        log.debug("[{}] 拦截请求：{}", this.getClass().getSimpleName(), servletPath);

        //获取applicable.yml的配置常量
        WebProperties webProperties = SpringContextHolder.getBean("webProperties");
        if (webProperties == null) {
            log.debug("applicable.yml文件未配置单点登录信息");
            return;
        }
        Map<String, String> maps = webProperties.getMaps();
        String ssoServer = maps.get("sso.server");
        String logoutPath = maps.get("sso.logoutpath");
        String excludedPaths = maps.get("sso.excludedpaths");

        // 放行请求路径检查
        if (excludedPaths != null && excludedPaths.trim().length() > 0) {
            for (String excludedPath : excludedPaths.split(",")) {
                String uriPattern = excludedPath.trim();

                // 支持ANT表达式，判断当前的servletPath url是否在放行路径数组内
                if (ANT_PATH_MATCHER.match(uriPattern, servletPath)) {
                    // 放行servletPath访问路径
                    chain.doFilter(request, response);
                    return;
                }

            }
        }

        // 注销登录请求检查
        if (logoutPath != null
                && logoutPath.trim().length() > 0
                && logoutPath.equals(servletPath)) {

            // remove cookie
            SsoWebLoginHelper.removeSessionIdByCookie(req, res);

            // 重定向到单点登录的 /logout服务，移除用户登录状态的cookie和redis，然后跳转到login页面
            String logoutPageUrl = ssoServer.concat(Conf.SSO_LOGOUT);
            res.sendRedirect(logoutPageUrl);

            return;
        }

        // 检查用户登录状态, cookie + redirect
        SsoUser ssoUser = SsoWebLoginHelper.loginCheck(req, res);

        // valid login fail，登录失败返回错误信息，或者重定向到登录页面
        if (ssoUser == null) {

            String header = req.getHeader("content-type");
            boolean isJson = header != null && header.contains("json");
            if (isJson) {

                // json msg
                res.setContentType("application/json;charset=utf-8");
                res.getWriter().println("{\"code\":" + Conf.SSO_LOGIN_FAIL_RESULT.getCode() + ", \"msg\":\"" + Conf.SSO_LOGIN_FAIL_RESULT.getMsg() + "\"}");
                log.debug("登录失败-返回错误信息：{}", res);
                return;
            } else {

                // total link
                String link = req.getRequestURL().toString();

                // redirect to login page
                String loginPageUrl = ssoServer.concat(Conf.SSO_LOGIN)
                        + "?" + Conf.REDIRECT_URL + "=" + link;

                log.debug("登录失败-重定向url：{}", loginPageUrl);
                res.sendRedirect(loginPageUrl);
                return;
            }

        }

        // 请求设置 用户信息 sso user
        request.setAttribute(Conf.SSO_USER, ssoUser);

        // 已登录的请求进行放行
        chain.doFilter(request, response);
    }
}
