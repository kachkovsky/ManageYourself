package com.kachkovsky.busyhistory.data;

public class DecSequence {

    public static class Holder {
        static final DecSequence INSTANCE = new DecSequence();
    }

    private int i = -1;

    public int next() {
        return i--;
    }

}
