package com.asiainfo.lcims.omc.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数值计算
 * 
 * @author qinwoli
 * 
 */
public class NumCount {
    private static final BigDecimal RADIX = new BigDecimal(1024);
    private static final BigDecimal SECOND = new BigDecimal(60 * 60);
    private static final BigDecimal DIVIDER = RADIX.multiply(SECOND);
    private static final int LENGTH = 1;
    private static final DecimalFormat FORMAT = new DecimalFormat("###");

    static {
        FORMAT.setMaximumFractionDigits(LENGTH);
    }

    private NumCount() {

    }

    /**
     * 计算增长率
     * 
     * @param current
     * @param reference
     * @return
     */
    public static String growthRate(int current, int reference) {
        if (reference == 0) {
            return "NA";
        } else {
            Double radio = (double) ((current - reference) / (double) reference);
            return FORMAT.format(radio * 100) + "%";
        }
    }

    /**
     * 计算增长率
     * 
     * @param current
     * @param reference
     * @return
     */
    public static String growthRate(BigDecimal current, BigDecimal reference) {
        if (reference.intValue() == 0) {
            return "NA";
        } else {
            BigDecimal growth = current.subtract(reference);
            BigDecimal radio = growth.divide(reference, LENGTH + 2, BigDecimal.ROUND_HALF_UP);
            return FORMAT.format(radio.doubleValue() * 100) + "%";
        }
    }
    
    /**
     * 计算偏差率
     * 
     * @param current
     * @param reference
     * @return
     */
    public static String offsetRate(Double current, Double reference) {
        if (reference.intValue() == 0) {
            return "NA";
        } else {
            Double radio = (current - reference) / reference;
            return FORMAT.format(radio * 100);
        }
    }

    /**
     * 计算同比
     * 
     * @param current
     * @param reference
     * @return
     */
    public static String compared(Double current, Double reference) {
        if (reference == 0) {
            return "NA";
        } else {
            Double radio = current / reference;
            return FORMAT.format(radio * 100) + "%";
        }
    }

    /**
     * 计算同比
     * 
     * @param current
     * @param reference
     * @return
     */
    public static String compared(Integer current, Integer reference) {
        if (reference == 0) {
            return "NA";
        } else {
            Double radio = (double) current / (double) reference;
            return FORMAT.format(radio * 100);
        }
    }

    /**
     * 计算宽带速率(mb/s)
     * 
     * @param current
     * @param reference
     * @return
     */
    public static Integer downRate(BigDecimal flow) {
        BigDecimal radio = flow.divide(SECOND, 0, BigDecimal.ROUND_HALF_UP);
        return radio.intValue();
    }

    /**
     * 计算宽带速率(gb/s)
     * 
     * @param current
     * @param reference
     * @return
     */
    public static Integer downRateGb(BigDecimal flow) {
        BigDecimal radio = flow.divide(DIVIDER, 0, BigDecimal.ROUND_HALF_UP);
        return radio.intValue();
    }

    /**
     * 流量从MB转为GB
     * 
     * @param current
     * @param reference
     * @return
     */
    public static BigDecimal transferGb(BigDecimal flow) {
        return flow.divide(RADIX, 0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 值域范围等分时，若实际等分值有小数则调整最大值
     * 
     * @param max
     * @param splitNumber
     * @return
     */
    public static Integer adjustMax(int max, int min, int splitNumber) {
        int remainder = (max - min) % splitNumber;
        if (remainder > 0) {
            return max + (splitNumber - remainder);
        } else {
            return max;
        }
    }

}
