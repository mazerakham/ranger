package ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import ranger.math.SparseVector;

public class SparseVectorTest {

  @Test
  public void testPlus() {
    UUID x = UUID.randomUUID();
    UUID y = UUID.randomUUID();
    UUID z = UUID.randomUUID();
    SparseVector a = new SparseVector();
    SparseVector b = new SparseVector();
    a.put(x, 42.0);
    a.put(y, 64.0);
    b.put(y, 5.0);
    b.put(z, 420.0);
    SparseVector sum = a.plus(b);
    checkState(sum.get(x) == 42.0);
    checkState(sum.get(y) == 69.0);
    checkState(sum.get(z) == 420.0);
  }
}
