
package com.asiainfo.lcims.omc.alarm.sms.gdcu;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.asiainfo.lcims.omc20.alarm.sms.gdcu package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendSmsReq_QNAME = new QName("http://alarmforward.zznode.com", "sendSmsReq");
    private final static QName _SendSmsRsp_QNAME = new QName("http://alarmforward.zznode.com", "sendSmsRsp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.asiainfo.lcims.omc20.alarm.sms.gdcu
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendSmsReq }
     * 
     */
    public SendSmsReq createSendSmsReq() {
        return new SendSmsReq();
    }

    /**
     * Create an instance of {@link SendSmsRsp }
     * 
     */
    public SendSmsRsp createSendSmsRsp() {
        return new SendSmsRsp();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsReq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://alarmforward.zznode.com", name = "sendSmsReq")
    public JAXBElement<SendSmsReq> createSendSmsReq(SendSmsReq value) {
        return new JAXBElement<SendSmsReq>(_SendSmsReq_QNAME, SendSmsReq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsRsp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://alarmforward.zznode.com", name = "sendSmsRsp")
    public JAXBElement<SendSmsRsp> createSendSmsRsp(SendSmsRsp value) {
        return new JAXBElement<SendSmsRsp>(_SendSmsRsp_QNAME, SendSmsRsp.class, null, value);
    }

}
