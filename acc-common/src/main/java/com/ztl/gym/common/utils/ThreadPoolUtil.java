package com.ztl.gym.common.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtil {


    public static ExecutorService newFixedExecutor(String prefix, int n) {
        return newFixedExecutor(prefix, n, new LinkedBlockingQueue<>(102400));
    }

    public static class CustomThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;

        public CustomThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            String threadName = Thread.currentThread().getName();
            namePrefix = prefix + "-";
        }

        @Override
        public Thread newThread(Runnable r) {
            // 创建时指定线程的名称
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public static ExecutorService newFixedExecutor(String prefix, int n, BlockingQueue<Runnable> workQueue) {
        return new ThreadPoolExecutor(n,
                n,
                30L,
                TimeUnit.SECONDS,
                workQueue,
                new CustomThreadFactory(prefix));
    }
}
