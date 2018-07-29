package com.sharevar.appstudio.common.convert;

import com.sharevar.appstudio.object.function.Function;
import com.sharevar.appstudio.object.function.Parameter;
import com.sharevar.appstudio.repository.EntityRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class ProjectTypeConverter {

    public static Method toMethod(Class clazz,Function function){
        try {
            Class[] parameters=new Class[function.getParameters().size()];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i]=function.getParameters().get(i).getType().classType();
            }
            Method method=clazz.getDeclaredMethod(function.getName(),parameters);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object invokeClassFunction(Object instance,Function function){
        Method method=toMethod(instance.getClass(),function);
        Object[] args=new Object[function.getParameters().size()];
        for (int i = 0; i < args.length; i++) {
            Parameter parameter=function.getParameters().get(i);
            args[i]=parameter.getValue();
        }
        try {
            method.setAccessible(true);
            return method.invoke(instance,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
