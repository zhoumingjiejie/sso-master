package com.erp.service;

import com.erp.model.ReturnT;
import com.erp.model.beans.UserInfo;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.service.impl.UserService]
 * @Description 用户service接口
 * @Date 2020/3/24 10:56
 */
public interface UserService {
    /**
     * 获取用户信息
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息
     */
    ReturnT<UserInfo> findUser(String userName, String password);
}
