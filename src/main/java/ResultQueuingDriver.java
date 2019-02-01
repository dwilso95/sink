import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class ResultQueuingDriver {

  public static void main(String[] args) {
    final int queueSize = 4;
    final Map<String, QueryResult> resultSetCache = new ConcurrentHashMap<>();
    final ExecutorService executor = Executors.newCachedThreadPool();

    final Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).iterator();
    final LinkedBlockingQueue<Integer> results = new LinkedBlockingQueue<>(queueSize);
    final Callable<Integer> query = () -> {
      while (iter.hasNext()) {
        if (results.size() < queueSize) {
          System.out.println("added " + results.offer(iter.next()));
        } else {
          System.out.println("Queue full. Waiting...");
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            System.out.println("WTF!");
          }
        }
      }
      System.out.println("FINISHED!");
      return 0;
    };
    final Future<Integer> queryFuture = executor.submit(query);
    final QueryResult queryResult = new QueryResult("abc", results, queryFuture);

    resultSetCache.put(queryResult.getQueryId(), queryResult);

    final Runnable poll = () -> {
      while (!queryResult.getResultQueue().isEmpty() || !queryResult.getQueryTaskFuture().isDone()) {
        try {
          System.out.println("Poll: " + queryResult.getResultQueue().take());
        } catch (InterruptedException e) {
        }
      }

    };

    executor.execute(poll);

    System.out.println("Done!");
  }

}
