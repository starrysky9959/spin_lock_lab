package com.example.spinlock;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements MyLock {
    class QNode {
        public volatile boolean locked = false;
        public volatile QNode next = null;
    }

    AtomicReference<QNode> queue;
    ThreadLocal<QNode> myNode;

    public MCSLock() {
        queue = new AtomicReference<QNode>(null);
        // initialize thread-local variable
        myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    public void lock() {
        QNode qnode = myNode.get();
        QNode pred = queue.getAndSet(qnode);
        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            while (qnode.locked) {
            } // spin
        }
    }

    public void unlock() {
        QNode qnode = myNode.get();
        if (qnode.next == null) {
            if (queue.compareAndSet(qnode, null))
                return;
            while (qnode.next == null) {
            } // spin
        }
        qnode.next.locked = false;
        qnode.next = null;
    }

}
