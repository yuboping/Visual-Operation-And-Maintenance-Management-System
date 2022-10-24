package com.asiainfo.lcims.omc.alarm.syslog;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.alarm.model.AlarmDetail;
import com.asiainfo.lcims.omc.alarm.model.AlarmSyslog;
import com.asiainfo.lcims.util.DateTools;

public class SyslogSender {
    private static final Logger log = LoggerFactory.getLogger(SyslogSender.class);
    private final byte[] buf = new byte[8192];
    private DatagramSocket ds = null;
    private static final int timeout = 10000;
    private final String ip;
    private final int port;

    public SyslogSender(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            ds = new DatagramSocket();
            ds.setSoTimeout(timeout);
        } catch (SocketException e) {
            log.error("", e);
        }
    }

    public String receive() throws Exception {
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);
        String info = new String(dp.getData(), 0, dp.getLength());
        return info;
    }

    public DatagramPacket send(byte[] msg) throws Exception {
        DatagramPacket dp = new DatagramPacket(msg, msg.length, InetAddress.getByName(ip), port);
        ds.send(dp);
        return dp;
    }

    public void close() {
        ds.close();
    }

    public static void main(String[] args) {
        String FORMAT = "yyyy-MM-dd HH:mm";
        DateTools DATATOOL = new DateTools(FORMAT);
        AlarmSyslog syslog = new AlarmSyslog();
        syslog.setDevice("node1");
        AlarmDetail detail = new AlarmDetail();
        detail.setHostip("254.1.1.1");
        detail.setHostname("testalarm");
        detail.setAlarmdesc("告警上报测试信息");
        detail.setAlarmlevel("1");
        detail.setAlarmtime(DATATOOL.getCurrentDate());
        detail.setUniquemark("test-000001");
        detail.setFlag("active");
        syslog.setDetail(detail);
        //GB18030
        //UDP 数据
        SyslogSender sender = new SyslogSender("132.96.231.102", 514);
        try {
        	sender.send(syslog.toString().getBytes("GB18030"));
            //sender.send((syslog.toString() + "\n").getBytes());
            log.info("send alarm msg=" + syslog.toString());
            String ret = sender.receive();
            log.info("receive msg=" + ret);
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
