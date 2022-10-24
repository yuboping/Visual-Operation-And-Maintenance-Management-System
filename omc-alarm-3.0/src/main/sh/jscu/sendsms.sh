#!/bin/bash
LANG=zh_CN.utf8
export LANG
WORKHOME=/lcims/omc/omc-alarm/sh/jscu
JAVA_HOME=/lcims/omc/jdk8

phone=$1;
alarmmsg=$2;

file_index=tmp_$RANDOM;
filename_utf8=$file_index"_uft8"
filename_gbk=$file_index"_gbk"

echo $alarmmsg > $WORKHOME/$filename_utf8

iconv -f utf8 -t gbk $WORKHOME/$filename_utf8 > $WORKHOME/$filename_gbk
rm $WORKHOME/$filename_utf8

sh_send(){
LANG=zh_CN.gbk
export LANG
cat $WORKHOME/$filename_gbk|while read msg
do
`messagesend smsserver $msg yjomc`
done;
rm $WORKHOME/$filename_gbk
}

sh_send;