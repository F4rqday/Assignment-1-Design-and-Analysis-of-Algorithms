import java.util.*;

public class DeterministicSelect {
    public static int select(int[] a, int k) {
        if (a == null || k < 0 || k >= a.length) throw new IllegalArgumentException();
        return sel(a, 0, a.length - 1, k);
    }

    private static int sel(int[] a, int l, int r, int k) {
        while (true) {
            if (l == r) return a[l];
            int p = pivot(a, l, r);        
            int m = part(a, l, r, p);      
            if (k == m) return a[m];       
            else if (k < m) r = m - 1;     
            else l = m + 1;                
        }
    }

    private static int pivot(int[] a, int l, int r) {
        int n = r - l + 1;
        if (n <= 5) { isort(a, l, r); return l + (n - 1) / 2; }
        int c = 0;
        for (int i = l; i <= r; i += 5) {
            int rr = Math.min(i + 4, r);
            isort(a, i, rr);
            int med = i + (Math.min(rr - i + 1, 5) - 1) / 2;
            swap(a, l + c, med);
            c++;
        }
        int mid = l + (c - 1) / 2;
        int val = sel(a, l, l + c - 1, mid);  
        for (int i = l; i <= r; i++) if (a[i] == val) return i;
        return l;
    }

    private static void isort(int[] a, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int x = a[i], j = i - 1;
            while (j >= l && a[j] > x) { a[j + 1] = a[j]; j--; }
            a[j + 1] = x;
        }
    }

    private static int part(int[] a, int l, int r, int p) {
        int pv = a[p]; swap(a, p, r);
        int i = l;
        for (int j = l; j < r; j++) if (a[j] <= pv) swap(a, i++, j);
        swap(a, i, r);
        return i;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    public static void main(String[] args) {
        int[] a = {7, 1, 5, 3, 9, 2, 6, 8, 4, 0};
        int k = 4;
        int val = select(Arrays.copyOf(a, a.length), k);
        Arrays.sort(a);
        System.out.println("k=" + k + " -> " + val + " (ожидалось " + a[k] + ")");
    }
}
