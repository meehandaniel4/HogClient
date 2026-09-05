package com.hogv1.util;

import java.util.ArrayDeque;
import java.util.Deque;

public final class ClickTracker {
    private final Deque<Long> left = new ArrayDeque<>(), right = new ArrayDeque<>();
    public void left() { left.addLast(System.currentTimeMillis()); }
    public void right() { right.addLast(System.currentTimeMillis()); }
    public int leftCps() { return count(left); }
    public int rightCps() { return count(right); }
    private int count(Deque<Long> queue) { long cutoff=System.currentTimeMillis()-1000; while (!queue.isEmpty() && queue.peekFirst()<cutoff) queue.removeFirst(); return queue.size(); }
}
