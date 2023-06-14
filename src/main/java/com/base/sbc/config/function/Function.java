package com.base.sbc.config.function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class Function  implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments applicationArguments)  {
        init();
    }

    public void init() {
        /*sum*/
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                List<Number> o = (List<Number>)env.get(((AviatorJavaType) arg1).getName());
                BigDecimal add = NumberUtil.add(o.toArray(new Number[0]));
                return new AviatorDouble(add.doubleValue());
            }

            @Override
            public String getName() {
                return "SUM";
            }
        });

        /*Avg*/
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
                List<Number> o = (List<Number>)env.get(((AviatorJavaType) arg1).getName());
                BigDecimal add = NumberUtil.add(o.toArray(new Number[0]));
                return new AviatorDouble(add.doubleValue()/o.size());
            }
            @Override
            public String getName() {
                return "AVG";
            }
        });


        /*if*/
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
                Boolean ifResult = FunctionUtils.getBooleanValue(arg1, env);
                Number ifTrue = FunctionUtils.getNumberValue(arg2, env);
                Number ifFalse = FunctionUtils.getNumberValue(arg3, env);
                if (ifResult) {
                    return new AviatorDouble(ifTrue.doubleValue());
                } else {
                    return new AviatorDouble(ifFalse.doubleValue());
                }
            }
            /**
             * 返回方法名
             */
            @Override
            public String getName() {
                return "IF";
            }
        });

        /**
         * DATEDIF函数
         * 示例
         * DATEDIF(2021-2-2,'2022-2-2','m')
         */
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
                String startTime = FunctionUtils.getStringValue(arg1, env);
                String endTime = FunctionUtils.getStringValue(arg2, env);
                String s = FunctionUtils.getStringValue(arg3, env);
                DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                long m = 0;
                try {
                    Date star = dft.parse(startTime);//开始时间
                    Date endDay = dft.parse(endTime);//结束时间
                    Long num = endDay.getTime() - star.getTime();//时间戳相差的毫秒数

                    switch (s) {
                        case "y":
                            m = endDay.getYear() - star.getYear();
                            break;
                        case "m":
                            m = num / (24 * 60 * 60 * 1000)/30;
                            break;
                        case "d":
                            m = num / (24 * 60 * 60 * 1000);
                            break;
                        default:

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return new AviatorDouble(m);
            }
            /**
             * 返回方法名
             */
            @Override
            public String getName() {
                return "DATEDIF";
            }
        });

    }


}
