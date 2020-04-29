package com.erp.controller.resolver;

import com.erp.exception.SsoException;
import com.erp.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一异常处理（Controller切面方式实现）
 * <p>
 * 1、@ControllerAdvice：扫描所有Controller；
 * 2、@ControllerAdvice(annotations=RestController.class)：扫描指定注解类型的Controller；
 * 3、@ControllerAdvice(basePackages={"com.aaa","com.bbb"})：扫描指定package下的Controller
 *
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.resolver.WebExceptionResolver]
 * @Description 统一异常处理
 * @Date 2020/3/24 10:11
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class WebExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error(e.getMessage(), e);

        // if json
        boolean isJson = false;
        HandlerMethod method = (HandlerMethod) o;
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        if (responseBody != null) {
            isJson = true;
        }

        // error result
        ReturnT<String> errorResult;
        if (e instanceof SsoException) {
            errorResult = new ReturnT<>(ReturnT.FAIL_CODE, e.getMessage());
        } else {
            errorResult = new ReturnT<>(ReturnT.FAIL_CODE, e.toString().replaceAll("\n", "<br/>"));
        }

        // response
        ModelAndView mv = new ModelAndView();
        if (isJson) {
            try {
                httpServletResponse.setContentType("application/json;charset=utf-8");
                httpServletResponse.getWriter().print("{\"code\":" + errorResult.getCode() + ", \"msg\":\"" + errorResult.getMsg() + "\"}");
            } catch (IOException ioe) {
                log.error(ioe.getMessage(), ioe);
            }
            return mv;
        } else {

            mv.addObject("exceptionMsg", errorResult.getMsg());
            mv.setViewName("/common/common.exception");
            return mv;
        }
    }
}
