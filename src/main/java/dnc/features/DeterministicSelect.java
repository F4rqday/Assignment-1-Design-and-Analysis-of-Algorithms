package dnc.features;


public class DeterministicSelect {
    public static int kth(int[] a, int k) {
        if (a == null) throw new IllegalArgumentException("array is null");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        return select(a, 0, a.length - 1, k, null);
    }

    public static int kth(int[] a, int k, Metrics m) {
        long t0 = System.nanoTime();
        int ans = select(a, 0, a.length - 1, k, m);
        long t1 = System.nanoTime();
        if (m != null) m.timeMs = (t1 - t0) / 1_000_000;
        return ans;
    }

    private static int select(int[] a, int left, int right, int k, Metrics m) {
        while (true) {
            if (left == right) return a[left];
            if (m != null) m.enter();

            int pivot = medianOfMedians(a, left, right, m);
            int pivotIndex = partitionAroundValue(a, left, right, pivot, m);

            if (k == pivotIndex) { if (m != null) m.exit(); return a[k]; }
            else if (k < pivotIndex) { right = pivotIndex - 1; }
            else { left = pivotIndex + 1; }

            if (m != null) m.exit();
        }
    }

    private static int medianOfMedians(int[] a, int left, int right, Metrics m) {
        int n = right - left + 1;
        int groups = (n + 4) / 5;
        for (int g = 0; g < groups; g++) {
            int l = left + g*5;
            int r = Math.min(l + 5, right + 1);
            insertion(a, l, r, m);
            int medianIdx = l + (r - l - 1)/2;
            swap(a, left + g, medianIdx, m);
        }
        int mid = left + (groups - 1)/2;
        return select(a, left, left + groups - 1, mid, m);
    }

    private static int partitionAroundValue(int[] a, int left, int right, int pv, Metrics m) {
        int p = left;
        while (p <= right && a[p] != pv) p++;
        if (p > right) p = right;
        swap(a, p, right, m);
        int store = left;
        for (int i = left; i < right; i++) {
            if (m != null) m.incComparisons();
            if (a[i] < pv) { swap(a, store++, i, m); }
        }
        swap(a, store, right, m);
        return store;
    }

    private static void insertion(int[] a, int l, int r, Metrics m) {
        for (int i = l + 1; i < r; i++) {
            int x = a[i], j = i - 1;
            while (j >= l) {
                if (m != null) m.incComparisons();
                if (a[j] > x) { a[j + 1] = a[j]; if (m != null) m.incMoves(); j--; }
                else break;
            }
            a[j + 1] = x; if (m != null) m.incMoves();
        }
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i != j) { int t = a[i]; a[i] = a[j]; a[j] = t; if (m != null) m.moves += 2; }
    }
}
