package org.seckill.concurrent;

import java.util.concurrent.*;

/**
 * @author Sam Ma
 * @date 2021/03/14
 * 使用CyclicBarrier(循环栅栏)实现线程间的等待，但是它的功能比CountDownLatch更加复杂和强大
 */
public class CyclicBarrierDemo {

    // 请求的数量
    private static final int threadCount = 550;

    // 需要同步的线程数量，用于在线程到屏障时，优先执行barrierAction（方便处理更复杂的业务场景）
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
        System.out.println("---- 当线程数达到之后，优先执行 -----");
    });

    /*
     * threadNum:{0} is ready threadNum:{1} is ready threadNum:{2} is ready
     * threadNum:{3} is ready threadNum:{4} is ready threadNum:{4} is finish threadNum:{0} is finish
     * threadNum:{1} is finish threadNum:{2} is finish threadNum:{3} is finish
     */
    /*public static void main(String[] args) throws InterruptedException {
        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < threadCount; ++i) {
            final int threadNum = i;
            Thread.sleep(1000);
            threadPool.execute(() -> {
                try {
                    test(threadNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        threadPool.shutdown();
    }*/

    public static void test(int threadNum) throws InterruptedException {
        System.out.println("threadNum:{" + threadNum + "} is ready");
        try {
            /* 等待60s，保证子线程完全执行结束 */
            cyclicBarrier.await(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("--- CyclicBarrierException ---");
        }
        System.out.println("threadNum:{" + threadNum + "} is finish");
    }

}
