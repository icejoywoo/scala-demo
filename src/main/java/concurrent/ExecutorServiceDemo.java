package concurrent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        {
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Asynchronous task");
                }
            });

            Future future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Asynchronous task");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            // future 如果任务结束执行则返回 null
            System.out.println("future.get()=" + future.get());

            Future future1 = executorService.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    System.out.println("Asynchronous Callable");
                    Thread.sleep(1000);
                    return "Callable Result";
                }
            });

            System.out.println("future.get()=" + future1.get());

            executorService.shutdown();
        }

        {
            ExecutorService executorService = Executors.newFixedThreadPool(3);

            Set<Callable<String>> callables = new HashSet<Callable<String>>();

            callables.add(new Callable<String>() {
                public String call() throws Exception {
                    return "Task 1";
                }
            });
            callables.add(new Callable<String>() {
                public String call() throws Exception {
                    return "Task 2";
                }
            });
            callables.add(new Callable<String>() {
                public String call() throws Exception {
                    return "Task 3";
                }
            });

            String result = executorService.invokeAny(callables);

            System.out.println("result = " + result);

            executorService.shutdown();
        }

        {
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            Set<Callable<String>> callables = new HashSet<Callable<String>>();

            callables.add(new Callable<String>() {
                public String call() throws Exception {
                    return "Task 1";
                }
            });
            callables.add(new Callable<String>() {
                public String call() throws Exception {
                    return "Task 2";
                }
            });
            callables.add(new Callable<String>() {
                public String call() throws Exception {
                    return "Task 3";
                }
            });

            List<Future<String>> futures = executorService.invokeAll(callables);

            for(Future<String> future : futures){
                System.out.println("future.get = " + future.get());
            }

            executorService.shutdown();
        }
    }
}
