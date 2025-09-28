package dnc.features;

import java.util.Random;

public class QuickSort {
    public static void sort(int[] a) { sort(a, null); }

    public static void sort(int[] a, Metrics m) {
        long t0 = System.nanoTime();
        quickIterSmallerFirst(a, 0, a.length - 1, new Random(), m);
        long t1 = System.nanoTime();
        if (m != null) m.timeMs = (t1 - t0) / 1_000_000;
    }

    private static void quickIterSmallerFirst(int[] a, int l, int r, Random rnd, Metrics m) {
        while (l < r) {
            if (m != null) m.enter();
            int i = l, j = r, p = a[l + rnd.nextInt(r - l + 1)];
            while (i <= j) {
                while (a[i] < p) { i++; if (m != null) m.incComparisons(); }
                while (a[j] > p) { j--; if (m != null) m.incComparisons(); }
                if (i <= j) {
                    swap(a, i, j); if (m != null) m.moves += 2;
                    i++; j--;
                }
            }
            // recurse on smaller segment; iterate on larger to bound stack
            if (j - l < r - i) {
                if (l < j) quickIterSmallerFirst(a, l, j, rnd, m);
                l = i;
            } else {
                if (i < r) quickIterSmallerFirst(a, i, r, rnd, m);
                r = j;
            }
            if (m != null) m.exit();
        }
    }

    private static void swap(int[] a, int i, int j) {
        if (i != j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
    }
}
