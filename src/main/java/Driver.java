import java.util.*;
import uk.gov.gchq.gaffer.jsonserialisation.*;
import uk.gov.gchq.gaffer.store.schema.*;
import uk.gov.gchq.gaffer.*;
import uk.gov.gchq.gaffer.data.*;

import com.google.common.collect.Sets;

import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.binaryoperator.KorypheBinaryOperator;

import uk.gov.gchq.gaffer.commonutil.StreamUtil;
import uk.gov.gchq.gaffer.data.element.IdentifierType;
import uk.gov.gchq.gaffer.data.element.function.ElementAggregator;
import uk.gov.gchq.gaffer.data.element.function.ElementFilter;
import uk.gov.gchq.gaffer.data.elementdefinition.exception.SchemaException;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.serialisation.Serialiser;
import uk.gov.gchq.gaffer.serialisation.ToBytesSerialiser;
import uk.gov.gchq.gaffer.serialisation.implementation.JavaSerialiser;
import uk.gov.gchq.gaffer.serialisation.implementation.MapSerialiser;
import uk.gov.gchq.gaffer.serialisation.implementation.StringSerialiser;
import uk.gov.gchq.gaffer.serialisation.implementation.raw.RawLongSerialiser;
import uk.gov.gchq.gaffer.store.library.HashMapGraphLibrary;
import uk.gov.gchq.koryphe.impl.predicate.Exists;
import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsXMoreThanY;
import uk.gov.gchq.koryphe.tuple.binaryoperator.TupleAdaptedBinaryOperator;
import uk.gov.gchq.koryphe.tuple.predicate.TupleAdaptedPredicate;

import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Driver {
    
    public static void main(String[] args) throws Exception {
        SchemaModifier modifier = new DefaultSchemaModifier();
        modifier.initialize(new HashMap<String, String>());
        Schema schema = createSchema();
        modifier.modifySchema(schema);
        //printSchemaAsJson(schema);
    }
    
    
    private static Schema createSchema() {
        MapSerialiser mapSerialiser = new MapSerialiser();
        mapSerialiser.setKeySerialiser(new StringSerialiser());
        mapSerialiser.setValueSerialiser(new RawLongSerialiser());
        mapSerialiser.setMapClass(LinkedHashMap.class);
        return new Schema.Builder()
                .edge(TestGroups.EDGE, new SchemaEdgeDefinition.Builder()
                        .source(TestTypes.ID_STRING)
                        .destination(TestTypes.ID_STRING)
                        .property(TestPropertyNames.PROP_1, TestTypes.PROP_STRING)
                        .property(TestPropertyNames.PROP_2, TestTypes.PROP_INTEGER)
                        .property(TestPropertyNames.TIMESTAMP, TestTypes.TIMESTAMP)
                        .groupBy(TestPropertyNames.PROP_1)
                        .description(EDGE_DESCRIPTION)
                        .validator(new ElementFilter.Builder()
                                .select(TestPropertyNames.PROP_1)
                                .execute(new ExampleFilterFunction())
                                .build())
                        .build())
                .entity(TestGroups.ENTITY, new SchemaEntityDefinition.Builder()
                        .vertex(TestTypes.ID_STRING)
                        .property(TestPropertyNames.PROP_1, TestTypes.PROP_STRING)
                        .property(TestPropertyNames.PROP_2, TestTypes.PROP_INTEGER)
                        .property(TestPropertyNames.TIMESTAMP, TestTypes.TIMESTAMP)
                        .groupBy(TestPropertyNames.PROP_1)
                        .description(EDGE_DESCRIPTION)
                        .validator(new ElementFilter.Builder()
                                .select(TestPropertyNames.PROP_1)
                                .execute(new ExampleFilterFunction())
                                .build())
                        .build())
                .type(TestTypes.ID_STRING, new TypeDefinition.Builder()
                        .clazz(String.class)
                        .description(STRING_TYPE_DESCRIPTION)
                        .build())
                .type(TestTypes.PROP_MAP, new TypeDefinition.Builder()
                        .description(MAP_TYPE_DESCRIPTION)
                        .clazz(LinkedHashMap.class)
                        .serialiser(mapSerialiser)
                        .build())
                .type(TestTypes.PROP_STRING, new TypeDefinition.Builder()
                        .clazz(String.class)
                        .description(STRING_TYPE_DESCRIPTION)
                        .build())
                .type(TestTypes.PROP_INTEGER, new TypeDefinition.Builder()
                        .clazz(Integer.class)
                        .description(INTEGER_TYPE_DESCRIPTION)
                        .build())
                .type(TestTypes.TIMESTAMP, new TypeDefinition.Builder()
                        .clazz(Long.class)
                        .description(TIMESTAMP_TYPE_DESCRIPTION)
                        .build())
                .visibilityProperty(TestPropertyNames.VISIBILITY)
                .timestampProperty(TestPropertyNames.TIMESTAMP)
                .config("key", "value")
                .build();
    }
    
    private static void printSchemaAsJson(Schema schema){
        System.out.println(new String(schema.toJson(true)));
    }
    private static void writeProgramaticSchemaAsJson() throws Exception {

        System.out.println(String.format("{%n" +
                "  \"edges\" : {%n" +
                "    \"BasicEdge\" : {%n" +
                "      \"properties\" : {%n" +
                "        \"property1\" : \"prop.string\",%n" +
                "        \"property2\" : \"prop.integer\",%n" +
                "        \"timestamp\" : \"timestamp\"%n" +
                "      },%n" +
                "      \"groupBy\" : [ \"property1\" ],%n" +
                "      \"description\" : \"Edge description\",%n" +
                "      \"source\" : \"id.string\",%n" +
                "      \"destination\" : \"id.string\",%n" +
                "      \"validateFunctions\" : [ {%n" +
                "        \"predicate\" : {%n" +
                "          \"class\" : \"ExampleFilterFunction\"%n" +
                "        },%n" +
                "        \"selection\" : [ \"property1\" ]%n" +
                "      } ]%n" +
                "    }%n" +
                "  },%n" +
                "  \"entities\" : {%n" +
                "    \"BasicEntity\" : {%n" +
                "      \"properties\" : {%n" +
                "        \"property1\" : \"prop.string\",%n" +
                "        \"property2\" : \"prop.integer\",%n" +
                "        \"timestamp\" : \"timestamp\"%n" +
                "      },%n" +
                "      \"groupBy\" : [ \"property1\" ],%n" +
                "      \"description\" : \"Edge description\",%n" +
                "      \"vertex\" : \"id.string\",%n" +
                "      \"validateFunctions\" : [ {%n" +
                "        \"predicate\" : {%n" +
                "          \"class\" : \"ExampleFilterFunction\"%n" +
                "        },%n" +
                "        \"selection\" : [ \"property1\" ]%n" +
                "      } ]%n" +
                "    }%n" +
                "  },%n" +
                "  \"types\" : {%n" +
                "    \"id.string\" : {%n" +
                "      \"description\" : \"String type description\",%n" +
                "      \"class\" : \"java.lang.String\"%n" +
                "    },%n" +
                "    \"prop.map\" : {%n" +
                "      \"serialiser\" : {%n" +
                "          \"class\" : \"uk.gov.gchq.gaffer.serialisation.implementation.MapSerialiser\",%n" +
                "          \"keySerialiser\" : \"uk.gov.gchq.gaffer.serialisation.implementation.StringSerialiser\",%n" +
                "          \"valueSerialiser\" : \"uk.gov.gchq.gaffer.serialisation.implementation.raw.RawLongSerialiser\",%n" +
                "          \"mapClass\" : \"java.util.LinkedHashMap\"%n" +
                "      },%n" +
                "      \"description\" : \"Map type description\",%n" +
                "      \"class\" : \"java.util.LinkedHashMap\"%n" +
                "    },%n" +
                "    \"prop.string\" : {%n" +
                "      \"description\" : \"String type description\",%n" +
                "      \"class\" : \"java.lang.String\"%n" +
                "    },%n" +
                "    \"prop.integer\" : {%n" +
                "      \"description\" : \"Integer type description\",%n" +
                "      \"class\" : \"java.lang.Integer\"%n" +
                "    },%n" +
                "    \"timestamp\" : {%n" +
                "      \"description\" : \"Timestamp type description\",%n" +
                "      \"class\" : \"java.lang.Long\"%n" +
                "    }%n" +
                "  },%n" +
                "  \"visibilityProperty\" : \"visibility\",%n" +
                "  \"timestampProperty\" : \"timestamp\",%n" +
                "  \"config\" : {\n" +
                "    \"key\" : \"value\",\n" +
                "    \"timestampProperty\" : \"timestamp\"\n" +
                "  }" +
                "}"));
    }

class TestGroups {
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



class TestPropertyNames {
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


class TestTypes {
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


class ExampleAggregateFunction extends KorypheBinaryOperator<Object> {
    @Override
    public Object _apply(final Object a, final Object b) {
        if (a.toString().compareTo(b.toString()) <= 0) {
            return a;
        }
        return b;
    }
}

static class ExampleFilterFunction extends KoryphePredicate<Object> {
    @Override
    public boolean test(final Object input) {
        return false;
    }
}
}