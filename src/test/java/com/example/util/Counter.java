package com.example.util;

import com.example.spinlock.MyLock;

public class Counter {
    public static MyLock lock;
    public static int counter = 0;
    public static final int TARGET = 1000000;
}
