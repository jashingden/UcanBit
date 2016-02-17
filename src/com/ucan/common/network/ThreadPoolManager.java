package com.ucan.common.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPool
 * @author 李欣駿
 */
public class ThreadPoolManager
{
//	private static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private static int CORE_POOL_SIZE = 5;
    private static int MAX_POOL_SIZE = 50;
    private static int KEEP_ALIVE_TIME = 10000;
    private static ThreadPoolExecutor threadPool;
//    private static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
    private static BlockingQueue<Runnable> workQueue = new SynchronousQueue<Runnable>();
    
	private ThreadPoolManager() {}

    private static ThreadFactory threadFactory = new ThreadFactory() 
    {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) 
        {
            return new Thread(r, "myThreadPool thread:" + integer.getAndIncrement());
        }
    };
    
    static 
    {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, workQueue, threadFactory);
    }

    public static void execute(Runnable runnable)
    {
        threadPool.execute(runnable);
    }
    
    public static void shutdown()
    {
    	threadPool.shutdown();
    }
}
