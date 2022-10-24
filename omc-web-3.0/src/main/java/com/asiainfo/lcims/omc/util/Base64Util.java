package com.asiainfo.lcims.omc.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base64Util {

    private static final Logger LOG = LoggerFactory.getLogger(Base64Util.class);

    private static final String CHARTSET_NAME = "UTF-8";

    private static final Decoder DECODER = Base64.getDecoder();

    private static final Encoder ENCODER = Base64.getEncoder();

    /**
     * 加密
     * 
     * @param text
     * @return
     */
    public static String encoder(String text) {
        try {
            byte[] textByte = text.getBytes(CHARTSET_NAME);
            String encodedText = ENCODER.encodeToString(textByte);
            return encodedText;
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 解密
     * 
     * @param text
     * @return
     */
    public static String decoder(String text) {
        try {
            byte[] textByte = DECODER.decode(text);
            String decodedText = new String(textByte, CHARTSET_NAME);
            return decodedText;
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            return "";
        }
    }

}
