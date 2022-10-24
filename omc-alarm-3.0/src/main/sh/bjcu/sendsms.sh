#!/bin/bash
LANG=zh_CN.utf8
export LANG
WORKHOME=/data1/omc/omc-alarm-3.0/sh/bjcu
JAVA_HOME=/data1/omc/jdk1.8/jdk1.8.0_131 
SMS_SHELL_NAME=sms.sh

phone=$1;
alarmmsg=$2;

file_index=tmp_$RANDOM;
filename_utf8=$file_index"_uft8"
#filename_gbk=$file_index"_gbk"

echo $phone $alarmmsg > $WORKHOME/$filename_utf8

#iconv -f utf8 -t gbk $WORKHOME/$filename_utf8 > $WORKHOME/$filename_gbk
#rm $WORKHOME/$filename_utf8

sh_send(){
LANG=zh_CN.utf8
export LANG
cat $WORKHOME/$filename_utf8|while read phone msg
do
cd $WORKHOME
sh $SMS_SHELL_NAME $phone $msg
done;
rm $WORKHOME/$filename_utf8
}

sh_send;