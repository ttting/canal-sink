package me.ttting.canal.sink;

import lombok.extern.slf4j.Slf4j;
import me.ttting.canal.Sink;
import me.ttting.canal.SinkRunner;
import me.ttting.canal.lifecycle.LifecycleAware;
import me.ttting.canal.lifecycle.LifecycleState;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jiangtiteng on 2018/10/17
 */
@Slf4j
public class PollableSinkRunner extends SinkRunner implements LifecycleAware {

    private Thread runnerThread;

    private PollingRunner runner;

    private LifecycleState lifecycleState;


    @Override
    public void start() {
        runner = new PollingRunner();
        runner.sink = getSink();
        runner.shouldStop = new AtomicBoolean(false);

        runnerThread = new Thread(runner);
        runnerThread.setName("SinkRunner-PollingRunner-" + getSink().getClass().getSimpleName());
        runnerThread.start();

        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        lifecycleState = LifecycleState.STOP;
        if (runnerThread != null) {
            runner.shouldStop.set(true);
            try {
                runnerThread.interrupt();
                runnerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public LifecycleState getLifeCycleState() {
        return null;
    }

    public static class PollingRunner implements Runnable {

        private AtomicBoolean shouldStop;

        private Sink sink;

        private AtomicInteger backOffCount = new AtomicInteger(0);

        @Override
        public void run() {
            while (!shouldStop.get()) {
                if (sink.process().equals(Sink.Status.BACKOFF)) {
                    try {
                        backOffCount.addAndGet(1);
                        Thread.sleep(100L);
                        if (backOffCount.get() % 100 == 0)
                            log.info("Sink Runner backoff count {}", backOffCount.get());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
