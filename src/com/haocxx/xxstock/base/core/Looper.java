package com.haocxx.xxstock.base.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Haocxx
 * on 2021-1-24
 */
public class Looper {
    private final static ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();
    private static Looper sMainLooper;
    private static volatile boolean sExit;
    private final Queue<Runnable> mQueue = new ConcurrentLinkedQueue<>();

    public static void initMain() {
        sMainLooper = getLooper();
    }

    public static Looper getMainLooper() {
        return sMainLooper;
    }

    public static synchronized Looper getLooper() {
        Looper looper = sThreadLocal.get();
        if (looper == null) {
            looper = new Looper();
            sThreadLocal.set(looper);
        }
        return looper;
    }

    public static void exit() {
        sExit = true;
    }

    public void loop() {
        do {
            Runnable runnable = mQueue.poll();
            if (runnable != null) {
                runnable.run();
            } else {
                try {
                    synchronized (mQueue) {
                        mQueue.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (!sExit);
    }

    public void enqueue(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException(" enqueue a null runnable");
        }
        synchronized (mQueue) {
            mQueue.add(runnable);
            mQueue.notifyAll();
        }
    }
}
