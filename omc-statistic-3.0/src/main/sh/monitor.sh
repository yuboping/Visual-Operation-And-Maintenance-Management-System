#!/bin/bash
LANG=@sh.LANG@
export LANG

WORK_HOME=@prefix@
LOG_HOME=@log.home@
RunFlag='target=@sh.target@';

checkprocess=`ps -ef | grep $RunFlag | grep -v "grep" `
if [ -z "$checkprocess" ] ; then
    sh $WORK_HOME/sh/start.sh;
    time=`date +%Y%m%d%H%M%S`;
    echo $time"restart service">> $LOG_HOME/monitor.log
fi