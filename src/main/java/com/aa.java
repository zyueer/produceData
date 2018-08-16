package com;
import java.util.regex.Pattern;

import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;
import com.time.util.DateUtil;

public class aa {
    public static void main(String []args){
        String path = TimeNormalizer.class.getResource("").getPath();
        String classPath = path.substring(0, path.indexOf("/com/time"));
        System.out.println(classPath + "/TimeExp.m");
        TimeNormalizer normalizer = new TimeNormalizer(classPath + "/TimeExp.m");

        normalizer.parse("Hi，all.下周一下午三点开会");// 抽取时间
        TimeUnit[] unit = normalizer.getTimeUnit();
        System.out.println("Hi，all.下周一下午三点开会");
        System.out.println(DateUtil.formatDateDefault(unit[0].getTime()) + "-" + unit[0].getIsAllDayTime());

    }
}
