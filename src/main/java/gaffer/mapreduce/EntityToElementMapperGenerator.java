package gaffer.mapreduce;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.MapContext;

import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.hdfs.operation.mapper.generator.MapperGenerator;

public class EntityToElementMapperGenerator implements MapperGenerator<NullWritable, Text> {

  @Override
  public Iterable<? extends Element> getElements(NullWritable keyIn, Text valueIn, MapContext<NullWritable, Text, ?, ?> context) {
    // TODO Auto-generated method stub
    return null;
  }

}
