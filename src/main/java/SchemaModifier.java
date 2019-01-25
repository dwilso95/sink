import uk.gov.gchq.gaffer.store.schema.Schema;
import java.util.Map;

public interface SchemaModifier {
    
    Schema modifySchema(final Schema schema);
    
    void initialize(final Map<String, String> configuration);
}