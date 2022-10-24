package com.asiainfo.lcims.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ByteUtil.class);

    /**
     * Big-Endian,将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
     */
    public static byte[] intToBytes2(int value)
    {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
 
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));
        return value;
    }
 
 
    /**
     * Big-Endian,将short数值转换为占二个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
     */
    public static byte[] shortToBytes2(short value)
    {
        byte[] src = new byte[2];
        src[0] = (byte) ((value>>8)&0xFF);
        src[1] = (byte) (value & 0xFF);
        return src;
    }
 
    public static short bytesToShort2(byte[] src, int offset) {
        short value;
        value = (short) (
                ((src[offset] & 0xFF)<<8)
                |(src[offset+1] & 0xFF));
        return value;
    }
 
    /**
     * 单字节处理为字节数组
     */
    public static byte[] byteToBytes2(byte value){
        byte[] src = new byte[1];
        src[0] = value;
        return src;
    }
 
    public static byte bytesToByte(byte[] src, int offset) {
        byte value;
        value =  (src[offset]);
        return value;
    }
    
    /**
     * 字符转字节数组
     * @param strBody
     * @param msgType
     * @return
     */
    public static byte[] getStrToByteArr(String strBody, byte msgType) {
        int bodylength = strBody.getBytes().length;
        byte[] allMessage = new byte[9 + bodylength];
        short startSign = (short) 0xFFFF;
        int timeStamp = (int) (System.currentTimeMillis() / 1000);
        short bodyLen = (short) bodylength;
        System.arraycopy(shortToBytes2(startSign), 0, allMessage, 0, 2);
        System.arraycopy(byteToBytes2(msgType), 0, allMessage, 2, 1);
        System.arraycopy(intToBytes2(timeStamp), 0, allMessage, 3, 4);
        System.arraycopy(shortToBytes2(bodyLen), 0, allMessage, 7, 2);
        System.arraycopy(strBody.getBytes(), 0, allMessage, 9, bodylength);
        return allMessage;
    }
    
    /**
     * 读取9字节数据
     * @param allMessage
     * @return
     */
    public static List<Object> getByteToTopMsgList(byte[] allMessage){
        List<Object> list = new ArrayList<Object>();
        short startSignParser = bytesToShort2(allMessage, 0);
        byte msgTypeParser = bytesToByte(allMessage, 2);
        int timeStampParser = bytesToInt2(allMessage, 3);
        short bodyLenParser = bytesToShort2(allMessage, 7);
        list.add(startSignParser);
        list.add(msgTypeParser);
        list.add(timeStampParser);
        list.add(bodyLenParser);
        return list;
    }
    
    public static List<Object> getByteToObjectList(byte[] allMessage){
        List<Object> list = new ArrayList<Object>();
        short startSignParser = bytesToShort2(allMessage, 0);
        byte msgTypeParser = bytesToByte(allMessage, 2);
        int timeStampParser = bytesToInt2(allMessage, 3);
        short bodyLenParser = bytesToShort2(allMessage, 7);
        byte[] bodyParser = new byte[bodyLenParser];
        System.arraycopy(allMessage, 9, bodyParser, 0, bodyLenParser);
        String bodyParserStr = new String(bodyParser);
        list.add(startSignParser);
        list.add(msgTypeParser);
        list.add(timeStampParser);
        list.add(bodyLenParser);
        list.add(bodyParserStr);
        LOG.info(
                "startSignParser = {}, msgTypeParser = {}, timeStampParser = {}, bodyLenParser = {}, bodyParserStr = {}",
                startSignParser, msgTypeParser, timeStampParser, bodyLenParser, bodyParserStr);
        return list;
    }
    
}
