package com.iptiq.assessment.producer;

import com.iptiq.assessment.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FillerProducer implements Runnable {

    private final BlockingQueue<Process> queue;
    private final int maxSize;
    private final AtomicInteger lastId = new AtomicInteger();

    public FillerProducer(BlockingQueue<Process> queue, int maxSize) {
        this.queue = queue;
        this.maxSize = maxSize;
    }

    public void run() {
        try {
            fillQueue();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void fillQueue() throws InterruptedException {
        for (int i = 0; i < maxSize; i++) {
            queue.put(new Process(lastId.incrementAndGet()));
        }
    }
}
