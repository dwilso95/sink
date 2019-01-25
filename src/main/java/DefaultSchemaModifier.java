import uk.gov.gchq.gaffer.store.schema.*;
import java.util.Map;

public class DefaultSchemaModifier implements SchemaModifier {
    
    public DefaultSchemaModifier(){
        
    }
    
    public Schema modifySchema(final Schema schema){
        final String visibilityProperty = schema.getVisibilityProperty();
        System.out.println("Visibility property: " + visibilityProperty);
        for(Map.Entry<String, ? extends SchemaElementDefinition> element : schema.getEdges().entrySet()){
            System.out.println(element.getKey());
            System.out.println(element.getValue());
        }
        return schema;
    }
    
    public void initialize(Map<String,String> properties){
        
    }
}