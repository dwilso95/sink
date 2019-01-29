import java.util.Map;
import java.util.Map.Entry;

import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.store.schema.SchemaElementDefinition;
import uk.gov.gchq.gaffer.store.schema.SchemaEntityDefinition;
import uk.gov.gchq.gaffer.store.schema.TypeDefinition;

public class GroupByVisibilityPropertySchemaModifier extends DefaultSchemaModifier {

  private String visibilityPropertyName;

  public final Schema modify(final Schema schema) {
    final Schema.Builder schemaBuilder = new Schema.Builder();
    final String schemaVisibilityPropertyName = schema.getVisibilityProperty();

    
    // modify type definitions
    for (final Entry<String, TypeDefinition> typeDefinitionEntry : schema.getTypes().entrySet()) {
      
      if (schema.getTypes().containsKey(this.visibilityPropertyName)) {
        // check class for type
        
      } else {
        schemaBuilder.type(this.visibilityPropertyName, String.class);
      }
      
      System.out.println(typeDefinitionEntry.getValue().getFullClassString());
    }

    if (schemaVisibilityPropertyName == null || schemaVisibilityPropertyName.equals("visibilityPropertyName")) {
      schemaBuilder.visibilityProperty(this.visibilityPropertyName);
    }

    for (final Map.Entry<String, ? extends SchemaElementDefinition> element : schema.getEdges().entrySet()) {
      modifyElement(element.getKey(), element.getValue(), schema, schemaBuilder);
    }

    return schemaBuilder.build();
  }

  private void modifyElement(final String elementGroup, final SchemaElementDefinition elementDefinition, final Schema schema,
      final Schema.Builder schemaBuilder) {

    if (!elementDefinition.getProperties().contains(this.visibilityPropertyName)) {
      if (elementDefinition instanceof SchemaEdgeDefinition) {
        final SchemaEdgeDefinition edgeDef = (SchemaEdgeDefinition) elementDefinition;
        final SchemaEdgeDefinition.Builder schemaEdgeDefinitionBuilder = new SchemaEdgeDefinition.Builder().merge(edgeDef);

        if (!edgeDef.getProperties().contains(this.visibilityPropertyName)) {
          schemaEdgeDefinitionBuilder.property(this.visibilityPropertyName, "java.lang.String");
        }

        if (!elementDefinition.getGroupBy().contains(this.visibilityPropertyName)) {
          schemaEdgeDefinitionBuilder.groupBy(this.visibilityPropertyName);
        }

        schemaBuilder.edge(elementGroup, schemaEdgeDefinitionBuilder.build());
      } else if (elementDefinition instanceof SchemaEntityDefinition) {
        final SchemaEntityDefinition entityDef = (SchemaEntityDefinition) elementDefinition;
        final SchemaEntityDefinition.Builder schemaEntityDefinitionBuilder = new SchemaEntityDefinition.Builder().merge(entityDef);

        if (!entityDef.getProperties().contains(this.visibilityPropertyName)) {
          schemaEntityDefinitionBuilder.property(this.visibilityPropertyName, "java.lang.String");
        }

        if (!entityDef.getGroupBy().contains(this.visibilityPropertyName)) {
          schemaEntityDefinitionBuilder.groupBy(this.visibilityPropertyName);
        }

        schemaBuilder.entity(elementGroup, schemaEntityDefinitionBuilder.build());
      }
    }

  }

  public final SchemaModifier initialize(final Map<String, String> configuration) {
    this.visibilityPropertyName = configuration.getOrDefault("gaffer.schema.modifier.groupByVisibility", "VISIBILITY");
    return this;
  }

}