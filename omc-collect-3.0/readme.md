# 宽带业务分析系统-多维度数据采集

--------------

## Requirements

* jdk 1.8+

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

java进程，起多个线程执行特定目录下的多维度数据采集脚本，并解析返回结果，把解析后的数据插入指标记录表
