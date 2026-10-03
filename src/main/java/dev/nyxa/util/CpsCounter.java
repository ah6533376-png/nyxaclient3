package dev.nyxa.util;

import java.util.ArrayDeque;
import java.util.Deque;

public final class CpsCounter {
    private static final Deque<Long> LEFT = new ArrayDeque<>();
    private static final Deque<Long> RIGHT = new ArrayDeque<>();

    public static void leftClick() { LEFT.addLast(System.currentTimeMillis()); }
    public static void rightClick() { RIGHT.addLast(System.currentTimeMillis()); }

    public static void prune() {
        long now = System.currentTimeMillis();
        while (!LEFT.isEmpty() && now - LEFT.peekFirst() > 1000) LEFT.pollFirst();
        while (!RIGHT.isEmpty() && now - RIGHT.peekFirst() > 1000) RIGHT.pollFirst();
    }

    public static int leftCps() { prune(); return LEFT.size(); }
    public static int rightCps() { prune(); return RIGHT.size(); }
}
