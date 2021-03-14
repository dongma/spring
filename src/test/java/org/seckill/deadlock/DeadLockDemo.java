package org.seckill.deadlock;

/**
 * @author Sam Ma
 * @date 2021/03/14
 * 模拟多线程间由于互相等待资源而出现的死锁情况(deadlock)
 */
public class DeadLockDemo {

    private static Object resource1 = new Object(); // 资源1

    private static Object resource2 = new Object(); // 资源2

    public static void main(String[] args) {
        /*
         * final result(两个线程相互等待，最终导致死锁):
         * Thread[thread 1,5,main] get resource1、Thread[thread 2,5,main] get resource2
         * Thread[thread 1,5,main] waiting get resource2、Thread[thread 2,5,main] waiting get resource1
         */
        new Thread(() -> {
            synchronized (resource1) {
                System.out.println(Thread.currentThread() + " get resource1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " waiting get resource2");
                synchronized (resource2) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }, "thread 1").start();

        new Thread(() -> {
            synchronized (resource2) {
                System.out.println(Thread.currentThread() + " get resource2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " waiting get resource1");
                synchronized (resource1) {
                    System.out.println(Thread.currentThread() + "get resource1");
                }
            }
        }, "thread 2").start();
    }

}
