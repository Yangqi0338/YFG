package com.base.sbc.config.function;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
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
        /*sum函数*/
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {

                Number left = FunctionUtils.getNumberValue(arg1, env);
                Number right = FunctionUtils.getNumberValue(arg2, env);
                return new AviatorDouble(left.doubleValue() + right.doubleValue());
            }


            @Override
            public String getName() {
                return "SUM";
            }
        });
        /*if*/
        AviatorEvaluator.addFunction(new AbstractFunction() {
            @Override
            public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
                Number left = FunctionUtils.getNumberValue(arg1, env);
                Number right = FunctionUtils.getNumberValue(arg2, env);
                return new AviatorDouble(left.doubleValue() + right.doubleValue());
            }

            @Override
            public String getName() {
                return "IF";
            }
        });


    }


}
