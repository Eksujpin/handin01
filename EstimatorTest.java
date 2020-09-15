import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class EstimatorTest {

    @Test
    public void lostCounter() {
        List<String> sent = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05"));
        List<String> received = new ArrayList<>(Arrays.asList("00", "01", "01", "03", "02", "05"));
        
        int[] expected = new int[] {1, 1, 2};

        int[] actual = Estimator.LostCounter(sent, received);

        assertEquals(expected, actual);
    }

}