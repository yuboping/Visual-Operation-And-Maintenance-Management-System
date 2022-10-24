package com.asiainfo.lcims.omc.web.ctrl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("serial")
@Controller
public class CreateVerifycodeAction extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CreateVerifycodeAction.class);

    private int width = 78;
    private int height = 34;
    private Random rand = new Random();
    private String illegalCode = "012oizOIZl";

    /**
     * 绘制校验码
     * 
     * @return 校验码
     */
    @RequestMapping("/login/createverify")
    public void createVerify(HttpServletResponse response, HttpSession session) {
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 构建缓存图像
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 准备绘图工具
        Graphics2D g2d = bi.createGraphics();
        // 绘制背景
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, width - 1, height - 1);
        // 设定字体
        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 24));

        String code = "";
        // 随机输出不同颜色不同字体坐标也不同的中文字符
        for (int i = 0; i < 4; i++) {
            setLetterColor(g2d);
            // 用以旋转字体的类
            AffineTransform at = new AffineTransform();
            // 准备一个随机数字 范围在-1---1 之间
            int number = rand.nextInt(3) - 1;
            // 获得随机的+45 度或者是-45 度
            double role = number * rand.nextDouble() * (Math.PI / 4);
            // 设置旋转角度以及旋转的相对原点坐标这个坐标与字体坐标应当保持一致,PS 这个
            // 公式是要根据字体以及间距来推断的可以通过效果来逐步调整
            at.setToRotation(role, 13 * i + 8.0, 22.0);
            // 旋转字体
            g2d.setTransform(at);
            String letter = getRandomLetter();
            // 调整验证码生成规则
            if (illegalCode.contains(letter)) {
                i--;
            } else {
                code += letter;
                // 绘制字体
                g2d.drawString(letter, 13 * i + 8, 22);
            }
        }
        // 绘制干扰线
        this.drawLine(g2d);

        ServletOutputStream sos;
        try {
            sos = response.getOutputStream();
            ImageIO.write(bi, "jpeg", sos);
            sos.flush();
            sos.close();
        } catch (IOException e) {
            LOG.error("imageIO write error, reason : {}", e);
        }
        session.setAttribute("session_vcode", code);
    }

    /**
     * 设置颜色，随机
     * 
     * @param g2d
     */
    private void setLetterColor(Graphics2D g2d) {
        int red = rand.nextInt(120);
        int blue = rand.nextInt(100);
        int green = rand.nextInt(60);
        Color color = new Color(red, blue, green);
        g2d.setColor(color);
    }

    /**
     * 获取随机字母或者数字
     * 
     * @return
     */
    private String getRandomLetter() {

        // 随机整数
        int number = rand.nextInt(200);
        // 字符从0-9，A-Z中产生，对应的ASCII码为48-57，65-90,97-122
        number = number % 62;

        if (number < 10) {
            number += 48;
        } else if (number < 36) {
            number += 55;
        } else {
            number += 61;
        }
        return String.valueOf((char) number);
    }

    /**
     * 绘制干扰线
     * 
     * @param g
     */
    private void drawLine(Graphics2D g) {
        int widths[] = new int[width];
        int heights[] = new int[width];
        Point p0 = new Point(0, rand.nextInt(height));
        Point p1 = new Point(rand.nextInt(width), rand.nextInt(height));
        Point p2 = new Point(rand.nextInt(width), rand.nextInt(height));
        Point p3 = new Point(rand.nextInt(width), rand.nextInt(height));
        Point p4 = new Point(width, rand.nextInt(height));
        for (int i = 0; i < width; i++) {
            double t = ((double) i) / width;
            Point temp = get(t, p0, p1, p2, p3, p4);
            widths[i] = temp.x;
            heights[i] = temp.y;
        }
        g.drawPolyline(widths, heights, width);
    }

    /**
     * 获取点
     * 
     * @param t
     * @param points
     * @return
     */
    private Point get(double t, Point... points) {
        t = formart(t, 2);
        int n = points.length - 1;
        double x = 0;
        double y = 0;
        for (int i = 0; i <= n; i++) {
            x += factorial(n) / (factorial(n - i) * factorial(i) * 1.0) * points[i].x
                    * Math.pow(1 - t, (n - i) * 1.0) * Math.pow(t, i);
            y += factorial(n) / (factorial(n - i) * factorial(i) * 1.0) * points[i].y
                    * Math.pow(1 - t, (n - i) * 1.0) * Math.pow(t, i);
        }
        int xint = Integer.parseInt("" + (long) x);
        int yint = Integer.parseInt("" + (long) y);
        return new Point(xint, yint);
    }

    private double formart(double arg, int num) {
        double multiplicand = Math.pow(10, num);
        return Math.round(arg * multiplicand) / multiplicand;
    }

    /**
     * 
     * 得到一个数的阶乘
     */
    private int factorial(int num) {
        int value = 1;
        for (int i = 1; i <= num; i++) {
            value *= i;
        }
        return value;
    }
}
