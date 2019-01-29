import java.util.Map;

import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.koryphe.ValidationResult;

public class DefaultSchemaValidator implements SchemaValidator {

  public ValidationResult validate(final Schema schema) {
    return schema.validate();
  }

  public SchemaValidator initialize(final Map<String, String> configuration) {
    return this;
  }

}