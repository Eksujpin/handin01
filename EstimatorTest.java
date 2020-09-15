import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class EstimatorTest {

    @Test
    public void lostCounterCombined() {
        List<String> sent = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05"));
        List<String> received = new ArrayList<>(Arrays.asList("00", "01", "01", "03", "02", "05"));
        
        int[] expected = new int[] {1, 1, 2};

        int[] actual = Estimator.LostCounter(sent, received);

        assertEquals(expected, actual);
    }

    @Test
    public void lostCounterLost() {
        List<String> sent = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05"));
        List<String> received = new ArrayList<>(Arrays.asList("00", "01", "04", "05"));
        
        int[] expected = new int[] {2, 0, 0};

        int[] actual = Estimator.LostCounter(sent, received);

        assertEquals(expected, actual);
    }

    @Test
    public void lostCounter() {
        List<String> sent = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05"));
        List<String> received = new ArrayList<>(Arrays.asList("05", "04", "03", "02", "01", "00"));
        
        int[] expected = new int[] {0, 0, 6};

        int[] actual = Estimator.LostCounter(sent, received);

        assertEquals(expected, actual);
    }

    /*@Test
    public void lostCounter() {
        List<String> sent = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05"));
        List<String> received = new ArrayList<>(Arrays.asList("05", "04", "03", "02", "01", "00"));
        
        int[] expected = new int[] {0, 0, 6};

        int[] actual = Estimator.LostCounter(sent, received);

        assertEquals(expected, actual);
    }
    
    @Test
    public void lostCounter() {
        List<String> sent = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05"));
        List<String> received = new ArrayList<>(Arrays.asList("06", "05", "04", "03", "02", "01"));
        
        int[] expected = new int[] {0, 0, 6};

        int[] actual = Estimator.LostCounter(sent, received);

        assertEquals(expected, actual);
    }*/

}