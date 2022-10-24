package com.asiainfo.lcims.omc.report.dto;

import java.lang.reflect.Field;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.lcims.omc.report.conf.UploadConf;
import com.asiainfo.lcims.util.DateTools;
import com.asiainfo.lcims.util.XmlUtil;

public class XmlFileContent {

    private static final Logger LOG = LoggerFactory.getLogger(XmlFileContent.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static void rootElement(Element root) {
        root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        XmlUtil.setAttribute(root, "xsi:noNamespaceSchemaLocation",
                "file:///C:/Users/Administrator/Desktop/schema.xsd");
    }

    public static void headerElement(Element fileHeader, UploadConf conf) {
        String currentTime = getCurrentTime(DATE_FORMAT);
        XmlUtil.setText(fileHeader.addElement("TimeStamp"), currentTime);
        XmlUtil.setText(fileHeader.addElement("TimeZone"), "UTC+8");
        XmlUtil.setText(fileHeader.addElement("VendorName"),
                conf.getUploadProtocol().getVendorName());
        XmlUtil.setText(fileHeader.addElement("ElementType"),
                conf.getUploadProtocol().getElementType());
        XmlUtil.setText(fileHeader.addElement("CmVersion"),
                conf.getUploadProtocol().getCmVersion());
    }

    public static void fieldNameElement(Element fieldName, List<String> columnNames) {
        for (int i = 0; i < columnNames.size(); i++) {
            String num = String.valueOf(i + 1);
            String text = columnNames.get(i);
            fieldName.addElement("N").addAttribute("i", num).addText(text);
        }
    }

    public static void fieldValueElement(Element fieldValue, List<String> columnNames,
            List<?> nrmDataList, UploadConf conf) {
        for (int i = 0; i < nrmDataList.size(); i++) {
            Element object = fieldValue.addElement("Object");
            Object data = nrmDataList.get(i);
            object.addAttribute("rmUID", conf.getUploadProtocol().getRmUID());
            for (int j = 0; j < columnNames.size(); j++) {
                String text = columnNames.get(j);
                try {
                    Field field = data.getClass().getDeclaredField(text);
                    field.setAccessible(true);
                    String value = (String) field.get(data);
                    String num = String.valueOf(j + 1);
                    object.addElement("V").addAttribute("i", num).addText(value);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    public static String getCurrentTime(String dateFormat) {
        DateTools dateTools = new DateTools(dateFormat);
        String currentTime = dateTools.getCurrentDate();
        return currentTime;
    }

}
