package com.example.spinlock;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackoffLock implements MyLock {
    private AtomicBoolean state = new AtomicBoolean(false);
    private int minDelay;
    private int maxDelay;
    private final Random random;
    private int limit;

    public BackoffLock(int min, int max) {
        minDelay = min;
        maxDelay = max;
        limit = minDelay;
        random = new Random();
    }

    private void backoff() throws InterruptedException {
        int delay = random.nextInt(limit);
        Thread.sleep(delay);
        if (limit < maxDelay) {
            limit <<= 1;
        }
    }

    public void lock() {
        while (true) {
            while (state.get()) {
            }

            if (!state.getAndSet(true)) {
                return;
            } else {
                try {
                    backoff();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void unlock() {
        state.set(false);
    }
}