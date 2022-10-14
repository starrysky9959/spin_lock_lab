package com.example.util;

public class MyThread extends Thread {

    @Override
    public void run() {
        while (true) {
            Counter.lock.lock();
            if (Counter.counter < Counter.TARGET) {
                Counter.counter++;
                Counter.lock.unlock();
            } else {
                Counter.lock.unlock();
                break;
            }
        }
    }
}