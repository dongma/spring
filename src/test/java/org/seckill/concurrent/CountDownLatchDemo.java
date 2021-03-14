package org.seckill.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sam Ma
 * @date 2021/03/14
 * CountDownLatch（倒计时器）允许count个线程阻塞在一个地方，直到所有线程的任务都执行完毕
 */
public class CountDownLatchDemo {

    // 请求的数量
    private static final int threadCount = 550;

    /*
     * threadNum: 537 threadNum: 539
     * finish (直到这550个请求执行完成，才会执行最终的finish)
     */
    /*public static void main(String[] args) throws InterruptedException {
        // 创建一个具有固定线程数量的线程池对象（若这里线程池的线程数量给太少的话，你就会发现执行的很慢）
        ExecutorService threadPool = Executors.newFixedThreadPool(300);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; ++i) {
            final int threadNum = i;
            threadPool.execute(() -> {
                try {
                    test(threadNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();  // 表示一个请求已经完成
                }
            });
        }
        countDownLatch.await();
        threadPool.shutdown();
        System.out.println("finish");
    }*/

    public static void test(int threadNum) throws InterruptedException {
        Thread.sleep(1000); // 模拟请求的耗时操作
        System.out.println("threadNum: " + threadNum);
        Thread.sleep(1000); // 模拟请求的耗时操作
    }

}
