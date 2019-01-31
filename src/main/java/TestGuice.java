import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestGuice {

  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new TestModule());
    injector.getInstance(ServiceThingy.class).doStuff();

  }

}
