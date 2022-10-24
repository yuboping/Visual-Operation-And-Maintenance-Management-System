# 宽带业务分析系统3.0

--------------

## Requirements

* jdk 1.7+
* spring 3.2.11

--------------

## 项目构建

开发部署测试请编辑环境文件[config.groovy](env/config.groovy)文件中的环境变量。
然后执行
>    `gradle -Penv=dev clean build`

自动部署执行

>    `gradle -Penv=dev clean build deploy`

默认项目发布到`${prefix}`下


--------------

## 已完成的功能


--------------

## 待实现功能
高版本mysql：nmct、xjcu
[group: 'mysql', name: 'mysql-connector-java',version:'8.0.11']
低版本mysql
[group: 'mysql', name: 'mysql-connector-java',version:'5.1.31']
