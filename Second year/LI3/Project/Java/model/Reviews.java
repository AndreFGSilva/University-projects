package src.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Reviews implements Serializable {
    private TreeSet<Review> reviews;

    public Reviews() {
        this.reviews = new TreeSet<>();
    }

    public Reviews(TreeSet<Review> reviews) {
        this.reviews = reviews;
    }

    public Reviews(Reviews r) {
        this.reviews = r.getReviews();
    }

    // Getters
    public TreeSet<Review> getReviews() {
        return this.reviews;
    }

    // Clone
    public Reviews clone() {
        return new Reviews(this);
    }

    // Método que adiciona uma avaliação caso ela seja válida.
    public boolean addReview(Review review) {
        if (!review.getReviewID().equals("review_id") && !review.getReviewID().equals("") && !review.getUserID().equals("") && !review.getBusinessID().equals("") && review.getStars() != -1 && review.getUseful() != -1 && review.getFunny() != -1 && review.getCool() != -1 && (review.getDate() instanceof Calendar))
            return reviews.add((Review) review);
        return false;
    }
    
    //
    public Set<String> setIDs() {
        return this.reviews.stream().map(Review::getReviewID).collect(Collectors.toSet());
    }

    //
    public Set<String> setBusinessesIDs() {
        return this.reviews.stream().map(Review::getBusinessID).collect(Collectors.toSet());
    }

    // Hash code
    public int hashCode() {
        return Objects.hash(reviews);
    }
}