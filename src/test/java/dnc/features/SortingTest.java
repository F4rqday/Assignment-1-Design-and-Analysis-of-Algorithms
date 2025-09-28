package dnc.features;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

public class SortingTest {
    @Test void mergeSort_ok() {
        int[] a = {5,3,2,4,1};
        MergeSort.sort(a, new Metrics());
        assertArrayEquals(new int[]{1,2,3,4,5}, a);
    }
    @Test void quickSort_ok() {
        int[] a = {5,3,2,4,1,1,9,0};
        QuickSort.sort(a, new Metrics());
        int[] b = a.clone(); Arrays.sort(b);
        assertArrayEquals(b, a);
    }
}
