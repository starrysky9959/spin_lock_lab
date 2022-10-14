package com.example.consensus;

public interface Consensus<T> {
    public T decide(T value, int threadID);
}
