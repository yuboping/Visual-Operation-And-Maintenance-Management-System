#!/bin/bash
LANG=@sh.LANG@
export LANG

JAVA_HOME=@JAVA_HOME@
WORK_HOME=@prefix@
LOG_HOME=@log.home@
LIB_HOME=${WORK_HOME}/libs
RunFlag='target=@sh.target@';

CLASSPATH="$WORK_HOME/config"

for jar in `ls ${LIB_HOME}/*.jar`
do
    CLASSPATH="$CLASSPATH":"$jar"
done

cd ${WORK_HOME}/sh
sh stop.sh
sleep 1

cd ${WORK_HOME}

nohup ${JAVA_HOME}/bin/java -D${RunFlag} -Djava.awt.headless=true -Xms256m -Xmx512m -classpath ${CLASSPATH} com.asiainfo.lcims.omc.boot.MainServer > ${LOG_HOME}/main.log 2>&1 & 