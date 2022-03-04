package src.model;

public class TupleIIF {
    private int[] x;
    private int[] y;
    private float[] z;

    public TupleIIF(int[] numRevs, int[] difBizs, float[] average) {
        this.x = numRevs;
        this.y = difBizs;
        this.z = average;
    }

    // Getters
    public int[] getX() {
        return this.x;
    }

    public int[] getY() {
        return this.y;
    }

    public float[] getZ() {
        return this.z;
    }
}
