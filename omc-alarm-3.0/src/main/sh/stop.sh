#!/bin/bash
LANG=@sh.LANG@
export LANG

RunFlag='target=@sh.target@';
for pid in `ps -ef | grep "${RunFlag}" | grep -v "grep" | awk ' { print $2 } '`
do
kill -9 $pid;
echo $pid;
done