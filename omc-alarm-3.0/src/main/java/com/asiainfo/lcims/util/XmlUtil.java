package com.asiainfo.lcims.util;

import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhujiansheng
 * @date 2020年5月28日 上午11:01:38
 * @version V1.0
 */
public class XmlUtil {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUtil.class);

    private static final String XML_ENCODING = "UTF-8";

    /**
     * 更改节点的属性值
     * 
     * @param element
     * @param name
     * @param value
     */
    public static void setAttribute(Element element, String name, String value) {
        element.addAttribute(name, value);
    }

    /**
     * 设置text的值
     * 
     * @param element
     * @param value
     */
    public static void setText(Element element, String value) {
        element.setText(value);
    }

    /**
     * 设置element的值
     * 
     * @param document
     * @param value
     */
    public static void setElement(Document document, String value) {
        document.addElement(value);
    }

    /**
     * 设置XML文档格式
     * 
     * @return
     */
    public static OutputFormat setPrettyPrint() {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(XML_ENCODING);
        format.setIndentSize(4); // 行缩进
        format.setNewlines(true); // 一个结点为一行
        format.setTrimText(true); // 去重空格
        format.setPadText(true);
        format.setNewLineAfterDeclaration(false); // 放置xml文件中第二行为空白行
        return format;
    }

    /**
     * 保存文档
     * 
     * @param document
     * @param fileName
     */
    public static void saveXml(Document document, String fileName, OutputFormat format) {
        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileWriter(fileName), format);
            writer.write(document);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

}
