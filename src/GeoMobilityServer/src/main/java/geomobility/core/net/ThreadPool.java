/**
 * Copyright 2010 Hieu Rocker & Tien Trum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geomobility.core.net;

import geomobility.core.net.TaskPool.TaskCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * A Thread-Pool mechanism help execute many {@link TaskPool} by reuse Thread
 * instance in pool.
 *
 */
public class ThreadPool {
    /**
     * The number of threads in pool, WARNING: do not use big number
     */
    private final static int POOL_SIZE = 3;

    private ExecutorService pool;

    static ThreadPool threadPool;

    public static ThreadPool getInstance() {
        if (threadPool == null) {
            threadPool = new ThreadPool();
        }
        return threadPool;
    }

    /**
     * Constructs a {@link ThreadPool} with a specified number of threads in
     * pool.
     *
     * @param poolsize
     */
    public ThreadPool(int poolsize) {
        pool = Executors.newFixedThreadPool(poolsize);
    }

    /**
     * Constructs a {@link ThreadPool} with default number of threads in pool.
     */
    public ThreadPool() {
        this(POOL_SIZE);
    }

    /**
     * Execute an given {@link TaskPool}.
     *
     * @param task
     *            Which {@link TaskPool} you want to execute.
     * @param callback
     *            An instance of {@link TaskCallback} which help inform the
     *            status of {@link TaskPool}. Pass null if don't want to receive
     *            the status.
     */
    public void execute(final TaskPool task, final TaskCallback callback) {
        pool.execute(new Runnable() {
            public void run() {
                task.execute(callback);
            }
        });
    }

    /**
     * Shutdown this {@link ThreadPool}. When shutting down, this
     * {@link ThreadPool} no longer accept to execute any new {@link TaskPool}.
     */
    public void shutdownAndAwaitTermination() {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}