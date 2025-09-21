import java.util.*;

public class QuickSort {
    public static void sort(int[] a) {
        if (a == null || a.length <= 1) return;
        qs(a, 0, a.length - 1, new Random());
    }

    private static void qs(int[] a, int l, int r, Random rnd) {
        while (l < r) {
            int p = l + rnd.nextInt(r - l + 1), pv = a[p]; 
            swap(a, p, r); 
            int i = l;
            for (int j = l; j < r; j++) if (a[j] <= pv) swap(a, i++, j);
            swap(a, i, r); 

            if (i - l < r - i) {
                if (l < i - 1) qs(a, l, i - 1, rnd); 
                l = i + 1; 
            } else {
                if (i + 1 < r) qs(a, i + 1, r, rnd); 
                r = i - 1; 
            }
        }
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    public static void main(String[] args) {
        int[] a = {5, 2, 9, 1, 5, 6, -3, 10, 0, 4, 7};
        System.out.println("before: " + Arrays.toString(a));
        sort(a);
        System.out.println("after:  " + Arrays.toString(a));
    }
}
