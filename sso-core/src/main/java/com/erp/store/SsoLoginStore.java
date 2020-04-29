package com.erp.store;

import com.erp.conf.Conf;
import com.erp.model.SsoUser;
import com.erp.utils.RedisUtil;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.store.SsoLoginStore]
 * @Description 单点登录商店
 * @Date 2020/3/24 11:28
 */
@SuppressWarnings("unused")
public class SsoLoginStore {

    /**
     * redis有效时间 1440 minute, 24 hour
     */
    private static int redisExpireMinute = 1440;

    public static void setRedisExpireMinute(int redisExpireMinute) {
        int m = 30;
        if (redisExpireMinute < m) {
            redisExpireMinute = m;
        }
        SsoLoginStore.redisExpireMinute = redisExpireMinute;
    }

    public static int getRedisExpireMinute() {
        return redisExpireMinute;
    }

    /**
     * 获取用户信息
     *
     * @param storeKey 商店key
     * @return 用户信息
     */
    public static SsoUser getSsoUser(String storeKey) {
        Object o = RedisUtil.getInstance().get(storeKey);
        return o == null ? null : (SsoUser) o;
    }

    /**
     * 设置用户信息
     *
     * @param storeKey 商店key
     * @param ssoUser  用户信息
     */
    public static void setSsoUser(String storeKey, SsoUser ssoUser) {
        //分转秒
        RedisUtil.getInstance().set(storeKey, ssoUser, redisExpireMinute * 60);
    }

    /**
     * 删除用户信息
     *
     * @param storeKey 商店key
     */
    public static void delSsoUser(String storeKey) {
        RedisUtil.getInstance().del(storeKey);
    }

    /**
     * 获取商店key
     *
     * @param sessionId 用户session id
     * @return 返回商店key
     */
    private static String getStoreKey(String sessionId) {
        return Conf.SSO_SESSIONID.concat("#").concat(sessionId);
    }
}
