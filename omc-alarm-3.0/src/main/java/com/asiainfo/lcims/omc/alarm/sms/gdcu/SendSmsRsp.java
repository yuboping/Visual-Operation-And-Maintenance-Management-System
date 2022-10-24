
package com.asiainfo.lcims.omc.alarm.sms.gdcu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * sendSmsRsp complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="sendSmsRsp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sendResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="remark" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendSmsRsp", propOrder = {
    "id",
    "sendResult",
    "remark"
})
public class SendSmsRsp {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String sendResult;
    @XmlElement(required = true)
    protected String remark;

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
     * 获取sendResult属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSendResult() {
        return sendResult;
    }

    /**
     * 设置sendResult属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSendResult(String value) {
        this.sendResult = value;
    }

    /**
     * 获取remark属性的值。
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置remark属性的值。
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRemark(String value) {
        this.remark = value;
    }

}
