package com.iptiq.assessment.consumer;

import com.iptiq.assessment.Process;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class KillAllConsumer implements Runnable {

    final BlockingQueue<Process> queue;

    public KillAllConsumer(BlockingQueue<Process> queue) {
        this.queue = queue;
    }

    public void run() {
        queue.clear();
    }
}
