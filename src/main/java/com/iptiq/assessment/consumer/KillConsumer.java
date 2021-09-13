package com.iptiq.assessment.consumer;

import com.iptiq.assessment.Process;

import java.util.concurrent.BlockingQueue;

public class KillConsumer implements Runnable {

    final BlockingQueue<Process> queue;
    final Process process;

    public KillConsumer(BlockingQueue<Process> queue, Process process) {
        this.queue = queue;
        this.process = process;
    }

    public void run() {
        queue.remove(process);
    }
}
