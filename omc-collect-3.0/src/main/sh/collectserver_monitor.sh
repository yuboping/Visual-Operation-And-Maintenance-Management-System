#!/bin/bash
LANG=@LANG@
export LANG

##crontab 定时监控进程是否存在
## * * * * * cd ${WORK_HOME}/sh;sh collectserver_monitor.sh

WORK_HOME=@server_work_home@
LOG_HOME=${WORK_HOME}/logs
RunFlag='target=@collectServerTarget@';

checkprocess=`ps -ef | grep $RunFlag | grep -v "grep" `
if [ -z "$checkprocess" ] ; then
    sh $WORK_HOME/sh/collectserverstart.sh;
    time=`date +%Y%m%d%H%M%S`;
    echo $time"restart service">> $LOG_HOME/monitor.log
fi