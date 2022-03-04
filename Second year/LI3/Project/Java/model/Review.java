package src.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class Review implements Comparable<Review>, Serializable {
    private final String reviewID;
    private final String userID;
    private final String businessID;
    private final float stars;
    private final int useful;
    private final int funny;
    private final int cool;
    private final Calendar date;
    private final String text;

    public Review() {
        this.reviewID = new String();
        this.userID = new String();
        this.businessID = new String();
        this.stars = 0;
        this.useful = 0;
        this.funny = 0;
        this.cool = 0;
        this.date = Calendar.getInstance();
        this.text = new String();
    }
    
    public Review(String reviewID, String userID, String businessID, float stars, int useful, int funny, int cool, Calendar date, String text) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.businessID = businessID;
        this.stars = stars;
        this.useful = useful;
        this.funny = funny;
        this.cool = cool;
        this.date = date;
        this.text = text;
    }

    public Review(Review r) {
        this.reviewID = r.getReviewID();
        this.userID = r.getUserID();
        this.businessID = r.getBusinessID();
        this.stars = r.getStars();
        this.useful = r.getUseful();
        this.funny = r.getFunny();
        this.cool = r.getCool();
        this.date = r.getDate();
        this.text = r.getText();
    }

    // Getters
    public String getReviewID() {
        return this.reviewID;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getBusinessID() {
        return this.businessID;
    }

    public float getStars() {
        return this.stars;
    }

    public int getUseful() {
        return this.useful;
    }

    public int getFunny() {
        return this.funny;
    }

    public int getCool() {
        return this.cool;
    }

    public Calendar getDate() {
        return this.date;
    }

    public String getText() {
        return this.text;
    }

    // Compare
    public int compareTo(Review r) {
        return this.reviewID.compareTo(r.reviewID);
    }

    // Equals
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review r = (Review) o;
        return  this.reviewID.equals(r.getReviewID());
    }

    // Clone
    public Review clone() {
        return new Review(this);
    }

    // Hash code
    public int hashCode() {
        return Objects.hash(reviewID, userID, businessID, stars, useful, funny, cool,date,text);
    }
}