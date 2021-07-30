package com.ztl.gym.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author shenz
 * @create 2021-07-27 10:11
 */
public class ConversionUtill {

    /**
     * Object转BigDecimal类型
     *
     * @param value 传入Object值
     * @return 转成的BigDecimal类型数据
     */
    public static BigDecimal ToBigDecimal(Object value) {
        BigDecimal bigDec = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                bigDec = (BigDecimal) value;
            } else if (value instanceof String) {
                bigDec = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                bigDec = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                bigDec = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Can Not make [" + value + "] into a BigDecimal.");
            }
        }
        return bigDec;
    }

}
