import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestTest {

  @SneakyThrows
  @Test
  public void test(){
    Assertions.assertThat(2).isEqualTo(Integer.valueOf(2));

    String path = System.getProperty("user.dir");

    System.out.println(path);

  }
}
