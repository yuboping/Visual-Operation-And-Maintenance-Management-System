#!/bin/bash
LANG=@LANG@
export LANG

JAVA_HOME=@JAVA_HOME@
WORK_HOME=@server_work_home@
RunFlag='target=@collectServerTarget@';

LOG_HOME=${WORK_HOME}/logs
LIB_HOME=${WORK_HOME}/libs

CLASSPATH="$WORK_HOME/config"

for jar in `ls ${LIB_HOME}/*.jar`
do
    CLASSPATH="$CLASSPATH":"$jar"
done

cd ${WORK_HOME}/sh
sh collectserverstop.sh
sleep 1

cd ${WORK_HOME}

nohup ${JAVA_HOME}/bin/java -D${RunFlag} -Xms256m -Xmx1024m -XX:MaxDirectMemorySize=32m -classpath ${CLASSPATH} com.asiainfo.lcims.omc.agentserver.MainServer > ${LOG_HOME}/main.log 2>&1 & 

