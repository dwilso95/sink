import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

public class QueryResult {

  private String queryId;
  private BlockingQueue<Integer> resultQueue;
  private Future<Integer> queryTaskFuture;

  public QueryResult(String queryId, BlockingQueue<Integer> resultQueue, Future<Integer> queryTaskFuture) {
    this.queryId = queryId;
    this.resultQueue = resultQueue;
    this.queryTaskFuture = queryTaskFuture;
  }

  public String getQueryId() {
    return queryId;
  }

  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  public BlockingQueue<Integer> getResultQueue() {
    return resultQueue;
  }

  public void setResultQueue(BlockingQueue<Integer> resultQueue) {
    this.resultQueue = resultQueue;
  }

  public Future<Integer> getQueryTaskFuture() {
    return queryTaskFuture;
  }

  public void setQueryTaskFuture(Future<Integer> queryTaskFuture) {
    this.queryTaskFuture = queryTaskFuture;
  }
}
