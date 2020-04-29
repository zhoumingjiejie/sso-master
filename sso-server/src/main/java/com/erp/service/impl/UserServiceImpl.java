package com.erp.service.impl;

import com.erp.model.ReturnT;
import com.erp.model.beans.UserInfo;
import com.erp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.service.impl.UserServiceImpl]
 * @Description 用户信息实现类
 * @Date 2020/3/24 10:59
 */
@Service
public class UserServiceImpl implements UserService {

    private static List<UserInfo> mockUserList = new ArrayList<>();

    static {
        for (int i = 0; i < 5; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(1000 + i);
            userInfo.setUserName("user" + (i > 0 ? String.valueOf(i) : ""));
            userInfo.setPassword("123456");
            mockUserList.add(userInfo);
        }
    }

    @Override
    public ReturnT<UserInfo> findUser(String userName, String password) {
        if (userName == null || userName.trim().length() == 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "Please input username.");
        }
        if (password == null || password.trim().length() == 0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "Please input password.");
        }

        //根据密码，账号查询用户，返回用户信息
        // mock user
        for (UserInfo mockUser : mockUserList) {
            if (mockUser.getUserName().equals(userName) && mockUser.getPassword().equals(password)) {
                return new ReturnT<>(mockUser);
            }
        }

        return new ReturnT<>(new UserInfo());
    }
}
