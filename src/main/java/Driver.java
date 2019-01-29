import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import uk.gov.gchq.gaffer.data.element.function.ElementFilter;
import uk.gov.gchq.gaffer.serialisation.implementation.MapSerialiser;
import uk.gov.gchq.gaffer.serialisation.implementation.StringSerialiser;
import uk.gov.gchq.gaffer.serialisation.implementation.raw.RawLongSerialiser;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.store.schema.SchemaEntityDefinition;
import uk.gov.gchq.gaffer.store.schema.TypeDefinition;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.binaryoperator.KorypheBinaryOperator;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;

@SuppressWarnings("deprecation")
public class Driver {

  public static void main(String[] args) throws Exception {
    Map<String, String> config = Maps.newHashMap();
    final Schema schema = createSchema();
    // printSchemaAsJson(schema);

    final ValidationResult schemaValidationResult = new GroupByVisibilityPropertySchemaValidator().initialize(config).validate(schema);
    System.out.println("<------- Checking Validity ------->");
    System.out.println(schemaValidationResult.isValid());
    for (final String error : schemaValidationResult.getErrors()) {
      System.out.println(error);
    }

    final Schema modifiedSchema = new GroupByVisibilityPropertySchemaModifier().initialize(config).modify(schema);
    final ValidationResult modifiedSchemaValidationResult = new GroupByVisibilityPropertySchemaValidator().initialize(config)
        .validate(modifiedSchema);
    
    System.out.println("\n<------- Checking Validity ------->");
    System.out.println(modifiedSchemaValidationResult.isValid());
    for (final String error : modifiedSchemaValidationResult.getErrors()) {
      System.out.println(error);
    }

  }

  private static Schema createRealSchema() {
    MapSerialiser mapSerialiser = new MapSerialiser();
    mapSerialiser.setKeySerialiser(new StringSerialiser());
    mapSerialiser.setValueSerialiser(new RawLongSerialiser());
    mapSerialiser.setMapClass(LinkedHashMap.class);
    return new Schema.Builder()
        .edge("edge", new SchemaEdgeDefinition.Builder().source("source1").destination("dest1")
            .property(TestPropertyNames.PROP_1, "java.lang.String").property(TestPropertyNames.PROP_2, "java.lang.Integer")
            .property(TestPropertyNames.TIMESTAMP, "java.util.Date").groupBy(TestPropertyNames.PROP_1).description(EDGE_DESCRIPTION)
            .validator(new ElementFilter.Builder().select(TestPropertyNames.PROP_1).execute(new ExampleFilterFunction()).build()).build())
//        .entity(TestGroups.ENTITY, new SchemaEntityDefinition.Builder().vertex(TestTypes.ID_STRING)
//            .property(TestPropertyNames.PROP_1, TestTypes.PROP_STRING).property(TestPropertyNames.PROP_2, TestTypes.PROP_INTEGER)
//            .property(TestPropertyNames.TIMESTAMP, TestTypes.TIMESTAMP).groupBy(TestPropertyNames.PROP_1).description(EDGE_DESCRIPTION)
//            .validator(new ElementFilter.Builder().select(TestPropertyNames.PROP_1).execute(new ExampleFilterFunction()).build()).build())
//        .type(TestTypes.ID_STRING, new TypeDefinition.Builder().clazz(String.class).description(STRING_TYPE_DESCRIPTION).build())
//        .type(TestTypes.PROP_MAP,
//            new TypeDefinition.Builder().description(MAP_TYPE_DESCRIPTION).clazz(LinkedHashMap.class).serialiser(mapSerialiser).build())
//        .type(TestTypes.PROP_STRING, new TypeDefinition.Builder().clazz(String.class).description(STRING_TYPE_DESCRIPTION).build())
//        .type(TestTypes.PROP_INTEGER, new TypeDefinition.Builder().clazz(Integer.class).description(INTEGER_TYPE_DESCRIPTION).build())
//        .type(TestTypes.TIMESTAMP, new TypeDefinition.Builder().clazz(Long.class).description(TIMESTAMP_TYPE_DESCRIPTION).build())
        .timestampProperty(TestPropertyNames.TIMESTAMP).config("key", "value").build();
  }

  private static Schema createSchema() {
    MapSerialiser mapSerialiser = new MapSerialiser();
    mapSerialiser.setKeySerialiser(new StringSerialiser());
    mapSerialiser.setValueSerialiser(new RawLongSerialiser());
    mapSerialiser.setMapClass(LinkedHashMap.class);
    return new Schema.Builder()
        .edge(TestGroups.EDGE, new SchemaEdgeDefinition.Builder().source(TestTypes.ID_STRING).destination(TestTypes.ID_STRING)
            .property(TestPropertyNames.PROP_1, TestTypes.PROP_STRING).property(TestPropertyNames.PROP_2, TestTypes.PROP_INTEGER)
            .property(TestPropertyNames.TIMESTAMP, TestTypes.TIMESTAMP).groupBy(TestPropertyNames.PROP_1).description(EDGE_DESCRIPTION)
            .validator(new ElementFilter.Builder().select(TestPropertyNames.PROP_1).execute(new ExampleFilterFunction()).build()).build())
        .entity(TestGroups.ENTITY, new SchemaEntityDefinition.Builder().vertex(TestTypes.ID_STRING)
            .property(TestPropertyNames.PROP_1, TestTypes.PROP_STRING).property(TestPropertyNames.PROP_2, TestTypes.PROP_INTEGER)
            .property(TestPropertyNames.TIMESTAMP, TestTypes.TIMESTAMP).groupBy(TestPropertyNames.PROP_1).description(EDGE_DESCRIPTION)
            .validator(new ElementFilter.Builder().select(TestPropertyNames.PROP_1).execute(new ExampleFilterFunction()).build()).build())
        .type(TestTypes.ID_STRING, new TypeDefinition.Builder().clazz(String.class).description(STRING_TYPE_DESCRIPTION).build())
        .type(TestTypes.PROP_MAP,
            new TypeDefinition.Builder().description(MAP_TYPE_DESCRIPTION).clazz(LinkedHashMap.class).serialiser(mapSerialiser).build())
        .type(TestTypes.PROP_STRING, new TypeDefinition.Builder().clazz(String.class).description(STRING_TYPE_DESCRIPTION).build())
        .type(TestTypes.PROP_INTEGER, new TypeDefinition.Builder().clazz(Integer.class).description(INTEGER_TYPE_DESCRIPTION).build())
        .type(TestTypes.TIMESTAMP, new TypeDefinition.Builder().clazz(Long.class).description(TIMESTAMP_TYPE_DESCRIPTION).build())
        .visibilityProperty(TestPropertyNames.VISIBILITY).timestampProperty(TestPropertyNames.TIMESTAMP).config("key", "value").build();
  }

  
  private static void printSchemaAsJson(Schema schema) {
    System.out.println(new String(schema.toJson(true)));
  }

  public class TestGroups {
    public static final String ENTITY = "BasicEntity";
    public static final String ENTITY_2 = "BasicEntity2";
    public static final String ENTITY_3 = "BasicEntity3";
    public static final String ENTITY_4 = "BasicEntity4";
    public static final String ENTITY_5 = "BasicEntity5";
    public static final String NON_AGG_ENTITY = "NonAggEntity";

    public static final String EDGE = "BasicEdge";
    public static final String EDGE_2 = "BasicEdge2";
    public static final String EDGE_3 = "BasicEdge3";
    public static final String EDGE_4 = "BasicEdge4";
    public static final String EDGE_5 = "BasicEdge5";
    public static final String NON_AGG_EDGE = "NonAggEdge";
  }

  public class TestPropertyNames {
    public static final String INT = "intProperty";
    public static final String STRING = "stringProperty";
    public static final String SET = "setProperty";
    public static final String DATE = "dateProperty";
    public static final String TIMESTAMP = "timestamp";
    public static final String COUNT = "count";
    public static final String VISIBILITY = "visibility";

    public static final String PROP_1 = "property1";
    public static final String PROP_2 = "property2";
    public static final String PROP_3 = "property3";
    public static final String PROP_4 = "property4";
    public static final String PROP_5 = "property5";

    public static final String TRANSIENT_1 = "transientProperty1";
  }

  public class TestTypes {
    // Type Names
    public static final String TIMESTAMP = "timestamp";
    public static final String TIMESTAMP_2 = "timestamp2";
    public static final String VISIBILITY = "visibility";
    public static final String ID_STRING = "id.string";
    public static final String DIRECTED_EITHER = "directed.either";
    public static final String DIRECTED_TRUE = "directed.true";
    public static final String PROP_STRING = "prop.string";
    public static final String PROP_INTEGER = "prop.integer";
    public static final String PROP_INTEGER_2 = "prop.integer.2";
    public static final String PROP_LONG = "prop.long";
    public static final String PROP_COUNT = "prop.count";
    public static final String PROP_MAP = "prop.map";
    public static final String PROP_SET_STRING = "prop.set.string";
    public static final String VERTEX_STRING = "vertex.string";
    public static final String PROP_DATE = "prop.date";
  }

  public static final String EDGE_DESCRIPTION = "Edge description";
  public static final String ENTITY_DESCRIPTION = "Entity description";
  public static final String STRING_TYPE_DESCRIPTION = "String type description";
  public static final String INTEGER_TYPE_DESCRIPTION = "Integer type description";
  public static final String TIMESTAMP_TYPE_DESCRIPTION = "Timestamp type description";
  public static final String DATE_TYPE_DESCRIPTION = "Date type description";
  public static final String MAP_TYPE_DESCRIPTION = "Map type description";

  public class ExampleAggregateFunction extends KorypheBinaryOperator<Object> {
    @Override
    public Object _apply(final Object a, final Object b) {
      if (a.toString().compareTo(b.toString()) <= 0) {
        return a;
      }
      return b;
    }
  }

  public static class ExampleFilterFunction extends KoryphePredicate<Object> {
    @Override
    public boolean test(final Object input) {
      return false;
    }
  }
}