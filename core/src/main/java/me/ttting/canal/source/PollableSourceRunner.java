package me.ttting.canal.source;

import lombok.extern.slf4j.Slf4j;
import me.ttting.canal.Source;
import me.ttting.canal.SourceRunner;
import me.ttting.canal.lifecycle.LifecycleState;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jiangtiteng on 2018/10/9
 */
@Slf4j
public class PollableSourceRunner extends SourceRunner {
    private LifecycleState lifecycleState;

    private PollingRunner runner;

    private Thread runnerThread;

    public PollableSourceRunner() {
        lifecycleState = LifecycleState.IDLE;
    }

    @Override
    public void start() {
        Source source = getSource();
        runner = new PollingRunner();
        runner.source = (PollableSource) source;

        source.start();

        runnerThread = new Thread(runner);
        runnerThread.setName(getClass().getSimpleName() + "-" + source.getClass().getSimpleName() + "-" + source.getName());
        runnerThread.start();

        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        runner.shouldStop.set(true);

        try {
            runnerThread.interrupt();
            runnerThread.join();
        } catch (InterruptedException e) {
            log.warn("Interrupted while waiting for polling runner to stop. Please report this.", e);
        }

        Source source = getSource();
        source.stop();

        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public LifecycleState getLifeCycleState() {
        return lifecycleState;
    }

    public static class PollingRunner implements Runnable {
        private AtomicBoolean shouldStop = new AtomicBoolean(false);

        private PollableSource source;

        @Override
        public void run() {
            log.debug("Polling runner starting. Source:{}", source);
            while (!shouldStop.get()) {
                if (source.process() == PollableSource.Status.BACKOFF) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //todo
                }
            }
        }
    }
}
