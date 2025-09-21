import java.util.*;

public class ClosestPair2D {
    public static class Pt { public int x,y; public Pt(int x,int y){this.x=x;this.y=y;} }
    public static class Res { public Pt a,b; public double d; Res(Pt a,Pt b,double d){this.a=a;this.b=b;this.d=d;} }

    public static Res closest(Pt[] pts){
        if(pts==null || pts.length<2) throw new IllegalArgumentException("need >=2 points");
        Pt[] x = Arrays.copyOf(pts, pts.length);
        Arrays.sort(x, Comparator.comparingInt(p->p.x)); 
        Pt[] tmp = new Pt[x.length];                     
        return solve(x, 0, x.length-1, tmp);
    }

    private static Res solve(Pt[] a, int l, int r, Pt[] tmp){
        int n = r-l+1;
        if(n<=3){ 
            Res best = new Res(a[l], a[l+1], dist(a[l],a[l+1]));
            for(int i=l;i<=r;i++) for(int j=i+1;j<=r;j++){
                double d=dist(a[i],a[j]); if(d<best.d) best=new Res(a[i],a[j],d);
            }
            Arrays.sort(a, l, r+1, Comparator.comparingInt(p->p.y));
            return best;
        }
        int m=l+(r-l)/2, midX=a[m].x;
        Res L=solve(a,l,m,tmp), R=solve(a,m+1,r,tmp);
        Res best = L.d<=R.d?L:R; double delta = best.d;

        int i=l, j=m+1, k=l;
        while(i<=m && j<=r) tmp[k++] = (a[i].y<=a[j].y)? a[i++] : a[j++];
        while(i<=m) tmp[k++]=a[i++]; while(j<=r) tmp[k++]=a[j++];
        for(int t=l;t<=r;t++) a[t]=tmp[t];

        Pt[] strip = new Pt[n]; int s=0;
        for(int t=l;t<=r;t++) if(Math.abs(a[t].x - midX) < delta) strip[s++] = a[t];

        for(int u=0; u<s; u++){
            for(int v=u+1; v<s && (strip[v].y - strip[u].y) < delta && v<=u+7; v++){
                double d=dist(strip[u], strip[v]); if(d<best.d){ best=new Res(strip[u],strip[v],d); delta=d; }
            }
        }
        return best;
    }

    private static double dist(Pt p, Pt q){
        long dx=p.x-q.x, dy=p.y-q.y; 
        return Math.sqrt((double)dx*dx + (double)dy*dy);
    }

    public static void main(String[] args){
        Pt[] pts = { new Pt(0,0), new Pt(5,4), new Pt(3,1), new Pt(1,2), new Pt(6,1),
                     new Pt(4,4), new Pt(7,3), new Pt(2,1), new Pt(9,9), new Pt(8,3) };
        Res r = closest(pts);
        System.out.println("Closest: ("+r.a.x+","+r.a.y+") - ("+r.b.x+","+r.b.y+"), d="+r.d);
    }
}
