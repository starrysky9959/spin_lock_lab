package com.example.spinlock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.util.Counter;
import com.example.util.Solution;

public class TASLockTest {
    @BeforeEach
    public void beforeTest() {
        Counter.counter = 0;
        Counter.lock = new TASLock();
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void runTest(int threadNum) throws InterruptedException {
        // Solution.run(threadNum);
        double avg = 0;
        for (int i = 0; i < 5; ++i) {
            beforeTest();
            avg += Solution.run(threadNum);
        }
        avg /= 5.0;
        System.out.print(avg + ", ");
    }
}
