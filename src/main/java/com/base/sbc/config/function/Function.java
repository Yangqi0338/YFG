package com.base.sbc.config.function;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Function  implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments applicationArguments)  {
        init();
    }

    public void init() {
        /*sum*/
        AviatorEvaluator.addFunction(new AbstractVariadicFunction() {
            @Override
            public AviatorObject variadicCall(Map<String, Object> map, AviatorObject... args) {
                Double sum = 0D;
                for(AviatorObject arg : args){
                    Number number = FunctionUtils.getNumberValue(arg, map);
                    sum+=number.doubleValue();
                }
                return new AviatorDouble(sum);
            }

            @Override
            public String getName() {
                return "SUM";
            }
        });

        /*Avg*/
        AviatorEvaluator.addFunction(new AbstractVariadicFunction() {
            @Override
            public AviatorObject variadicCall(Map<String, Object> map, AviatorObject... args) {
                Double sum = 0D;
                Integer count = 0;
                for(AviatorObject arg:args){
                    Number number = FunctionUtils.getNumberValue(arg, map);
                    sum+=number.doubleValue();
                    count++;
                }
                return new AviatorDouble(sum / count);
            }

            @Override
            public String getName() {
                return "Avg";
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


    }


}
