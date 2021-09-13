package com.iptiq.assessment.producer;

import com.google.common.collect.MinMaxPriorityQueue;
import com.iptiq.assessment.Priority;
import com.iptiq.assessment.PriorityProcess;

import java.util.concurrent.atomic.AtomicInteger;

public class PriorityFillerProducer implements Runnable {

    private final MinMaxPriorityQueue<PriorityProcess> queue;
    private final int maxSize;
    private final AtomicInteger lastId = new AtomicInteger();

    public PriorityFillerProducer(MinMaxPriorityQueue<PriorityProcess> queue, int maxSize) {
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
            queue.add(new PriorityProcess(lastId.incrementAndGet(), Priority.LOW));
//                queue.put(new PriorityProcess(lastId.incrementAndGet(), Priority.LOW));
        }
    }
}
