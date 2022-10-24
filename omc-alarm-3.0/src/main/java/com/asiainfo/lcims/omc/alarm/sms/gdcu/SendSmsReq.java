
package com.asiainfo.lcims.omc.alarm.sms.gdcu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * sendSmsReq complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="sendSmsReq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="passWord" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="smsContext" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="phones" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendSmsReq", namespace = "http://req.alarmforward.zznode.com", propOrder = {
    "id",
    "sourceID",
    "userName",
    "passWord",
    "smsContext",
    "phones"
})
public class SendSmsReq {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String sourceID;
    @XmlElement(required = true)
    protected String userName;
    @XmlElement(required = true)
    protected String passWord;
    @XmlElement(required = true)
    protected String smsContext;
    @XmlElement(required = true)
    protected String phones;

    /**
     * 获取id属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * 获取sourceID属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSourceID() {
        return sourceID;
    }

    /**
     * 设置sourceID属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSourceID(String value) {
        this.sourceID = value;
    }

    /**
     * 获取userName属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置userName属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * 获取passWord属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * 设置passWord属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPassWord(String value) {
        this.passWord = value;
    }

    /**
     * 获取smsContext属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSmsContext() {
        return smsContext;
    }

    /**
     * 设置smsContext属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSmsContext(String value) {
        this.smsContext = value;
    }

    /**
     * 获取phones属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPhones() {
        return phones;
    }

    /**
     * 设置phones属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPhones(String value) {
        this.phones = value;
    }

}
