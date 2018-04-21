package com.newgameplus.framework.misc;

public final class Couple<A, B> {
    private A first;
    private B second;

    public Couple(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
