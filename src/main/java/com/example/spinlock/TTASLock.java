package com.example.spinlock;

import java.util.concurrent.atomic.AtomicBoolean;

public class TTASLock implements MyLock{
    // false means available
    private AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        while (true) {
            while (state.get()) {
            }
            
            if (!state.getAndSet(true)) {
                return;
            }
        }
    }

    public void unlock() {
        state.set(false);
    }
}
