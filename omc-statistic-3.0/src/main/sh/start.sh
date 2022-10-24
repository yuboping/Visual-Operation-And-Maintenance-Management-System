#!/bin/bash
LANG=@sh.LANG@
export LANG

JAVA_HOME=@JAVA_HOME@
WORK_HOME=@prefix@
LOG_HOME=${WORK_HOME}/logs
LIB_HOME=${WORK_HOME}/libs
RunFlag='target=@sh.target@';

for jar in `ls ${LIB_HOME}/*.jar`
do
    CLASSPATH="$CLASSPATH":"$jar"
done

cd ${WORK_HOME}/sh
sh stop.sh
sleep 1

cd ${WORK_HOME}

nohup ${JAVA_HOME}/bin/java -D${RunFlag} -Xms256m -Xmx512m -classpath ${CLASSPATH} com.asiainfo.ais.omcstatistic.OmcStatisticApplication >/dev/null 2>&1 &

