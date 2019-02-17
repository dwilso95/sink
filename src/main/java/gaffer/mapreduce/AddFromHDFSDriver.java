package gaffer.mapreduce;

import uk.gov.gchq.gaffer.hdfs.operation.AddElementsFromHdfs;
import uk.gov.gchq.gaffer.hdfs.operation.mapper.generator.MapperGenerator;

public class AddFromHDFSDriver {

  public static final void main(final String[] args) {
    System.out.println("Add from HDFS thing");

    MapperGenerator generator;

    final AddElementsFromHdfs addElements = new AddElementsFromHdfs.Builder().failurePath("/gaffer/failure/jobId")
        .outputPath("/gaffer/success/jobId").addInputMapperPair("inputFile", "mappergenerator").build();
  }

}
