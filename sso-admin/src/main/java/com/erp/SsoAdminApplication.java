package com.erp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 0.0.0
 */
@Slf4j
@SpringBootApplication
public class SsoAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoAdminApplication.class, args);
        log.debug("sso-admin服务启动成功！温馨提示：代码千万行，注释第一行，命名不规范，同事泪两行");
    }

}
