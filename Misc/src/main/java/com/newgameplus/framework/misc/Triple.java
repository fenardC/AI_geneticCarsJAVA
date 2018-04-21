package com.newgameplus.framework.misc;

public final class Triple<A, B, C> {
    public Triple(A first, B second, C third) {
        super();
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    private A first;

    private B second;

    private C third;
}
