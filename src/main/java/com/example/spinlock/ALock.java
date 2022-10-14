package com.example.spinlock;

import java.util.concurrent.atomic.AtomicInteger;

public class ALock implements MyLock {
    private boolean[] flags;
    private AtomicInteger next; // 下一个等待 slot
    private ThreadLocal<Integer> mySlot;
    private int size;

    public ALock(int capacity) {
        size = capacity;
        flags = new boolean[capacity * 2];
        flags[0] = true;
        next = new AtomicInteger(0);
        mySlot = new ThreadLocal<Integer>();
    }

    public void lock() {
        // 设置下一个等待 slot
        int slot = next.getAndIncrement();
        mySlot.set(slot);
        // 空转，忙等在自己的 slot 上
        while (!flags[slot % size]) {
        }
        // 获得锁，占用中
        flags[slot % size] = false;
    }

    public void unlock() {
        // 下一个线程可用
        flags[(mySlot.get() + 1) % size] = true;
    }
}
