#!/bin/bash
LANG=@sh.LANG@
export LANG
CYCLE_TIME=$1
JAVA_HOME=@JAVA_HOME@
WORK_HOME=@prefix@
LOG_HOME=@log.home@
LIB_HOME=${WORK_HOME}/libs
RunFlag='target=@sh.target@';

for jar in `ls ${LIB_HOME}/*.jar`
do
    CLASSPATH="$CLASSPATH":"$jar"
done

cd ${WORK_HOME}/sh
sh stop.sh
sleep 20

cd ${WORK_HOME}

nohup ${JAVA_HOME}/bin/java -D${RunFlag} -Xms256m -Xmx512m -classpath ${CLASSPATH} com.asiainfo.lcims.omc.alarm.business.MainService "$CYCLE_TIME" > ${LOG_HOME}/main.log 2>&1 & 

