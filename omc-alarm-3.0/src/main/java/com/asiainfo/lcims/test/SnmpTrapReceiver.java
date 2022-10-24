package com.asiainfo.lcims.test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

public class SnmpTrapReceiver implements CommandResponder {

    private static final Logger log = LoggerFactory.getLogger(SnmpTrapReceiver.class);

    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp = null;
    private Address listenAddress;
    private ThreadPool threadPool;

    @SuppressWarnings("unchecked")
    private void init() throws UnknownHostException, IOException {
        threadPool = ThreadPool.create("Trap", 2);
        dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        listenAddress = GenericAddress
                .parse(System.getProperty("snmp4j.listenAddress", "udp:192.168.53.33/162")); // 本地IP与监听端口
        TransportMapping transport;
        // 对TCP与UDP协议进行处理
        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
        }
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(),
                new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();
    }

    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
            log.info("开始监听Trap信息!");
        } catch (Exception ex) {
            log.error("SnmpTrapReceiver run error", ex.getMessage());
        }
    }

    /**
     * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息 当接收到trap时，会自动进入这个方法
     */
    @SuppressWarnings("unchecked")
    @Override
    public void processPdu(CommandResponderEvent event) {
        if (event != null && event.getPDU() != null) {
            Vector<VariableBinding> recVBs = (Vector<VariableBinding>) event.getPDU()
                    .getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out.println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }
    }

    public static String getChinese(String octetString) {
        try {
            String[] temps = octetString.split(":");
            byte[] bs = new byte[temps.length];
            for (int i = 0; i < temps.length; i++) {
                bs[i] = (byte) Integer.parseInt(temps[i], 16);
            }
            return new String(bs, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String chinese = getChinese(
                "53:4e:2d:50:53:2d:52:41:44:49:55:53:2d:41:46:43:30:31:e4:b8:bb:e6:9c:ba:e5:86:85:e5:ad:98:e4:bd:bf:e7:94:a8:e7:8e:87:3e:30");
        System.out.println(chinese);
        // String uuid = UUID.randomUUID().toString();
        // String id = uuid.replaceAll("-", "");
        // System.out.println(id);
        // SnmpTrapReceiver snmpTrapReceiver = new SnmpTrapReceiver();
        // snmpTrapReceiver.run();
    }

}