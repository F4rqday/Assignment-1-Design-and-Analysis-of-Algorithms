package dnc.features;

public class Metrics {
    public long comparisons = 0;
    public long moves = 0;
    public long allocations = 0;
    public int  curDepth = 0;
    public int  maxRecDepth = 0;
    public long timeMs = 0;

    public void incComparisons() { comparisons++; }
    public void incMoves()       { moves++; }
    public void incAllocations() { allocations++; }

    public void enter() { 
        curDepth++;
        if (curDepth > maxRecDepth) maxRecDepth = curDepth;
    }
    public void exit()  { 
        curDepth--;
    }

    public void reset() {
        comparisons = moves = allocations = timeMs = 0;
        curDepth = maxRecDepth = 0;
    }

    @Override public String toString() {
        return String.format("time=%dms, cmp=%d, moves=%d, alloc=%d, maxDepth=%d",
                timeMs, comparisons, moves, allocations, maxRecDepth);
    }
}
