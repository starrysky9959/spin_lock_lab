package com.example.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Solution {
    public static long run(int threadNum) throws InterruptedException {
        Thread[] threads = new Thread[threadNum];

        for (int i = 0; i < threadNum; i++) {
            threads[i] = new MyThread();
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threadNum; i++) {
            threads[i].join();
        }

        assertEquals(Counter.TARGET, Counter.counter);
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        // System.out.println(threadNum + " threads cost " + costTime + "ms");
        // System.out.print(costTime+", ");
        return costTime;
    }
}
