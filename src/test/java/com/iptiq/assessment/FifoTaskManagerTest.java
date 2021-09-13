package com.iptiq.assessment;

import com.iptiq.assessment.producer.FillerProducer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FifoTaskManagerTest {

    private static final int BOUND = 100;
    private static final AtomicInteger lastId = new AtomicInteger();

    @SneakyThrows
    @Test
    void default_addition_success() {
        BlockingQueue<Process> queue = new LinkedBlockingQueue<>(BOUND);

        FifoTaskManager.add(queue, new Process(lastId.incrementAndGet()));
        Thread.sleep(1000);
        assertEquals(1, queue.size());
    }

    @SneakyThrows
    @Test
    void fifo_addition() {
        BlockingQueue<Process> queue = new LinkedBlockingQueue<>(BOUND);

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        Process process = new Process(lastId.incrementAndGet());
        FifoTaskManager.add(queue, process);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());
        assertTrue(queue.contains(process));
        assertEquals(2, queue.peek().getId());
    }

    @SneakyThrows
    @Test
    void list() {
        BlockingQueue<Process> queue = new LinkedBlockingQueue<>(BOUND);
        List<Process> list = new ArrayList<>();

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        Process process = new Process(lastId.incrementAndGet());
        FifoTaskManager.add(queue, process);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());
        assertTrue(queue.contains(process));
        assertEquals(2, queue.peek().getId());

        FifoTaskManager.list(queue, list);
        Thread.sleep(1000);
        assertEquals(0, queue.size());
        assertEquals(BOUND, list.size());
        assertEquals(2, list.get(0).getId());
    }

    @SneakyThrows
    @Test
    void kill_all() {
        BlockingQueue<Process> queue = new LinkedBlockingQueue<>(BOUND);

        fillQueue(queue);
        Thread.sleep(1000);
        assertEquals(BOUND, queue.size());

        FifoTaskManager.killAll(queue);
        Thread.sleep(1000);
        assertEquals(0, queue.size());
    }

    @SneakyThrows
    @Test
    void kill() {
        BlockingQueue<Process> queue = new LinkedBlockingQueue<>(BOUND);
        Process process = new Process(lastId.incrementAndGet());

        FifoTaskManager.add(queue, process);
        Thread.sleep(1000);
        assertEquals(1, queue.size());

        FifoTaskManager.kill(queue, process);
        Thread.sleep(1000);
        assertEquals(0, queue.size());
    }

    private void fillQueue(BlockingQueue<Process> queue) {
        FillerProducer producer = new FillerProducer(queue, BOUND);
        new Thread(producer).start();
    }
}
