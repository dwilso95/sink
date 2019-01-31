import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class TestModule extends AbstractModule {

  public TestModule() {

  }

  @Override
  protected void configure() {
    final MapBinder<String, VisibilityExtractor> extractorBinder = MapBinder.newMapBinder(binder(), String.class,
        VisibilityExtractor.class);
    extractorBinder.addBinding("DEFAULT").to(DefaultVisibilityExtractor.class);
  }

}
