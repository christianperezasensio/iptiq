package com.iptiq.assessment;

import com.iptiq.assessment.consumer.KillAllConsumer;
import com.iptiq.assessment.consumer.KillConsumer;
import com.iptiq.assessment.consumer.ListConsumer;
import com.iptiq.assessment.producer.BoundedProducer;
import com.iptiq.assessment.producer.FifoBoundedProducer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FifoTaskManager {

    private static final int BOUND = 100;

    public static void main(String[] args) {
        BlockingQueue<Process> queue = new LinkedBlockingQueue<>(BOUND);
    }

    protected static void add(BlockingQueue<Process> queue, Process process) {
        BoundedProducer producer = new FifoBoundedProducer(queue, process);
        new Thread(producer).start();
    }

    protected static void list(BlockingQueue<Process> queue, List<Process> list) {
        ListConsumer consumer = new ListConsumer(queue, list);
        new Thread(consumer).start();
    }

    protected static void killAll(BlockingQueue<Process> queue) {
        KillAllConsumer consumer = new KillAllConsumer(queue);
        new Thread(consumer).start();
    }

    protected static void kill(BlockingQueue<Process> queue, Process process) {
        KillConsumer consumer = new KillConsumer(queue, process);
        new Thread(consumer).start();
    }
}
