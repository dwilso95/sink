import java.util.Map;

import uk.gov.gchq.gaffer.store.schema.Schema;

public class DefaultSchemaModifier implements SchemaModifier {

  public DefaultSchemaModifier() {

  }

  public Schema modify(final Schema schema) {
    return schema;
  }

  public SchemaModifier initialize(Map<String, String> properties) {
    return this;
  }
}