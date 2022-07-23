package com.simplescrumpoker.aop;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MethodProcessingHelper {

    public <T> T getMethodParameterByName(JoinPoint joinPoint, String parameterName, Class<T> cls) {
        return cls.cast(getMethodParameterByName(joinPoint, parameterName));
    }

    public <T> T getMethodParameterByName(Map<String, Object> parameters, String parameterName, Class<T> cls) {
        return cls.cast(getMethodParameterByName(parameters, parameterName));
    }


    public Object getMethodParameterByName(JoinPoint joinPoint, String parameterName) {
        return getMethodParameters(joinPoint).getOrDefault(parameterName, null);
    }

    public Object getMethodParameterByName(Map<String, Object> parameters, String parameterName) {
        return parameters.getOrDefault(parameterName, null);
    }


    public Map<String, Object> getMethodParameters(JoinPoint joinPoint) {

        var params = new HashMap<String, Object>();

        var args = joinPoint.getArgs();
        if (args.length == 0) {
            return params;
        }

        var signature = (MethodSignature) joinPoint.getSignature();
        var names = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            params.put(names[i], args[i]);
        }

        return params;
    }

}
