package com.erp.exception;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.exception.SsoException]
 * @Description 单点登录异常类
 * @Date 2020/3/24 10:30
 */
@SuppressWarnings("unused")
public class SsoException extends RuntimeException {
    private static final long serialVersionUID = 42L;

    public SsoException(String msg) {
        super(msg);
    }

    public SsoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SsoException(Throwable cause) {
        super(cause);
    }
}
