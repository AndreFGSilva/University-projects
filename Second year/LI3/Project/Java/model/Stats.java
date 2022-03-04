package src.model;

import java.io.Serializable;

public class Stats implements Serializable {
    private String usersPath;
    private String businessesPath;
    private String reviewsPath;
    // Estatísticas referentes aos utilizadores
    private int validUsers;
    private int usersThatReviewed;
    private int usersThatDidntReview;
    // Estatísticas referentes às avaliações
    private int invalidReviews;
    private int noImpactReviews;
    // Estatísticas referentes aos negócios
    private int validBusinesses;
    private int businessesReviewed;
    private int businessesNotReviewed;

    public Stats() {
        this.usersPath = null;
        this.businessesPath = null;
        this.reviewsPath = null;
        
        this.validUsers = 0;
        this.usersThatReviewed = 0;
        this.usersThatDidntReview = 0;
        
        this.invalidReviews = 0;
        this.noImpactReviews = 0;
        
        this.validBusinesses = 0;
        this.businessesReviewed = 0;
        this.businessesNotReviewed = 0;
    }

    public Stats(String usersPath, String businessesPath, String reviewsPath, int validUsers, int usersThatReviewed, int usersThatDidntReview,
                    int invalidReviews, int noImpactReviews, int validBusinesses, int businessesReviewed, int businessesNotReviewed) {
        this.usersPath = usersPath;
        this.businessesPath = businessesPath;
        this.reviewsPath = reviewsPath;
        
        this.validUsers = validUsers;
        this.usersThatReviewed = usersThatReviewed;
        this.usersThatDidntReview = usersThatDidntReview;
        
        this.invalidReviews = invalidReviews;
        this.noImpactReviews = noImpactReviews;
        
        this.validBusinesses = validBusinesses;
        this.businessesReviewed = businessesReviewed;
        this.businessesNotReviewed = businessesNotReviewed;
    }

    public Stats(String usersPath, String businessesPath, String reviewsPath) {
        this.usersPath = usersPath;
        this.businessesPath = businessesPath;
        this.reviewsPath = reviewsPath;

        this.validUsers = 0;
        this.usersThatReviewed = 0;
        this.usersThatDidntReview = 0;

        this.invalidReviews = 0;
        this.noImpactReviews = 0;

        this.validBusinesses = 0;
        this.businessesReviewed = 0;
        this.businessesNotReviewed = 0;
    }

    // Getters
    public String getUsersPath() {
        return this.usersPath;
    }

    public String getBusinessPath() {
        return this.businessesPath;
    }

    public String getReviewsPath() {
        return this.reviewsPath;
    }

    public Integer getValidUsers() {
        return this.validUsers;
    }

    public Integer getUsersThatReviewed() {
        return this.usersThatReviewed;
    }

    public Integer getUsersThatDidntReview() {
        return this.usersThatDidntReview;
    }

    public Integer getInvalidReviews() {
        return this.invalidReviews;
    }

    public Integer getNoImpactReviews() {
        return this.noImpactReviews;
    }

    public Integer getValidBusinesses() {
        return this.validBusinesses;
    }

    public Integer getBusinessesReviewed() {
        return this.businessesReviewed;
    }

    public Integer getBusinessesNotReviewed() {
        return this.businessesNotReviewed;
    }

    // Setters
    public void setUsersPath(String upath) {
        this.usersPath = upath;
    }

    public void setBusinessPath(String bpath) {
        this.businessesPath = bpath;
    }

    public void setReviewsPath(String rpath) {
        this.reviewsPath = rpath;
    }

    public void setValidUsers(Integer vusers) {
        this.validUsers = vusers;
    }

    public void setUsersThatReviewed(Integer usersReviewed) {
        this.usersThatReviewed = usersReviewed;
    }

    public void setUsersThatDidntReview(Integer usersDidntReview) {
        this.usersThatDidntReview = usersDidntReview;
    }

    public void setInvalidReviews(Integer invalidR) {
        this.invalidReviews = invalidR;
    }

    public void setNoImpactReviews(Integer noImpactR) {
        this.noImpactReviews = noImpactR;
    }

    public void setValidBusinesses(Integer validB) {
        this.validBusinesses = validB;
    }

    public void setBusinessesReviewed(Integer bReviewed) {
        this.businessesReviewed = bReviewed;
    }

    public void setBusinessesNotReviewed(Integer bNotReviewed) {
        this.businessesNotReviewed = bNotReviewed;
    }

    public void addUserStats(boolean valid) {
        if (valid) this.validUsers++;
    }

    public void addBusinessStats(boolean valid) {
        if (valid) this.validBusinesses++;
    }

    public void addReviewStats(boolean valid) {
        if (!valid) this.invalidReviews++;
    }

    public void addNoImpactReview() {
        this.noImpactReviews++;
    }

    public void addBusinessesReviewed() {
        this.businessesReviewed++;
    }

    public void addBusinessesNotReviewed() {
        this.businessesNotReviewed++;
    }
    
    public void addUsersThatReviewed() {
        this.usersThatReviewed++;
    }

    public void addUsersThatDidntReview() {
        this.usersThatDidntReview++;
    }

    // ToString
    public String toString() {
        final StringBuffer sb = new StringBuffer("Stats {\n");
        sb.append("\tUsersPath = '").append(usersPath).append("'\n");
        sb.append("\tBusinessesPath = ").append(businessesPath).append('\n');
        sb.append("\tReviewsPath = ").append(reviewsPath).append('\n');
        sb.append("\tValidUsers = ").append(validUsers).append("\n");
        sb.append("\tUsersThatReviewed = '").append(usersThatReviewed).append("'\n");
        sb.append("\tUsersThatDidntReview = ").append(usersThatDidntReview).append('\n');
        sb.append("\tInvalidReviews = ").append(invalidReviews).append('\n');
        sb.append("\tNoImpactReviews = '").append(noImpactReviews).append("'\n");
        sb.append("\tValidBusinesses = ").append(validBusinesses).append('\n');
        sb.append("\tBusinessesReviewed = ").append(businessesReviewed).append('\n');
        sb.append("\tBusinessesNotReviewed = ").append(businessesNotReviewed).append("\n");
        sb.append("\n}");
        return sb.toString();
    }

    // Clone
    public Stats clone() {
        return new Stats(usersPath, businessesPath, reviewsPath, validUsers, usersThatReviewed,
        usersThatDidntReview, invalidReviews, noImpactReviews, validBusinesses, businessesReviewed, businessesNotReviewed);
    }
}
