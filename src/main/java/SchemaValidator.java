import java.util.Map;

import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.koryphe.ValidationResult;

public interface SchemaValidator {

  ValidationResult validate(final Schema schema);

  SchemaValidator initialize(final Map<String, String> configuration);

}