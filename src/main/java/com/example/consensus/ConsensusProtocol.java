package com.example.consensus;

import java.lang.reflect.Array;

public abstract class ConsensusProtocol<T> implements Consensus<T> {
    protected T[] proposed; // 每个线程指定的输入值

    public ConsensusProtocol(Class<T> componentType, int n) {
        proposed = (T[]) Array.newInstance(componentType, n);
    }

    protected void propose(T value, int threadID) {
        // int threadID = (int) Thread.currentThread().getId();
        proposed[threadID] = value; // 输入值
    }

    abstract public T decide(T value, int threadID); // 决定值，抽象方法意味着由子类实现
}
