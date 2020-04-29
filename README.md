#特性
1、简洁：API直观简洁，可快速上手
2、轻量级：环境依赖小，部署与接入成本较低
3、单点登录：只需要登录一次就可以访问所有相互信任的应用系统
4、分布式：接入SSO认证中心的应用，支持分布式部署
5、HA：Server端与Client端，均支持集群部署，提高系统可用性
6、跨域：支持跨域应用接入SSO认证中心
7、Cookie+Token均支持：支持基于Cookie和基于Token两种接入方式，并均提供Sample项目
8、Web+APP均支持：支持Web和APP接入
9、实时性：系统登陆、注销状态，全部Server与Client端实时共享
10、CS结构：基于CS结构，包括Server”认证中心”与Client”受保护应用”
11、记住密码：未记住密码时，关闭浏览器则登录态失效；记住密码时，支持登录态自动延期，在自定义延期时间的基础上，原则上可以无限延期
12、路径排除：支持自定义多个排除路径，支持Ant表达式,用于排除SSO客户端不需要过滤的路径

#1 环境要求
1、安装redis

#2 模块介绍
- sso-server：中央认证服务，支持集群
- sso-core：Client端依赖
- sso-admin：单点登陆Client端接入示例项目

#3 使用说明
- 1、application-api.yml 文件声明单点登录服务
- 2、添加SsoConfig配置

#4 账号验证
sso-server模块UserServiceImpl中，默认使用user、123456测试