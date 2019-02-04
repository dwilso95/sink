import uk.gov.gchq.gaffer.accumulostore.operation.hdfs.handler.job.factory.AccumuloAddElementsFromHdfsJobFactory;
import uk.gov.gchq.gaffer.hdfs.operation.AddElementsFromHdfs;
import uk.gov.gchq.gaffer.hdfs.operation.handler.job.initialiser.TextJobInitialiser;
import uk.gov.gchq.gaffer.hdfs.operation.handler.job.tool.AddElementsFromHdfsTool;

public class GafferIngest {

  public static void main(String[] args) {
    final AddElementsFromHdfs add = new AddElementsFromHdfs.Builder().addInputMapperPair("inputPath", "EntityToElementGenerator.class").failurePath("namespace/failure")
        .jobInitialiser(new TextJobInitialiser()).outputPath("staging_bulk_import").build();
    
    // Create our own "tool"
    // Metrics output from operation? This will be difficult or we need multi output?
    // Monitoring! Failure, eventing to domain progress ingest!
    // This is single stage?
    
    AddElementsFromHdfsTool tool  = new AddElementsFromHdfsTool(new AccumuloAddElementsFromHdfsJobFactory(), add, null);

  }

}
