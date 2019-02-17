import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.graph.hook.GraphHook;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;

public class GraphHookDriver {

  public static final void main(final String[] args) throws SerialisationException, IOException {
    System.out.println("Graph hook loading example");

    final List<GraphHook> hooks = JSONSerialiser.deserialise(FileUtils.readFileToByteArray(new File("graphHooks.json")),
        new TypeReference<List<GraphHook>>() {
        });

    for (final GraphHook hook : hooks) {
      System.out.println("Hook: " + hook.getClass());
    }

  }

}
