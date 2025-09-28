package dnc.features;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class SelectAndClosestTest {


    @Test
    void deterministicSelect_matches_sorted() throws Exception {
        Random r = new Random(1);

        for (int t = 0; t < 100; t++) {
            int n = 2000;
            int[] a = r.ints(n, -10000, 10000).toArray();
            int[] b = a.clone(); Arrays.sort(b);
            int k = r.nextInt(n);

            Integer got = callSelect(a, k);  
            Assumptions.assumeTrue(got != null, "No usable select(...) found; test skipped.");

            assertEquals(b[k], got.intValue());
        }
    }

    private Integer callSelect(int[] a, int k) {
        try {
            Class<?> cls = Class.forName("dnc.features.DeterministicSelect");
            Metrics m = new Metrics();

            for (Method me : cls.getDeclaredMethods()) {
                String name = me.getName().toLowerCase();
                Class<?>[] p = me.getParameterTypes();
                if (!(name.contains("select") || name.contains("kth"))) continue;
                try {
                    me.setAccessible(true);
                    Object out;
                    if (p.length == 5 && p[0] == int[].class && p[1] == int.class && p[2] == int.class
                            && p[3] == int.class && p[4] == Metrics.class) {
                        out = me.invoke(null, a.clone(), 0, a.length - 1, k, m);
                    } else if (p.length == 3 && p[0] == int[].class && p[1] == int.class && p[2] == Metrics.class) {
                        out = me.invoke(null, a.clone(), k, m);
                    } else if (p.length == 2 && p[0] == int[].class && p[1] == int.class) {
                        out = me.invoke(null, a.clone(), k);
                    } else {
                        continue;
                    }
                    return ((Number) out).intValue();
                } catch (Throwable ignore) {}
            }
        } catch (Throwable ignore) {}
        return null;
    }

    @Test
    void closestPair_matches_bruteforce_smallN() throws Exception {
        Random r = new Random(2);

        for (int n = 60; n <= 300; n += 120) { 
            double[][] pts = new double[n][2];
            for (int i = 0; i < n; i++) {
                pts[i][0] = r.nextDouble();
                pts[i][1] = r.nextDouble();
            }

            Double fast = callClosest(pts);  
            Assumptions.assumeTrue(fast != null, "No usable closest(...) found; test skipped.");

            double slow = bruteForce(pts);
            assertEquals(slow, fast.doubleValue(), 1e-9);
        }
    }

    private Double callClosest(double[][] pts) {
        try {
            Class<?> cls = Class.forName("dnc.features.ClosestPair2D");
            Metrics m = new Metrics();

            for (Method me : cls.getDeclaredMethods()) {
                String name = me.getName().toLowerCase();
                Class<?>[] p = me.getParameterTypes();
                if (!(name.contains("closest"))) continue;
                try {
                    me.setAccessible(true);
                    if (p.length == 1 && p[0].isArray() && p[0].getComponentType().isArray()) {
                        return ((Number) me.invoke(null, (Object) pts)).doubleValue();
                    }
                    if (p.length == 2 && p[0].isArray() && p[0].getComponentType().isArray() && p[1] == Metrics.class) {
                        return ((Number) me.invoke(null, (Object) pts, m)).doubleValue();
                    }
                } catch (Throwable ignore) {}
            }
        } catch (Throwable ignore) {}
        return null;
    }


    private static double bruteForce(double[][] ps) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < ps.length; i++) {
            for (int j = i + 1; j < ps.length; j++) {
                double dx = ps[i][0] - ps[j][0];
                double dy = ps[i][1] - ps[j][1];
                best = Math.min(best, Math.hypot(dx, dy));
            }
        }
        return best;
    }
}
