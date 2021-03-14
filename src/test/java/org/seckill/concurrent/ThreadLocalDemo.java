package org.seckill.concurrent;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * @author Sam Ma
 * @date 2021/03/14
 * ThreadLocal变量的demo,要想实现每一个线程都有自己的专属本地变量该如何解决
 */
public class ThreadLocalDemo implements Runnable {

    // SimpleDateFormat并不是线程安全的，所以每个线程都要有自己的副本
    private static final ThreadLocal<SimpleDateFormat> formatter =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd HHmm"));

    /*  Thread Name= 0 default Formatter = yyyyMMdd HHmm
        Thread Name= 0 formatter = yy-M-d ah:mm
    Thread Name= 1 default Formatter = yyyyMMdd HHmm
    Thread Name= 1 formatter = yy-M-d ah:mm
    Thread Name= 2 default Formatter = yyyyMMdd HHmm
    Thread Name= 2 formatter = yy-M-d ah:mm
    Thread Name= 3 default Formatter = yyyyMMdd HHmm
    Thread Name= 3 formatter = yy-M-d ah:mm
    Thread Name= 4 default Formatter = yyyyMMdd HHmm
    Thread Name= 5 default Formatter = yyyyMMdd HHmm
    Thread Name= 4 formatter = yy-M-d ah:mm
    Thread Name= 5 formatter = yy-M-d ah:mm */

    /*public static void main(String[] args) throws InterruptedException {
        ThreadLocalDemo runnable = new ThreadLocalDemo();
        for (int i = 0; i < 6; i++) {
            Thread t = new Thread(runnable, "" + i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }*/

    @Override
    public void run() {
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " default Formatter = "
                + formatter.get().toPattern());
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // formatter pattern is changed here by thread, but it won't reflect to other threads
        formatter.set(new SimpleDateFormat());
        System.out.println("Thread Name= " + Thread.currentThread().getName() + " formatter = "
                + formatter.get().toPattern());
    }

}
