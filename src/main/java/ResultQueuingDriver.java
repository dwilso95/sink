import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/*
  Each call to create and next are executed in their own thread.
  
  Create should build a scanner, add results to a bounded LinkedBlockingQueue. (Perhaps wait when offer fails?) 
  The queue is then kept track of in a ConcurrentHashMap  (or a more complex object which includes a "future" (boolean) 
  of whether the scanner is done or not)?

  Next, gets the Queue and "future" reads results until batch size is met or "future" is complete
  


*/
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
        }
      }
      return 0;
    };
    final Future<Integer> queryFuture = executor.submit(query);
    final QueryResult queryResult = new QueryResult("abc", results, queryFuture);

    resultSetCache.put(queryResult.getQueryId(), queryResult);
    int batchSize = 2;
    final Runnable poll = () -> {
      final List<Integer> list = new ArrayList<>(batchSize);
      while (!queryResult.getResultQueue().isEmpty() || !queryResult.getQueryTaskFuture().isDone()) {
        while (list.size() < batchSize) {
          queryResult.getResultQueue().drainTo(list, batchSize - list.size());
        }
        System.out.println("Poll: " + list);
        list.clear();
      }

    };

    executor.execute(poll);

    System.out.println("Done!");
  }

}
