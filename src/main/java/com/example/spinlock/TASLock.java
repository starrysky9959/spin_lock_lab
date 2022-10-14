package com.example.spinlock;

import java.util.concurrent.atomic.AtomicBoolean;

public class TASLock implements MyLock {
    // false means available
    private AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        while (state.getAndSet(true)) {
        }
    }

    public void unlock() {
        state.set(false);
    }
}
