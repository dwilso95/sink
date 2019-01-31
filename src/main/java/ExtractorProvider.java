import java.util.Map;

import com.google.inject.Inject;

public class ExtractorProvider {

  final Map<String, VisibilityExtractor> map;

  @Inject
  public ExtractorProvider(final Map<String, VisibilityExtractor> map) {
    this.map = map;
  }

  public VisibilityExtractor getExtractor(String name) {
    return map.get(name);
  }

}
