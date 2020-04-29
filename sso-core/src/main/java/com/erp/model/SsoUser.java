package com.erp.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.model.SsoUser]
 * @Description 单点登录对象
 * @Date 2020/3/24 11:10
 */
@Data
public class SsoUser implements Serializable {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户登录版本
     */
    private String version;

    /**
     * 有时间（默认：24H）
     */
    private int expireMinute;
    /**
     * （从这个时间开始，24H后，身份凭证失效，redis移除信息）
     */
    private long expireFreshTime;
}
