package com.iptiq.assessment.consumer;

import com.iptiq.assessment.Process;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListConsumer implements Runnable {

    final BlockingQueue<Process> queue;
    final List<Process> list;

    public ListConsumer(BlockingQueue<Process> queue, List<Process> list) {
        this.queue = queue;
        this.list = list;
    }

    public void run() {
        queue.drainTo(list);
    }
}
