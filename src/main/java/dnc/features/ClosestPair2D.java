package dnc.features;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair2D {
    public static double solve(double[][] pts) { return solve(pts, null); }

    public static double solve(double[][] pts, Metrics m) {
        long t0 = System.nanoTime();
        int n = pts.length;
        double[][] px = Arrays.copyOf(pts, n);
        double[][] py = Arrays.copyOf(pts, n);
        Arrays.sort(px, Comparator.comparingDouble(p -> p[0]));
        Arrays.sort(py, Comparator.comparingDouble(p -> p[1]));
        if (m != null) m.allocations += 2L * n;
        double ans = dnc(px, py, 0, n, m);
        long t1 = System.nanoTime();
        if (m != null) m.timeMs = (t1 - t0) / 1_000_000;
        return ans;
    }

    private static double dnc(double[][] px, double[][] py, int l, int r, Metrics m) {
        int n = r - l;
        if (n <= 3) return brute(px, l, r);
        if (m != null) m.enter();

        int mid = l + n/2;
        double midx = px[mid][0];

        double[][] pyL = new double[n][];
        double[][] pyR = new double[n][];
        int il = 0, ir = 0;
        for (double[] p : py) {
            if (p[0] < midx || (p[0] == midx && indexOf(px, l, mid, p) != -1)) pyL[il++] = p;
            else pyR[ir++] = p;
        }

        double dl = dnc(px, Arrays.copyOf(pyL, il), l, mid, m);
        double dr = dnc(px, Arrays.copyOf(pyR, ir), mid, r, m);
        double d = Math.min(dl, dr);

        
        double[][] strip = new double[n][];
        int s = 0;
        for (int i = 0; i < py.length; i++) {
            double[] p = py[i];
            if (Math.abs(p[0] - midx) < d) strip[s++] = p;
        }
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s && (strip[j][1] - strip[i][1]) < d; j++) {
                d = Math.min(d, dist(strip[i], strip[j]));
                if (m != null) m.incComparisons();
            }
        }

        if (m != null) m.exit();
        return d;
    }

    private static int indexOf(double[][] a, int l, int r, double[] p) {
        for (int i = l; i < r; i++) if (a[i] == p) return i; return -1;
    }

    private static double brute(double[][] a, int l, int r) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = l; i < r; i++)
            for (int j = i + 1; j < r; j++)
                best = Math.min(best, dist(a[i], a[j]));
        return best;
    }

    private static double dist(double[] p, double[] q) {
        double dx = p[0] - q[0], dy = p[1] - q[1];
        return Math.hypot(dx, dy);
    }
}
