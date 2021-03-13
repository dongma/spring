package org.seckill.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Sam Ma
 * @date 2021/03/13
 * java的JDK动态代理、以及cglib进行代理 (代理设计模式相关)
 */
interface SmsService {
    /* 定义SmsService服务接口，用于进行发短信 */
    String send(String message);
}

/* 实现发短信的接口 */
class SmsServiceImpl implements SmsService {
    public String send(String message) {
        System.out.println("send message: {" + message + "}");
        return message;
    }
}

/* 定义一个jdk动态代理，InvocationHandler, JDK动态代理致命问题：只能代理实现了接口的类 */
class DebugInvocationHandler implements InvocationHandler {
    // 代理类中的真实对象
    private final Object target;

    public DebugInvocationHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /* 调用方法之前，我们可以添加自己的操作, 在method.invoke(target, args)之后，同样可以添加自己的操作 */
        System.out.println("before method: " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("after method: " + method.getName());
        return result;
    }
}

/* 获取代理对象的工厂类, getProxy() */
class JdkProxyFactory {
    public static Object getProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),  // 目标类的类加载器
                target.getClass().getInterfaces(),   // 代理需要实现的接口，可以指定多个
                new DebugInvocationHandler(target)   // 代理对象对应的自定义InvocationHandler
        );
    }
}

public class DynamicProxy {

//    public static void main(String[] args) {
        /*
         * before method: send、send message: {java} 、after method: send
         * 相对于静态代理模式，动态代理更加灵活。我们不需要针对每个目标类都创建一个代理类，并且也不需要我们实现接口，可直接用CGLIB实现
         */
//        SmsService proxyService = (SmsService) JdkProxyFactory.getProxy(new SmsServiceImpl());
//        proxyService.send("java");

        /*
         * before method: send、send message: {java}、 after method: send
         * 使用Cglib对类实现代理的功能，实现MethodInterceptor接口，同时使用Enhancer进行增强。cglib不能代理final类
         */
//        AliSmsService cglibProxy = (AliSmsService) CglibProxyFactory.getProxy(AliSmsService.class);
//        cglibProxy.send("java");
//    }

}

/* 用CGLIB实现一个使用阿里云发送短信的类 */
class AliSmsService {
    public String send(String message) {
        System.out.println("send message: {" + message + "}");
        return message;
    }
}

/* 自定义MethodInterceptor的实现 */
class DebugMethodInterceptor implements MethodInterceptor {
    /**
     * @param o           被代理的对象 (需增强的对象)
     * @param method      被拦截的方法 (需要增强的方法)
     * @param args     方法入参
     * @param methodProxy 用于调用原始方法
     * @return
     * @throws Throwable
     */
    public Object intercept(Object o, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        System.out.println("before method: " + method.getName());
        Object result = methodProxy.invokeSuper(o, args);
        System.out.println("after method: " + method.getName());
        return result;
    }

}

class CglibProxyFactory {
    public static Object getProxy(Class<?> clazz) {
        // 创建动态代理的增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        // 设置被代理的类
        enhancer.setSuperclass(clazz);
        // 设置方法拦截器
        enhancer.setCallback(new DebugMethodInterceptor());
        // 创建代理类
        return enhancer.create();
    }
}


