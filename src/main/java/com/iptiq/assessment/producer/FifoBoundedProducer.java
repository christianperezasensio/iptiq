package com.iptiq.assessment.producer;

import com.iptiq.assessment.Process;

import java.util.concurrent.BlockingQueue;

public class FifoBoundedProducer extends BoundedProducer {

    public FifoBoundedProducer(BlockingQueue<Process> queue, Process process) {
        super(queue, process);
    }

    @Override
    public void run() {
        try {
            queue.add(process);
        } catch (IllegalStateException e) {
            try {
                queue.take();
                queue.add(process);
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
