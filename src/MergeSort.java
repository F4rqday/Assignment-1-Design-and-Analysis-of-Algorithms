import java.util.*;

public class MergeSort {
    private static final int CUTOFF = 16;

    public static void sort(int[] a) {
        if (a == null || a.length <= 1) return;
        int[] buf = new int[a.length];
        ms(a, 0, a.length - 1, buf);
    }

    private static void ms(int[] a, int l, int r, int[] b) {
        if (r - l + 1 <= CUTOFF) { ins(a, l, r); return; }
        int m = l + (r - l) / 2;
        ms(a, l, m, b); ms(a, m + 1, r, b);
        if (a[m] <= a[m + 1]) return; 
        mg(a, l, m, r, b);
    }

    private static void ins(int[] a, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int x = a[i], j = i - 1;
            while (j >= l && a[j] > x) { a[j + 1] = a[j]; j--; }
            a[j + 1] = x;
        }
    }

    private static void mg(int[] a, int l, int m, int r, int[] b) {
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) b[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        while (i <= m) b[k++] = a[i++];
        while (j <= r) b[k++] = a[j++];
        for (int t = l; t <= r; t++) a[t] = b[t];
    }

    public static void main(String[] args) {
        int[] a = {5,2,9,1,5,6,-3,10,0,4,7};
        System.out.println("before: " + Arrays.toString(a));
        sort(a);
        System.out.println("after:  " + Arrays.toString(a));
    }
}
