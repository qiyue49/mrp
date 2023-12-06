package com.sunseagear.common.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TaskHelper {

    @Getter
    private int handlerCount = 1;
    @Getter
    private int bufferSize = 1024;
    private Disruptor<TaskEvent> disruptor;
    private TaskEventProducer taskEventProducer;

    public TaskHelper() {

    }

    public TaskHelper(int handlerCount, int bufferSize) {
        this.handlerCount = handlerCount;
        this.bufferSize = bufferSize;
    }

    @SuppressWarnings("deprecation")
    @PostConstruct
    private void start() {
        // Executor that will be used to construct new threads for consumers
        //Executor executor = Executors.newCachedThreadPool();
        ExecutorService executor = new ThreadPoolExecutor(2, 5, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());



        // The factory for the event
        TaskEventFactory factory = new TaskEventFactory();

        // Specify the size of the ring buffer, must be power of 2.

        // Construct the Disruptor
        // 单线程模式，获取额外的性能
        disruptor = new Disruptor<>(factory, bufferSize, executor, ProducerType.SINGLE,
                new BlockingWaitStrategy());
        List<TaskHandler> TaskHandlers = new ArrayList<>();
        for (int i = 0; i < handlerCount; i++) {
            TaskHandlers.add(new TaskHandler());
        }
        disruptor.handleExceptionsWith(new IgnoreExceptionHandler());
        // 多个消费者，每个消费者竞争消费不同数据
        disruptor.handleEventsWithWorkerPool(TaskHandlers.toArray(new TaskHandler[0]));
        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<TaskEvent> ringBuffer = disruptor.getRingBuffer();
        taskEventProducer = new TaskEventProducer(ringBuffer);
    }

    /**
     * 停止
     */
    public void shutdown() {
        doHalt();
    }

    private void doHalt() {
        disruptor.halt();
    }

    public void doTask(Task task) {
        taskEventProducer.doTask(task);
    }

    public void setHandlerCount(int handlerCount) {
        this.handlerCount = handlerCount;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
