package com.erp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.model.result.ReturnT]
 * @Description 返回结果对象
 * @Date 2020/3/24 10:22
 */
@SuppressWarnings("all")
@Data
public class ReturnT<T> implements Serializable {
    public static final long serialVersionUID = 42L;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;
    public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
    public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);

    private int code;
    private String msg;
    private T data;

    public ReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnT(T data) {
        this.code = SUCCESS_CODE;
        this.data = data;
    }
}
