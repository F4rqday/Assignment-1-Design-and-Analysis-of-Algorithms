package dnc.features;

public class MergeSort {
    private static final int CUTOFF = 16; 

    public static void sort(int[] a) {
        int[] buf = new int[a.length];
        ms(a, buf, 0, a.length);
    }

    public static void sort(int[] a, Metrics m) {
        long t0 = System.nanoTime();
        int[] buf = new int[a.length];
        if (m != null) m.allocations += a.length; 
        ms(a, buf, 0, a.length, m);
        long t1 = System.nanoTime();
        if (m != null) m.timeMs = (t1 - t0) / 1_000_000;
    }

    private static void ms(int[] a, int[] b, int l, int r) {
        int n = r - l;
        if (n <= 1) return;
        if (n <= CUTOFF) { insertion(a, l, r); return; }
        int m = l + n / 2;
        ms(a, b, l, m);
        ms(a, b, m, r);
        merge(a, b, l, m, r);
    }

    private static void ms(int[] a, int[] b, int l, int r, Metrics m) {
        int n = r - l;
        if (n <= 1) return;
        if (m != null) m.enter();
        if (n <= CUTOFF) { insertion(a, l, r, m); if (m != null) m.exit(); return; }
        int mid = l + n/2;
        ms(a, b, l, mid, m);
        ms(a, b, mid, r, m);
        merge(a, b, l, mid, r, m);
        if (m != null) m.exit();
    }

    private static void merge(int[] a, int[] b, int l, int m, int r) {
        int i=l, j=m, k=0;
        while (i<m && j<r) b[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        while (i<m) b[k++] = a[i++];
        while (j<r) b[k++] = a[j++];
        System.arraycopy(b, 0, a, l, k);
    }
    private static void merge(int[] a, int[] b, int l, int m, int r, Metrics mt) {
        int i=l, j=m, k=0;
        while (i<m && j<r) {
            if (mt != null) mt.incComparisons();
            b[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
            if (mt != null) mt.incMoves();
        }
        while (i<m) { b[k++] = a[i++]; if (mt != null) mt.incMoves(); }
        while (j<r) { b[k++] = a[j++]; if (mt != null) mt.incMoves(); }
        System.arraycopy(b, 0, a, l, k);
        if (mt != null) mt.moves += k;
    }

    private static void insertion(int[] a, int l, int r) {
        for (int i = l+1; i < r; i++) {
            int x = a[i], j = i-1;
            while (j >= l && a[j] > x) { a[j+1] = a[j]; j--; }
            a[j+1] = x;
        }
    }
    private static void insertion(int[] a, int l, int r, Metrics m) {
        for (int i = l+1; i < r; i++) {
            int x = a[i], j = i-1;
            while (j >= l) {
                if (m != null) m.incComparisons();
                if (a[j] > x) { a[j+1] = a[j]; if (m != null) m.incMoves(); j--; }
                else break;
            }
            a[j+1] = x; if (m != null) m.incMoves();
        }
    }
}
