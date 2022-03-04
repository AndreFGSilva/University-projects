package src.model;

public class PairF {
    private float x;
    private int y;

    public PairF() {
        this.x = 0;
        this.y = 0;
    }

    public PairF(Float x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public PairF(PairF p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    // Getters
    public float getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    // Clone
    public PairF clone() {
        return new PairF(this);
    }
}
