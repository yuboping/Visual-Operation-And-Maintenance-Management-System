package com.asiainfo.lcims.omc.service.gdcu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.model.gdcu.AccessLog;
import com.asiainfo.lcims.omc.util.SortList;
import com.asiainfo.lcims.omc.util.ToolsUtils;

public class HBaseUtils {
    private static Logger log = LoggerFactory.getLogger(HBaseUtils.class);
    private static Connection connection;
    private static Configuration configuration;
    private static HBaseUtils hBaseUtils;
    
    /**
     * 初始化数据
     */
    private void init(){
        log.info("init HBase connection start");
        if (configuration == null) {
            configuration = HBaseConfiguration.create();
        }
        try {
            Path path = new Path("/hbase-site.xml");
            log.info("path.getName(): {}", path.getName());
            configuration.addResource(path);
            if (connection == null || connection.isClosed()) {
                connection = ConnectionFactory.createConnection(configuration);
            }
            log.info("init HBase connection end");
            log.info("connection.isClosed():{}", connection.isClosed());
        } catch (Exception e) {
            log.error("hbase init error", e);
        }
    }
    
    /**
     * 关闭连接池
     */
    public static void close(){
        try {
            if (connection!=null)connection.close();
        } catch (IOException e) {
            log.error("error", e);
        }
    }
    
    private HBaseUtils(){}
    
    /**
     * 唯一实例，线程安全，保证连接池唯一
     * @return
     */
    public static HBaseUtils getInstance() {
        try {
            synchronized (HBaseUtils.class) {
                if (hBaseUtils == null) {
                    hBaseUtils = new HBaseUtils();
                    hBaseUtils.init();
                } else {
                    if (connection == null || connection.isClosed()) {
                        log.info("hBaseUtils.connection == null");
                        hBaseUtils.init();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return hBaseUtils;
    }
    
    public static List<AccessLog> getAccessLogGdRows(HbaseParm request){
        List<AccessLog> access = new ArrayList<AccessLog>();
        Table table = null;
        try {
            log.info("hbase param : {}", request.toString());
            table = connection.getTable(TableName.valueOf("access_log_gd"));
            
            RowFilter accountFileter = new RowFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(request.getAccount())));
            ValueFilter querydateFileter = new ValueFilter(CompareOp.EQUAL,new RegexStringComparator(request.getQuerydate()));
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            filterList.addFilter(accountFileter);
            filterList.addFilter(querydateFileter);
            log.info("query hbase filter List : {}", filterList);
            
            Scan scan = new Scan();
            scan.setStartRow(request.getAccount().getBytes());
            scan.setStopRow(request.getAccount().getBytes());
            scan.setFilter(filterList);
            ResultScanner rs = table.getScanner(scan);
            String value = null;
            int ct = 0;
            for (Result r : rs) {
                log.info("获得到rowkey:" + new String(r.getRow()));
                NavigableMap<byte[],NavigableMap<byte[],NavigableMap<Long,byte[]>>> navigableMap = r.getMap();
                for (byte[] family:navigableMap.keySet()){
                    for (byte[] column : navigableMap.get(family).keySet()){
                        for (Long t : navigableMap.get(family).get(column).keySet()){
                            //组装数据
                            value = new String(navigableMap.get(family).get(column).get(t));
                            log.debug("hbase value: {}", value);
                            ct++;
                            makeAccessLogList(value, access);
                        }
                    }
                }
            }
            log.info("recevice hbase data num: {}", ct);
            rs.close();
        } catch (IOException e) {
            log.error("e", e);
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                log.error("e", e);
            }
        }
        SortList<AccessLog> sortList = new SortList<AccessLog>();
//        Collections.reverse(access);
        sortList.Sort(access, "getLogtime", "desc");  
        return access;
    }
    
    
    public static void getAllTabl(){
        try {
            Admin admin = connection.getAdmin();
            if(admin!=null) {
                HTableDescriptor[] allTable = admin.listTables();
                for (HTableDescriptor hTableDescriptor : allTable) {
                    log.info(hTableDescriptor.getNameAsString());
                }
            }else{
                log.info("admin is null");
            }
        } catch (IOException e) {
            log.error("getAllTabl", e);
        }
        
    }
    
    private static void makeAccessLogList(String data, List<AccessLog> access){
        if(ToolsUtils.StringIsNull(data))
            return;
        if(data.endsWith("|")){
            data = data +" ";
        }
        String [] valueArr = data.split("\\|");
        newAccessLog(valueArr,access);
    }
    
    private static void newAccessLog(String [] valueArr,List<AccessLog> access){
        if(valueArr.length<16){
            log.info("newAccessLog:"+valueArr.length);
            return;
        }
        AccessLog accessLog = new AccessLog();
        accessLog.setUsername(valueArr[0]);
        accessLog.setDomainname(valueArr[1]);
        accessLog.setLogtime(valueArr[2]);
        accessLog.setSvctype(valueArr[3]);
        accessLog.setOperatetype(valueArr[4]);
        accessLog.setReason(valueArr[5]);
        accessLog.setNasip(valueArr[6]);
        accessLog.setNasport(valueArr[7]);
        accessLog.setPorttype(valueArr[8]);
        accessLog.setFrameip(valueArr[9]);
        accessLog.setCallerid(valueArr[10]);
        accessLog.setCalleeid(valueArr[11]);
        accessLog.setNaslocation(valueArr[14]);
        access.add(accessLog);
    }
    
}
