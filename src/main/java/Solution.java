import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Solution {
    private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    private final int aLen;
    private final long[] a;
    private final List<Long> powersOfTwoList;

    public Solution(int aLen, long randomMin, int randomMax) {
        if (aLen > SOFT_MAX_ARRAY_LENGTH) {
            throw new IllegalArgumentException(String.format("Requested array size of %d is greater than max=%d", aLen, SOFT_MAX_ARRAY_LENGTH));
        }
        this.aLen = aLen;
        this.a = generateSortedArray(randomMin, randomMax);

        this.powersOfTwoList = new ArrayList<>();
    }

    private long[] generateSortedArray(long randomMin, long randomMax) {
        long[] arr = new Random().longs(aLen, randomMin, randomMax).toArray();
        Arrays.sort(arr);
        return arr;
    }

    private static int ceilLog2OfPositive(long n) {
        return Long.SIZE - Long.numberOfLeadingZeros(n - 1);
    }

    private static int floorLog2OfPositive(long n) {
        return Long.SIZE - 1 - Long.numberOfLeadingZeros(n);
    }

    /**
     * Perform binary search O(log(n)) at most 64 times but potentially less by probing array values at its bounds,
     * so overall it's still O(log(n)) time complexity.
     */
    public void findPowersOfTwoAndPrint() {
        int l = ceilLog2OfPositive(a[0]);
        int r = floorLog2OfPositive(a[aLen - 1]);
        System.out.printf("a[0]=%d, a[len-1]=%d, searching 2^%d..2^%d only%n", a[0], a[aLen - 1], l, r);
        for (int i = l; i <= r; i++) {
            long pot = 1L << i;
            int bsr = Arrays.binarySearch(a, pot);
            if (bsr < 0) {
                int insp = -bsr - 1;
                String placement = insp == aLen ? "after" : "   at";
                int idx = insp == aLen ? insp - 1 : insp;
                System.out.printf("Searched: 2^%2d = %20d, would be %s %10d: %20d (%20d)%n",
                        i, pot, placement, idx, a[idx], a[idx] - pot);
            } else {
                System.out.printf("Searched: 2^%2d = %20d, found it    at %10d%n", i, pot, bsr);
                powersOfTwoList.add(a[bsr]);
            }
        }
        System.out.printf("Powers of two #(%d): %s", powersOfTwoList.size(), powersOfTwoList);
    }
}
