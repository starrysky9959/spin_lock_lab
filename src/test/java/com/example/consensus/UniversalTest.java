package com.example.consensus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UniversalTest {
    @BeforeEach
    public void beforeTest() {
        Counter.counter = 0;

    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
    public void runTest(int threadNum) throws InterruptedException {

        // int threadNum = 2;
        Counter.universal = new Universal(threadNum);
        Thread[] threads = new Thread[threadNum];

        for (int i = 0; i < threadNum; i++) {
            threads[i] = new TestThread(i);
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threadNum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        assertEquals(Counter.TARGET, Counter.counter);
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.print(costTime+", ");

    }

}

class TestThread extends Thread {
    private int threadID;

    public TestThread(int id) {
        threadID = id;
    }

    @Override
    public void run() {
        // System.out.println("var getThreadID =" + threadID);
        int x = Counter.counter;
        while (x < Counter.TARGET) {
            Counter.universal.apply(Counter.getInvocation(x), threadID);
            x = Counter.counter;
        }

    }
}
