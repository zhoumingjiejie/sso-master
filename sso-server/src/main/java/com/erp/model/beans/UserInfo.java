package com.erp.model.beans;

import lombok.Data;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.model.beans.UserInfo]
 * @Description 用户信息
 * @Date 2020/3/24 10:55
 */
@Data
public class UserInfo {
    private long userId;
    private String userName;
    private String password;
}
