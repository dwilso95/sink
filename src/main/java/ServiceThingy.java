import com.google.inject.Inject;

public class ServiceThingy {

  @Inject
  private ExtractorProvider extractorProvider;

  public void doStuff() {
    String v = extractorProvider.getExtractor("DEFAULT").extractVisibility(new Entity());
    System.out.println(v);
  }

}
