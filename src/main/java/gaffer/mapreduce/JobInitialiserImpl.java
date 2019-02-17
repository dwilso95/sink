package gaffer.mapreduce;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Job;

import uk.gov.gchq.gaffer.hdfs.operation.MapReduce;
import uk.gov.gchq.gaffer.hdfs.operation.handler.job.initialiser.JobInitialiser;
import uk.gov.gchq.gaffer.store.Store;

public class JobInitialiserImpl implements JobInitialiser {

  @Override
  public void initialiseJob(Job job, MapReduce operation, Store store) throws IOException {
    // TODO Auto-generated method stub
    
  }

}
