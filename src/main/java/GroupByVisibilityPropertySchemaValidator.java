import java.util.Map;

import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.store.schema.SchemaElementDefinition;
import uk.gov.gchq.koryphe.ValidationResult;

public class GroupByVisibilityPropertySchemaValidator extends DefaultSchemaValidator {

  private String visibilityPropertyName;

  public final ValidationResult validate(final Schema schema) {
    final ValidationResult validationResult = super.validate(schema);

    if (!validationResult.isValid()) {
      return validationResult;
    }

    final String schemaVisibilityPropertyName = schema.getVisibilityProperty();

    if (schemaVisibilityPropertyName == null) {
      validationResult.addError("Schema is missing visibility property");
    } else if (schemaVisibilityPropertyName.equals(this.visibilityPropertyName)) {
      validationResult.addError(new StringBuffer("Schema visibility property is set to [").append(schemaVisibilityPropertyName)
          .append("] but should be set to [").append(this.visibilityPropertyName).append("]").toString());
    }

    for (final Map.Entry<String, ? extends SchemaElementDefinition> element : schema.getEdges().entrySet()) {
      validateElement(element.getKey(), schema, element.getValue(), validationResult);
    }

    return validationResult;

  }

  private ValidationResult validateElement(final String elementGroup, final Schema schema, final SchemaElementDefinition elementDefinition,
      final ValidationResult validationResult) {

    if (!elementDefinition.getProperties().contains(this.visibilityPropertyName)) {
      validationResult.addError("Schema is missing visibility property for element group [" + elementGroup + "]");
    }

    if (!elementDefinition.getGroupBy().contains(this.visibilityPropertyName)) {
      validationResult.addError("Schema is missing visibility property in group by for element group [" + elementGroup + "]");
    }

    return validationResult;
  }

  public final SchemaValidator initialize(final Map<String, String> configuration) {
    this.visibilityPropertyName = configuration.getOrDefault("gaffer.schema.validator.groupByVisibility", "VISIBILITY");
    return this;
  }

}