import java.util.Map;

import uk.gov.gchq.gaffer.store.schema.Schema;

public interface SchemaModifier {

  Schema modify(final Schema schema);

  SchemaModifier initialize(final Map<String, String> configuration);

}