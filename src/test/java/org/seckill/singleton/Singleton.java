package org.seckill.singleton;

/**
 * @author Sam Ma
 * @date 2021/03/14
 * 双重校验实现对象单例（线程安全）
 */
public class Singleton {

    /* volatile关键字很重要，其可以禁止jvm进行指令重新排序 */
    private volatile static Singleton uniqueObject;

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
        // 先判断对象是否已经实例化，没有实例化才进入加锁代码
        if (uniqueObject == null) {
            // 类对象加锁, synchronized对对象uniqueObject进行检测
            synchronized (Singleton.class) {
                if (uniqueObject == null) {
                    uniqueObject = new Singleton();
                }
            }
        }
        return uniqueObject;
    }

}
