package src.model;

import java.io.Serializable;
import java.util.Arrays;

public class StructuresStats implements Serializable {
    private int[] revPerMonth;
    private double[] mediaRevPerMonth;
    private int[] revUserPerMonth;

    public StructuresStats() {
        this.revPerMonth = new int[12];
        this.mediaRevPerMonth = new double[12];
        this.revUserPerMonth = new int[12];
    }
    
    public StructuresStats(StructuresStats ss) {
        this.revPerMonth = ss.revPerMonth.clone();
        this.mediaRevPerMonth = ss.mediaRevPerMonth.clone();
        this.revUserPerMonth = ss.revUserPerMonth.clone();
    }

    // Getters
    public int getRevPerMonth(int month) {
        return this.revPerMonth[month];
    }

    public double getMediaPerMonth(int month) {
        return this.mediaRevPerMonth[month];
    }

    public int getRevUserPerMonth(int month) {
        return this.revUserPerMonth[month];
    }

    // Clone
    public StructuresStats clone() {
        return new StructuresStats(this);
    }

    // Hash code
    public int hashCode() {
        int result = Arrays.hashCode(revPerMonth);
        result = 31 * result + Arrays.hashCode(mediaRevPerMonth);
        result = 31 * result + Arrays.hashCode(revUserPerMonth);
        return result;
    }

    // 
    public void addRev(int month) {
        this.revPerMonth[month]++;
    }

    //
    public void addMedia(int month, float cla) {
        this.mediaRevPerMonth[month] += cla;
    }

    //
    public void finishMedia() {
        for(int i = 0; i < 12; i++)
            mediaRevPerMonth[i] /= revPerMonth[i];
    }

    //
    public void addRevUser(int month) {
        this.revUserPerMonth[month]++;
    }
}
