package com.iptiq.assessment.producer;

import com.iptiq.assessment.Process;

import java.util.concurrent.BlockingQueue;

public class BoundedProducer implements Runnable {

    final BlockingQueue<Process> queue;
    final Process process;

    public BoundedProducer(BlockingQueue<Process> queue, Process process) {
        this.queue = queue;
        this.process = process;
    }

    public void run() {
        queue.add(process);
    }
}
