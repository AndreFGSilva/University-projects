package src.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Comparable<User>, Serializable {
    private final String userID;
    private final String userName;

    public User() {
        this.userID = new String();
        this.userName = new String();
    }

    public User(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public User(String userID) {
        this.userID = userID;
        this.userName = null;
    }

    // Getters
    public String getID() {
        return this.userID;
    }

    public String getUserName() {
        return this.userName;
    }

    // Compare
    public int compareTo(User c) {
        return this.userID.compareTo(c.userID);
    }

    // Equals
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.userID.equals(user.userID);
    }

    // Hash code
    public int hashCode() {
        return Objects.hash(userID,userName);
    }
}