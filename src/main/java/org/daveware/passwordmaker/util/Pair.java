package org.daveware.passwordmaker.util;

public class Pair<L, R> {
    public final L first;
    public final R second;

    public static <L, R> Pair<L, R> pair(L first, R second) {
        return new Pair<L, R>(first, second);
    }

    public Pair(L first, R second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
