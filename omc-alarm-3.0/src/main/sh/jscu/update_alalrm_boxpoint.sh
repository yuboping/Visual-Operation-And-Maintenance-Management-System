#!/bin/bash;

curr_day=`date +%Y%m%d`
mon_first_day=`date -d "$curr_day" +%Y%m01`

if [ "$mon_first_day" == "$curr_day" ]; then
    mon_first_day=`date -d "$mon_first_day -1 day" +%Y%m01`
fi

tab_pref=`date -d "$mon_first_day" +%m_%d`

boxpoint_authsuccess_mult=2
boxpoint_authrate_mult=1.5
yesterday_auth_total=150

mysql -ujscuomc -pJSCUomc@123 -Djscuomc <<EOF
##行号
DROP TABLE IF EXISTS tmp_boxpoint_info;
CREATE TABLE tmp_boxpoint_info AS SELECT @group_row := CASE WHEN @code1 = a.metric_id AND 
@code2 = a.attr1 THEN @group_row + 1  ELSE 1 END AS groupRow,
@code1 := a.metric_id AS metric_id,@code2 := a.attr1 AS attr1,a.mvalue,now() as create_time
FROM(SELECT * FROM statis_data_day_$tab_pref a WHERE a.metric_id IN(SELECT id FROM md_metric
WHERE METRIC_IDENTITY IN('radius_auth_rate','radius_auth_success')) AND attr2='bas') a,
(SELECT @group_row:=1, @code1:='', @code2:='') AS b
ORDER BY a.metric_id,a.attr1,a.mvalue+0;

#四分位数据
DROP TABLE IF EXISTS tmp_boxpoint_allcount; 
DROP TABLE IF EXISTS tmp_boxpoint_q1; 
DROP TABLE IF EXISTS tmp_boxpoint_q2; 
DROP TABLE IF EXISTS tmp_boxpoint_q3; 
CREATE TABLE tmp_boxpoint_allcount AS SELECT metric_id,attr1,COUNT(*) AS allcount,now() as create_time FROM tmp_boxpoint_info GROUP BY metric_id,attr1;
CREATE TABLE tmp_boxpoint_q1 AS SELECT a.metric_id,a.attr1,b.grouprow,b.mvalue,now() as create_time FROM tmp_boxpoint_allcount a,tmp_boxpoint_info b 
WHERE a.metric_id=b.metric_id AND a.attr1=b.attr1 AND b.grouprow=CEIL (a.allcount/4);
CREATE TABLE tmp_boxpoint_q2 AS SELECT a.metric_id,a.attr1,b.grouprow,b.mvalue,now() as create_time FROM tmp_boxpoint_allcount a,tmp_boxpoint_info b 
WHERE a.metric_id=b.metric_id AND a.attr1=b.attr1 AND b.grouprow=CEIL (a.allcount/2);
CREATE TABLE tmp_boxpoint_q3 AS SELECT a.metric_id,a.attr1,b.grouprow,b.mvalue,now() as create_time FROM tmp_boxpoint_allcount a,tmp_boxpoint_info b 
WHERE a.metric_id=b.metric_id AND a.attr1=b.attr1 AND b.grouprow=CEIL (a.allcount*3/4);

#collect Q
DROP TABLE IF EXISTS tmp_boxpoint; 
CREATE TABLE tmp_boxpoint AS SELECT a.metric_id,a.attr1,b.mvalue AS q1,c.mvalue AS q2,d.mvalue AS q3,
TRUNCATE(d.mvalue - b.mvalue,4) AS q,a.allcount,b.grouprow AS q1row,c.grouprow AS q2row,d.grouprow AS q3row,now() as create_time
FROM tmp_boxpoint_allcount a LEFT JOIN tmp_boxpoint_q1 b ON a.metric_id=b.metric_id AND a.attr1=b.attr1
LEFT JOIN tmp_boxpoint_q2 c ON a.metric_id=c.metric_id AND a.attr1=c.attr1
LEFT JOIN tmp_boxpoint_q3 d ON a.metric_id=d.metric_id AND a.attr1=d.attr1; 

##radius_auth_success alram rule
DROP TABLE IF EXISTS tmp_boxpoint_alarm_authsuccess;
CREATE TABLE tmp_boxpoint_alarm_authsuccess AS SELECT alarm_id,CONCAT('newest<',q_lower) AS alarm_rule,now() as create_time FROM
(SELECT alarm_id,CASE WHEN q_lower<=0 THEN 0 ELSE q_lower END AS q_lower FROM 
(SELECT a.alarm_id,COALESCE(TRUNCATE(b.q1-"$boxpoint_low_mult"*q,0),0) AS q_lower,b.q1,b.q3,b.q
FROM md_alarm_rule_detail a LEFT JOIN (SELECT t1.*,t2.id
FROM tmp_boxpoint t1,bd_nas t2 WHERE t1.attr1=t2.NAS_IP)b
ON a.metric_id=b.metric_id AND a.dimension2=b.id WHERE a.url LIKE '/VIEW/class/SERVER/module/radiusbras/AREA/bras%'
AND a.metric_id=(SELECT id FROM md_metric WHERE METRIC_IDENTITY='radius_auth_success')) AS t1)AS t2;

##radius_auth_rate alram rule
DROP TABLE IF EXISTS tmp_boxpoint_alarm_authrate;
CREATE TABLE tmp_boxpoint_alarm_authrate AS SELECT alarm_id,CONCAT('newest<',q_lower) AS alarm_rule,now() as create_time FROM
(SELECT alarm_id,CASE WHEN q_lower<=0 THEN 0 ELSE q_lower END AS q_lower FROM 
(SELECT a.alarm_id,COALESCE(TRUNCATE(b.q1-"$boxpoint_authsuccess_mult" *q,0),0) AS q_lower,b.q1,b.q3,b.q
FROM md_alarm_rule_detail a LEFT JOIN (SELECT t1.*,t2.id
FROM tmp_boxpoint t1,bd_nas t2 WHERE t1.attr1=t2.NAS_IP)b
ON a.metric_id=b.metric_id AND a.dimension2=b.id WHERE a.url LIKE '/VIEW/class/SERVER/module/radiusbras/AREA/bras%'
AND a.metric_id=(SELECT id FROM md_metric WHERE METRIC_IDENTITY='radius_auth_rate')) AS t1)AS t2;

##radius_auth_success alram rule update to table md_alarm_rule_detail
UPDATE md_alarm_rule_detail a SET a.ALARM_RULE=(SELECT alarm_rule FROM tmp_boxpoint_alarm_authsuccess b
WHERE a.ALARM_ID=b.alarm_id) where alarm_id in (select alarm_id from tmp_boxpoint_alarm_authsuccess);

##radius_auth_rate alram rule update to table md_alarm_rule_detail 
UPDATE md_alarm_rule_detail a SET a.ALARM_RULE=(SELECT alarm_rule FROM tmp_boxpoint_alarm_authrate b
WHERE a.ALARM_ID=b.alarm_id) where alarm_id in (select alarm_id from tmp_boxpoint_alarm_authrate); 

## yesterday_auth_total <150
DROP TABLE IF EXISTS tmp_yesterday_auth_total_limit;
create table tmp_yesterday_auth_total_limit as SELECT t1.alarm_id,'newest<0' AS alarm_rule FROM md_alarm_rule_detail t1,bd_nas t2 WHERE 
t2.nas_ip IN (SELECT attr1 FROM statis_data_day_$tab_pref t WHERE t.METRIC_ID IN(SELECT id FROM 
md_metric WHERE metric_identity='radius_auth_success')
AND attr2='bas' GROUP BY attr1 HAVING SUM(mvalue)<"$yesterday_auth_total") AND t1.DIMENSION2=t2.ID;

UPDATE md_alarm_rule_detail a SET a.ALARM_RULE=(SELECT alarm_rule FROM tmp_yesterday_auth_total_limit b
WHERE a.ALARM_ID=b.alarm_id) where alarm_id in (select alarm_id from tmp_yesterday_auth_total_limit);

SELECT NOW() as exec_time FROM DUAL;

EOF
