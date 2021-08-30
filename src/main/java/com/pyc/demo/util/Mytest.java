package com.pyc.demo.util;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * <b>功能名：Mytest</b><br>
 * <b>说明：</b><br>
 * <b>著作权：</b> Copyright (C) 2021 HUIFANEDU CORPORATION<br>
 * <b>修改履历：
 *
 * @author 2021-08-30 lity
 */
public class Mytest {
    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(60);
        Stream.of(3.455, 0.323, 6.783, 4.2, 34.56, 555.321, 32.1,72.11111,60.001).mapToInt(a -> new BigDecimal(a).divide(bigDecimal, 0, BigDecimal.ROUND_UP).multiply(bigDecimal).intValue()).forEach(System.out::println);
            int sum = Stream.of(3.455, 0.323, 6.783, 4.2, 34.56, 555.321, 32.1,72.11111,60.001).mapToInt(a -> new BigDecimal(a).divide(bigDecimal, 0, BigDecimal.ROUND_UP).multiply(bigDecimal).intValue()).sum();
        System.out.println(sum);
    }
}
