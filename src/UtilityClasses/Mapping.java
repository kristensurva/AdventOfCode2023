package UtilityClasses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Mapping implements Comparable<Mapping>{
    public long destinationStartRange;
    public long sourceStartRange;
    public long rangeLength;
    public int depthLevel;
    public ArrayList<Mapping> lowerMappings = new ArrayList<>();
    public Set<Mapping> higherMappings = new HashSet<>();

    public Mapping(long destinationStartRange, long sourceStartRange, long rangeLength, int depthLevel) {
        this.destinationStartRange = destinationStartRange;
        this.sourceStartRange = sourceStartRange;
        this.rangeLength = rangeLength;
        this.depthLevel = depthLevel;
    }

    @Override
    public int compareTo(Mapping other) {
        // First, compare by destinationStartRange
        int destinationComparison = Long.compare(this.destinationStartRange, other.destinationStartRange);
        if (destinationComparison != 0) {
            return destinationComparison;
        }

        // If destinationStartRange is same, compare depthLevel
        return Integer.compare(other.depthLevel, this.depthLevel);
    }

    @Override
    public String toString() {
        if (depthLevel!=-1) {
            return destinationStartRange + " " + sourceStartRange + " " + rangeLength + " D"+depthLevel;
        }
        return destinationStartRange + " " + rangeLength;
    }
}
