package com.example.spinlock;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements MyLock {
    class QNode {
        public volatile boolean locked = false;
    }
    AtomicReference<QNode> tail;

    ThreadLocal<QNode> myNode;
    ThreadLocal<QNode> myPred;

    public CLHLock() {
        tail = new AtomicReference<QNode>(new QNode());
        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
        myPred = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return null;
            }
        };
    }

    public void lock() {
        myNode.get().locked = true;
        QNode pred = tail.getAndSet(myNode.get());
        myPred.set(pred); // remember predecessor
        while (pred.locked) {
        } // spin
    }

    public void unlock() {
        myNode.get().locked = false;
        myNode.set(myPred.get()); // reuse predecessor
    }
}

