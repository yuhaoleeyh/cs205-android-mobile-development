package com.zain.game;

import java.util.concurrent.atomic.AtomicInteger;

public class BirdThread  implements Runnable{
    private final int id;
    private final AtomicInteger jumps;
    private final AtomicInteger score;
    private volatile boolean running;


    public BirdThread(int id, AtomicInteger jumps, AtomicInteger score, boolean running) {
        this.id = id;
        this.jumps = jumps;
        this.score = score;
        this.running = running;
    }

    public int incrementJumps() {
        jumps.incrementAndGet();
        return jumps.get();
    }

    public int incrementScore() {
        score.incrementAndGet();
        return score.get();
    }

    public void resetScore() {
        score.set(0);
    }

    public void resetJumps() {
        jumps.set(0);
    }

    @Override
    public void run() {
        while (running) {
            System.out.println("Thread " + id + " is running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
