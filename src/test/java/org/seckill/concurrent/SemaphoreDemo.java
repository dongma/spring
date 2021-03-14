package org.seckill.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Sam Ma
 * @date 2021/03/14
 * semaphore（信号量）-允许多个线程同时访问某个资源resource
 */
public class SemaphoreDemo {

    // 请求的数量
    private static final int threadCount = 550;

    /*
     * finish threadNum: 4
     * threadNum: 0 threadNum: 2 threadNum: 3
     * threadNum: 1 threadNum: 5 threadNum: 7
     * threadNum: 8 threadNum: 9 threadNum: 6
     * threadNum: 10
     */
    /*public static void main(String[] args) {
        // 创建一个具有固定线程数量的线程池对象（若这里线程池的线程数量给太少的话，你就会发现执行的很慢）
        ExecutorService threadPool = Executors.newFixedThreadPool(300);
        // 一次只允许执行的线程数量，在构造Semaphore时，都必须提供许可的数量及是否公平{fairness}
        final Semaphore semaphore = new Semaphore(20);

        for (int i = 0; i < threadCount; ++i) {
            final int threadNum = i;
            threadPool.execute(() -> {
                try {
                    // lambda表达式的运用, 执行acquire方法阻塞，直到有一个许可证可以获得然后拿走一个许可证
                    semaphore.acquire();
                    test(threadNum);
                    semaphore.release();  // 释放一个许可
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        threadPool.shutdown();
        System.out.println("finish");
    }*/

    public static void test(int threadNum) throws InterruptedException {
        Thread.sleep(1000); // 模拟请求的耗时操作
        System.out.println("threadNum: " + threadNum);
        Thread.sleep(1000); // 模拟请求的耗时操作
    }

}
