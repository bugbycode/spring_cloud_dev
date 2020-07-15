# spring_cloud_dev
Spring cloud Security Oauth2 本地事务、分布式事务练习

项目说明：

1、fort-commons、fort-*-commons为公共依赖包

2、fort-*-service 为服务提供者

微服务说明：

1、fort-discovery-service 为Eureka注册中心 其余的fort-*-service为Eureka客户端微服务

2、fort-oauth2-service 为身份认证中心 使用spring security oauth2框架实现

3、项目中所有的配置统一使用配置中心微服务（fort-config-service）管理

4、分布式事务使用seata实现

本项目为前后端分离项目的服务端。

项目启动顺序说明：

1、启动注册中心微服务（fort-discovery-service）

2、启动配置中心微服务（fort-config-service）

3、启动其他微服务

接口响应状态码说明：

200 成功

400 参数未通过校验

401 认证失败

403 权限不足

404 找不到请求的资源

500 服务器内部错误
