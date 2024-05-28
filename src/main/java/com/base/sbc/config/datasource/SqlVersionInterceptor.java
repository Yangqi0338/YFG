package com.base.sbc.config.datasource;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SqlVersionInterceptor implements Interceptor {

    private List<InnerInterceptor> interceptors = new ArrayList();

    public void addInnerInterceptor(InnerInterceptor innerInterceptor) {
        this.interceptors.add(innerInterceptor);
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
        if (target instanceof Executor){
            Executor executor = (Executor)target;
            Object parameter = args[1];
            boolean isUpdate = args.length == 2;
            MappedStatement ms = (MappedStatement)args[0];
            if (isUpdate && ms.getSqlCommandType().equals(SqlCommandType.UPDATE)){
                Iterator var8 = this.interceptors.iterator();

                while(var8.hasNext()) {
                    InnerInterceptor update = (InnerInterceptor)var8.next();
                    if (!update.willDoUpdate(executor, ms, parameter)) {
                        return -1;
                    }
                    update.beforeUpdate(executor, ms, parameter);
                }
            }
        }
        return invocation.proceed();
    }
}
