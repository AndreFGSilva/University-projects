package src.model;

import java.util.Comparator;

public class ComparatorQuery9 implements Comparator<PairF> {

    public int compare(PairF o1, PairF o2) {
        if (o1.getY() < o2.getY())
            return 1;
        else
            if (o1.getY() > o2.getY())
                return -1;
        return 0;
    }
}