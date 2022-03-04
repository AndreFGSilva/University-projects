package src.model;

public class Pair {
    private int x;
    private int y;

    public Pair() {
        this.x = 0;
        this.y = 0;
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair(Pair p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    // Getters
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    // Clone
    public Pair clone() {
        return new Pair(this);
    }
}
